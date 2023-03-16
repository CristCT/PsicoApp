package com.example.psicoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class mostrarActivity extends AppCompatActivity{

    RecyclerView recyclerPa;
    DatabaseReference database;
    Adapter adapter;
    public ArrayList<Pacientes> list;
    private String userID, userID_S;
    private SearchView searchView_Pa;
    public ImageButton buttonbuttonmenu;
    private int a = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);

        buttoninit();
        ocultarUpBar();
        searchViewinit();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userID_S = userID.substring(0,5);

        recyclerPa = findViewById(R.id.RecyclerPa);
        database = FirebaseDatabase.getInstance().getReference("Pacientes"+userID_S);
        recyclerPa.setHasFixedSize(true);
        recyclerPa.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new Adapter(this, list, new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pacientes item) {
                move(item);
                searchView_Pa.clearFocus();
            }
        });
        recyclerPa.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Pacientes pacientes = dataSnapshot.getValue(Pacientes.class);
                    pacientes.setKey(dataSnapshot.getKey());
                    list.add(pacientes);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // Funciones

    public void buttoninit(){
        buttonbuttonmenu = findViewById(R.id.buttonmenu);
        buttonbuttonmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega un Popup Menu a un Button
                PopupMenu popupMenu = new PopupMenu(mostrarActivity.this, buttonbuttonmenu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_filter_pa, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_pa_rut:
                                if (item.isChecked()) item.setChecked(false);
                                else item.setChecked(true);
                                searchView_Pa.setInputType(InputType.TYPE_CLASS_NUMBER);
                                searchView_Pa.setQueryHint("Buscar paciente por Rut");
                                a = 1;
                                break;
                            case R.id.menu_pa_nombre:
                                if (item.isChecked()) item.setChecked(false);
                                else item.setChecked(true);
                                searchView_Pa.setInputType(InputType.TYPE_CLASS_TEXT);
                                searchView_Pa.setQueryHint("Buscar paciente por Nombre");
                                a = 2;
                                break;
                            case R.id.menu_pa_apellidos:
                                if (item.isChecked()) item.setChecked(false);
                                else item.setChecked(true);
                                searchView_Pa.setInputType(InputType.TYPE_CLASS_TEXT);
                                searchView_Pa.setQueryHint("Buscar paciente por Apellidos");
                                a = 3;
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
        searchView_Pa = findViewById(R.id.searchView_Pa);
        searchView_Pa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView_Pa.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (a == 1){
                    filterRut(newText); // Filtra por Rut
                } if (a == 2){
                    filterNombre(newText); // Filtra por Nombre
                } if (a == 3){
                    filterApellido(newText); // Filtra por Apellidos
                }
                return false;
            }
        });
    }

    private void filterRut(String text) {
        ArrayList<Pacientes> filteredlist = new ArrayList<>();
        for (Pacientes item : list) {
            if (item.getRut().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredlist);
        }
    }

    private void filterNombre(String text) {
        ArrayList<Pacientes> filteredlist = new ArrayList<>();
        for (Pacientes item : list) {
            if (item.getNombre().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredlist);
        }
    }

    private void filterApellido(String text) {
        ArrayList<Pacientes> filteredlist = new ArrayList<>();
        for (Pacientes item : list) {
            if (item.getApellidos().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredlist);
        }
    }

    public void move(Pacientes item){
        Intent intent = new Intent(this, FichaPacientes_Activity.class);
        intent.putExtra("pacienteDetalles", item);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void ocultarUpBar(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}