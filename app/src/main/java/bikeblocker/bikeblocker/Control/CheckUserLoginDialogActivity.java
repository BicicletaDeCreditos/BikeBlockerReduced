package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;

public class CheckUserLoginDialogActivity extends Activity {
    private final int OK = 0;
    private final int INCORRECT = 1;
    private final int NO_USER = 2;
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

        switch (loginResult){
            case OK:
                int credits = checkCredits(user);
                if(credits > 0){
                    allow(user);
                }else{
                    deny();
                }
                break;
            case INCORRECT:
                deny();
                break;
            case NO_USER:
                notifyNoUser();
        }
    }

    private void notifyNoUser() {
        Intent mServiceIntent = new Intent("START_SERVICE");
        mServiceIntent.putExtra("status", "nouser");
        mServiceIntent.putExtra("user", "");
        mServiceIntent.putExtra("credits", 0);
        startService(mServiceIntent);
    }

    private void deny() {
        Toast toast = Toast.makeText(getApplicationContext(), "You can't access this app. Check your credits and your credentials", Toast.LENGTH_LONG);
        toast.show();
        //voltar para home
        Intent homeIntent= new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }

    private void allow(String user) {
        Toast toast = Toast.makeText(getApplicationContext(), "Enjoy you time!", Toast.LENGTH_LONG);
        toast.show();
        Intent mServiceIntent = new Intent("START_SERVICE");
        mServiceIntent.putExtra("status", "logged");
        mServiceIntent.putExtra("user", user);
        startService(mServiceIntent);
    }

    private int checkCredits(String user_name){
        return UserDAO.getInstance(getApplicationContext()).selectUser(user_name).getCredits();
    }
}
