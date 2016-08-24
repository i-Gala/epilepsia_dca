package com.ua.igala.epilepsia_dca;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;

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

    private BleDevice dispositivosBluetooth;
    private Handler handler;
    private Runnable lectorPeriodico;

    private String bluetooth_nombre;


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
                dispositivosBluetooth.readRemoteRssi();
                smartband.readAccelerationEnergyMagnitude();
                mostrarCalorias();
                handler.postDelayed(lectorPeriodico, RSSI_UPDATE_INTERVAL);
            }
        };

        smartband.setActivity(this);
        smartband.configurateStates(IDLE, SCANNING, CONNECTED);
        switch (smartband.getSmartbandState()) {
            case IDLE:      ICON_CONNECTED.setBackgroundResource(R.drawable.desconectado_white);  break;
            case CONNECTED: ICON_CONNECTED.setBackgroundResource(R.drawable.conectado_white);     break;
            default:        break;
        }
    }

    protected void onStart() {
        super.onStart();
        conectar();
    }

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

    private void startScan() {
        smartband.ScanCallback();
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

    private void conectar() {
        if(dispositivosBluetooth != null)
            dispositivosBluetooth.disconnect();
        dispositivosBluetooth = new BleDevice(this, deviceLivecycleCallback, handler);

        //incomplete
    }

    private final BleDevice.LifecycleCallback deviceLivecycleCallback = new BleDevice.LifecycleCallback() {
        @Override
        public void onBluetoothServicesDiscovered(BleDevice dispositivo) {
            dispositivo = smartband.getServices(dispositivo);

            //dibujar todos los servicios
        }

        @Override
        public void onBluetoothDeviceDisconnected() {

        }

        public void onReadRemoteRssi(final int rssi) {

        }


    };

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
                bluetooth_nombre = bluetoothDevice.getAddress();
                ICON_CONNECTED.setBackgroundResource(R.drawable.conectado_white);
                smartband.setSmartbandState(CONNECTED);
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
}
