package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import bikeblocker.bikeblocker.R;
import bikeblocker.bikeblocker.util.StartBluetoothConnection;

public class StartConnectionActivity extends Activity {

    private BluetoothAdapter bluetoothAdapter; // mudar para bluetoothconnection
    private StartBluetoothConnection startBluetoothConnection;
    private AlertDialog builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_connection);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        startBluetoothConnection = new StartBluetoothConnection(this);

        askForTurnBikeBlockerOn();
    }

    private void askForTurnBikeBlockerOn() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(this);

        builder.setTitle("Turn BikeBlocker device on");
        builder.setMessage("Make sure the device coupled to your bike is turned on.");
        builder.setPositiveButton("It is turned on", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startBluetoothConnection.turnBluetoothOn();
            }
        });

        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    public void connectToBikeBlocker(View view){
        startBluetoothConnection.turnBluetoothOn();
    }

    public void askForEnablingBluetooth(String requestEnableString){
        Intent turnOn = new Intent(requestEnableString);
        startActivityForResult(turnOn, 0);
    }

    public void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void showConnectionDialog(){
        AlertDialog.Builder  myBuilder = new AlertDialog.Builder(this);
        myBuilder.setMessage("Connecting to BikeBlocker Bluetooth device...");
        builder = myBuilder.create();
        builder.show();
    }

    public void dismissDialog(){
        builder.dismiss();
    }

    public void askForTryAgain(){
        AlertDialog.Builder  builder = new AlertDialog.Builder(this);

        builder.setTitle("Try Again");
        builder.setMessage("Connect to BikeBlocker Bluetooth device.");
        builder.setPositiveButton(R.string.connect, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startBluetoothConnection.turnBluetoothOn();
            }
        });

        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    public void startCyclingActivity(){
        Intent intent = new Intent(this, CyclingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startBluetoothConnection.close();
    }

}
