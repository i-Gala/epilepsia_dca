package com.ua.igala.epilepsia_dca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ua.igala.epilepsia_dca.sqlite.OperacionesBD;

public class ProfileActivity extends AppCompatActivity {

    private OperacionesBD database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = OperacionesBD.obtenerInstancia(getApplicationContext());
    }

    protected void logoutOnClick(View v) {
        Global.getInstance().setOnlineUser(false);
        Global.getInstance().setIDUserOnline(null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void homeOnClick(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


}
