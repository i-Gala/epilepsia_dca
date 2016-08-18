package com.ua.igala.epilepsia_dca.model;

/**
 * Created by Gala on 18/08/2016.
 */
public class RegistroMensual {
    public String idRegistro;
    public int mes;
    public int anyo;
    public String idUsuario;

    public RegistroMensual(String idRegistro, int mes, int anyo, String idUsuario) {
        this.idRegistro = idRegistro;
        this.mes = mes;
        this.anyo = anyo;
        this.idUsuario = idUsuario;
    }
}
