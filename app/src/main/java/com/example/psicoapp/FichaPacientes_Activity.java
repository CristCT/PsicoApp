package com.example.psicoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FichaPacientes_Activity extends AppCompatActivity {

    private TextView fichaRut, fichaNombre, fichaApellido, fichaEmail, fichaPhone, fichaMotivoConsulta;
    private Button verFotosPA, agregarFotosPA;
    private String userID, userID_S;
    private DatabaseReference database;
    private long numHijos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_pacientes);

        initViews();
        initValues();
        ocultarUpbar();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userID_S = userID.substring(0,5);

        String rutSend = fichaRut.getText().toString(); // Obtener RUT del TextView fichaRut

        database = FirebaseDatabase.getInstance().getReference("Imagen"+userID_S+rutSend);


        verFotosPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.addListenerForSingleValueEvent(new ValueEventListener() { // Solo actualiza una vez, en cambio el "addValueEventListener" está constantemente activo
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long numHijos = dataSnapshot.getChildrenCount();
                        if (numHijos == 0){
                            alertNotieneExamenPaciente(v);
                        } if (numHijos >= 1) {
                            verFotosPA(rutSend);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Fallo la lectura: " + databaseError.getCode());
                    }
                });


            }
        });

        agregarFotosPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirFotosPA(rutSend);
            }
        });
    }

    // Funciones

    private void alertNotieneExamenPaciente(View v) {
        new MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_warning)
                .setTitle("El paciente no cuenta con Examenes!")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {// un listener que al pulsar, cierre la aplicacion
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //android.os.Process.killProcess(android.os.Process.myPid()); // Su función es similar a cuando se presiona el botón "Forzar Detención" o "Administrar aplicaciones", lo cuál elimina el proceso de la aplicación
                        //finish(); Si solo se quiere mandar la aplicación a segundo plano
                    }
                }).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void verFotosPA(String rutSend){
        Intent intent = new Intent(FichaPacientes_Activity.this, mostrarFotosPaActivity.class);
        intent.putExtra("pacienteRut", rutSend);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void subirFotosPA(String rutSend){
        Intent intent = new Intent(FichaPacientes_Activity.this, subirFotosPaActivity.class);
        intent.putExtra("pacienteRut", rutSend);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
    }
    private void initViews(){
        fichaRut = findViewById(R.id.tvfichaRut);
        fichaNombre = findViewById(R.id.tvfichaNombre);
        fichaApellido = findViewById(R.id.tvfichaApellido);
        fichaEmail = findViewById(R.id.tvfichaEmail);
        fichaPhone = findViewById(R.id.tvfichaPhone);
        fichaMotivoConsulta = findViewById(R.id.tvfichaMotivoConsulta);
        verFotosPA = findViewById(R.id.buttonVerFotosPA);
        agregarFotosPA = findViewById(R.id.buttonAgregarFotosPa);
    }
    private void initValues(){
        Pacientes pacienteDetalles = (Pacientes) getIntent().getExtras().getSerializable("pacienteDetalles");

        fichaRut.setText(pacienteDetalles.getRut());
        fichaNombre.setText(pacienteDetalles.getNombre());
        fichaApellido.setText(pacienteDetalles.getApellidos());
        fichaEmail.setText(pacienteDetalles.getEmail());
        fichaPhone.setText(pacienteDetalles.getPhone());
        fichaMotivoConsulta.setText(pacienteDetalles.getMotivoConsulta());
    }
    private void ocultarUpbar(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}