package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.R;

public class AddUserAppActivity extends Activity {

    private ListView allInstalledAppsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_user_app);

        allInstalledAppsList = (ListView) findViewById(R.id.allInstalledAppsList);
        allInstalledAppsList.setAdapter(getInstalledAppsAdapter());

        if(true){
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

    public SimpleAdapter getInstalledAppsAdapter(){
        String[] from ={"app_name"};
        int[] to = new int[]{ R.id.installedApp };
        return  new SimpleAdapter(this, getApps(), R.layout.installed_apps, from, to);
    }


    public ArrayList<HashMap<String, String>> getApps(){
        String[] apps_to_display = new String[]{"YouTube", "Gmail", "Instagram", "Flipboard", "Google+", "Email","Messenger", "Facebook"};
        ArrayList<String> all_apps = getInstalledAppsName();
        ArrayList<HashMap<String, String>> apps = new ArrayList<HashMap<String, String>>();
        for(int i = 0; i < apps_to_display.length; i++){
            if(all_apps.contains(apps_to_display[i])){
                HashMap<String, String> app = new HashMap<String, String>();
                app.put("app_name", apps_to_display[i]);
                app.put("description", "");
                apps.add(app);
            }
        }
        return apps;
    }

    public ArrayList<String> getInstalledAppsName(){
        List<ApplicationInfo> list_apps = getInstalledApps();
        PackageManager manager = getApplicationContext().getPackageManager();
        ArrayList<String> installed_apps_name = new ArrayList<String>();
        for(int i = 0; i < list_apps.size(); i++){
            installed_apps_name.add((String) list_apps.get(i).loadLabel(manager));
        }
        return installed_apps_name;
    }

    public List<ApplicationInfo> getInstalledApps(){
        PackageManager manager = getPackageManager();
        return manager.getInstalledApplications(PackageManager.GET_META_DATA);
    }



}
