package com.example.psicoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class subirFotosPaActivity extends AppCompatActivity {

    private String UserID_1, UserID;
    private Button buttonSubirFoto, buttonVerFotosPA;
    private EditText editTextNombre, editTextObservacion;
    private ImageView imageView;
    private ProgressBar progressBar;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private FirebaseFirestore realtimeRef;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_fotos_pa);

        ocultarUpBar();
        obtenerUserID();

        String rutReceived = getIntent().getStringExtra("pacienteRut");

        databaseRef = FirebaseDatabase.getInstance().getReference("Imagen"+UserID+rutReceived);
        storageRef = FirebaseStorage.getInstance().getReference();
        realtimeRef = FirebaseFirestore.getInstance();

        buttonSubirFoto = findViewById(R.id.buttonSubirFotoPA);
        buttonVerFotosPA = findViewById(R.id.buttonVerFotosPA);

        editTextNombre = findViewById(R.id.editTextNombreFotoPa);
        editTextObservacion = findViewById(R.id.editTextObservacionFotoPa);

        imageView = findViewById(R.id.imageViewAddImagePA);

        progressBar = findViewById(R.id.progressBarPA);
        progressBar.setVisibility(View.INVISIBLE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });
        buttonVerFotosPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long numHijos = dataSnapshot.getChildrenCount();

                        if (numHijos == 0){
                            alertNotieneExamenPaciente(v);
                        } if (numHijos >= 1) {
                            //finish(); // Para ir directo a la ficha del paciente al usar "retroceder" (esto se salta el subirFotosPaActivity)
                            verFotosPAI(rutReceived);
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Fallo la lectura: " + databaseError.getCode());
                    }


                });
            }
        });

        buttonSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){
                    subirFireBase(imageUri);
                }else{
                    Toast.makeText(subirFotosPaActivity.this, "Selecciona una Imagen",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Funciones

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_reverse, R.anim.slide_up_reverse);
    }

    private void subirFireBase(Uri uri){
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtensions(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String rutReceived = getIntent().getStringExtra("pacienteRut"); // Recibe RUT del Paciente desde FichaPaciente_Activity

                        String nombreFoto = editTextNombre.getText().toString();
                        String observacionFoto = editTextObservacion.getText().toString();

                        if (TextUtils.isEmpty(nombreFoto)){
                            editTextNombre.setError("Ingrese Nombre");
                            editTextNombre.requestFocus();
                            progressBar.setVisibility(View.INVISIBLE);
                        } else if (TextUtils.isEmpty(observacionFoto)) {
                            editTextObservacion.setError("Ingrese una Observación");
                            editTextObservacion.requestFocus();
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {

                            Imagenes imagenes = new Imagenes(nombreFoto, observacionFoto,uri.toString());

                            databaseRef.push().setValue(imagenes); // Genera Key aleatoria, para obtenew la key usar getKey()

                            progressBar.setVisibility(View.INVISIBLE);

                            Toast.makeText(subirFotosPaActivity.this, "Subida Exitosa",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(subirFotosPaActivity.this, "Subida Fallida !!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void alertNotieneExamenPaciente(View v) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning)
                .setTitle("El paciente no cuenta con Examenes!")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private String getFileExtensions(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }
    }

    private void verFotosPAI(String rutReceived){
        Intent intent = new Intent(subirFotosPaActivity.this, mostrarFotosPaActivity.class);
        intent.putExtra("pacienteRut", rutReceived);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void obtenerUserID(){
        UserID_1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserID = UserID_1.substring(0,5);
    }

    private void ocultarUpBar(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}