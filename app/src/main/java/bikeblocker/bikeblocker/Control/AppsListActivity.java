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
import android.widget.Toast;

import bikeblocker.bikeblocker.Database.AppDAO;
import bikeblocker.bikeblocker.Model.App;
import bikeblocker.bikeblocker.R;

public class AppsListActivity extends Activity {
    private ListView appsUserList;
    private AppDAO appDAO;
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

        if(getUserApps().isEmpty()){
            Toast.makeText(getApplicationContext(), "There is no app registered!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        appsUserList.setAdapter(getUserApps());
        if(getUserApps().isEmpty()){
            Toast.makeText(getApplicationContext(), "There is no app registered!", Toast.LENGTH_LONG).show();
        }
    }

    private AdapterView.OnItemClickListener listener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                builder = new AlertDialog.Builder(AppsListActivity.this);

                String[] infos = parent.getItemAtPosition(position).toString().split(",");
                System.out.println(infos[0]);
                String app_name = infos[0].substring(10, (infos[0].length()));
                System.out.println("App name: " + app_name);
                final App app = appDAO.selectApp(app_name);

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
        builder = new AlertDialog.Builder(AppsListActivity.this);
        builder.setTitle(R.string.confirmTitle);
        builder.setMessage(R.string.confirmMessageApp);

        builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        appDAO.deleteApp(app);
                        finish();
                        Intent intent = new Intent(AppsListActivity.this, AppsListActivity.class);
                        startActivity(intent);
                    }
                }
        );

        builder.setNegativeButton(R.string.button_cancel, null);

        builder.show();
    }

    public void edit_app(final App app){
        final CharSequence[] credits_options = {"10", "20", "30"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AppsListActivity.this);

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
                        newApp.setAppName(app.getAppName());
                        newApp.setAppID(app.getAppID());
                        appDAO.editAppInformation(newApp);
                        finish();
                        Intent intent = new Intent(AppsListActivity.this, AppsListActivity.class);
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
        return new SimpleAdapter(this, appDAO.selectAllApps(), R.layout.user_apps, from, to);

    }

    public void startAddNewAppToUser(View view){
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AddAppActivity.class);
        startActivity(intent);
    }

    public void startViewUserActivity(View view) {
        Intent viewUser = new Intent(this, ViewUserActivity.class);
        startActivity(viewUser);
    }
}
