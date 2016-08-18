package com.ua.igala.epilepsia_dca.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import com.ua.igala.epilepsia_dca.sqlite.Userdata.Usuarios;
import com.ua.igala.epilepsia_dca.sqlite.Userdata.TelefonosEmergencias;
import com.ua.igala.epilepsia_dca.sqlite.Userdata.RegistrosDiarios;
import com.ua.igala.epilepsia_dca.sqlite.Userdata.RegistrosSemanales;
import com.ua.igala.epilepsia_dca.sqlite.Userdata.RegistrosMensuales;
import com.ua.igala.epilepsia_dca.sqlite.Userdata.RegistrosAnuales;

public class Database extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DATOS = "BDTFG.db";
    private static final int VERSION_ACTUAL = 1;
    private final Context contexto;

    interface Tablas {
        String USUARIOS = "usuarios";
        String TELEFONO_EMERGENCIA = "telefono_emergencias";
        String REGISTRO_DIARIO = "registro_diario";
        String REGISTRO_SEMANAL = "registro_semanal";
        String REGISTRO_MENSUAL = "registro_mensual";
        String REGISTRO_ANUAL = "registro_anual";
    }

    interface Referencias {
        String ID_USUARIOS = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                Tablas.USUARIOS, Usuarios.ID);

        String ID_TELEFONO_EMERGENCIA = String.format("REFERENCES %s(%s)",
                Tablas.TELEFONO_EMERGENCIA, TelefonosEmergencias.ID);

        String ID_REGISTRO = String.format("REFERENCES %s(%s)",
                Tablas.REGISTRO_DIARIO, RegistrosDiarios.ID);

        String ID_REGISTRO_DIARIO = String.format("REFERENCES %s(%s)",
                Tablas.REGISTRO_SEMANAL, RegistrosSemanales.ID);

        String ID_REGISTRO_MENSUAL= String.format("REFERENCES %s(%s)",
                Tablas.REGISTRO_MENSUAL, RegistrosMensuales.ID);

        String ID_REGISTRO_ANUAL= String.format("REFERENCES %s(%s)",
                Tablas.REGISTRO_ANUAL, RegistrosAnuales.ID);
    }

    public Database(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL," +
                        "%s TEXT NOT NULL, %s TEXT NOT NULL, %s INTENGER NOT NULL," +
                        "%s INTENGER NOT NULL, %s INTENGER NOT NULL)",
                Tablas.USUARIOS, BaseColumns._ID,
                Usuarios.EMAIL, Usuarios.ID,
                Usuarios.NOMBRE, Usuarios.APELLIDOS,
                Usuarios.PASSWORD, Usuarios.FIRST_CONEXION,
                Usuarios.ALARMA_BLUETOOTH, Usuarios.ALARMA_TELEFONO));


        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s INTENGER NOT NULL, %s TEXT NOT NULL %s)",
                Tablas.TELEFONO_EMERGENCIA, BaseColumns._ID,
                TelefonosEmergencias.ID, TelefonosEmergencias.TELEFONO,
                TelefonosEmergencias.ID_USUARIO, Referencias.ID_USUARIOS));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s DATETIME NOT NULL, %s TEXT NOT NULL %s)",
                Tablas.REGISTRO_DIARIO, BaseColumns._ID,
                RegistrosDiarios.ID, RegistrosDiarios.FECHA,
                RegistrosDiarios.ID_USUARIO, Referencias.ID_USUARIOS));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s INTENGER NOT NULL, %s INTENGER NOT NULL," +
                        "%s INTENGER NOT NULL, %s INTENGER NOT NULL %s)",
                Tablas.REGISTRO_SEMANAL, BaseColumns._ID,
                RegistrosSemanales.ID, RegistrosSemanales.DIA,
                RegistrosSemanales.MES, RegistrosSemanales.ANYO,
                RegistrosSemanales.ID_USUARIO, Referencias.ID_USUARIOS));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL, %s INTENGER NOT NULL," +
                        "%s INTENGER NOT NULL, %s INTENGER NOT NULL %s)",
                Tablas.REGISTRO_MENSUAL, BaseColumns._ID,
                RegistrosMensuales.ID,
                RegistrosMensuales.MES, RegistrosMensuales.ANYO,
                RegistrosMensuales.ID_USUARIO, Referencias.ID_USUARIOS));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL, %s INTENGER NOT NULL, %s INTENGER NOT NULL %s)",
                Tablas.REGISTRO_ANUAL, BaseColumns._ID,
                RegistrosAnuales.ID, RegistrosAnuales.ANYO,
                RegistrosAnuales.ID_USUARIO, Referencias.ID_USUARIOS));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TELEFONO_EMERGENCIA);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.REGISTRO_DIARIO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.REGISTRO_SEMANAL);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.REGISTRO_MENSUAL);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.REGISTRO_ANUAL);

        onCreate(db);
    }
}
