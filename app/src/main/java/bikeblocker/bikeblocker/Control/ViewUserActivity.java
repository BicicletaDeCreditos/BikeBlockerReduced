package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;

public class ViewUserActivity extends Activity {
    private String user_name;
    private TextView nameTextView;
    private TextView creditsTextView;
    UserDAO userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        creditsTextView = (TextView) findViewById(R.id.creditsTextView);

        Bundle extras = getIntent().getExtras();
        user_name = extras.getString("user_name");

        ImageButton deleteUserButton = (ImageButton) findViewById(R.id.deleteUserButton);
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        userdao = UserDAO.getInstance(ViewUserActivity.this);

        User user = userdao.selectUser();

        nameTextView.setText(user.getName());
        creditsTextView.setText(Integer.toString(user.getCredits()));
    }

    private void deleteUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserActivity.this);

        builder.setTitle(R.string.confirmTitle);
        builder.setMessage(R.string.confirmMessageUser);

        builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        userdao = UserDAO.getInstance(ViewUserActivity.this);
                        String user_name = nameTextView.getText().toString();
                        userdao.deleteUser(userdao.selectUser());
                        Intent intent = new Intent(ViewUserActivity.this, UserAppsListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        builder.setNegativeButton(R.string.button_cancel, null);

        builder.show();
    }

    public void startListAppsActivity(View view){
        Intent intent = new Intent(this, UserAppsListActivity.class);
        intent.putExtra("user_name", nameTextView.getText().toString());
        startActivity(intent);
    }


}
