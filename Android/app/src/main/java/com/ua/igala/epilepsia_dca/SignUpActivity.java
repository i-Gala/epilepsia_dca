package com.ua.igala.epilepsia_dca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ua.igala.epilepsia_dca.model.Usuario;
import com.ua.igala.epilepsia_dca.sqlite.OperacionesBD;

/**
 * Gestiona el registro en la Base de Datos (BD) en la aplicación.
 */

public class SignUpActivity extends AppCompatActivity {

    private OperacionesBD database;
    private Global global = Global.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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
        EditText et_name = (EditText) findViewById(R.id.field_name);
        EditText et_lastname = (EditText) findViewById(R.id.field_lastname);
        EditText et_email = (EditText) findViewById(R.id.field_email);
        EditText et_password = (EditText) findViewById(R.id.field_password);
        EditText et_passwordConfirm = (EditText) findViewById(R.id.field_passwordConfirm);

        String field_name = et_name.getText().toString();
        String field_lastname = et_lastname.getText().toString();
        String field_email = et_email.getText().toString();
        String field_password = et_password.getText().toString();
        String field_passwordConfirm = et_passwordConfirm.getText().toString();

        if(!field_name.equals("") && !field_lastname.equals("") && !field_email.equals("") && !field_password.equals("")) {
            if(field_name.length() > 2) {
                if(field_lastname.length() > 2) {
                    if (field_email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && field_email.length() > 0) {
                        if (field_password.length() > 5) {
                            if (field_password.equals(field_passwordConfirm)) {
                                try {
                                    database.getDb().beginTransaction();
                                    String usuario = database.addUsuario(new Usuario(null, field_email, field_name, field_lastname, field_password, true, false, false, 100, 60, 120));
                                    database.getDb().setTransactionSuccessful();
                                } finally {
                                    database.getDb().endTransaction();

                                    Toast.makeText(getApplicationContext(), R.string.signup_succesfully, Toast.LENGTH_LONG).show();
                                    //Intent intent = new Intent(this, MainActivity.class);
                                    //startActivity(intent);
                                    global.setOnlineUser(true);
                                    global.setIDUserOnline(database.getUserID(database.getUsuarioByEmail(field_email), true));
                                    global.guardarSharedPreferences(getApplicationContext());

                                    Intent intent = new Intent(this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.signup_password_error, Toast.LENGTH_LONG).show();
                                et_password.setText("");
                                et_passwordConfirm.setText("");
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.signup_passlength_error, Toast.LENGTH_LONG).show();
                            et_password.setText("");
                            et_passwordConfirm.setText("");
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.signup_email_error, Toast.LENGTH_LONG).show();
                        et_email.setText("");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.signup_lastname_error, Toast.LENGTH_LONG).show();
                    et_lastname.setText("");
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.signup_name_error, Toast.LENGTH_LONG).show();
                et_name.setText("");
            }
        } else
            Toast.makeText(getApplicationContext(), R.string.signup_empty_error, Toast.LENGTH_LONG).show();
    }
}
