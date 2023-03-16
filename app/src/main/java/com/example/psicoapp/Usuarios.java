package com.example.psicoapp;

import java.io.Serializable;

public class Usuarios implements Serializable {

    public String nombre;
    public String apellidos;
    public String correo;
    public String rut;

    public Usuarios(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public Usuarios(String nombre, String apellidos, String correo, String rut) {
        rut = "psi_"+rut; // Agrega el prefijo psi_ al rut para identificar al usuario como psicólogo
        this.rut = rut;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
    }
}
