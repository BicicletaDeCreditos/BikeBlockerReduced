package bikeblocker.bikeblocker.Control;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bikeblocker.bikeblocker.Model.UserAdmin;
import bikeblocker.bikeblocker.R;

/**
 * A login screen that offers login via password.
 */
public class SettingsAuthActivity extends Activity {
    private final int NO_ADMIN = 1;
    private final int INCORRECT = 2;
    private final int OK = 0;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private UserAdmin userToBeLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_auth);

        userToBeLogged = new UserAdmin();

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    public void attemptLogin() {

        boolean cancel = false;
        View focusView = null;

        mPasswordView.setError(null);

        String password = mPasswordView.getText().toString();
        int loginResult = userToBeLogged.verifyAdminPassword(password, getApplicationContext());

        if (loginResult == NO_ADMIN) {
            Intent intent = new Intent();
            intent.setClass(this, RegisterPasswordActivity.class);
            startActivity(intent);

            CharSequence text = "First access! Register a password";
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            toast.show();
            finish();
        } else if (loginResult == INCORRECT) {
            userToBeLogged = new UserAdmin();
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        } else {
            Intent intent = new Intent();
            intent.setClass(this, ListUsersActivity.class);
            startActivity(intent);
            finish();
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
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