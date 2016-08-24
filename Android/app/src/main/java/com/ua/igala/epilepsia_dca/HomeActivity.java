package com.ua.igala.epilepsia_dca;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;

import com.angel.sdk.BleDevice;

import junit.framework.Assert;

/**
 * Se trata de la HOME de la app, desde aquí se conectará la pulsera,
 * se mostrará un resumen de los datos actuales y se dará acceso
 * a todas las demás pantallas
 */

public class HomeActivity extends AppCompatActivity {
    Smartband smartband = Smartband.getInstance();

    // Definimos los estados en los que puede estar la conexión con la smartband
    private static final int IDLE = 0;
    private static final int SCANNING = 1;
    private static final int CONNECTED = 2;

    //Definimos intervalos
    private static final int RSSI_UPDATE_INTERVAL = 1000; // Milliseconds

    private Dialog mDeviceListDialog;
    private ListItemsAdapter mDeviceListAdapter;

    private BleDevice dispositivoBle;
    private Handler handler;
    private Runnable lectorPeriodico;

    private String dispositivoBle_direccion = "";


    ImageView ICON_CONNECTED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ICON_CONNECTED = (ImageView) findViewById(R.id.bluetooth_connected);

        handler = new Handler(this.getMainLooper());
        lectorPeriodico = new Runnable() {
            @Override
            public void run() {
                dispositivoBle.readRemoteRssi();
                smartband.readAccelerationEnergyMagnitude();
                mostrarCalorias();
                handler.postDelayed(lectorPeriodico, RSSI_UPDATE_INTERVAL);
            }
        };

        smartband.setActivity(this);
        smartband.configurateStates(IDLE, SCANNING, CONNECTED);
        mostrarDispositivoEnlazado();
    }

    protected void onStart() {
        super.onStart();
        if(!dispositivoBle_direccion.equals(""))
            conectar();
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        mostrarSenyal(0);
        unscheduleUpdaters();
        if(!dispositivoBle_direccion.equals(""))
            desconectar();
    }*/

    /****************************************************************
     *                            ONCLICK                           *
     ****************************************************************/

    protected void scanOnClick(View v) {
        mDeviceListAdapter = new ListItemsAdapter(this, R.layout.list_item);
        smartband.updateDeviceListAdapter(mDeviceListAdapter);
        switch (smartband.getSmartbandState()) {
            case IDLE:      startScan();    break;
            case SCANNING:  stopScan();     break;
            case CONNECTED: break;
        }
    }

    protected void logoutOnClick(View v) {
        Global.getInstance().setOnlineUser(false);
        Global.getInstance().setIDUserOnline(null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void profileOnClick(View v) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    protected void lastStatsOnClick(View v) {

    }

    protected void alarmOnClick(View v) {
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);
    }

    private void conectar() {
        if(dispositivoBle != null)
            desconectar();

        dispositivoBle = new BleDevice(this, deviceLifecycleCallback, handler);

        smartband.registrarServicios(dispositivoBle);
        smartband.setSmartbandState(CONNECTED);
        mostrarDispositivoEnlazado();
        dispositivoBle.connect(dispositivoBle_direccion);
        scheduleUpdaters();
        mostrarDesconexion();
    }

    private void desconectar() {
        dispositivoBle.disconnect();
        smartband.setSmartbandState(IDLE);
        mostrarDispositivoEnlazado();
    }

    private final BleDevice.LifecycleCallback deviceLifecycleCallback = new BleDevice.LifecycleCallback() {
        @Override
        public void onBluetoothServicesDiscovered(BleDevice dispositivo) {
            Log.d("GETSERVICIOS", "INICIO");
            dispositivo = smartband.getServicios(dispositivo);
            Log.d("GETSERVICIOS", "OBTENIDOS");

            //dibujar todos los servicios
            Log.d("GETSERVICIOS", "MOSTRAR_BATERIA");
            mostrarBateria(smartband.getValueBattery());
            /*mostrarHR();
            mostrarTemperatura();
            mostrarStepCount();*/
        }

        @Override
        public void onBluetoothDeviceDisconnected() {
            mostrarDesconexion();
            unscheduleUpdaters();
            conectar();
        }

        public void onReadRemoteRssi(final int rssi) {
            mostrarSenyal(rssi);
        }


    };

    /****************************************************************
     *                       ESCANER BLUETOOTH                      *
     ****************************************************************/

    private void startScan() {
        smartband.buscarDispositivoSmartband();
        smartband.setSmartbandState(SCANNING);
        smartband.startScan();
        showDeviceListDialog();
    }

    private void stopScan() {
        if(smartband.getSmartbandState() == SCANNING) {
            smartband.stopScan();
            smartband.setSmartbandState(IDLE);
        }
    }

    private void showDeviceListDialog() {
        mDeviceListAdapter = smartband.getDeviceListAdapter();
        mDeviceListDialog = new Dialog(this);
        mDeviceListDialog.setTitle(R.string.bluetooth_devicelist);
        mDeviceListDialog.setContentView(R.layout.device_list);
        ListView lv = (ListView) mDeviceListDialog.findViewById(R.id.lv);
        lv.setAdapter(mDeviceListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                smartband.stopScan();
                mDeviceListDialog.dismiss();

                BluetoothDevice bluetoothDevice = mDeviceListAdapter.getItem(position).getBluetoothDevice();
                Assert.assertTrue(bluetoothDevice != null);
                dispositivoBle_direccion = bluetoothDevice.getAddress();

                if(!dispositivoBle_direccion.equals("")) {
                    smartband.setSmartbandState(CONNECTED);
                    mostrarDispositivoEnlazado();

                    conectar();
                }
            }
        });

        mDeviceListDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                smartband.stopScan();
            }
        });
        mDeviceListDialog.show();
    }

    /****************************************************************
     *                      APARTADO GRÁFICO                        *
     ****************************************************************/

    private void mostrarCalorias() {
        int accelerationEnergyMagnitude = smartband.getValueAccelerationEnergyMagnitude();
        /*TextView textView = (TextView) findViewById(R.id.textview_acceleration);
        Assert.assertNotNull(textView);
        textView.setText(accelerationEnergyMagnitude + "g");

        ScaleAnimation effect =  new ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        effect.setDuration(ANIMATION_DURATION);
        effect.setRepeatMode(Animation.REVERSE);
        effect.setRepeatCount(1);

        View imageView = findViewById(R.id.imageview_acceleration);
        imageView.startAnimation(effect);*/
    }

    private void mostrarHR() {
        int value_hr = smartband.getValueHR();
    }

    private void mostrarTemperatura() {
        double value_temperatura = smartband.getValueTemperature();
    }

    private void mostrarStepCount() {
        int value_stepcount = smartband.getValueStepCount();
    }

    private void mostrarSenyal(int fuerza_senyal) {

    }

    private void mostrarBateria(int nivel_bateria) {
        int idIcono = R.drawable.bateria_0_white_t;
        Log.d("MOSTRAR_BATERIA", nivel_bateria+"");

        if(nivel_bateria <= 0)
            idIcono = R.drawable.bateria_0_white_t;
        else if(nivel_bateria <= 25)
            idIcono = R.drawable.bateria_25_white_t;
        else if(nivel_bateria <= 50)
            idIcono = R.drawable.bateria_50_white_t;
        else if(nivel_bateria <= 75)
            idIcono = R.drawable.bateria_75_white_t;
        else if(nivel_bateria <= 100)
            idIcono = R.drawable.bateria_100_white_t;

        Log.d("MOSTRAR_BATERIA", "EDITAR");
        ImageView icono = (ImageView) findViewById(R.id.battery_icon);
        icono.setBackgroundResource(idIcono);
        TextView info =(TextView) findViewById(R.id.battery_medida);
        if(nivel_bateria < 0)
            info.setText("-%");
        else
            info.setText(nivel_bateria + "%");
        Log.d("MOSTRAR_BATERIA", "FINAL");
    }

    private void mostrarDesconexion() {
        mostrarSenyal(-99);
        mostrarBateria(-99);
    }

    private void mostrarDispositivoEnlazado() {
        switch (smartband.getSmartbandState()) {
            case IDLE:      ICON_CONNECTED.setBackgroundResource(R.drawable.desconectado_white);  break;
            case CONNECTED: ICON_CONNECTED.setBackgroundResource(R.drawable.conectado_white);     break;
            default:        break;
        }
    }

    /****************************************************************
     *                           HANDLER                            *
     ****************************************************************/

    private void scheduleUpdaters() {
        handler.post(lectorPeriodico);
    }

    private void unscheduleUpdaters() {
        handler.removeCallbacks(lectorPeriodico);
    }
}
