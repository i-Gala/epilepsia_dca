package com.ua.igala.epilepsia_dca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Esta es la primera actividad que se inicia al abrir la aplicación,
 * si el usuario no ha iniciado sesión deberá iniciarla o - en caso
 * de no tener una cuenta - registrarse.
 */

public class MainActivity extends AppCompatActivity {
    private Global global = Global.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        global.setOnlineUser(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(global.getOnlineUser() == true) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    protected void loginOnClick(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    protected void signUpOnClick(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
