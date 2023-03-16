package com.example.psicoapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // Primero inicializar
    EditText editCorreo, editPass; // Box para ingresar Usuario y Pass
    Button buttonLogin;             // Button de Login
    TextView textViewOlvide, textViewRegistrarme;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Segundo Asignar
        editCorreo = findViewById(R.id.editCorreo);
        editPass = findViewById(R.id.editPass);
        textViewOlvide = findViewById(R.id.textViewOlvide);
        textViewRegistrarme = findViewById(R.id.textViewRegistrarme);
        textViewRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userButtonRegister();
            }
        });

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCorreo.getText().toString().equals("")){
                    editCorreo.setError("Ingrese un Correo");
                    editCorreo.requestFocus();
                } else {
                    if (editPass.getText().toString().equals("")){
                        editPass.setError("Ingrese una Contraseña");
                        editPass.requestFocus();
                    } else {
                        userLogin(v);
                        buttonLogin.setEnabled(false);
                    }
                }
            }
        });

        // Ocultar Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    // Funciones

    public void userButtonRegister(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void userLogin(View view){
        mAuth.signInWithEmailAndPassword(editCorreo.getText().toString(), editPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCustomToken:success");
                            Toast.makeText(getApplicationContext(), "Autentificacion correcta.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            if (mAuth != null) {
                                Log.w(TAG, "onAuthStateChanged - Logueado");}

                            Intent intent= new Intent (LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            buttonLogin.setEnabled(true);

                        } else {
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            toastLoginFailed();
                            buttonLogin.setEnabled(true);
                            //updateUI(null);
                        }
                    }
                });
    }

    public void toastLoginFailed(){
        Toast loginfailed = new Toast(getApplicationContext());
        View viewToastfailed = LayoutInflater.from(this).inflate(R.layout.toast_loginfailed_layout, null);
        TextView toastTextViewLF = viewToastfailed.findViewById(R.id.textViewToastfailed);
        toastTextViewLF.setText("Autentificación Fallida.");
        loginfailed.setView(viewToastfailed);
        loginfailed.setDuration(Toast.LENGTH_SHORT);
        loginfailed.show();
    }

    private void cerrarAplicacion() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning)
                .setTitle("¿Realmente desea cerrar la aplicación?")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {// Listener que al pulsar, cierre la aplicación
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid()); // Su función es algo similar a lo que se llama cuando se presiona el botón "Forzar Detención" o "Administrar aplicaciones", lo cuál mata la aplicación
                        //finish(); Si solo se quiere mandar la aplicación a segundo plano
                    }
                }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cerrarAplicacion();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}