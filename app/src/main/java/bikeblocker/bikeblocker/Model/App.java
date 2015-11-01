package bikeblocker.bikeblocker.Model;

import java.io.Serializable;

public class App implements Serializable{
    private int appID;
    private String appName;
    private int creditsPerHour;
    private String user_name;

    public App(){

    }

    public String getAppName(){
        return appName;
    }
    public int getAppID(){
        return appID;
    }
    public int getCreditsPerHour(){
        return creditsPerHour;
    }
    public String getUser(){
        return user_name;
    }

    public void setAppName(String appName){
        this.appName = appName;
    }

    public void setAppID(int id) {
        this.appID = id;
    }
    public void setCreditsPerHour(int creditsPerHour){
        this.creditsPerHour = creditsPerHour;
    }

    public void setUser(String user_name){
        this.user_name = user_name;
    }

}
