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

    private boolean detectado_ataque;
    private int max_hr;
    private int min_hr;
    private int tiempo_espera;
    private int segundos_activo;
    private int segundos_inactivo;

    private static Global instance;

    private Global() {
        online = false;
        user_id = null;
        dispositivoBle = null;
        dispositivoBle_direccion = "";
        detectado_ataque = false;
        max_hr = 60;
        min_hr = 100;
        tiempo_espera = 120;
        segundos_activo = 0;
        segundos_inactivo = 0;
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
        return this.dispositivoBle;
    }

    public void setDispositivoBle(BleDevice dispositivoBle) {
        this.dispositivoBle = dispositivoBle;
    }

    public String getDispositivoBleDireccion() {
        return this.dispositivoBle_direccion;
    }

    public void setDispositivoBleDireccion(String dispositivoBle_direccion) {
        this.dispositivoBle_direccion = dispositivoBle_direccion;
    }

    public void desconectarDispositivo() {
        if(dispositivoBle != null)
            dispositivoBle.disconnect();
    }

    public boolean getDetectarAtaque() {
        return this.detectado_ataque;
    }

    public void setDetectarAtaque(boolean detectado_ataque) {
        this.detectado_ataque = detectado_ataque;
    }

    public int getMaxHR() {
        return this.max_hr;
    }

    public void setMaxHR(int max_hr) {
        this.max_hr = max_hr;
    }

    public int getMinHR() {
        return this.min_hr;
    }

    public void setMinHR(int min_hr) {
        this.min_hr = min_hr;
    }

    public int getTiempoEspera() {
        return this.tiempo_espera;
    }

    public void setTiempoEspera(int segundos) {
        this.tiempo_espera = segundos * 1000;   // Almacenamos milisegundos
    }

    public int getSegundosACTIVO() {
        return this.segundos_activo;
    }

    public void setSegundosACTIVO(int segundos_activo) {
        this.segundos_activo = segundos_activo;
    }

    public int getSegundosINACTIVO() {
        return this.segundos_inactivo;
    }

    public void setSegundosINACTIVO(int segundos_inactivo) {
        this.segundos_inactivo = segundos_inactivo;
    }
}
