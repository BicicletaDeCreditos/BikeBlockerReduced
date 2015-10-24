package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import bikeblocker.bikeblocker.Database.AppDAO;
import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.App;
import bikeblocker.bikeblocker.R;

public class UserAppsListActivity extends Activity {
    private ListView appsUserList;
    private AppDAO appDAO;
    private String user_username;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDAO = AppDAO.getInstance(getApplicationContext());

        setContentView(R.layout.activity_user_apps_list);

        appsUserList = (ListView) findViewById(R.id.listUserApps);
        appsUserList.setAdapter(getUserApps());
        appsUserList.setOnItemClickListener(listener());

        Bundle extras = getIntent().getExtras();
        user_username = extras.getString("user_username");

        if(getUserApps().isEmpty()){
            Toast.makeText(getApplicationContext(), "There is no app registered!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        appsUserList.setAdapter(getUserApps());
        if(getUserApps().isEmpty()){
            Toast.makeText(getApplicationContext(), "There is no app registered!", Toast.LENGTH_LONG).show();
        }
    }

    private AdapterView.OnItemClickListener listener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                builder = new AlertDialog.Builder(UserAppsListActivity.this);
                builder.setTitle("Choose one option");
                //App app = appDAO.selectApp(view.toString(), user_username);

                builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int button) {
                                //delete_app(app);
                            }
                        }
                );

                builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //edit_app(app);
                    }
                });

                builder.setNeutralButton(R.string.button_cancel, null);

                builder.show();
            }
        };
    }

    public void delete_app(){
        builder = new AlertDialog.Builder(UserAppsListActivity.this);
        builder.setTitle(R.string.confirmTitle);
        builder.setMessage(R.string.confirmMessageApp);

        builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        //appDAO.deleteApp();

                    }
                }
        );

        builder.setNegativeButton(R.string.button_cancel, null);

        builder.show();
    }

    public void edit_app(){

    }

    public SimpleAdapter getUserApps(){
        String[] from ={"app_name", "credits_hour"};
        int[] to = new int[]{ R.id.appName, R.id.creditsPerHour };
        return new SimpleAdapter(this, appDAO.selectAllApps(user_username), R.layout.user_apps, from, to);
    }

    public void startAddNewAppToUser(View view){
        Intent intent = new Intent(this, AddUserAppActivity.class);
        intent.putExtra("user_username", user_username);
        startActivity(intent);
        finish();
    }
}
