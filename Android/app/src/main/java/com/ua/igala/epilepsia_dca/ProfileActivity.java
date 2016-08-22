package com.ua.igala.epilepsia_dca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ua.igala.epilepsia_dca.sqlite.OperacionesBD;

public class ProfileActivity extends AppCompatActivity {

    private OperacionesBD database;
    private Global global = Global.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = OperacionesBD.obtenerInstancia(getApplicationContext());
        Log.d("IDUSERCONECTADO",global.getIDUserOnline());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(global.getOnlineUser() == false) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    protected void logoutOnClick(View v) {
        //Global.getInstance().setOnlineUser(false);
        //Global.getInstance().setIDUserOnline(null);
        global.setOnlineUser(false);
        global.setIDUserOnline(null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void homeOnClick(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


}
