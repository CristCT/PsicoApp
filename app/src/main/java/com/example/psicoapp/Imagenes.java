package com.example.psicoapp;

import java.io.Serializable;

public class Imagenes implements Serializable {

    public String nombreImagen;
    public String observacionImagen;
    public String imagenUrl;
    public String keyImg;

    public Imagenes(){}

    public String getKeyImg() {
        return keyImg;
    }

    public void setKeyImg(String keyImg) {
        this.keyImg = keyImg;
    }

    public Imagenes(String nombreImagen, String observacionImagen, String imagenUrl) {
        this.nombreImagen = nombreImagen;
        this.observacionImagen = observacionImagen;
        this.imagenUrl = imagenUrl;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public String getObservacionImagen() {
        return observacionImagen;
    }

    public void setObservacionImagen(String observacionImagen) {
        this.observacionImagen = observacionImagen;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
