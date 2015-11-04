package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;

public class ViewUserActivity extends Activity {
    private TextView nameTextView;
    private TextView usernameTextView;
    private TextView creditsTextView;
    UserDAO userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        //Typeface profileTitleFace = Typeface.createFromAsset(getAssets(),"MJF Zhafira Demo.ttf");
        //TextView profileTitleText = (TextView)findViewById(R.id.nameTextView);
        //profileTitleText.setTypeface(profileTitleFace);


        nameTextView = (TextView) findViewById(R.id.nameTextView);
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        creditsTextView = (TextView) findViewById(R.id.creditsTextView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        userdao = UserDAO.getInstance(ViewUserActivity.this);

        User user = userdao.selectUser();

        nameTextView.setText(user.getName());
        usernameTextView.setText(user.getUsername());
        creditsTextView.setText(Integer.toString(user.getCredits()));
    }

    public void startRegisterNewPasswordActivity(View view){
        Intent intent = new Intent(this, RegisterPasswordActivity.class);
        startActivity(intent);
    }
}