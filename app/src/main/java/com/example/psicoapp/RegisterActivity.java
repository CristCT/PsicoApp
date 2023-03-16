package com.example.psicoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText editTextRNombre, editTextRApellidos, editTextRCorreo, editTextRRut, editTextRContraseña, editTextRContraseña2;
    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextRNombre = findViewById(R.id.editTextRNombre);
        editTextRCorreo = findViewById(R.id.editTextRCorreo);
        editTextRApellidos = findViewById(R.id.editTextRApellidos);
        editTextRRut = findViewById(R.id.editTextRRut);
        editTextRContraseña = findViewById(R.id.editTextRContraseña);
        editTextRContraseña2 = findViewById(R.id.editTextRContraseña2);

        mAuth = FirebaseAuth.getInstance();

        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    // Funciones
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void loginActivity(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void registerUser() {
        String nombre = editTextRNombre.getText().toString();
        String correo = editTextRCorreo.getText().toString();
        String apellidos = editTextRApellidos.getText().toString();
        String rut = editTextRRut.getText().toString();
        String contraseña = editTextRContraseña.getText().toString();
        String contraseña2 = editTextRContraseña2.getText().toString();


        if (TextUtils.isEmpty(rut)){
            editTextRRut.setError("Ingrese su Rut");
            editTextRRut.requestFocus();
        } else if (TextUtils.isEmpty(nombre)){
            editTextRNombre.setError("Ingrese su Nombre");
            editTextRNombre.requestFocus();
        } else if (TextUtils.isEmpty(apellidos)){
            editTextRApellidos.setError("Ingrese sus Apellidos");
            editTextRApellidos.requestFocus();
        } else if (TextUtils.isEmpty(correo)){
            editTextRCorreo.setError("Ingrese su Correo");
            editTextRCorreo.requestFocus();
        } else if (TextUtils.isEmpty(contraseña)){
            editTextRContraseña.setError("Ingrese una Contraseña");
            editTextRContraseña.requestFocus();
        } else if (TextUtils.isEmpty(contraseña2)){
            editTextRContraseña2.setError("Ingrese una Contraseña2");
            editTextRContraseña2.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Usuarios usuarios = new Usuarios(nombre, apellidos, correo, rut);

                                FirebaseDatabase.getInstance().getReference("Usuarios").
                                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                        setValue(usuarios).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "Usuario Creado",
                                                    Toast.LENGTH_SHORT).show();
                                            loginActivity();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "createUserWithEmail:failure",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "createUserWithEmail:failure",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

}