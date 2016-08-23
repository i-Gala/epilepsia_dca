package com.ua.igala.epilepsia_dca;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import com.angel.sdk.BleScanner;
import com.angel.sdk.BluetoothInaccessibleException;

import junit.framework.Assert;

/**
 * Created by Gala on 23/08/2016.
 */

public class AngelSensor {
    private Activity mCurrentActivity = null;
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

    private AngelSensor() {
        angel_state = 0;
    }

    public static synchronized AngelSensor getInstance(){
        if(instance == null){
            instance = new AngelSensor();
        }
        return instance;
    }

    public int getAngelState() {
        return this.angel_state;
    }

    public void configurateStates(int idle, int scanning, int connected) {
        this.IDLE = idle;
        this.SCANNING = scanning;
        this.CONNECTED = connected;
    }

    public void setAngelState(int state) {
        this.angel_state = state;
    }

    public void startScan() {
        try {
            if (mBleScanner == null) {
                mBleScanner = new BleScanner(mCurrentActivity, mScanCallback);
            }
        } catch (BluetoothInaccessibleException e) {
            throw new AssertionError(R.string.bluetooth_error);
        }

        angel_state = SCANNING;
        mBleScanner.startScan();
    }

    public void stopScan() {
        if (angel_state == SCANNING) {
            mBleScanner.stopScan();
            angel_state = IDLE;
        }
    }

    public void updateDeviceListAdapter(ListItemsAdapter mDeviceListAdapter) {
        this.mDeviceListAdapter = mDeviceListAdapter;
    }

    public ListItemsAdapter getDeviceListAdapter() {
        return this.mDeviceListAdapter;
    }

    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }
}
