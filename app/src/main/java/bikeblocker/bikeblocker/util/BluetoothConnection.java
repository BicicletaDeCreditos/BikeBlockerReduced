package bikeblocker.bikeblocker.util;

import android.bluetooth.BluetoothAdapter;

public class BluetoothConnection {
    private BluetoothAdapter bluetoothAdapter;
    public static final int ONLINE = 1;
    public static final int OFFLINE = 2;
    public static final int NO_EXISTS = 3;

    public BluetoothConnection(BluetoothAdapter btAdapter) {
        bluetoothAdapter = btAdapter;
    }

    public int checkBluetoothStatus(){
        if (bluetoothAdapter != null){
            if (bluetoothAdapter.isEnabled()){
                return ONLINE;
            }else{
                return OFFLINE;
            }
        }else{
            return NO_EXISTS;
        }
    }

    public void startDiscovery(){
        bluetoothAdapter.startDiscovery();
    }

    public void stopDiscovery(){
        bluetoothAdapter.cancelDiscovery();
    }

}
