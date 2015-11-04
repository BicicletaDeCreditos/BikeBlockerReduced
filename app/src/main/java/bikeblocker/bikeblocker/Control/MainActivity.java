package bikeblocker.bikeblocker.Control;

import bikeblocker.bikeblocker.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mServiceIntent = new Intent("START_SERVICE");
        startService(mServiceIntent);
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


