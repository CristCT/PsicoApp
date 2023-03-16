package com.example.psicoapp;

import java.io.Serializable;

public class Pacientes implements Serializable {

    public String rut;
    public String nombre;
    public String apellidos;
    public String email;
    public String phone;
    public String motivoConsulta;

    public String key;

    public Pacientes(){

    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public Pacientes(String rut, String nombre, String apellidos, String email, String phone, String motivoConsulta){
        this.rut = rut;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.phone = phone;
        this.motivoConsulta = motivoConsulta;
    }
}
