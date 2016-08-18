package com.ua.igala.epilepsia_dca.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.ua.igala.epilepsia_dca.model.Usuario;
import com.ua.igala.epilepsia_dca.model.TelefonoEmergencias;
import com.ua.igala.epilepsia_dca.model.RegistroDiario;
import com.ua.igala.epilepsia_dca.model.RegistroSemanal;
import com.ua.igala.epilepsia_dca.model.RegistroMensual;
import com.ua.igala.epilepsia_dca.model.RegistroAnual;
import com.ua.igala.epilepsia_dca.sqlite.Database.Tablas;
import com.ua.igala.epilepsia_dca.sqlite.Userdata.Usuarios;
import com.ua.igala.epilepsia_dca.sqlite.Userdata.TelefonosEmergencias;
import com.ua.igala.epilepsia_dca.sqlite.Userdata.RegistrosDiarios;
import com.ua.igala.epilepsia_dca.sqlite.Userdata.RegistrosSemanales;
import com.ua.igala.epilepsia_dca.sqlite.Userdata.RegistrosMensuales;
import com.ua.igala.epilepsia_dca.sqlite.Userdata.RegistrosAnuales;

public final class OperacionesBD {
    private static Database database;
    private static OperacionesBD instancia = new OperacionesBD();

    private OperacionesBD() {}

    public static OperacionesBD obtenerInstancia(Context contexto) {
        if(database == null) {
            database = new Database(contexto);
        }

        return instancia;
    }

    /**
     * TELEFONO DE EMERGENCIAS
     */

    public Cursor getTelefonoEmergencias() {
        SQLiteDatabase db = database.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TELEFONO_EMERGENCIAS_JOIN_USER);
        return builder.query(db, projection_TelefonoEmergencias, null, null, null, null, null);
    }

    public Cursor getTelefonoEmergencias(String id) {
        SQLiteDatabase db = database.getWritableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        String seleccion = String.format("%s=?", TelefonosEmergencias.ID);
        String[] seleccionArgs = {id};

        builder.setTables(TELEFONO_EMERGENCIAS_JOIN_USER);
        return builder.query(db, projection_TelefonoEmergencias, seleccion, seleccionArgs, null, null, null);
    }

    public String addTelefonoEmergencias(TelefonoEmergencias telefono_emergencias) {
        SQLiteDatabase db = database.getWritableDatabase();

        String idTelefonoEmergencia = TelefonosEmergencias.generarIdTelefonoEmergencias();

        ContentValues values = new ContentValues();
        values.put(TelefonosEmergencias.ID, idTelefonoEmergencia);
        values.put(TelefonosEmergencias.TELEFONO, telefono_emergencias.telefono);
        values.put(TelefonosEmergencias.ID_USUARIO, telefono_emergencias.idUsuario);

        db.insertOrThrow(Tablas.TELEFONO_EMERGENCIA, null, values);
        return idTelefonoEmergencia;
    }

    public boolean updateTelefonoEmergencias(TelefonoEmergencias telefono_emergencias) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TelefonosEmergencias.TELEFONO, telefono_emergencias.telefono);
        values.put(TelefonosEmergencias.ID_USUARIO, telefono_emergencias.idUsuario);

        String condicion = String.format("%s=?", TelefonosEmergencias.ID);
        String[] condicionArgs = {telefono_emergencias.idTelefonoEmergencias};

        int resultado = db.update(Tablas.TELEFONO_EMERGENCIA, values, condicion, condicionArgs);
        return resultado > 0;
    }

    public boolean deleteTelefonoEmergencias(String idTelefonoEmergencias) {
        SQLiteDatabase db = database.getWritableDatabase();

        String condicion = TelefonosEmergencias.ID + "=?";
        String[] condicionArgs = {idTelefonoEmergencias};

        int resultado = db.delete(Tablas.TELEFONO_EMERGENCIA, condicion, condicionArgs);
        return resultado > 0;
    }

    private static String TELEFONO_EMERGENCIAS_JOIN_USER = "telefono_emergencias " +
            "INNER JOIN usuario " +
            "ON telefono_emergencias.id_usuario = usuario.id";

    private final String[] projection_TelefonoEmergencias = new String[] {
            Tablas.TELEFONO_EMERGENCIA + "." + TelefonosEmergencias.ID,
            TelefonosEmergencias.TELEFONO,
            Usuarios.NOMBRE,
            Usuarios.APELLIDOS};
}
