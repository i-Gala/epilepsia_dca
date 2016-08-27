package com.ua.igala.epilepsia_dca.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

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

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    /******************************************
     *                USUARIOS                *
     ******************************************/

    public Cursor getUsuario() {
        SQLiteDatabase db = database.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.USUARIOS);

        return db.rawQuery(sql, null);
    }

    public String getUserName(Cursor c, boolean close) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;

        if(c.getCount() < 1) {
            c.close();
            return "CODE_USER_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(Usuarios.NOMBRE);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);
        //System.out.println(array[0]);
        if(close)
            c.close();
        return array[0];
    }

    public String getUserLastname(Cursor c, boolean close) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;

        if(c.getCount() < 1) {
            c.close();
            return "CODE_USER_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(Usuarios.APELLIDOS);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);

        if(close)
            c.close();
        return array[0];
    }

    public String getUserMail(Cursor c, boolean close) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;

        if(c.getCount() < 1) {
            c.close();
            return "CODE_USER_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(Usuarios.EMAIL);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);

        if(close)
            c.close();
        return array[0];
    }

    public String getUserPassword(Cursor c, boolean close) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;

        if(c.getCount() < 1) {
            c.close();
            return "CODE_USER_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(Usuarios.PASSWORD);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);

        if(close)
            c.close();
        return array[0];
    }

    public String getUserID(Cursor c, boolean close) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;
        if(c.getCount() < 1) {
            c.close();
            return "CODE_USER_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(Usuarios.ID);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);

        if(close)
            c.close();
        return array[0];
    }

    public String getUserAlarmBluetooth(Cursor c, boolean close) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;
        if(c.getCount() < 1) {
            c.close();
            return "CODE_USER_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(Usuarios.ALARMA_BLUETOOTH);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);

        if(close)
            c.close();
        return array[0];
    }

    public String getUserAlarmPhone(Cursor c, boolean close) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;
        if(c.getCount() < 1) {
            c.close();
            return "CODE_USER_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(Usuarios.ALARMA_TELEFONO);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);

        if(close)
            c.close();
        return array[0];
    }

    public String getUserMaxHR(Cursor c, boolean close) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;

        if(c.getCount() < 1) {
            c.close();
            return "CODE_USER_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(Usuarios.MAX_HR);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);

        if(close)
            c.close();
        return array[0];
    }

    public String getUserMinHR(Cursor c, boolean close) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;

        if(c.getCount() < 1) {
            c.close();
            return "CODE_USER_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(Usuarios.MIN_HR);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);

        if(close)
            c.close();
        return array[0];
    }

    public String getUserTiempoEspera(Cursor c, boolean close) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;

        if(c.getCount() < 1) {
            c.close();
            return "CODE_USER_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(Usuarios.TIEMPO_ESPERA);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);

        if(close)
            c.close();
        return array[0];
    }

    public Cursor getUsuarioByEmail(String email) {
        SQLiteDatabase db = database.getReadableDatabase();
        //Cursor cursor = db.query(Tablas.USUARIOS, null, "email=?", new String[]{email}, null, null, null, null);

        String sql = String.format("SELECT * FROM %s WHERE %s = '%s'",
                Tablas.USUARIOS, Usuarios.EMAIL, email);

        return db.rawQuery(sql, null);

        //return cursor;
    }

    public Cursor getUsuarioByID(String idUser) {
        SQLiteDatabase db = database.getReadableDatabase();
        //Cursor cursor = db.query(Tablas.USUARIOS, null, "email=?", new String[]{email}, null, null, null, null);

        String sql = String.format("SELECT * FROM %s WHERE %s = '%s'",
                Tablas.USUARIOS, Usuarios.ID, idUser);

        return db.rawQuery(sql, null);

        //return cursor;
    }

    public String addUsuario(Usuario usuario) {
        SQLiteDatabase db = database.getWritableDatabase();

        String idUsuario = Usuarios.generarIdUsuario();

        ContentValues values = new ContentValues();
        values.put(Usuarios.ID, idUsuario);
        values.put(Usuarios.EMAIL, usuario.email);
        values.put(Usuarios.NOMBRE, usuario.nombre);
        values.put(Usuarios.APELLIDOS, usuario.apellidos);
        values.put(Usuarios.PASSWORD, usuario.password);
        values.put(Usuarios.FIRST_CONEXION, usuario.first_conexion);
        values.put(Usuarios.ALARMA_BLUETOOTH, usuario.alarma_bluetooth);
        values.put(Usuarios.ALARMA_TELEFONO, usuario.alarma_telefono);
        values.put(Usuarios.MAX_HR, usuario.max_hr);
        values.put(Usuarios.MIN_HR, usuario.min_hr);
        values.put(Usuarios.TIEMPO_ESPERA, usuario.tiempo_espera);

        db.insertOrThrow(Tablas.USUARIOS, null, values);
        //db.close();
        return idUsuario;
    }

    public boolean updateUsuario(Usuario usuario) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Usuarios.EMAIL, usuario.email);
        values.put(Usuarios.NOMBRE, usuario.nombre);
        values.put(Usuarios.APELLIDOS, usuario.apellidos);
        values.put(Usuarios.PASSWORD, usuario.password);
        values.put(Usuarios.FIRST_CONEXION, usuario.first_conexion);
        values.put(Usuarios.ALARMA_BLUETOOTH, usuario.alarma_bluetooth);
        values.put(Usuarios.ALARMA_TELEFONO, usuario.alarma_telefono);
        values.put(Usuarios.MAX_HR, usuario.max_hr);
        values.put(Usuarios.MIN_HR, usuario.min_hr);
        values.put(Usuarios.TIEMPO_ESPERA, usuario.tiempo_espera);

        String condicion = String.format("%s=?", Usuarios.ID);
        String[] condicionArgs = {usuario.idUsuario};

        int resultado = db.update(Tablas.USUARIOS, values, condicion, condicionArgs);
        return resultado > 0;
    }

    public boolean deleteUsuarios(String idUsuario) {
        SQLiteDatabase db = database.getWritableDatabase();

        String condicion = Usuarios.ID + "=?";
        String[] condicionArgs = {idUsuario};

        int resultado = db.delete(Tablas.USUARIOS, condicion, condicionArgs);
        return resultado > 0;
    }

    /******************************************
     *         TELEFONO DE EMERGENCIAS        *
     ******************************************/

    public String getPhone(Cursor c) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;
        if(c.getCount() < 1) {
            c.close();
            return "PHONE_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(TelefonosEmergencias.TELEFONO);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);
        c.close();
        return array[0];
    }

    public String getTelefonoEmergenciasID(Cursor c) {
        ArrayList<String> mArrayList = new ArrayList<String>();
        String[] array;
        if(c.getCount() < 1) {
            c.close();
            return "PHONE_ERROR";
        } else if( c.getCount() >= 1 ) {
            int id = c.getColumnIndex(TelefonosEmergencias.ID);

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mArrayList.add(c.getString(id));
            }
        }
        array = mArrayList.toArray(new String[0]);
        c.close();
        return array[0];
    }


    public Cursor getTelefonoEmergencias() {
        SQLiteDatabase db = database.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.TELEFONO_EMERGENCIA);

        return db.rawQuery(sql, null);
    }

    public Cursor getTelefonoEmergenciasByUser(String idUser) {
        SQLiteDatabase db = database.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s = '%s'",
                Tablas.TELEFONO_EMERGENCIA, TelefonosEmergencias.ID_USUARIO, idUser);

        return db.rawQuery(sql, null);
    }

    public String addTelefonoEmergencias(TelefonoEmergencias telefono_emergencias) {
        SQLiteDatabase db = database.getWritableDatabase();

        String idTelefonoEmergencia = TelefonosEmergencias.generarIdTelefonoEmergencias();

        ContentValues values = new ContentValues();
        values.put(TelefonosEmergencias.ID, idTelefonoEmergencia);
        values.put(TelefonosEmergencias.TELEFONO, telefono_emergencias.telefono);
        values.put(TelefonosEmergencias.ID_USUARIO, telefono_emergencias.idUsuario);

        /*Log.d("-----------------","-----------------");
        Log.d("ValuesID", idTelefonoEmergencia);
        Log.d("ValuesTLF", telefono_emergencias.telefono+"");
        Log.d("ValuesUSER", telefono_emergencias.idUsuario);*/

        db.insertOrThrow(Tablas.TELEFONO_EMERGENCIA, null, values);
        /*SQLiteDatabase db3 = database.getReadableDatabase();

        String sql3 = String.format("SELECT * FROM %s",
                Tablas.USUARIOS);

        Log.d("ValuesUSU", "ValuesUSU");
        DatabaseUtils.dumpCursor(db3.rawQuery(sql3, null));

        String sql2 = String.format("SELECT * FROM %s",
                Tablas.TELEFONO_EMERGENCIA);

        Log.d("ValuesTLF", "ValuesTLF");
        DatabaseUtils.dumpCursor(db.rawQuery(sql2, null));

        Log.d("-----------------","-----------------");*/

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

    /******************************************
     *             REGISTRO DIARIO            *
     ******************************************/

    public Cursor getRegistroDiario() {
        SQLiteDatabase db = database.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.REGISTRO_DIARIO);

        return db.rawQuery(sql, null);
    }

    public Cursor getRegistroDiarioByUser(String idUser) {
        SQLiteDatabase db = database.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=%s",
                Tablas.REGISTRO_DIARIO, RegistrosDiarios.ID_USUARIO, idUser);

        return db.rawQuery(sql, null);
    }

    public String addRegistroDiario(RegistroDiario registro_diario) {
        SQLiteDatabase db = database.getWritableDatabase();

        String idRegistroDiario = RegistrosDiarios.generarIdRegistroDiario();

        ContentValues values = new ContentValues();
        values.put(RegistrosDiarios.ID, idRegistroDiario);
        values.put(RegistrosDiarios.FECHA, registro_diario.fecha);
        values.put(RegistrosDiarios.ID_USUARIO, registro_diario.idUsuario);

        db.insertOrThrow(Tablas.REGISTRO_DIARIO, null, values);
        return idRegistroDiario;
    }

    public boolean updateRegistroDiario(RegistroDiario registro_diario) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RegistrosDiarios.FECHA, registro_diario.fecha);
        values.put(RegistrosDiarios.ID_USUARIO, registro_diario.idUsuario);

        String condicion = String.format("%s=?", RegistrosDiarios.ID);
        String[] condicionArgs = {registro_diario.idRegistro};

        int resultado = db.update(Tablas.REGISTRO_DIARIO, values, condicion, condicionArgs);
        return resultado > 0;
    }

    public boolean deleteRegistroDiario(String idRegistroDiario) {
        SQLiteDatabase db = database.getWritableDatabase();

        String condicion = RegistrosDiarios.ID + "=?";
        String[] condicionArgs = {idRegistroDiario};

        int resultado = db.delete(Tablas.REGISTRO_DIARIO, condicion, condicionArgs);
        return resultado > 0;
    }

    /******************************************
     *             REGISTRO SEMANAL           *
     ******************************************/

    public Cursor getRegistroSemanal() {
        SQLiteDatabase db = database.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.REGISTRO_SEMANAL);

        return db.rawQuery(sql, null);
    }

    public Cursor getRegistroSemanalByUser(String idUser) {
        SQLiteDatabase db = database.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=%s",
                Tablas.REGISTRO_SEMANAL, RegistrosSemanales.ID_USUARIO, idUser);

        return db.rawQuery(sql, null);
    }

    public String addRegistroSemanal(RegistroSemanal registro_semanal) {
        SQLiteDatabase db = database.getWritableDatabase();

        String idRegistroSemanal = RegistrosSemanales.generarIdRegistroSemanal();

        ContentValues values = new ContentValues();
        values.put(RegistrosSemanales.ID, idRegistroSemanal);
        values.put(RegistrosSemanales.DIA, registro_semanal.dia);
        values.put(RegistrosSemanales.MES, registro_semanal.mes);
        values.put(RegistrosSemanales.ANYO, registro_semanal.anyo);
        values.put(RegistrosSemanales.ID_USUARIO, registro_semanal.idUsuario);

        db.insertOrThrow(Tablas.REGISTRO_SEMANAL, null, values);
        return idRegistroSemanal;
    }

    public boolean updateRegistroSemanal(RegistroSemanal registro_semanal) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RegistrosSemanales.DIA, registro_semanal.dia);
        values.put(RegistrosSemanales.MES, registro_semanal.mes);
        values.put(RegistrosSemanales.ANYO, registro_semanal.anyo);
        values.put(RegistrosSemanales.ID_USUARIO, registro_semanal.idUsuario);

        String condicion = String.format("%s=?", RegistrosSemanales.ID);
        String[] condicionArgs = {registro_semanal.idRegistro};

        int resultado = db.update(Tablas.REGISTRO_SEMANAL, values, condicion, condicionArgs);
        return resultado > 0;
    }

    public boolean deleteRegistroSemanal(String idRegistroSemanal) {
        SQLiteDatabase db = database.getWritableDatabase();

        String condicion = RegistrosSemanales.ID + "=?";
        String[] condicionArgs = {idRegistroSemanal};

        int resultado = db.delete(Tablas.REGISTRO_SEMANAL, condicion, condicionArgs);
        return resultado > 0;
    }

    /******************************************
     *             REGISTRO MENSUAL           *
     ******************************************/

    public Cursor getRegistroMensual() {
        SQLiteDatabase db = database.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.REGISTRO_MENSUAL);

        return db.rawQuery(sql, null);
    }

    public Cursor getRegistroMensualByUser(String idUser) {
        SQLiteDatabase db = database.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=%s",
                Tablas.REGISTRO_MENSUAL, RegistrosMensuales.ID_USUARIO, idUser);

        return db.rawQuery(sql, null);
    }

    public String addRegistroMensual(RegistroMensual registro_mensual) {
        SQLiteDatabase db = database.getWritableDatabase();

        String idRegistroMensual = RegistrosMensuales.generarIdRegistroMensual();

        ContentValues values = new ContentValues();
        values.put(RegistrosMensuales.ID, idRegistroMensual);
        values.put(RegistrosMensuales.MES, registro_mensual.mes);
        values.put(RegistrosMensuales.ANYO, registro_mensual.anyo);
        values.put(RegistrosMensuales.ID_USUARIO, registro_mensual.idUsuario);

        db.insertOrThrow(Tablas.REGISTRO_MENSUAL, null, values);
        return idRegistroMensual;
    }

    public boolean updateRegistroMensual(RegistroMensual registro_mensual) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RegistrosMensuales.MES, registro_mensual.mes);
        values.put(RegistrosMensuales.ANYO, registro_mensual.anyo);
        values.put(RegistrosMensuales.ID_USUARIO, registro_mensual.idUsuario);

        String condicion = String.format("%s=?", RegistrosMensuales.ID);
        String[] condicionArgs = {registro_mensual.idRegistro};

        int resultado = db.update(Tablas.REGISTRO_MENSUAL, values, condicion, condicionArgs);
        return resultado > 0;
    }

    public boolean deleteRegistroMensual(String idRegistroMensual) {
        SQLiteDatabase db = database.getWritableDatabase();

        String condicion = RegistrosMensuales.ID + "=?";
        String[] condicionArgs = {idRegistroMensual};

        int resultado = db.delete(Tablas.REGISTRO_MENSUAL, condicion, condicionArgs);
        return resultado > 0;
    }

    /******************************************
     *              REGISTRO ANUAL            *
     ******************************************/

    public Cursor getRegistroAnual() {
        SQLiteDatabase db = database.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.REGISTRO_ANUAL);

        return db.rawQuery(sql, null);
    }

    public Cursor getRegistroAnualByUser(String idUser) {
        SQLiteDatabase db = database.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=%s",
                Tablas.REGISTRO_ANUAL, RegistrosAnuales.ID_USUARIO, idUser);

        return db.rawQuery(sql, null);
    }

    public String addRegistroAnual(RegistroAnual registro_anual) {
        SQLiteDatabase db = database.getWritableDatabase();

        String idRegistroAnual = RegistrosAnuales.generarIdRegistroAnual();

        ContentValues values = new ContentValues();
        values.put(RegistrosAnuales.ID, idRegistroAnual);
        values.put(RegistrosAnuales.ANYO, registro_anual.anyo);
        values.put(RegistrosAnuales.ID_USUARIO, registro_anual.idUsuario);

        db.insertOrThrow(Tablas.REGISTRO_ANUAL, null, values);
        return idRegistroAnual;
    }

    public boolean updateRegistroAnual(RegistroAnual registro_anual) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RegistrosAnuales.ANYO, registro_anual.anyo);
        values.put(RegistrosAnuales.ID_USUARIO, registro_anual.idUsuario);

        String condicion = String.format("%s=?", RegistrosAnuales.ID);
        String[] condicionArgs = {registro_anual.idRegistro};

        int resultado = db.update(Tablas.REGISTRO_ANUAL, values, condicion, condicionArgs);
        return resultado > 0;
    }

    public boolean deleteRegistroAnual(String idRegistroAnual) {
        SQLiteDatabase db = database.getWritableDatabase();

        String condicion = RegistrosAnuales.ID + "=?";
        String[] condicionArgs = {idRegistroAnual};

        int resultado = db.delete(Tablas.REGISTRO_ANUAL, condicion, condicionArgs);
        return resultado > 0;
    }

    public SQLiteDatabase getDb() {
        return database.getWritableDatabase();
    }
}
