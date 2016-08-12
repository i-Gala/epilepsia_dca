package com.example.gala.tfg_gala;

/**
 * TABLA DE USUARIO
 */

public class Userdata {
    int _id;
    String name;
    String lastname;
    String email;
    String password;

    boolean alert_sms;
    String  alert_mobile;
    boolean alert_bluetooth;

    boolean first_time;

    // Constructor vacío
    public Userdata() {
        alert_sms = false;
        alert_bluetooth = false;
        alert_mobile = null;
        first_time = true;
    }

    // Constructor
    public Userdata(int id, String n, String ln, String e) {
        this._id = id;
        this.name = n;
        this.lastname = ln;
        this.email = e;

        alert_sms = false;
        alert_bluetooth = false;
        alert_mobile = null;
        first_time = true;
    }

    /**********************************************/
    /*                  GET / SET                 */
    /**********************************************/

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String ln) {
        this.lastname = ln;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String pw) {
        this.password = pw;
    }

    public boolean getAlertSMS() {
        return this.alert_sms;
    }

    public void setAlertSMS(boolean b) {
        this.alert_sms = b;
    }

    public String getAlertMobileNo() {
        return this.alert_mobile;
    }

    public void setAlertMobileNo(String No) {
        this.alert_mobile = No;
    }

    public boolean getAlertBluetooth() {
        return this.alert_bluetooth;
    }

    public void setAlertBluetooth(boolean b) {
        this.alert_bluetooth = b;
    }

    public boolean getFirstTime() {
        return this.first_time;
    }

    public void disableFirstTime() {
        this.first_time = false;
    }
}
