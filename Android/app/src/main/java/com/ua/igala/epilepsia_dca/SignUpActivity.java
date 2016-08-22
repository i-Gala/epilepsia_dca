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

        if(field_password.equals(field_passwordConfirm)) {
            try {
                database.getDb().beginTransaction();
                String usuario = database.addUsuario(new Usuario(null, field_email, field_name, field_lastname, field_password, true, false, false));
                database.getDb().setTransactionSuccessful();
            } finally {
                database.getDb().endTransaction();

                Toast.makeText(getApplicationContext(), R.string.signup_succesfully, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.signup_password_error, Toast.LENGTH_LONG).show();
            et_password.setText("");
            et_passwordConfirm.setText("");
        }
    }
}
