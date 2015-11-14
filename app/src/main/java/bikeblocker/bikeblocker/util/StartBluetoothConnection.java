package bikeblocker.bikeblocker.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import bikeblocker.bikeblocker.Control.StartConnectionActivity;

public class StartBluetoothConnection {
    private StartConnectionActivity callerActivity;
    private BluetoothConnection bluetoothConnection;
    private IntentFilter filter;
    private boolean registered = false;




    public StartBluetoothConnection(StartConnectionActivity activity){
        bluetoothConnection = new BluetoothConnection(BluetoothAdapter.getDefaultAdapter());
        callerActivity = activity;
        filter = new IntentFilter();
    }

    public void turnBluetoothOn() {
        switch (bluetoothConnection.checkBluetoothStatus()){
            case BluetoothConnection.OFFLINE:
                callerActivity.askForEnablingBluetooth(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                break;
            case BluetoothConnection.NO_EXISTS:
                callerActivity.showToast("Device does not support bluetooth connection");
                break;
            case BluetoothConnection.ONLINE:
                if(!registered) {
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

                    registered= true;
                    //callerActivity.registerReceiver(btActionReceiver, filter);
                }
                //bluetoothConnection.startDiscovery();
                break;
        }

    }





}
