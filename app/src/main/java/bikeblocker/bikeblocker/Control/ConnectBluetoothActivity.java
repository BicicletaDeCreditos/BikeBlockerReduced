package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import bikeblocker.bikeblocker.R;

public class ConnectBluetoothActivity extends Activity {
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver mReceiver;
    private ListView devicesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<BluetoothDevice> devicesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_bluetooth);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        devicesListView = (ListView) findViewById(R.id.devicesListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        devicesListView.setAdapter(adapter);
    }

    public void turnBluetoothOn(View view) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(getVisible, 0);

            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);

            Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void turnBluetoothOff(View view) {
        adapter.clear();
        bluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
    }

    public void listDevices(View view){
        adapter.clear();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        if(bluetoothAdapter.startDiscovery()){
            mReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        String deviceInfo = device.getName() + "\n" + device.getAddress();
                        if(adapter.getPosition(deviceInfo) < 0){
                            devicesList.add(device);
                            adapter.add(deviceInfo);
                        }
                    }
                }
            };
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mReceiver != null) {
                this.unregisterReceiver(mReceiver);
            }
        } catch (IllegalArgumentException e) {
            mReceiver = null;
        }
    }

}
