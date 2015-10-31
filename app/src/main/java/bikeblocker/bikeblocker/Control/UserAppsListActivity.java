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
    private String user_name;
    private static String credits_amount_string = "10";
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
        user_name = extras.getString("user_name");

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

                String infos = parent.getItemAtPosition(position).toString();
                String app_name = infos.substring(27, (infos.length() - 1));
                final App app = appDAO.selectApp(app_name, user_name);

                builder.setTitle("Choose one option");

                builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int button) {
                                delete_app(app);
                            }
                        }
                );

                builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edit_app(app);
                    }
                });

                builder.setNeutralButton(R.string.button_cancel, null);

                builder.show();
            }
        };
    }

    public void delete_app(final App app){
        builder = new AlertDialog.Builder(UserAppsListActivity.this);
        builder.setTitle(R.string.confirmTitle);
        builder.setMessage(R.string.confirmMessageApp);

        builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        appDAO.deleteApp(app);
                        finish();
                        Intent intent = new Intent(UserAppsListActivity.this, UserAppsListActivity.class);
                        intent.putExtra("user_name", user_name);
                        startActivity(intent);
                    }
                }
        );

        builder.setNegativeButton(R.string.button_cancel, null);

        builder.show();
    }

    public void edit_app(final App app){
        final CharSequence[] credits_options = {"10", "20", "30"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserAppsListActivity.this);

        builder.setTitle(app.getAppName().toUpperCase() + "\nSelect the number of credits needed to access this app for 1 hour.");

        builder.setSingleChoiceItems(credits_options, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                credits_amount_string = credits_options[item].toString();
            }
        });

        builder.setPositiveButton(R.string.button_OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        App newApp = new App();
                        newApp.setCreditsPerHour(Integer.parseInt(credits_amount_string));
                        newApp.setUser(app.getUser());
                        newApp.setAppName(app.getAppName());
                        newApp.setAppID(app.getAppID());
                        appDAO.editAppInformations(newApp);
                        finish();
                        Intent intent = new Intent(UserAppsListActivity.this, UserAppsListActivity.class);
                        intent.putExtra("user_name", user_name);
                        startActivity(intent);
                    }
                }
        );

        builder.setNegativeButton(R.string.button_cancel, null);

        builder.show();

    }

    public SimpleAdapter getUserApps(){
        String[] from ={"app_name", "credits_hour"};
        int[] to = new int[]{ R.id.appName, R.id.creditsPerHour };
        return new SimpleAdapter(this, appDAO.selectAllApps(user_name), R.layout.user_apps, from, to);
    }

    public void startAddNewAppToUser(View view){
        Intent intent = new Intent(this, AddUserAppActivity.class);
        intent.putExtra("user_name", user_name);
        startActivity(intent);
        finish();
    }
}
