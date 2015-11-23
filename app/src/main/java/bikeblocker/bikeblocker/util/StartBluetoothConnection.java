package bikeblocker.bikeblocker.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import bikeblocker.bikeblocker.Control.CyclingActivity;
import bikeblocker.bikeblocker.Control.StartConnectionActivity;

public class StartBluetoothConnection {
    private StartConnectionActivity callerActivity;
    private static BluetoothConnection bluetoothConnection;
    private BluetoothDevice bluetoothDevice;
    private static Handler handler;
    private IntentFilter filter;
    private boolean registered = false;
    private final String MAC_ADDRESS = "20:15:06:10:11:75"; // change to the actual mac address


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                callerActivity.showConnectionDialog();

            } else if(action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice
                        .EXTRA_DEVICE);
                if(device.getAddress().equalsIgnoreCase(MAC_ADDRESS)) {
                    bluetoothConnection.stopDiscovery();
                    bluetoothDevice = device;
                    if(bluetoothConnection.pairWithDevice(bluetoothDevice) == BluetoothConnection.PAIRED) {
                        callerActivity.showToast("Device was found. You need to connect to it.");
                        callerActivity.dismissDialog();
                        if(bluetoothConnection.openSocketConnection(bluetoothDevice, handler,
                                getConnectNotification(), getExceptionNotification()) != BluetoothConnection.OPENED) {
                            callerActivity.showToast("Connection failure.");
                        }
                    }
                }

            } else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if(bluetoothDevice == null) {
                    bluetoothConnection.stopDiscovery();
                    callerActivity.dismissDialog();
                    callerActivity.askForTryAgain();
                }

            } else if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {//if it was not paired
                final int BOND_STATE = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
                final int PREVIOUS_BOND_STATE = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1);
                if(BOND_STATE == BluetoothDevice.BOND_BONDED) {//paired
                    callerActivity.showToast("Bound state == bounded. Device was found. You need to connect to it.");
                    callerActivity.dismissDialog();
                    if(bluetoothConnection.openSocketConnection(bluetoothDevice, handler,
                                getConnectNotification(), getExceptionNotification()) != BluetoothConnection.OPENED) {
                        callerActivity.showToast("Connection failure.");
                    }
                } else if((PREVIOUS_BOND_STATE == BluetoothDevice.BOND_BONDING) && (BOND_STATE == BluetoothDevice.BOND_NONE)) {
                    callerActivity.dismissDialog();
                }
            }
        }
    };

    public StartBluetoothConnection(StartConnectionActivity activity){
        handler = new Handler(Looper.getMainLooper());
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
                    callerActivity.registerReceiver(mReceiver, filter);
                }
                bluetoothConnection.startDiscovery();
                break;
        }

    }

    public Runnable getExceptionNotification() {
        return new Runnable() {
            @Override
            public void run() {
                bluetoothConnection.openSocketConnection(bluetoothDevice, handler,
                        getConnectNotification(), getExceptionNotification());
            }
        };
    }

    public Runnable getConnectNotification() {
        return new Runnable() {
            @Override
            public void run() {
                callerActivity.dismissDialog();
                callerActivity.showToast("Connected to BikeBlocker device.");
                callerActivity.startCyclingActivity();
            }
        };
    }

    public static BluetoothConnection getBluetoothConnection(){
        return bluetoothConnection;
    }

    public static Handler getHandler(){
        return handler;
    }

    public void closeResources(){
        bluetoothConnection.stopDiscovery();
        try {
            if (mReceiver != null) {
                callerActivity.unregisterReceiver(mReceiver);
            }
            bluetoothConnection.closeSocketConnection();
        } catch (IllegalArgumentException e) {
            Log.e("ON DESTROY METHOD", "Error: " + e.getMessage());
        } catch (IOException e) {
            Log.e("ON DESTROY METHOD IO", "Error: " + e.getMessage());
        }
        turnBluetoothOff();
    }

    public void turnBluetoothOff() {
        bluetoothConnection.turnOff();
    }

}
