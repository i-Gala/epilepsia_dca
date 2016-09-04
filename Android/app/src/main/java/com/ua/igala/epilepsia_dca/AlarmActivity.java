package com.ua.igala.epilepsia_dca;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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

    private EditText max_HR;
    private EditText min_HR;
    private EditText tiempo_espera;
    private TextView warning_hr;
    private TextView warning_tiempo;

    private TextView title_bluetooth;
    private TextView title_phone;
    private TextView title_hr;
    private TextView title_tiempoEspera;

    private TextView numeracion;

    private ImageView flecha;
    private ImageView flecha2;

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

        max_HR = (EditText) findViewById(R.id.rango_maxhr);
        min_HR = (EditText) findViewById(R.id.rango_minhr);
        tiempo_espera = (EditText) findViewById(R.id.tiempoesperahr);
        warning_hr = (TextView) findViewById(R.id.info_hr);
        warning_tiempo = (TextView) findViewById(R.id.info_segundos);

        flecha = (ImageView) findViewById(R.id.flecha);
        flecha2 = (ImageView) findViewById(R.id.flecha2);

        title_bluetooth = (TextView) findViewById(R.id.field_bluetooth);
        title_phone = (TextView) findViewById(R.id.field_telefono);
        title_hr = (TextView) findViewById(R.id.field_hr);
        title_tiempoEspera = (TextView) findViewById(R.id.field_segundos);

        numeracion = (TextView) findViewById(R.id.numeracion);


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
            loadScreen1();
            cargarDatos();
        }
    }

    protected void loadScreen1OnClick(View v) {
        loadScreen1();
    }

    protected void loadScreen2OnClick(View v) {
        loadScreen2();
    }

    protected void logoutOnClick(View v) {
        global.desconectarDispositivo();
        global.setOnlineUser(false);
        global.deleteSharedPreferences(getApplicationContext());
        global.setIDUserOnline(null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void homeOnClick(View v) {
        if(!global.getAlertaBle() && !global.getAlertaSMS()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setMessage(R.string.alarm_disable);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goToScreen(1);
                    dialog.cancel();
                }
            });
            dialog.show();
        } else
            goToScreen(1);
    }

    protected void profileOnClick(View v) {
        if(!global.getAlertaBle() && !global.getAlertaSMS()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setMessage(R.string.alarm_disable);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goToScreen(2);
                    dialog.cancel();
                }
            });
            dialog.show();
        } else
            goToScreen(2);
    }

    protected void sendOnClick(View v) {
        boolean exito = true;
        boolean fail_range = false;

        int max_hr = Integer.parseInt(max_HR.getText().toString());
        int min_hr = Integer.parseInt(min_HR.getText().toString());

        if(max_hr < min_hr) {
            Cursor cursor = database.getUsuarioByID(global.getIDUserOnline());
            fail_range = true;
            max_hr = Integer.parseInt(database.getUserMaxHR(cursor, false));
            min_hr = Integer.parseInt(database.getUserMinHR(cursor, true));
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
            }
        }

        try {
            Cursor cursor = database.getUsuarioByID(global.getIDUserOnline());
            database.getDb().beginTransaction();
            exito = database.updateUsuario(new Usuario(global.getIDUserOnline(), database.getUserMail(cursor, false), database.getUserName(cursor, false), database.getUserLastname(cursor, false),
                    database.getUserPassword(cursor, true), false, state_bluetooth, state_phone,
                    max_hr, min_hr, Integer.parseInt(tiempo_espera.getText().toString())));
            database.getDb().setTransactionSuccessful();
        } finally {
            database.getDb().endTransaction();
        }

        updateDetectarAtaque();

        if(fail_range) exito = false;

        if (exito)
            Toast.makeText(getApplicationContext(), R.string.alarma_succesfully, Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(getApplicationContext(), R.string.alarma_error, Toast.LENGTH_LONG).show();
            cargarDatos();
        }

    }

    private void goToScreen(int id) {
        Intent intent;
        switch (id) {
            case 0: // LOGOUT
                global.desconectarDispositivo();
                global.setOnlineUser(false);
                global.setIDUserOnline(null);
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case 1: // HOME
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
            case 2: // PROFILE
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void updateDetectarAtaque() {
        Cursor cursor = database.getUsuarioByID(global.getIDUserOnline());
        global.setMaxHR(Integer.parseInt(database.getUserMaxHR(cursor, false)));
        global.setMinHR(Integer.parseInt(database.getUserMinHR(cursor, false)));
        global.setTiempoEspera(Integer.parseInt(database.getUserTiempoEspera(cursor, false)));
        global.setAlertaBle(Integer.parseInt(database.getUserAlarmBluetooth(cursor, false)) != 0);
        global.setAlertaSMS(Integer.parseInt(database.getUserAlarmPhone(cursor, true)) != 0);
    }

    private void cargarDatos() {
        Cursor cursor = database.getUsuarioByID(global.getIDUserOnline());
        state_bluetooth = (Integer.parseInt(database.getUserAlarmBluetooth(cursor, false)) != 0);
        state_phone = (Integer.parseInt(database.getUserAlarmPhone(cursor, false)) != 0);

        max_HR.setText(database.getUserMaxHR(cursor, false));
        min_HR.setText(database.getUserMinHR(cursor, false));
        tiempo_espera.setText(database.getUserTiempoEspera(cursor, true));

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

    private void loadScreen1() {
        switch_bluetooth.setVisibility(View.VISIBLE);
        switch_phone.setVisibility(View.VISIBLE);
        phone_number.setVisibility(View.VISIBLE);
        warning_bluetooth.setVisibility(View.VISIBLE);
        warning_phone.setVisibility(View.VISIBLE);

        flecha.setVisibility(View.VISIBLE);
        flecha2.setVisibility(View.INVISIBLE);

        max_HR.setVisibility(View.INVISIBLE);
        min_HR.setVisibility(View.INVISIBLE);
        tiempo_espera.setVisibility(View.INVISIBLE);
        warning_hr.setVisibility(View.INVISIBLE);
        warning_tiempo.setVisibility(View.INVISIBLE);

        title_bluetooth.setVisibility(View.VISIBLE);
        title_phone.setVisibility(View.VISIBLE);
        title_hr.setVisibility(View.INVISIBLE);
        title_tiempoEspera.setVisibility(View.INVISIBLE);

        numeracion.setText("1/2");
    }

    private void loadScreen2() {
        switch_bluetooth.setVisibility(View.INVISIBLE);
        switch_phone.setVisibility(View.INVISIBLE);
        phone_number.setVisibility(View.INVISIBLE);
        warning_bluetooth.setVisibility(View.INVISIBLE);
        warning_phone.setVisibility(View.INVISIBLE);

        flecha.setVisibility(View.INVISIBLE);
        flecha2.setVisibility(View.VISIBLE);

        max_HR.setVisibility(View.VISIBLE);
        min_HR.setVisibility(View.VISIBLE);
        tiempo_espera.setVisibility(View.VISIBLE);
        warning_hr.setVisibility(View.VISIBLE);
        warning_tiempo.setVisibility(View.VISIBLE);

        title_bluetooth.setVisibility(View.INVISIBLE);
        title_phone.setVisibility(View.INVISIBLE);
        title_hr.setVisibility(View.VISIBLE);
        title_tiempoEspera.setVisibility(View.VISIBLE);

        numeracion.setText("2/2");
    }

}
