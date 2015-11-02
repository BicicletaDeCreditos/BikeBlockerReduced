package bikeblocker.bikeblocker.Control;

import bikeblocker.bikeblocker.R;
import bikeblocker.bikeblocker.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mServiceIntent = new Intent("START_SERVICE");
        mServiceIntent.putExtra("status", "notlogged");
        mServiceIntent.putExtra("user", "");
        startService(mServiceIntent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void startSettingsAuthActivity(View view){
        Intent intent = new Intent(this, SettingsAuthActivity.class);
        startActivity(intent);
    }

}


