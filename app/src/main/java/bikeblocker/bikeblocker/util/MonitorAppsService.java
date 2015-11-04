package bikeblocker.bikeblocker.util;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import java.util.List;

import bikeblocker.bikeblocker.Control.CheckUserLoginDialogActivity;
import bikeblocker.bikeblocker.Database.AppDAO;
import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.App;
import bikeblocker.bikeblocker.Model.User;

public class MonitorAppsService extends Service implements Runnable {
    private String status = "";
    private String user_name;
    private String previousApp = "";
    private int counter = 0;
    private int counterPerCredit;// how many times a tread will sleep until one credits is consumed
    private final int threadsPerMin = 6;// the thread sleep time will be reduced for 5 sec (final value = 12)
    private final int TEN = 36; // the thread sleep time will be reduced for 5 sec (final value = 72)
    private final int TWENTY = 18; // the thread sleep time will be reduced for 5 sec (final value = 18)
    private final int THIRTY = 12; // the thread sleep time will be reduced for 5 sec (final value = 24)
    private final int HALF_MINUTE = 3;
    private final int A_MINUTE = 6;
    private final int TWO_MINUTES = 12;

    @Override
    public void onCreate(){
        super.onCreate();
        Thread aThread = new Thread(this);
        aThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        status = intent.getStringExtra("status");
        user_name = intent.getStringExtra("user");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
                System.out.println("Status: " + status);
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
            if(status.equalsIgnoreCase("notlogged")){
                loginDialog();
                previousApp = foregroundTaskAppName;
            } else if (status.equalsIgnoreCase("logged")) {//inserir timeout de 1 hora para deslogar o usuario
                App app = AppDAO.getInstance(getApplicationContext()).selectApp(foregroundTaskAppName);
                if(app != null){
                    System.out.println("User has app");
                    monitorAppUsage(app.getCreditsPerHour(), app.getAppName());
                }
            }
        }
    }

    private void loginDialog() {
        Intent intent;
        intent = new Intent(this, CheckUserLoginDialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void monitorAppUsage(int creditsPerHour, String app_name) {
        if(checkCredits(user_name) > 0){
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
        status = "notlogged";
        user_name = "";
        counter = 0;
    }

    private void debit() {
        int actualCredits = checkCredits(user_name);
        User user = UserDAO.getInstance(getApplicationContext()).selectUser();
        user.setCredits(actualCredits - 1);
        System.out.println("Quantidade atual (nome): " + UserDAO.getInstance(getApplicationContext()).selectUser().getName());
        System.out.println("Quantidade atual: " + UserDAO.getInstance(getApplicationContext()).selectUser().getCredits());
        UserDAO.getInstance(getApplicationContext()).editUserInformation(user);
        System.out.println("Debitado creditos de usuario. ");
        System.out.println("Quantidade atual: " + UserDAO.getInstance(getApplicationContext()).selectUser().getCredits());
    }

    private void blockApp(){
        Toast toast = Toast.makeText(getApplicationContext(), "Sorry! Your credits ran out.", Toast.LENGTH_LONG);
        toast.show();
        //voltar para home
        Intent homeIntent= new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }

    private String getForegroundApp() throws Exception{
        ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);

        String foregroundTaskPackageName = foregroundTaskInfo .topActivity.getPackageName();
        PackageManager pm = this.getPackageManager();
        PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
        return foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();
    }

    private int checkCredits(String user_name){
        User user = UserDAO.getInstance(getApplicationContext()).selectUser();
        if (user == null){
            return 0;
        }
        return user.getCredits();
    }
}
