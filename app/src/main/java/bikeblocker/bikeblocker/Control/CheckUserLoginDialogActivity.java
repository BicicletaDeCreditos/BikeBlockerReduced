package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;

public class CheckUserLoginDialogActivity extends Activity {
    private final int OK = 0;
    private EditText txtUsername;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Dialog login = new Dialog(this);
        login.setContentView(R.layout.login_dialog);
        txtUsername = (EditText)login.findViewById(R.id.txtUsername);
        txtPassword = (EditText)login.findViewById(R.id.txtPassword);
        login.setTitle("Login into BikeBlocker");

        Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
        Button btnCancel = (Button) login.findViewById(R.id.btnCancel);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.dismiss();
                finish();
            }
        });
        login.show();
    }

    private void attemptLogin(){
        String user = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        int loginResult = new User().getAuthentication(user, password, this.getApplicationContext());

        if (loginResult == OK) {
            //move this toast to another place
            Toast toast = Toast.makeText(getApplicationContext(), "Enjoy you time!", Toast.LENGTH_LONG);
            toast.show();
            Intent mServiceIntent = new Intent("START_SERVICE");
            mServiceIntent.putExtra("status", "logged");
            mServiceIntent.putExtra("user", user);
            startService(mServiceIntent);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG);
            toast.show();
            //voltar para home
        }
    }
}
