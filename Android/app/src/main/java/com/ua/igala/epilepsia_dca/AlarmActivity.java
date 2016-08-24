package com.ua.igala.epilepsia_dca;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ua.igala.epilepsia_dca.model.TelefonoEmergencias;
import com.ua.igala.epilepsia_dca.model.Usuario;
import com.ua.igala.epilepsia_dca.sqlite.OperacionesBD;

public class AlarmActivity extends AppCompatActivity {
    private OperacionesBD database;
    private Global global = Global.getInstance();

    private Switch switch_bluetooth;
    private Switch switch_phone;
    private EditText phone_number;
    private TextView warning_bluetooth;
    private TextView warning_phone;

    private boolean state_bluetooth = false;
    private boolean state_phone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        database = OperacionesBD.obtenerInstancia(getApplicationContext());

        switch_bluetooth = (Switch) findViewById(R.id.switch_bluetooth);
        switch_phone = (Switch) findViewById(R.id.switch_phone);
        phone_number = (EditText) findViewById(R.id.phone_number);
        warning_bluetooth = (TextView) findViewById(R.id.text_bluetooth);
        warning_phone = (TextView) findViewById(R.id.text_telefono);

        switch_phone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  // Activar/desactivar switch de ALERTA TELÉFONO
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkPhone();
                state_phone = switch_phone.isChecked();
            }
        });

        switch_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  // Activar/desactivar switch de ALERTA BLUETOOTH
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkBluetooth();
                state_bluetooth = switch_bluetooth.isChecked();
            }
        });

        cargarDatos();  // Actualizar los datos que vemos en pantalla
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void homeOnClick(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    protected void profileOnClick(View v) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    protected void sendOnClick(View v) {
        boolean exito = true;

        try {
            Cursor cursor = database.getUsuarioByID(global.getIDUserOnline());
            database.getDb().beginTransaction();
            exito = database.updateUsuario(new Usuario(global.getIDUserOnline(), database.getUserMail(cursor, false), database.getUserName(cursor, false), database.getUserLastname(cursor, false),
                    database.getUserPassword(cursor, true), false, state_bluetooth, state_phone));
            database.getDb().setTransactionSuccessful();
        } finally {
            database.getDb().endTransaction();
        }



        if(state_phone) {   // Si el teléfono está activo
            if(!phone_number.getText().toString().equals("")) {
                Cursor cursor = database.getTelefonoEmergenciasByUser(global.getIDUserOnline());
                String phone = database.getPhone(cursor);
                int numberphone = Integer.parseInt(phone_number.getText().toString());

                if(phone.equals("PHONE_ERROR")) {   // No hay números de teléfono agregados
                    try {
                        database.getDb().beginTransaction();
                        database.addTelefonoEmergencias(new TelefonoEmergencias(null, numberphone, global.getIDUserOnline()));
                        database.getDb().setTransactionSuccessful();
                    } finally {
                        database.getDb().endTransaction();
                    }
                } else {
                    try {
                        cursor = database.getTelefonoEmergenciasByUser(global.getIDUserOnline());
                        String idPhone = database.getTelefonoEmergenciasID(cursor);

                        database.getDb().beginTransaction();
                        exito = database.updateTelefonoEmergencias(new TelefonoEmergencias(idPhone, Integer.parseInt(phone_number.getText().toString()), global.getIDUserOnline()));
                        database.getDb().setTransactionSuccessful();
                    } finally {
                        database.getDb().endTransaction();
                    }
                }
            } else {             // Si se ha activado el teléfono y no se ha agregado teléfono: ERROR!!!
                exito = false;
                state_phone = false;
                try {
                    Cursor cursor = database.getUsuarioByID(global.getIDUserOnline());
                    database.getDb().beginTransaction();
                    database.updateUsuario(new Usuario(global.getIDUserOnline(), database.getUserMail(cursor, false), database.getUserName(cursor, false), database.getUserLastname(cursor, false),
                            database.getUserPassword(cursor, true), false, state_bluetooth, state_phone));
                    database.getDb().setTransactionSuccessful();
                } finally {
                    database.getDb().endTransaction();
                }
                cargarDatos();
            }
        }


        if (exito)
            Toast.makeText(getApplicationContext(), R.string.alarma_succesfully, Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(getApplicationContext(), R.string.alarma_error, Toast.LENGTH_LONG).show();
            cargarDatos();
        }

    }

    private void cargarDatos() {
        Cursor cursor = database.getUsuarioByID(global.getIDUserOnline());
        state_bluetooth = (Integer.parseInt(database.getUserAlarmBluetooth(cursor, false)) != 0);
        state_phone = (Integer.parseInt(database.getUserAlarmPhone(cursor, true)) != 0);

        switch_bluetooth.setChecked(state_bluetooth);
        checkBluetooth();
        switch_phone.setChecked(state_phone);
        checkPhone();
        checkNumberPhone();
    }

    private void checkBluetooth() {
        if(switch_bluetooth.isChecked())
            warning_bluetooth.setText(R.string.bluetooth_enable);
        else
            warning_bluetooth.setText(R.string.bluetooth_disable);
    }

    private void checkPhone() {
        if (switch_phone.isChecked()){
            warning_phone.setText(R.string.sms_enable);
            phone_number.setVisibility(View.VISIBLE);
        } else {
            warning_phone.setText(R.string.sms_disable);
            phone_number.setVisibility(View.INVISIBLE);
        }
    }

    private void checkNumberPhone() {
        Cursor cursor = database.getTelefonoEmergenciasByUser(global.getIDUserOnline());
        String phone = database.getPhone(cursor);
        if(phone.equals("PHONE_ERROR")) {
            phone_number.setText("");
        } else {
            phone_number.setText(phone+"");
        }
    }

}
