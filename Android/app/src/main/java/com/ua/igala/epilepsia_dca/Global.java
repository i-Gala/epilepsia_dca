package com.ua.igala.epilepsia_dca;


/**
 * Variables globales de la aplicación
 */

public class Global {
    private boolean online = false;
    private String user_id = null;
    private static Global instance = new Global();

    private Global() {
        online = false;
        user_id = null;
    }

    public static Global getInstance() {
        return Global.instance;
    }

    public boolean getOnlineUser() {
        return online;
    }

    public void setOnlineUser(boolean o) {
        online = o;
    }

    public String getIDUserOnline() {
        return user_id;
    }

    public void setIDUserOnline(String id) {
        user_id = id;
    }
}
