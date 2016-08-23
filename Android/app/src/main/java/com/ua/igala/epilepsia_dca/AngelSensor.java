package com.ua.igala.epilepsia_dca;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import com.angel.sdk.BleScanner;
import com.angel.sdk.BluetoothInaccessibleException;

/**
 * Se trata de una clase fachada, para en el futuro facilitar
 * el cambio de pulsera
 */

public class AngelSensor {
    private Activity actividad_actual = null;
    static private int angel_state;

    private static AngelSensor instance;
    private BleScanner mBleScanner;

    private int IDLE;
    private int SCANNING;
    private int CONNECTED;

    private ListItemsAdapter mDeviceListAdapter;

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

    public AngelSensor() {
        angel_state = 0;
    }

    public static synchronized AngelSensor getInstance(){
        if(instance == null){
            instance = new AngelSensor();
        }
        return instance;
    }

    public int getState() {
        return this.angel_state;
    }
    public void setState(int state) {
        this.angel_state = state;
    }

    public void configurateStates(int idle, int scanning, int connected) {
        this.IDLE = idle;
        this.SCANNING = scanning;
        this.CONNECTED = connected;
    }


    public void ScanCallback() {
        try {
            if (mBleScanner == null) {
                mBleScanner = new BleScanner(actividad_actual, mScanCallback);
            }
        } catch (BluetoothInaccessibleException e) {
            throw new AssertionError(R.string.bluetooth_error);
        }
    }

    public void startScan() {
        mBleScanner.startScan();
    }

    public void stopScan() {
        mBleScanner.stopScan();
    }
    /**
     * Teniendo en cuenta que la lista se actualiza desde el escaner,
     * debemos tener una copia en la global de la smartband.
     */
    public void updateDeviceListAdapter(ListItemsAdapter mDeviceListAdapter) {
        this.mDeviceListAdapter = mDeviceListAdapter;
    }

    public ListItemsAdapter getDeviceListAdapter() {
        return this.mDeviceListAdapter;
    }

    /**
     * Como los datos deben aparecer en HomeActivity, debemos recibir
     * la actividad real para que la aplicación funcione
     */
    public Activity getActivity(){
        return actividad_actual;
    }
    public void setActivity(Activity mCurrentActivity){
        this.actividad_actual = mCurrentActivity;
    }
}
