package com.ua.igala.epilepsia_dca;


/**
 * Variables globales de la aplicación
 */

public class Global {
    private boolean online = false;
    private int user_id = -1;
    private static Global instance;

    static {
        instance = new Global();
    }

    private Global() {
        online = false;
        user_id = -1;
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

    public int getIDUserOnline() {
        return user_id;
    }

    public void setIDUserOnline(int id) {
        user_id = id;
    }
}
