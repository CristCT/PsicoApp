package com.example.psicoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FichaImagenExamenPA_Activity extends AppCompatActivity {

    private TextView fichaNombreExamen, fichaObservacionExamen;
    private ImageView imagenExamen;
    private ProgressBar progressBarImage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_imagen_examen_pa);

        initViews();
        initValues();
        ocultarUpbar();

    }

    // Funciones

    private void initViews(){
        fichaNombreExamen = findViewById(R.id.tvfichaNombreExamen);
        fichaObservacionExamen = findViewById(R.id.tvfichaObservacionExameno);
        imagenExamen = findViewById(R.id.tvimagenexamen);
        progressBarImage2 = findViewById(R.id.progressBarImage2);
    }

    private void initValues(){
        Imagenes imagenesDetalles = (Imagenes) getIntent().getExtras().getSerializable("imagenDetalles");

        fichaNombreExamen.setText(imagenesDetalles.getNombreImagen());
        fichaObservacionExamen.setText(imagenesDetalles.getObservacionImagen());
        String imagenURL = imagenesDetalles.getImagenUrl();
        Picasso.with(this).load(imagenURL).into(imagenExamen, new Callback() {
            @Override
            public void onSuccess() {
                progressBarImage2.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
    }

    private void ocultarUpbar(){
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