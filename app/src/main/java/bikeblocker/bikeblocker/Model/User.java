package bikeblocker.bikeblocker.Model;


import android.content.Context;

import java.io.Serializable;

import bikeblocker.bikeblocker.Database.UserDAO;

public class User implements Serializable {
    private final int OK = 0;
    private final int INCORRECT = 1;
    private final int NO_USERNAME = 2;

    private String password;
    private String name;
    private static int credits = 0;

    private static UserDAO userDAO;

    public User(){

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

    public void setPassword(String password){
        this.password = password;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCredits(int credits){
        this.credits = credits;
    }

    public int getAuthentication(String username, String password, Context context){
        userDAO = userDAO.getInstance(context);
        User user = userDAO.selectUser(username);
        if((user.getName() == null) || user == null) {
            return NO_USERNAME;
        }else if (user.getPassword().equals(password)){
            return OK;
        }else {
            return INCORRECT;
        }
    }

}
