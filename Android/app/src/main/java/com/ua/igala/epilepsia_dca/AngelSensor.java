package com.ua.igala.epilepsia_dca;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import com.angel.sdk.BleCharacteristic;
import com.angel.sdk.BleDevice;
import com.angel.sdk.BleScanner;
import com.angel.sdk.BluetoothInaccessibleException;
import com.angel.sdk.ChAccelerationEnergyMagnitude;
import com.angel.sdk.ChBatteryLevel;
import com.angel.sdk.ChHeartRateMeasurement;
import com.angel.sdk.ChStepCount;
import com.angel.sdk.ChTemperatureMeasurement;
import com.angel.sdk.SrvActivityMonitoring;
import com.angel.sdk.SrvBattery;
import com.angel.sdk.SrvHealthThermometer;
import com.angel.sdk.SrvHeartRate;

import junit.framework.Assert;

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

    private ChAccelerationEnergyMagnitude mChAccelerationEnergyMagnitude = null;
    private int value_accelerationEnergyMagnitude;    // Calorias
    private int value_HR;
    private double value_temperature;
    private int value_battery;
    private int value_stepCount;

    /****************************************************************
     *                           CONSTRUCTOR                        *
     ****************************************************************/

    public AngelSensor() {
        angel_state = 0;
    }

    public static synchronized AngelSensor getInstance(){
        if(instance == null){
            instance = new AngelSensor();
        }
        return instance;
    }

    /****************************************************************
     *                      LISTA DISPOSITIVOS                      *
     ****************************************************************/

    BleScanner.ScanCallback mScanCallback = new BleScanner.ScanCallback() {
        @Override
        public void onBluetoothDeviceFound(BluetoothDevice device) {
            // Agrega los dispositivos de Bluetooth encontrados a la lista de dispositivos.
            // Después el usuario podrá hacer click en el dispositivo para conectarlo.
            if (device.getName() != null && device.getName().startsWith("Angel")) {
                ListItem newDevice = new ListItem(device.getName(), device.getAddress(), device);
                mDeviceListAdapter.add(newDevice);
                mDeviceListAdapter.addItem(newDevice);
                mDeviceListAdapter.notifyDataSetChanged();
            }
        }
    };

    public void updateDeviceListAdapter(ListItemsAdapter mDeviceListAdapter) {
        // Necesitamos enviar la lista porque se modifica en el escaner de dispositivos.
        this.mDeviceListAdapter = mDeviceListAdapter;
    }

    public ListItemsAdapter getDeviceListAdapter() {
        return this.mDeviceListAdapter;
    }

    /****************************************************************
     *                       ESTADO SMARTBAND                       *
     ****************************************************************/

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

    /****************************************************************
     *                            BLUETOOTH                         *
     ****************************************************************/

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

    /****************************************************************
     *                         ACTIVIDAD                            *
     ****************************************************************/

    public Activity getActivity(){
        return actividad_actual;
    }

    public void setActivity(Activity actividad){
        // Como los datos deben aparecer en HomeActivity, debemos recibir
        // la actividad real para que la aplicación funcione
        this.actividad_actual = actividad;
    }


    /****************************************************************
     *                ACCELERATION ENERGY MAGNITUDE                 *
     *                          (CALORIAS)                          *
     ****************************************************************/

    private final BleCharacteristic.ValueReadyCallback<ChAccelerationEnergyMagnitude.AccelerationEnergyMagnitudeValue> mAccelerationEnergyMagnitudeListener =
            new BleCharacteristic.ValueReadyCallback<ChAccelerationEnergyMagnitude.AccelerationEnergyMagnitudeValue>() {
                @Override
                public void onValueReady(final ChAccelerationEnergyMagnitude.AccelerationEnergyMagnitudeValue accelerationEnergyMagnitudeValue) {
                    value_accelerationEnergyMagnitude = accelerationEnergyMagnitudeValue.value;
                }
            };

    public void readAccelerationEnergyMagnitude() {
        if (mChAccelerationEnergyMagnitude != null) {
            mChAccelerationEnergyMagnitude.readValue(mAccelerationEnergyMagnitudeListener);
        }
    }

    public int getValueAccelerationEnergyMagnitude() {
        return value_accelerationEnergyMagnitude;
    }

    public void addServiceAccelerationEnergyMagnitude(BleDevice dispositivo) {
        mChAccelerationEnergyMagnitude = dispositivo.getService(SrvActivityMonitoring.class).getChAccelerationEnergyMagnitude();
        Assert.assertNotNull(mChAccelerationEnergyMagnitude);
    }

    /****************************************************************
     *                      ACTIVITY MONITORING                     *
     *                           (PASOS)                            *
     ****************************************************************/

    private void addServiceActivityMonitoring(BleDevice dispositivo) {
        dispositivo.getService(SrvActivityMonitoring.class).getStepCount().enableNotifications(mStepCountListener);
    }

    private final BleCharacteristic.ValueReadyCallback<ChStepCount.StepCountValue> mStepCountListener =
            new BleCharacteristic.ValueReadyCallback<ChStepCount.StepCountValue>() {
                @Override
                public void onValueReady(final ChStepCount.StepCountValue stepCountValue) {
                    value_stepCount = stepCountValue.value;
                }
            };

    public int getValueStepCount() {
        return value_stepCount;
    }

    /****************************************************************
     *                           BATERÍA                            *
     ****************************************************************/

    private void addServiceBattery(BleDevice dispositivo) {
        dispositivo.getService(SrvBattery.class).getBatteryLevel().enableNotifications(mBatteryLevelListener);
    }

    private final BleCharacteristic.ValueReadyCallback<ChBatteryLevel.BatteryLevelValue> mBatteryLevelListener =
            new BleCharacteristic.ValueReadyCallback<ChBatteryLevel.BatteryLevelValue>() {
                @Override
                public void onValueReady(final ChBatteryLevel.BatteryLevelValue batteryLevel) {
                    value_battery = batteryLevel.value;
                }
            };

    public int getValueBattery() {
        return value_battery;
    }

    /****************************************************************
     *                       TEMPERATURA                            *
     ****************************************************************/

    private void addServiceTemperature(BleDevice dispositivo) {
        dispositivo.getService(SrvHealthThermometer.class).getTemperatureMeasurement().enableNotifications(mTemperatureListener);
    }

    private final BleCharacteristic.ValueReadyCallback<ChTemperatureMeasurement.TemperatureMeasurementValue> mTemperatureListener =
            new BleCharacteristic.ValueReadyCallback<ChTemperatureMeasurement.TemperatureMeasurementValue>() {
                @Override
                public void onValueReady(final ChTemperatureMeasurement.TemperatureMeasurementValue temperature) {
                    value_temperature = temperature.getTemperatureMeasurement();
                }
            };

    public double getValueTemperature() {
        return value_temperature;
    }

    /****************************************************************
     *                        HEART RATE                            *
     ****************************************************************/

    private void addServiceHR(BleDevice dispositivo) {
        dispositivo.getService(SrvHeartRate.class).getHeartRateMeasurement().enableNotifications(mHeartRateListener);
    }

    private final BleCharacteristic.ValueReadyCallback<ChHeartRateMeasurement.HeartRateMeasurementValue> mHeartRateListener = new BleCharacteristic.ValueReadyCallback<ChHeartRateMeasurement.HeartRateMeasurementValue>() {
        @Override
        public void onValueReady(final ChHeartRateMeasurement.HeartRateMeasurementValue hrMeasurement) {
            value_HR = hrMeasurement.getHeartRateMeasurement();
        }
    };

    public int getValueHR() {
        return value_HR;
    }

    /****************************************************************
     *                       PRESTACIONES                           *
     ****************************************************************/

    public BleDevice getServices(BleDevice dispositivo) {
        addServiceHR(dispositivo);
        addServiceTemperature(dispositivo);
        addServiceBattery(dispositivo);
        addServiceActivityMonitoring(dispositivo);
        addServiceAccelerationEnergyMagnitude(dispositivo);

        return dispositivo;
    }

    public boolean registerServices(BleDevice dispositivo) {
        boolean exito = false;
        try {
            dispositivo.registerServiceClass(SrvHeartRate.class);
            dispositivo.registerServiceClass(SrvHealthThermometer.class);
            dispositivo.registerServiceClass(SrvBattery.class);
            dispositivo.registerServiceClass(SrvActivityMonitoring.class);
            exito = true;
        } catch (NoSuchMethodException e) {
            throw new AssertionError();
        } catch (IllegalAccessException e) {
            throw new AssertionError();
        } catch (InstantiationException e) {
            throw new AssertionError();
        }

        return exito;
    }


}
