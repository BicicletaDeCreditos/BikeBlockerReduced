package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;

public class AddNewUserActivity extends Activity {
    private User user;
    private UserDAO userdao;
    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        user = new User();
        userdao = UserDAO.getInstance(getApplicationContext());

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);

        Button saveUserButton = (Button) findViewById(R.id.saveUserButton);
        saveUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser(){
        CharSequence text;
        Toast toast;
        nameEditText.setError(null);
        usernameEditText.setError(null);
        passwordEditText.setError(null);
        confirmPasswordEditText.setError(null);

        View focusView = null;
        boolean cancel = false;

        String name = nameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(name)){
            nameEditText.setError("You must enter a name.");
            focusView = nameEditText;
            cancel = true;
        }else{
            user.setName(name);
        }

        if (TextUtils.isEmpty(username)){
            usernameEditText.setError("You must enter a username.");
            focusView = usernameEditText;
            cancel = true;
        }else{
            user.setUsername(username);
        }

        if (TextUtils.isEmpty(password) || !validatePassword()){
            passwordEditText.setError("You must enter a password with more than 4 characters.");
            focusView = passwordEditText;
            cancel = true;
        }else if (TextUtils.isEmpty(confirmPassword)){
            confirmPasswordEditText.setError("Confirm your password.");
            focusView = confirmPasswordEditText;
            cancel = true;
        } else if(!passwordsAreEqual()){
            confirmPasswordEditText.setError("Passwords does not match.");
            focusView = confirmPasswordEditText;
            cancel = true;
        }else{
            user.setPassword(password);
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if(userdao.selectUser() != null){
                text = "User already exits!";
                toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            }else{
                text = "User saved successfully!";
                saveUser(user);
                toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            }
            Intent intent = new Intent();
            intent.setClass(this, UserAppsListActivity.class);
            startActivity(intent);
            toast.show();
            finish();
        }
    }
    private void saveUser(User user){
        userdao.saveUser(user);
    }

    private boolean validatePassword() {
        String password = passwordEditText.getText().toString();
        if((password != null) && (password.length() >= 4)) {
            return true;
        } else {
            return false;
        }
    }
    private boolean passwordsAreEqual(){
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        if (password.equals(confirmPassword)) {
            return true;
        }else{
            return false;
        }
    }
}
