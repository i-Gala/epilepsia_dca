package com.ua.igala.epilepsia_dca;


import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.angel.sdk.BleDevice;

/**
 * Variables globales de la aplicación
 */

public class Global {
    private boolean online = false;
    private String user_id = null;

    private BleDevice dispositivoBle;
    private String dispositivoBle_direccion = "";

    private static Global instance;

    private Global() {
        online = false;
        user_id = null;
        dispositivoBle = null;
        dispositivoBle_direccion = "";
    }

    public static synchronized Global getInstance(){
        if(instance == null){
            instance = new Global();
        }
        return instance;
    }

    public boolean getOnlineUser() {
        return this.online;
    }

    public void setOnlineUser(boolean o) {
        this.online = o;
    }

    public String getIDUserOnline() {
        return this.user_id;
    }

    public void setIDUserOnline(String id) {
        this.user_id = id;
    }

    public BleDevice getDispositivoBle() {
        return dispositivoBle;
    }

    public void setDispositivoBle(BleDevice dispositivoBle) {
        this.dispositivoBle = dispositivoBle;
    }

    public String getDispositivoBleDireccion() {
        return dispositivoBle_direccion;
    }

    public void setDispositivoBleDireccion(String dispositivoBle_direccion) {
        this.dispositivoBle_direccion = dispositivoBle_direccion;
    }
}
