package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import bikeblocker.bikeblocker.Database.AppDAO;
import bikeblocker.bikeblocker.R;

public class UserAppsListActivity extends Activity {
    private ListView appsUserList;
    private AppDAO appDAO;
    private String user_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** Get extras from view user details **/
        super.onCreate(savedInstanceState);
        appDAO = AppDAO.getInstance(getApplicationContext());

        setContentView(R.layout.activity_user_apps_list);

        appsUserList = (ListView) findViewById(R.id.listUserApps);
        appsUserList.setAdapter(getUserApps());

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

    public SimpleAdapter getUserApps(){
        String[] from ={"app_name", "credits_hour"};
        int[] to = new int[]{ R.id.appName, R.id.creditsPerHour };
        return new SimpleAdapter(this, appDAO.selectAllApps(user_username), R.layout.user_apps, from, to);
    }

    public void startAddNewAppToUser(View view){
        Intent intent = new Intent(this, AddUserAppActivity.class);
        startActivity(intent);
    }
}
