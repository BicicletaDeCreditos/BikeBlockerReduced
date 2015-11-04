package bikeblocker.bikeblocker.Control;

import bikeblocker.bikeblocker.R;
import bikeblocker.bikeblocker.util.MonitorAppsService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent mServiceIntent = new Intent(getApplicationContext(), MonitorAppsService.class);
        getApplicationContext().startService(mServiceIntent);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void startSettingsAuthActivity(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}


