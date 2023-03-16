package com.example.psicoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mostrarFotosPaActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private ArrayList<Imagenes> mImagenes;
    private String userID, userID_S;
    private SearchView searchView_Pa_fotos;
    private ImageButton buttonmenu_Pa_fotos;
    private int a = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_fotos_pa);

        ocultarUpBar();
        buttoninit();
        searchViewinit();

        String rutReceived = getIntent().getStringExtra("pacienteRut"); // Recibe RUT del Paciente desde FichaPaciente_Activity

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userID_S = userID.substring(0, 5);

        mRecyclerView = findViewById(R.id.recycler_view);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Imagen" + userID_S + rutReceived);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mImagenes = new ArrayList<>();
        mAdapter = new ImageAdapter(this, mImagenes, new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Imagenes item) {
                move(item);
                searchView_Pa_fotos.clearFocus();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mImagenes.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Imagenes imagenes = dataSnapshot.getValue(Imagenes.class);
                    imagenes.setKeyImg(dataSnapshot.getKey());
                    mImagenes.add(imagenes);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mostrarFotosPaActivity.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Funciones

    public void move(Imagenes item){
        Intent intent = new Intent(this, FichaImagenExamenPA_Activity.class);
        intent.putExtra("imagenDetalles", item);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void buttoninit(){
        buttonmenu_Pa_fotos = findViewById(R.id.buttonmenu_Pa_fotos);
        buttonmenu_Pa_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar un Popup Menu a un Button
                PopupMenu popupMenu = new PopupMenu(mostrarFotosPaActivity.this, buttonmenu_Pa_fotos);
                popupMenu.getMenuInflater().inflate(R.menu.menu_filter_pa_fotos, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_pa_nombre:
                                if (item.isChecked()) item.setChecked(false);
                                else item.setChecked(true);
                                searchView_Pa_fotos.setInputType(InputType.TYPE_CLASS_NUMBER);
                                searchView_Pa_fotos.setQueryHint("Buscar examen por Nombre");
                                a = 1;
                                break;
                            case R.id.menu_pa_observacion:
                                if (item.isChecked()) item.setChecked(false);
                                else item.setChecked(true);
                                searchView_Pa_fotos.setInputType(InputType.TYPE_CLASS_TEXT);
                                searchView_Pa_fotos.setQueryHint("Buscar examen por Observación");
                                a = 2;
                                break;
                            default:
                                return false;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    public void searchViewinit(){
        searchView_Pa_fotos = findViewById(R.id.searchView_Pa_fotos);
        searchView_Pa_fotos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView_Pa_fotos.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (a == 1){
                    filterNombre(newText); // Filtra por Nombre
                } if (a == 2){
                    filterObservacion(newText); // Filtra por Observacion
                }
                return false;
            }
        });
    }

    private void filterNombre(String text) {
        ArrayList<Imagenes> filteredlist = new ArrayList<>();
        for (Imagenes item : mImagenes) {
            if (item.getNombreImagen().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.filterList(filteredlist);
        }
    }

    private void filterObservacion(String text) {
        ArrayList<Imagenes> filteredlist = new ArrayList<>();
        for (Imagenes item : mImagenes) {
            if (item.getObservacionImagen().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.filterList(filteredlist);
        }
    }

    private void ocultarUpBar(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}