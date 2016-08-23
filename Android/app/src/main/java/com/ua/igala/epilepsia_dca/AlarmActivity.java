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

        switch_phone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switch_phone.isChecked()) {
                    warning_phone.setText(R.string.sms_enable);
                    phone_number.setVisibility(View.VISIBLE);
                } else {
                    warning_phone.setText(R.string.sms_disable);
                    phone_number.setVisibility(View.INVISIBLE);
                }
            }
        });

        switch_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switch_bluetooth.isChecked())
                    warning_bluetooth.setText(R.string.bluetooth_enable);
                else
                    warning_bluetooth.setText(R.string.bluetooth_disable);
            }
        });

        cargarDatos();
        if(!state_phone)
            phone_number.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(global.getOnlineUser() == false) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    protected void logoutOnClick(View v) {
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

    }

    private void cargarDatos() {
        Cursor cursor = database.getUsuarioByID(global.getIDUserOnline());
        state_bluetooth = (Integer.parseInt(database.getUserAlarmBluetooth(cursor, false)) != 0);
        state_phone = (Integer.parseInt(database.getUserAlarmPhone(cursor, true)) != 0);

        checkBluetooth();
        checkPhone();
    }

    private void checkBluetooth() {
        if(state_bluetooth != switch_bluetooth.isChecked()) {
            switch_bluetooth.setChecked(state_bluetooth);
            if(switch_bluetooth.isChecked())
                warning_bluetooth.setText(R.string.bluetooth_enable);
            else
                warning_bluetooth.setText(R.string.bluetooth_disable);
        }
    }

    private void checkPhone() {
        if (state_phone != switch_phone.isChecked()) {
            switch_phone.setChecked(state_phone);
            if (switch_phone.isChecked()){
                warning_phone.setText(R.string.sms_enable);
                phone_number.setVisibility(View.VISIBLE);
            } else {
                warning_phone.setText(R.string.sms_disable);
                phone_number.setVisibility(View.INVISIBLE);
            }
        }
    }

}
