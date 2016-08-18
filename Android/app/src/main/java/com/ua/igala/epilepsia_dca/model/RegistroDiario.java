package com.ua.igala.epilepsia_dca.model;

/**
 * Created by Gala on 18/08/2016.
 */
public class RegistroDiario {
    public String idRegistro;
    public String fecha;
    public String idUsuario;

    public RegistroDiario(String idRegistro, String fecha, String idUsuario) {
        this.idRegistro = idRegistro;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
    }
}
