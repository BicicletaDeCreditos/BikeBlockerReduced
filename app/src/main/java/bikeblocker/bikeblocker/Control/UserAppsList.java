package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import bikeblocker.bikeblocker.Database.AppDAO;
import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.R;

public class UserAppsList extends Activity {
    private ListView appsUserList;
    AppDAO appDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** Get extras from view user details **/
        super.onCreate(savedInstanceState);
        appDAO = AppDAO.getInstance(getApplicationContext());

        setContentView(R.layout.activity_user_apps_list);

        appsUserList = (ListView) findViewById(R.id.listUserApps);
        appsUserList.setAdapter(getUserApps());

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
        return new SimpleAdapter(this, appDAO.selectAllApps("username"), R.layout.apps, from, to);/**MUDAR nome do usuario**/
    }

    public void startAddNewAppToUser(View view){
        /** start new intent here **/
    }
}
