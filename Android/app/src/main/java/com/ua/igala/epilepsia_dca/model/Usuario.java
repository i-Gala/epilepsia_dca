package com.ua.igala.epilepsia_dca.model;

public class Usuario {
    public String idUsuario;
    public String email;
    public String nombre;
    public String apellidos;
    public String password;
    public Boolean first_conexion;
    public Boolean alarma_bluetooth;
    public Boolean alarma_telefono;

    public Usuario(String idUsuario, String email, String nombre, String apellidos, String password,
                   Boolean first_conexion, Boolean alarma_bluetooth, Boolean alarma_telefono) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.password = password;
        this.first_conexion = first_conexion;
        this.alarma_bluetooth = alarma_bluetooth;
        this.alarma_telefono = alarma_telefono;
    }
}
