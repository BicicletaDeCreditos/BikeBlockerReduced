package bikeblocker.bikeblocker.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnection {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private int[] data;
    private boolean writable = false;
    public static final int ONLINE = 1;
    public static final int OFFLINE = 2;
    public static final int NO_EXISTS = 3;
    public static final int IN_PROGRESS = 4;
    public static final int PAIRED = 5;
    public static final int OPENED = 6;
    public static final int UNKNOWN = 7;
    public static final int ERROR = 0;

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

    public void turnOff(){
        bluetoothAdapter.disable();
    }

    public void startDiscovery(){
        bluetoothAdapter.startDiscovery();
    }

    public void stopDiscovery(){
        bluetoothAdapter.cancelDiscovery();
    }


    public int pairWithDevice(BluetoothDevice device){
        final String deviceName = device.getName();
        BluetoothDevice pairedDevice = null;
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices != null){
            for(BluetoothDevice mDevice : pairedDevices){
                if(mDevice.getName().contains(deviceName)){
                    pairedDevice = mDevice;
                    break;
                }
            }

            if (pairedDevice == null){
                try{
                    device.createBond();
                    return IN_PROGRESS;
                }catch (Exception e){
                    Log.e("Connection Error", "Error: " + e.getMessage());
                    return ERROR;
                }
            }
        }
        return PAIRED;
    }


    public int openSocketConnection(BluetoothDevice btDevice, final Handler handler,
                                      final Runnable connectNotification,
                                      final Runnable exceptionNotification) {
        final String mUUID = "717BE010-7507-493B-95BE-A79DC5759B58";

        if(btDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
            return UNKNOWN;
        } else {
            try {
                stopDiscovery();
                bluetoothSocket = btDevice.createRfcommSocketToServiceRecord(UUID.fromString(mUUID));

                Thread connectThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bluetoothSocket.connect();
                            System.out.println("Im trying to connect...");
                            handler.post(connectNotification);
                        }catch (IOException e) {
                            Log.e("ERRO AO CONECTAR", "O erro foi " + e.getMessage());
                            handler.post(exceptionNotification);
                        }
                    }
                });
                connectThread.start();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return ERROR;
            }
        }
        return OPENED;
    }

    public void closeSocketConnection() throws IOException {
        if(bluetoothSocket != null){
            bluetoothSocket.close();
        }
    }

    public void readSocket(final int length, final Handler handler,
                         final Runnable dataReadNote, final Runnable exceptionNotification,
                         final boolean loop) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    setWritable(true);
                    data = new int[length];
                    InputStream input = bluetoothSocket.getInputStream();
                    while (loop) {
                        if(writable) {
                            for (int i = 0; i < length; i++) {
                                data[i] = input.read();
                            }
                            setWritable(false);
                            handler.post(dataReadNote);
                        }
                    }
                    input.close();
                }catch (IOException ioe) {
                    ioe.printStackTrace();
                    handler.post(exceptionNotification);
                }
            }
        });
        thread.start();
    }

    public int[] getData(){
        return data;
    }

    public void setWritable(boolean bool){
        writable = bool;
    }

}
