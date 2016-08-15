package com.ua.igala.epilepsia_dca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Gestiona el inicio de sesión en la aplicación.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    protected void sendOnClick(View v){
        DatabaseHandler BD = new DatabaseHandler(LoginActivity.this, null, null, 2);
        EditText et_email = (EditText) findViewById(R.id.field_email);
        EditText et_password = (EditText) findViewById(R.id.field_password);
        String field_email = et_email.getText().toString();
        String field_password = et_password.getText().toString();

        String BDPassword = BD.getUser(field_email);
        if(field_password.equals(BDPassword)) {
            Toast.makeText(getApplicationContext(), R.string.login_succesfully, Toast.LENGTH_LONG).show();
            //Global global = ((Global)getApplicationContext());
            //global.setOnlineUser(true);
            //global.setIDUserOnline(BD.getUserID(field_email));
            Global.getInstance().setOnlineUser(true);
            Global.getInstance().setIDUserOnline(BD.getUserID(field_email));
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
