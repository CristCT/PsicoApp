package com.example.psicoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ImageButton ButtonEJ1, ButtonEJ2, ButtonEJ3;
    private FirebaseAuth userAuth;
    private TextView textView2Hijo;
    private String userID;

    DatabaseReference database;
    ArrayList<Usuarios> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ocultarUpBar();

        userAuth = FirebaseAuth.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance().getReference("Usuarios").child(userID);

        textView2Hijo = findViewById(R.id.textView2Hijo);

        ButtonEJ1 = findViewById(R.id.imageView2);
        ButtonEJ1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NuevoPaciente();
            }
        });

        ButtonEJ2 = findViewById(R.id.imageView21);
        ButtonEJ2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPacientes();
            }
        });

        ButtonEJ3 = findViewById(R.id.imageView22);
        ButtonEJ3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarAplicacion(v); // Mensaje de Alerta/Confirmación de Salir
            }
        });
        list = new ArrayList<>();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);

                textView2Hijo.setText("!Hola"+" "+usuarios.getNombre()+"!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



    }

    // Funciones

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        textView2Hijo.setText(savedInstanceState.getString("TEXT_VIEW_KEY"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("TEXT_VIEW_KEY", (String) textView2Hijo.getText());
    }

    private void cerrarAplicacion(View v) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning)
                .setTitle("¿Realmente desea salir de la sesión?")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {// un listener que al pulsar, cierre la aplicacion
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout(v);
                        //android.os.Process.killProcess(android.os.Process.myPid()); // Su función es algo similar a lo que se llama cuando se presiona el botón "Forzar Detención" o "Administrar aplicaciones", lo cuál mata la aplicación
                        //finish(); Si solo se quiere mandar la aplicación a segundo plano
                    }
                }).show();
    }

    public void logout(View view){
        userAuth.signOut();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void mostrarPacientes(){
        Intent intent = new Intent(HomeActivity.this, mostrarActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void NuevoPaciente(){
        Intent intent = new Intent(HomeActivity.this, NuevoPacienteActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void ocultarUpBar(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}