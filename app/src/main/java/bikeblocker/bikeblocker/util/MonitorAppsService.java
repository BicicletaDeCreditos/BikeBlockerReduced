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

public class MonitorAppsService extends Service implements Runnable {
    private String status = "";
    @Override
    public void onCreate(){
        super.onCreate();
        Thread aThread = new Thread(this);
        aThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "On start command", Toast.LENGTH_LONG).show();

        status = intent.getStringExtra("status");
        String user_name = intent.getStringExtra("user");

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
        ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);

        String foregroundTaskPackageName = foregroundTaskInfo .topActivity.getPackageName();
        PackageManager pm = this.getPackageManager();
        PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
        String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();

        if(appsNameList.contains(foregroundTaskAppName)){
            System.out.println("Login");
            System.out.println("Status: " +status);
            if(status.equalsIgnoreCase("notlogged")){
                loginDialog();
            }else{
                System.out.println("Already Logged");
            }

            // login successfull -> checkCredits();
            // tem creditos -> monitorAppUsage();
            // nao tem creditos -> showAlertDialog("You dont have enough credits") && nao permite acesso
        }


        // OK verifica se o app em foreground pertence a lista
        // se pertencer, pede senha e login na primeira vez e procura na tabela de apps se o usuario tem aquele app configurado
        // Constantemente
        // verifica se tem creditos suficientes
        // se tem creditos, permite o uso
        // contabiliza tempo
        // se nao tem, exibe mensagem de bloqueio
        // ***CALIBRAR O CONSUMO DE CREDITOS DE ACORDO COM A QUANTIDADE PARA UMA HORA***
        // 10 creditos => 1 credito a cada 6 minutos
        // 20 creditos => 1 credito a cada 3 minutos
        // 30 creditos => 1 credito a cada 2 minutos

    }

    private void loginDialog() {
        Intent intent;
        intent = new Intent(this, CheckUserLoginDialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void blockApp(){

    }
}
