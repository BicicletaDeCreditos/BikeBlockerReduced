package bikeblocker.bikeblocker.Model;


import android.content.Context;

import java.io.Serializable;

import bikeblocker.bikeblocker.Database.UserDAO;

public class User implements Serializable {
    private final int OK = 0;
    private final int INCORRECT = 1;
    private final int NO_USER = 2;

    private String name;
    private String username;
    private String password;
    private static int credits = 10;

    private static UserDAO userDAO;

    public User(){

    }

    public String getUsername() {
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getName(){
        return name;
    }
    public int getCredits(){
        return credits;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setCredits(int credits){
        this.credits = credits;
    }

    public int getAuthentication(String password, Context context){
        userDAO = userDAO.getInstance(context);
        User user = userDAO.selectUser();
        if(user == null) {
            return NO_USER;
        }else if (user.getPassword().equals(password)){
            return OK;
        }else {
            return INCORRECT;
        }
    }

    public boolean verifyFirstTimeAccess(Context context){
        userDAO = userDAO.getInstance(context);
        User user = userDAO.selectUser();

        if (user == null){
            return true;
        }
        return false;
    }


}
