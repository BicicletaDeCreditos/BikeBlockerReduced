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

import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;

public class LoginActivity extends Activity {
    private final int OK = 0;
    private final int INCORRECT = 1;
    private final int NO_USER = 2;
    private EditText mPasswordView;
    private EditText mUsernameView;
    private View mProgressView;
    private View mLoginFormView;

    private User userToBeLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userToBeLogged = new User();

        mUsernameView = (EditText) findViewById(R.id.username);
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
        Intent intent;
        boolean cancel = false;
        View focusView = null;

        mPasswordView.setError(null);

        String password = mPasswordView.getText().toString();

        int loginResult = userToBeLogged.getAuthentication(password,getApplicationContext());

        switch (loginResult){
            case OK:
                intent = new Intent();
                intent.setClass(this, AppsListActivity.class);
                startActivity(intent);
                finish();
                break;
            case INCORRECT:
                userToBeLogged = new User();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                focusView = mPasswordView;
                cancel = true;
                break;
            default:
                intent = new Intent();
                intent.setClass(this, RegisterUserActivity.class);
                startActivity(intent);

                CharSequence text = "First access! Register a password";
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                toast.show();
                finish();
                break;
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