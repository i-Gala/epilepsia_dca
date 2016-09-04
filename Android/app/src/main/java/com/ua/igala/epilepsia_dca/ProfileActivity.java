package com.ua.igala.epilepsia_dca;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ua.igala.epilepsia_dca.model.Usuario;
import com.ua.igala.epilepsia_dca.sqlite.OperacionesBD;

public class ProfileActivity extends AppCompatActivity {

    private OperacionesBD database;
    private Global global = Global.getInstance();

    private EditText et_name;
    private EditText et_lastname;
    private EditText et_email;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = OperacionesBD.obtenerInstancia(getApplicationContext());
        Log.d("IDUSERCONECTADO",global.getIDUserOnline());

        et_name = (EditText) findViewById(R.id.field_name);
        et_lastname = (EditText) findViewById(R.id.field_lastname);
        et_email = (EditText) findViewById(R.id.field_email);
        et_password = (EditText) findViewById(R.id.field_password);

        cargarDatos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(global.getOnlineUser() == false) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            cargarDatos();
        }
    }

    protected void logoutOnClick(View v) {
        global.desconectarDispositivo();
        global.setOnlineUser(false);
        global.setIDUserOnline(null);
        global.deleteSharedPreferences(getApplicationContext());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void homeOnClick(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    protected void alarmOnClick(View v) {
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);

    }

    protected void sendOnClick(View v) {
        Cursor cursor = database.getUsuarioByID(global.getIDUserOnline());
        String field_name = et_name.getText().toString();
        String field_lastname = et_lastname.getText().toString();
        String field_email = et_email.getText().toString();
        String field_password = et_password.getText().toString();

        boolean field_alarmB = (Integer.parseInt(database.getUserAlarmBluetooth(cursor, false)) != 0);
        boolean field_alarmP = (Integer.parseInt(database.getUserAlarmPhone(cursor, false)) != 0);
        int field_maxhr = (Integer.parseInt(database.getUserMaxHR(cursor, false)));
        int field_minhr = (Integer.parseInt(database.getUserMinHR(cursor, false)));
        int field_tiempoEspera = (Integer.parseInt(database.getUserTiempoEspera(cursor, true)));
        boolean usuario_update = true;

        try {
            database.getDb().beginTransaction();
            usuario_update = database.updateUsuario(new Usuario(global.getIDUserOnline(), field_email, field_name, field_lastname, field_password, false, field_alarmB, field_alarmP, field_maxhr, field_minhr, field_tiempoEspera));
            database.getDb().setTransactionSuccessful();
        } finally {
            database.getDb().endTransaction();
            if (usuario_update)
                Toast.makeText(getApplicationContext(), R.string.profile_succesfully, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), R.string.profile_error, Toast.LENGTH_LONG).show();
        }
    }

    private void cargarDatos() {
        String value_field = null;
        Cursor cursor = database.getUsuarioByID(global.getIDUserOnline());

        value_field = database.getUserName(cursor, false);
        if(value_field != "CODE_USER_ERROR") {
            et_name.setText(value_field);
            cursor = database.getUsuarioByID(global.getIDUserOnline());
        }

        value_field = database.getUserLastname(cursor, false);
        if(value_field != "CODE_USER_ERROR") {
            et_lastname.setText(value_field);
            cursor = database.getUsuarioByID(global.getIDUserOnline());
        }

        value_field = database.getUserMail(cursor, false);
        if(value_field != "CODE_USER_ERROR") {
                et_email.setText(value_field);
                cursor = database.getUsuarioByID(global.getIDUserOnline());
            }

        value_field = database.getUserPassword(cursor, true);
        if(value_field != "CODE_USER_ERROR") {
            et_password.setText(value_field);
            cursor = database.getUsuarioByID(global.getIDUserOnline());
        }
    }


}
