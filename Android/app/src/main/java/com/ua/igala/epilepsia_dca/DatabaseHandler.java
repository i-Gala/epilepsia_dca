package com.ua.igala.epilepsia_dca;

/**
 * BASE DE DATOS
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler  extends SQLiteOpenHelper {
    public DatabaseHandler(Context context, Object name, Object factory, int version) {
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);         // Autogenerado
    }

    private static final int DATABASE_VERSION = 1;                      // Versión de la BD
    private static final String DATABASE_NAME = "BDTFG.db";             // Nombre de la BD

    // Declaramos la BD de Usuarios de la app
    private static final String TABLE_USER = "usuarios";
    public  static final String KEY_ID = "id";
    public  static final String KEY_NAME = "name";
    public  static final String KEY_LASTNAME = "lastname";
    public  static final String KEY_EMAIL = "email";
    public  static final String KEY_PASSWORD = "password";
    public  static final String KEY_ALERTSMS = "alert_sms";
    public  static final String KEY_ALERTMOBILE = "alert_mobile";
    public  static final String KEY_ALERTBLUETOOTH = "alert_bluetooth";
    public  static final String KEY_FIRSTTIME = "first_time";

/*  Expression Affinity     Column Declared Type
        TEXT	                    "TEXT"
        NUMERIC	                    "NUM"
        INTEGER	                    "INT"
        REAL	                    "REAL"
        NONE	                "" (empty string)
*/

    public  static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID + " INTENGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_LASTNAME + " TEXT,"
            + KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT,"
            + KEY_ALERTSMS + " NUM," + KEY_ALERTMOBILE + " NUM," + KEY_ALERTBLUETOOTH + " NUM,"
            + KEY_FIRSTTIME + " NUM " + " )";



    @Override
    public void onCreate(SQLiteDatabase BD) {
        BD.execSQL(CREATE_TABLE_USER);                                       // Autogenera
    }

    @Override
    public void onUpgrade(SQLiteDatabase BD, int oldVso, int newVso) {
        BD.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);           // Elimina la tabla antigua
        onCreate(BD);                                                   // Creamos la tabla de nuevo
    }

    // Agregamos nuevos usuarios
    void addUser(Userdata userdata) {
        SQLiteDatabase BD = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, userdata.getName());
        values.put(KEY_LASTNAME, userdata.getLastname());
        values.put(KEY_EMAIL, userdata.getEmail());
        values.put(KEY_PASSWORD, userdata.getPassword());
        values.put(KEY_ALERTSMS, userdata.getAlertSMS());
        values.put(KEY_ALERTMOBILE, userdata.getAlertMobileNo());
        values.put(KEY_ALERTBLUETOOTH, userdata.getAlertBluetooth());
        values.put(KEY_FIRSTTIME, userdata.getFirstTime());
        BD.insert(TABLE_USER, null, values);                        // Insertamos columna
        BD.close();                                                     // Cerramos la conexión con la BD
    }

    String  getUser(String email) {
        String password = null;

        SQLiteDatabase BD = this.getReadableDatabase();
        Cursor cursor = BD.query(TABLE_USER, null, "email=?", new String[]{email}, null, null, null, null);

        if(cursor.getCount() < 1) {
            cursor.close();
            return "CODE_USER_ERROR";
        } else if( cursor.getCount() >= 1 && cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            Global.user_id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            cursor.close();
        }

        return password;
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static String getKeyId() {
        return KEY_ID;
    }

    public static String getUserTable() {
        return TABLE_USER;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

}
