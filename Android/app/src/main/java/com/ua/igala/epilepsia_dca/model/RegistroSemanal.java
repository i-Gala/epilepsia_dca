package com.ua.igala.epilepsia_dca.model;

/**
 * Created by Gala on 18/08/2016.
 */
public class RegistroSemanal {
    public String idRegistro;
    public int dia;
    public int mes;
    public int anyo;
    public String idUsuario;

    public RegistroSemanal(String idRegistro, int dia, int mes, int anyo, String idUsuario) {
        this.idRegistro = idRegistro;
        this.dia = dia;
        this.mes = mes;
        this.anyo = anyo;
        this.idUsuario = idUsuario;
    }
}
