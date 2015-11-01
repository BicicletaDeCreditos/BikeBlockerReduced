package bikeblocker.bikeblocker.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MonitorAppsService extends Service implements Runnable {
    public static final String tag1 = "service";

    @Override
    public void onCreate(){
        super.onCreate();
        Thread aThread = new Thread(this);
        aThread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        while (true){
            try{
                System.out.println("This is my thread running in background");
                Thread.sleep(5000);
            }catch (Exception e){
                System.out.println("Exception: " + e.toString());
            }
        }
    }
}
