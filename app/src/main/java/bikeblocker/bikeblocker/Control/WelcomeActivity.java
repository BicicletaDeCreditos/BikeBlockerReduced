package bikeblocker.bikeblocker.Control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import bikeblocker.bikeblocker.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void startAddNewUserActivity(View view){
        Intent intent = new Intent(this, AddNewUserActivity.class);
        startActivity(intent);
    }

    public void startListUsersActivity(View view){
        Intent intent = new Intent(this, ListUsersActivity.class);
        startActivity(intent);
    }

    public void startChangePasswordActivity(View view){
        Intent intent = new Intent(this, RegisterPasswordActivity.class);
        startActivity(intent);
    }
}
