package bikeblocker.bikeblocker.util;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import bikeblocker.bikeblocker.Control.MainActivity;
import bikeblocker.bikeblocker.Control.NoEnoughCreditsDialogActivity;
import bikeblocker.bikeblocker.Control.ViewUserActivity;
import bikeblocker.bikeblocker.Database.AppDAO;
import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.App;
import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;

public class MonitorAppsService extends Service implements Runnable {
    private String previousApp = "";
    private App app;
    private User user;
    private int counter = 0;
    private int counterPerCredit;// how many times a tread will sleep until one credits is consumed
    private final int threadsPerMin = 6;// the thread sleep time will be reduced for 5 sec (final value = 12)
    private final int TEN = 36; // the thread sleep time will be reduced for 5 sec (final value = 72)
    private final int TWENTY = 18; // the thread sleep time will be reduced for 5 sec (final value = 18)
    private final int THIRTY = 12; // the thread sleep time will be reduced for 5 sec (final value = 24)
    private final int HALF_MINUTE = 3;
    private final int A_MINUTE = 6;
    private final int TWO_MINUTES = 12;

    private String contentTitleString = "";
    private String contentTextString = "";
    private int credits;
    private String name;

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void run() {
        Looper.prepare();
        AppDAO  appDAO = AppDAO.getInstance(getApplicationContext());
        List<String> apps;
        int count = 0;

        while (true){
            try{
                apps = appDAO.getAppsNameFromDatabase();
                System.out.println("This is my thread running in background " + count++);

                if(!apps.isEmpty()){
                    checkAppOnForeground(apps);
                }
                Thread.sleep(10000);
            }catch (Exception e){
                System.out.println("Exception: " + e.toString());
            }
        }
    }

    private void checkAppOnForeground(List<String> appsNameList) throws Exception{
        String foregroundTaskAppName = getForegroundApp();
        if(appsNameList.contains(foregroundTaskAppName)){
            System.out.println("User has app");
            app = AppDAO.getInstance(getApplicationContext()).selectApp(foregroundTaskAppName);
            monitorAppUsage(app.getCreditsPerHour(), app.getAppName());
        }
    }

    private void monitorAppUsage(int creditsPerHour, String app_name) {
        if(checkCredits() > 0){
            System.out.println("monitor app usage");
            if(app_name.equalsIgnoreCase(previousApp)){ //using the same app after thread sleep
                counter++;// incrementa contador
                counterPerCredit = (60/creditsPerHour)*threadsPerMin;
                if(counter == counterPerCredit){
                    debit();
                    counter = 0;
                }
            }else{
                resetParams();
            }
        }else{
            blockApp();
        }
        previousApp = app_name;
    }

    private void resetParams() {
        switch (counterPerCredit){
            case TEN:
                if(counter >= TWO_MINUTES){
                    debit();
                }
                break;
            case TWENTY:
                if(counter >= A_MINUTE ){
                    debit();
                }
                break;
            case THIRTY:
                if(counter >= HALF_MINUTE){
                    debit();
                }
                break;
        }
        counter = 0;
    }

    private void debit() {
        int actualCredits = checkCredits();
        User user = UserDAO.getInstance(getApplicationContext()).selectUser();
        user.setCredits(actualCredits - 1);
        System.out.println("Quantidade atual (nome): " + UserDAO.getInstance(getApplicationContext()).selectUser().getName());
        System.out.println("Quantidade atual: " + UserDAO.getInstance(getApplicationContext()).selectUser().getCredits());
        UserDAO.getInstance(getApplicationContext()).editUserInformation(user);
        System.out.println("Debitado creditos de usuario. ");
        System.out.println("Quantidade atual: " + UserDAO.getInstance(getApplicationContext()).selectUser().getCredits());

        credits = UserDAO.getInstance(getApplicationContext()).selectUser().getCredits();
        name = UserDAO.getInstance(getApplicationContext()).selectUser().getName();
        contentTitleString = "";
        contentTextString = "";

        if (credits > 1) {
            contentTitleString = "You just used 1 of " + credits + " total credits";
            contentTextString = name + ", You still have " + credits + " credits to use.";
        } else if (credits == 1) {
            contentTitleString = "You have only 1 credit";
            contentTextString = name + ", This is you last credit. Your application will be shutdown after this credit use.";
        }
        int mId = 1;
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(contentTitleString)
                            .setContentText(contentTextString);


                Intent resultIntent = new Intent(this, MainActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

                stackBuilder.addParentStack(MainActivity.class);

                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
               NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
                mNotificationManager.notify(mId, mBuilder.build());
    }

    private void blockApp(){
        Intent intent = new Intent(this, NoEnoughCreditsDialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private String getForegroundApp() throws Exception{
        ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);

        String foregroundTaskPackageName = foregroundTaskInfo .topActivity.getPackageName();
        PackageManager pm = this.getPackageManager();
        PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
        System.out.println(foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString());
        return foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();
    }

    private int checkCredits(){
        user = UserDAO.getInstance(getApplicationContext()).selectUser();
        if (user == null){
            return 0;
        }
        return user.getCredits();
    }
}
