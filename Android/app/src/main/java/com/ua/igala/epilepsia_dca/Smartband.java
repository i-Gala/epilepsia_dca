package com.ua.igala.epilepsia_dca;

import android.app.Activity;

import com.angel.sdk.BleDevice;

/**
 * Created by Gala on 24/08/2016.
 */
public class Smartband {
    private AngelSensor AngelSensor;

    private static Smartband instance;

    private Smartband(){ AngelSensor = new AngelSensor(); }

    public static synchronized Smartband getInstance(){
        if(instance == null){
            instance = new Smartband();
        }
        return instance;
    }

    /****************************************************************
     *                       ESTADO SMARTBAND                       *
     ****************************************************************/

    public void configurateStates(int idle, int scanning, int connected) {
        AngelSensor.configurateStates(idle, scanning, connected);
    }

    public int getSmartbandState() {
        return AngelSensor.getState();
    }

    public void setSmartbandState(int state) {
        AngelSensor.setState(state);
    }

    /****************************************************************
     *                            BLUETOOTH                         *
     ****************************************************************/

    public void buscarDispositivoSmartband() {
        AngelSensor.ScanCallback();
    }

    public void startScan() {
        AngelSensor.startScan();
    }

    public void stopScan() {
        AngelSensor.stopScan();
    }

    /****************************************************************
     *                      LISTA DISPOSITIVOS                      *
     ****************************************************************/

    public void updateDeviceListAdapter(ListItemsAdapter mDeviceListAdapter) {
        AngelSensor.updateDeviceListAdapter(mDeviceListAdapter);
    }

    public ListItemsAdapter getDeviceListAdapter() {
        return AngelSensor.getDeviceListAdapter();
    }

    /****************************************************************
     *                         ACTIVIDAD                            *
     ****************************************************************/

    public Activity getActivity() {
        return AngelSensor.getActivity();
    }

    public void setActivity(Activity actividad){
        AngelSensor.setActivity(actividad);
    }

    /****************************************************************
     *                ACCELERATION ENERGY MAGNITUDE                 *
     *                          (CALORIAS)                          *
     ****************************************************************/

    public void readAccelerationEnergyMagnitude() {
        AngelSensor.readAccelerationEnergyMagnitude();
    }

    public int getValueAccelerationEnergyMagnitude() {
        return AngelSensor.getValueAccelerationEnergyMagnitude();
    }

    /****************************************************************
     *                      ACTIVITY MONITORING                     *
     *                           (PASOS)                            *
     ****************************************************************/

    public int getValueStepCount() {
        return AngelSensor.getValueStepCount();
    }

    /****************************************************************
     *                           BATERÍA                            *
     ****************************************************************/

    public int getValueBattery() {
        return AngelSensor.getValueBattery();
    }

    /****************************************************************
     *                       TEMPERATURA                            *
     ****************************************************************/

    public double getValueTemperature() {
        return AngelSensor.getValueTemperature();
    }

    /****************************************************************
     *                        HEART RATE                            *
     ****************************************************************/

    public int getValueHR() {
        return AngelSensor.getValueHR();
    }

    /****************************************************************
     *                       PRESTACIONES                           *
     ****************************************************************/

    public BleDevice getServicios(BleDevice dispositivo) {
        return AngelSensor.getServices(dispositivo);
    }

    public boolean registrarServicios(BleDevice dispositivo) {
        return AngelSensor.registerServices(dispositivo);
    }


}
