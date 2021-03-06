package bikeblocker.bikeblocker.util;


import bikeblocker.bikeblocker.Control.CyclingActivity;
import android.os.Handler;

public class MonitorCyclingData {

    private static final int SPEED = 1;
    private static final int DISTANCE = 2;
    private static final int CREDITS = 3;
    private static final int CALORIES = 4;
    private CyclingActivity cyclingActivity;
    private BluetoothConnection bluetoothConnection;
    private Handler handler;
    private int distance = 0;
    private int aux = 0;
    private boolean read = false;

    public MonitorCyclingData(CyclingActivity activity, BluetoothConnection connection,
                              Handler mHandler) {
        cyclingActivity = activity;
        bluetoothConnection = connection;
        handler = mHandler;
    }

    public void startMonitoring() throws InterruptedException {
        Runnable readingNotes = new Runnable() {
            @Override
            public void run() {
                int[] data = bluetoothConnection.getData();
                int infoType = data[0];
                int readData = data[1];
                bluetoothConnection.setWritable(true);

                switch (infoType){
                    case SPEED:
                        cyclingActivity.setVelocityTextView(String.valueOf(readData));
                        break;
                    case DISTANCE:
                        cyclingActivity.setDistanceTextView(String.valueOf(distance));
                        if(readData == 255){
                            read = true;
                        }else if (readData == 0 && read == true){
                            aux++;
                            read = false;
                        }
                        distance = readData + (aux * 255);
                        break;
                    case CREDITS:
                        cyclingActivity.setCreditsTextView(String.valueOf(readData));
                        break;
                    case CALORIES:
                        cyclingActivity.setCaloriesTextView(String.valueOf(readData));
                }
            }
        } ;
        bluetoothConnection.readSocket(2, handler, readingNotes, null, true);
    }

}
