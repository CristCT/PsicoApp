package com.example.psicoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public class NuevoPacienteActivity extends AppCompatActivity {

    public String UserID_1, UserID_PA;
    private String RutPA;
    private ProgressBar progressBar;

    private FirebaseUser user;
    private FirebaseFirestore db;

    private EditText editTextRutPa, editTextNombrePa, editTextApellidoPa, editTextEmailPa, editTextPhonePa, editTextMotivoConsultaPa;
    private Button buttonAgregarPa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_paciente);

        db = FirebaseFirestore.getInstance();

        editTextRutPa = findViewById(R.id.editTextRutPa);
        editTextNombrePa = findViewById(R.id.editTextNombrePa);
        editTextApellidoPa = findViewById(R.id.editTextApellidoPa);
        editTextEmailPa = findViewById(R.id.editTextEmailPa);
        editTextPhonePa = findViewById(R.id.editTextPhonePa);
        editTextMotivoConsultaPa = findViewById(R.id.editTextMotivoConsultaPa);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        buttonAgregarPa = findViewById(R.id.buttonAgregarPa);
        buttonAgregarPa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarPaciente();
            }
        });

        // Oculta Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    // Funciones

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void registrarPaciente(){
        String rut = editTextRutPa.getText().toString();
        String nombre = editTextNombrePa.getText().toString();
        String apellido = editTextApellidoPa.getText().toString();
        String email = editTextEmailPa.getText().toString();
        String phone = editTextPhonePa.getText().toString();
        String motivoConsulta = editTextMotivoConsultaPa.getText().toString();

        RutPA = rut;


        if (TextUtils.isEmpty(rut)){
            editTextRutPa.setError("Ingrese Rut");
            editTextRutPa.requestFocus();
        } else if (TextUtils.isEmpty(nombre)){
            editTextNombrePa.setError("Ingrese un Nombre");
            editTextNombrePa.requestFocus();
        } else if (TextUtils.isEmpty(apellido)){
            editTextApellidoPa.setError("Ingrese Apellidos");
            editTextApellidoPa.requestFocus();
        } else if (TextUtils.isEmpty(email)){
            editTextEmailPa.setError("Ingrese un Correo");
            editTextEmailPa.requestFocus();
        } else if (TextUtils.isEmpty(phone)){
            editTextPhonePa.setError("Ingrese un Rut");
            editTextPhonePa.requestFocus();
        } else if (TextUtils.isEmpty(motivoConsulta)){
            editTextMotivoConsultaPa.setError("Ingrese motivo de Consulta");
            editTextMotivoConsultaPa.requestFocus();
        } else {

            Pacientes pacientes = new Pacientes(rut, nombre, apellido, email, phone, motivoConsulta);

            UserID_1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
            UserID_PA = UserID_1.substring(0,5);

            FirebaseDatabase.getInstance().getReference("Pacientes"+UserID_PA).child(UserID_PA+RutPA).
                    setValue(pacientes).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "create:success",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "create:failure",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}