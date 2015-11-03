package bikeblocker.bikeblocker.Control;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;

public class RegisterPasswordActivity extends Activity {

    private static User userToBeRegistered;
    private Context context;
    private UserDAO userDao;
    private EditText password;
    private EditText confirmPassword;

    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userToBeRegistered = new User();
        this.context = getApplicationContext();
        this.userDao = UserDAO.getInstance(getApplicationContext());

        this.password = (EditText) findViewById(R.id.insert_password);
        this.confirmPassword = (EditText) findViewById(R.id.confirm_password);

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPassword();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public static boolean validatePassword(String password) {
        if((password != null) && (password.length() >= 4)) {
            return true;
        } else {
            return false;
        }
    }

    private void savePassword(String password){
        userToBeRegistered.setPassword(password);
        if(userToBeRegistered.verifyFirstTimeAccess(getApplicationContext())){
            userDao.saveUser(userToBeRegistered);
        }else{
            userDao.editUserInformation(userToBeRegistered);
        }

    }

    public void registerPassword() {
        confirmPassword.setError(null);

        String passwordValue = password.getText().toString();
        String confirmPasswordValue = confirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(confirmPasswordValue) || !validatePassword(passwordValue) || !passwordValue.equals(confirmPasswordValue)) {
            password.setError(getString(R.string.password_error));
            focusView = password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            savePassword(passwordValue);
            Intent intent = new Intent();
            intent.setClass(this, UserAppsListActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Password saved successfully!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
