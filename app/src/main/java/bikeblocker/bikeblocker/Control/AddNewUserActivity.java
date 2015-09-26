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

        Button saveuserButton = (Button) findViewById(R.id.saveUserButton);
        saveuserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser(){
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

        if (TextUtils.isEmpty(name) || name == null){
            nameEditText.setError("You must enter a name.");
            focusView = nameEditText;
            cancel = true;
        }else{
            user.setName(name);
        }
        /**TO DO: verificar de se ja nao existe um usuario*/
        if (TextUtils.isEmpty(username) || username == null){
            usernameEditText.setError("You must enter a username.");
            focusView = nameEditText;
            cancel = true;
        }else{
            user.setUsername(name);
        }
        if (TextUtils.isEmpty(password) || password == null || !validatePassword()){
            passwordEditText.setError("You must enter a password with 6 to 15 characters.");
            focusView = passwordEditText;
            cancel = true;
        }else if (TextUtils.isEmpty(confirmPassword) || confirmPassword == null){
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
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            saveUser(user);
            //TO DO: change it to the actual next activity
            Intent intent = new Intent();
            intent.setClass(this, WelcomeActivity.class);
            intent.putExtra("user_name", name);
            startActivity(intent);

            CharSequence text = "Usuario salvo com sucesso!";
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            toast.show();
        }

    }
    private void saveUser(User user){
        userdao.saveUser(user);
    }

    private boolean validatePassword() {
        String password = passwordEditText.getText().toString();
        if((password != null) && (password.length() >= 6) && (password.length() <= 15)) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
