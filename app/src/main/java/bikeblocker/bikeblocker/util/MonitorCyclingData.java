package bikeblocker.bikeblocker.util;


import bikeblocker.bikeblocker.Control.CyclingActivity;
import android.os.Handler;

public class MonitorCyclingData {

    private CyclingActivity cyclingActivity;
    private BluetoothConnection bluetoothConnection;
    private Handler handler;

    public MonitorCyclingData(CyclingActivity activity, BluetoothConnection connection,
                              Handler mHandler) {
        cyclingActivity = activity;
        bluetoothConnection = connection;
        handler = mHandler;
    }

    public void startMonitoring() {
        Runnable readingNotes = new Runnable() {
            @Override
            public void run() {
                int[] data = bluetoothConnection.getData();
                int lowByte = data[0];
                int highByte = data[1] << 8;
                bluetoothConnection.setWritable(true);
                int readData = highByte | lowByte;
                cyclingActivity.setCreditsTextView(String.valueOf(readData));
            }
        } ;
        bluetoothConnection.readSocket(2, handler, readingNotes, null, true);
    }

}
