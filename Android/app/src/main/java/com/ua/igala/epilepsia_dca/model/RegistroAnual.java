package com.ua.igala.epilepsia_dca.model;

/**
 * Created by Gala on 18/08/2016.
 */
public class RegistroAnual {
    public String idRegistro;
    public int anyo;
    public String idUsuario;

    public RegistroAnual(String idRegistro, int anyo, String idUsuario) {
        this.idRegistro = idRegistro;
        this.anyo = anyo;
        this.idUsuario = idUsuario;
    }
}
