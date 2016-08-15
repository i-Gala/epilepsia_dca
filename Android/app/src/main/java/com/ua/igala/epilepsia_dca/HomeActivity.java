package com.ua.igala.epilepsia_dca;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;

import com.angel.sdk.BleScanner;
import com.angel.sdk.BluetoothInaccessibleException;

import junit.framework.Assert;

/**
 * Se trata de la HOME de la app, desde aquí se conectará la pulsera,
 * se mostrará un resumen de los datos actuales y se dará acceso
 * a todas las demás pantallas
 */

public class HomeActivity extends AppCompatActivity {
    // Definimos los estados en los que puede estar la conexión con la smartband
    private static final int IDLE = 0;
    private static final int SCANNING = 1;
    private static final int CONNECTED = 2;

    static private int angel_state = IDLE;  // Estado actual

    private BleScanner mBleScanner;

    private Dialog mDeviceListDialog;
    private ListItemsAdapter mDeviceListAdapter;


    ImageView ICON_CONNECTED;

    /**
     * Agrega los dispositivos de Bluetooth encontrados a la lista de dispositivas. Después
     * el usuario podrá hacer click en el dispositivo para conectarlo.
     */
    BleScanner.ScanCallback mScanCallback = new BleScanner.ScanCallback() {
        @Override
        public void onBluetoothDeviceFound(BluetoothDevice device) {
            if (device.getName() != null && device.getName().startsWith("Angel")) {
                ListItem newDevice = new ListItem(device.getName(), device.getAddress(), device);
                mDeviceListAdapter.add(newDevice);
                mDeviceListAdapter.addItem(newDevice);
                mDeviceListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ICON_CONNECTED = (ImageView) findViewById(R.id.bluetooth_connected);

        switch (angel_state) {
            case IDLE:      ICON_CONNECTED.setBackgroundResource(R.drawable.desconectado_white);  break;
            case CONNECTED: ICON_CONNECTED.setBackgroundResource(R.drawable.conectado_white);     break;
            default:        break;
        }

    }

    protected void scanOnClick(View v) {
        mDeviceListAdapter = new ListItemsAdapter(this, R.layout.list_item);
        switch (angel_state) {
            case IDLE:      startScan();    break;
            case SCANNING:  stopScan();     break;
            case CONNECTED: break;
        }
    }

    protected void logoutOnClick(View v) {
        //Global global = ((Global)getApplicationContext());
        //global.setOnlineUser(false);
        //global.setIDUserOnline(-1);
        Global.getInstance().setOnlineUser(false);
        Global.getInstance().setIDUserOnline(-1);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startScan() {
        try {
            if (mBleScanner == null)
                mBleScanner = new BleScanner(this, mScanCallback);
        } catch (BluetoothInaccessibleException e) {
            throw new AssertionError(R.string.bluetooth_error);
        }

        angel_state = SCANNING;
        mBleScanner.startScan();
        showDeviceListDialog();
    }

    private void stopScan() {
        if (angel_state == SCANNING) {
            mBleScanner.stopScan();
            angel_state = IDLE;
        }
    }

    private void showDeviceListDialog() {
        mDeviceListDialog = new Dialog(this);
        mDeviceListDialog.setTitle(R.string.bluetooth_devicelist);
        mDeviceListDialog.setContentView(R.layout.device_list);
        ListView lv = (ListView) mDeviceListDialog.findViewById(R.id.lv);
        lv.setAdapter(mDeviceListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                stopScan();
                mDeviceListDialog.dismiss();

                BluetoothDevice bluetoothDevice = mDeviceListAdapter.getItem(position).getBluetoothDevice();
                Assert.assertTrue(bluetoothDevice != null);
                Intent intent = new Intent(parent.getContext(), HomeActivity.class);
                intent.putExtra("ble_device_address", bluetoothDevice.getAddress());
                startActivity(intent);
                ICON_CONNECTED.setBackgroundResource(R.drawable.conectado_white);
                angel_state = CONNECTED;
            }
        });

        mDeviceListDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                stopScan();
            }
        });
        mDeviceListDialog.show();
    }
}
