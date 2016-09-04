package com.ua.igala.epilepsia_dca;


import android.content.Context;
import android.content.SharedPreferences;

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
    private boolean alerta_bluetooth;
    private boolean alerta_sms;
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
        alerta_bluetooth = false;
        alerta_sms = false;
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

    public boolean getAlertaBle() {
        return this.alerta_bluetooth;
    }

    public void setAlertaBle(boolean alerta_bluetooth) {
        this.alerta_bluetooth = alerta_bluetooth;
    }

    public boolean getAlertaSMS() {
        return this.alerta_sms;
    }

    public void setAlertaSMS(boolean alerta_sms) {
        this.alerta_sms = alerta_sms;
    }

    public void guardarSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ADcaEpilepsiaConfig", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ID_USER", user_id);
        editor.putBoolean("CONEXION", online);
        editor.commit();
    }

    public void cargarSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ADcaEpilepsiaConfig", context.MODE_PRIVATE);
        user_id = preferences.getString("ID_USER", null);
        online = preferences.getBoolean("CONEXION", false);
    }

    public void deleteSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ADcaEpilepsiaConfig", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("ID_USER");
        editor.remove("CONEXION");
        editor.clear();
        editor.commit();


    }
}
