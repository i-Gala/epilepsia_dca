package com.ua.igala.epilepsia_dca;

import android.app.Activity;

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

    public void configurateStates(int idle, int scanning, int connected) {
        AngelSensor.configurateStates(idle, scanning, connected);
    }

    public int getSmartbandState() {
        return AngelSensor.getState();
    }

    public void setSmartbandState(int state) {
        AngelSensor.setState(state);
    }

    public void ScanCallback() {
        AngelSensor.ScanCallback();
    }

    public void startScan() {
        AngelSensor.startScan();
    }

    public void stopScan() {
        AngelSensor.stopScan();
    }

    public void updateDeviceListAdapter(ListItemsAdapter mDeviceListAdapter) {
        AngelSensor.updateDeviceListAdapter(mDeviceListAdapter);
    }

    public ListItemsAdapter getDeviceListAdapter() {
        return AngelSensor.getDeviceListAdapter();
    }

    public Activity getActivity() {
        return AngelSensor.getActivity();
    }

    public void setActivity(Activity actividad){
        AngelSensor.setActivity(actividad);
    }



}
