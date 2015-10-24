package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bikeblocker.bikeblocker.Database.AppDAO;
import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.App;
import bikeblocker.bikeblocker.R;

public class AddUserAppActivity extends Activity {

    private ListView allInstalledAppsList;
    private String user_username;
    private static String selected_credits_amount_string = "10";
    private AppDAO appDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_user_app);

        allInstalledAppsList = (ListView) findViewById(R.id.allInstalledAppsList);
        allInstalledAppsList.setAdapter(getInstalledAppsAdapter());
        allInstalledAppsList.setOnItemClickListener(listener());

        Bundle extras = getIntent().getExtras();
        user_username = extras.getString("user_username");

        if(getInstalledAppsAdapter().isEmpty()){
            Toast.makeText(getApplicationContext(), "No apps", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        allInstalledAppsList.setAdapter(getInstalledAppsAdapter());
        if(getInstalledAppsAdapter().isEmpty()){
            Toast.makeText(getApplicationContext(), "No apps", Toast.LENGTH_LONG).show();
        }
    }

    public ArrayAdapter<String> getInstalledAppsAdapter(){
        String[] from ={"app_name"};
        int[] to = new int[]{ R.id.installedApp };
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getApps());
    }

    public ArrayList<String> getApps(){
        String[] apps_to_display = new String[]{"YouTube", "Gmail", "Instagram", "Google+", "Email","Messenger", "Facebook", "Twitter"};
        ArrayList<String> all_apps = getInstalledAppsName();
        ArrayList<String> apps = new ArrayList<String>();
        for(int i = 0; i < apps_to_display.length; i++){
            if(all_apps.contains(apps_to_display[i])){
                apps.add(apps_to_display[i]);
            }
        }
        return apps;
    }

    public ArrayList<String> getInstalledAppsName(){
        PackageManager manager = getPackageManager();
        List<ApplicationInfo> list_apps = manager.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<String> installed_apps_name = new ArrayList<String>();
        for(int i = 0; i < list_apps.size(); i++){
            installed_apps_name.add((String) list_apps.get(i).loadLabel(manager));
        }
        return installed_apps_name;
    }

    private AdapterView.OnItemClickListener listener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startAskForCredits(view);
            }
        };
    }

    public void startAskForCredits(final View view){
        final CharSequence[] credits_options = {"10", "20", "30"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddUserAppActivity.this);

        builder.setTitle(R.string.creditsMessage);

        builder.setSingleChoiceItems(credits_options, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                selected_credits_amount_string = credits_options[item].toString();
                System.out.println("On click: " + credits_options[item].toString());
            }
        });

        builder.setPositiveButton(R.string.button_OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        App newApp = new App();
                        appDAO = AppDAO.getInstance(AddUserAppActivity.this);
                        newApp.setCreditsPerHour(Integer.parseInt(selected_credits_amount_string));
                        newApp.setUser(user_username);
                        newApp.setAppName(((TextView) view).getText().toString());
                        appDAO.saveApp(newApp);
                        Intent intent = new Intent(AddUserAppActivity.this, UserAppsListActivity.class);
                        intent.putExtra("user_username", user_username);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        builder.setNegativeButton(R.string.button_cancel, null);

        builder.show();
    }
}
