package com.ua.igala.epilepsia_dca;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ua.igala.epilepsia_dca.sqlite.OperacionesBD;

import java.util.ArrayList;

/**
 * Gestiona el inicio de sesión en la aplicación.
 */

public class LoginActivity extends AppCompatActivity {

    private OperacionesBD database;
    private Global global = Global.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = OperacionesBD.obtenerInstancia(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(global.getOnlineUser() == true) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    protected void sendOnClick(View v){
        EditText et_email = (EditText) findViewById(R.id.field_email);
        EditText et_password = (EditText) findViewById(R.id.field_password);
        String field_email = et_email.getText().toString();
        String field_password = et_password.getText().toString();

        String BDPassword = database.getUserPassword(database.getUsuarioByEmail(field_email), true);
        if(field_password.equals(BDPassword)) {
            Toast.makeText(getApplicationContext(), R.string.login_succesfully, Toast.LENGTH_LONG).show();

            global.setOnlineUser(true);
            global.setIDUserOnline(database.getUserID(database.getUsuarioByEmail(field_email), true));

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            if(BDPassword.equals("CODE_USER_ERROR")) {
                Toast.makeText(getApplicationContext(), R.string.login_email_error, Toast.LENGTH_LONG).show();
                et_email.setText("");
                et_password.setText("");
            } else {
                Toast.makeText(getApplicationContext(), R.string.login_password_error, Toast.LENGTH_LONG).show();
                et_password.setText("");
            }
        }
    }

    protected void signUpOnClick(View v) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
