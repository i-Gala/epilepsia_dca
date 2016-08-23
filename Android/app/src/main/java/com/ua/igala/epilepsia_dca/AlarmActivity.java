package com.ua.igala.epilepsia_dca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AlarmActivity extends AppCompatActivity {
    Global global = Global.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
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
        global.setOnlineUser(false);
        global.setIDUserOnline(null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void profileOnClick(View v) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
