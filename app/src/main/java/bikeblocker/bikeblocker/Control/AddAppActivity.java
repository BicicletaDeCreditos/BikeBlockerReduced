package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bikeblocker.bikeblocker.Database.AppDAO;
import bikeblocker.bikeblocker.Model.App;
import bikeblocker.bikeblocker.R;
import bikeblocker.bikeblocker.AppsComparator;

public class AddAppActivity extends Activity {

    private ListView allInstalledAppsList;
    private static String selected_credits_amount_string = "10";
    private AppDAO appDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_user_app);

        allInstalledAppsList = (ListView) findViewById(R.id.allInstalledAppsList);
        allInstalledAppsList.setAdapter(getInstalledAppsAdapter());
        allInstalledAppsList.setOnItemClickListener(listener());

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
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getAppsNames());
    }




    public ArrayList<String> getAppsNames(){
        ArrayList<ApplicationInfo> all_apps = getInstalledAppsInfo();
        ArrayList<String> apps = new ArrayList<String>();
        ApplicationInfo[] apps_list = all_apps.toArray(new ApplicationInfo[(all_apps.size())]);
        for(int i = 0; i < apps_list.length; i++){
            if(!apps_list[i].loadLabel(getPackageManager()).toString().equals("BikeBlocker"))
            apps.add(apps_list[i].loadLabel(getPackageManager()).toString());
           }
        return apps;
    }

    public ArrayList<Drawable> getAppsIcon(){
        ArrayList<ApplicationInfo> all_apps = getInstalledAppsInfo();
        ArrayList<Drawable> app_icons = new ArrayList<Drawable>();
        ApplicationInfo[] apps_list = all_apps.toArray(new ApplicationInfo[(all_apps.size())]);


        for(int i = 0; i < apps_list.length; i++){
            if(!apps_list[i].loadLabel(getPackageManager()).toString().equals("BikeBlocker"))
            app_icons.add(apps_list[i].loadIcon(getPackageManager()));
        }
        return app_icons;
    }


    public ArrayList<ApplicationInfo> getInstalledAppsInfo(){
        PackageManager manager = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        List<ApplicationInfo> list_apps = manager.getInstalledApplications(PackageManager.GET_META_DATA);
        mainIntent.setAction(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        List<ResolveInfo> resolveInfos = manager.queryIntentActivities(mainIntent, 0);
        ArrayList<ApplicationInfo> installed_apps = new ArrayList<ApplicationInfo>();
;

        for(ResolveInfo info : resolveInfos) {
            installed_apps.add((ApplicationInfo) info.activityInfo.applicationInfo);
        }

        Collections.sort(installed_apps, new AppsComparator(manager));
        return installed_apps;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAppActivity.this);
        final String app_name = ((TextView) view).getText().toString();

        builder.setTitle(app_name.toUpperCase() + "\nSelect the number of credits needed to access this app for 1 hour.");

        builder.setSingleChoiceItems(credits_options, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                selected_credits_amount_string = credits_options[item].toString();
            }
        });

        builder.setPositiveButton(R.string.button_OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        appDAO = AppDAO.getInstance(AddAppActivity.this);
                        if (appDAO.selectApp(app_name) == null) {
                            App newApp = new App();
                            newApp.setCreditsPerHour(Integer.parseInt(selected_credits_amount_string));
                            newApp.setAppName(app_name);
                            appDAO.saveApp(newApp);
                        } else {
                            Toast.makeText(getApplicationContext(), "You already have this app on your list.", Toast.LENGTH_LONG).show();
                        }
                        Intent intent = new Intent(AddAppActivity.this, AppsListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        builder.setNegativeButton(R.string.button_cancel, null);

        builder.show();
    }
}
