package bikeblocker.bikeblocker.Model;


import android.content.Context;

import java.io.Serializable;

import bikeblocker.bikeblocker.Database.UserAdminDAO;

public class UserAdmin implements Serializable {
    private final int NO_ADMIN = 1;
    private final int INCORRECT = 2;
    private final int OK = 0;

    private final String userNameAdmin = "admin";
    private String userPasswordAdmin;

    private static UserAdminDAO userDAO;

    public UserAdmin(){

    }

    /*Username Admin is always "admin". There is no need do set this attribute."*/
    public String getUserNameAdmin(){
        return userNameAdmin;
    }

    public String getUserPasswordAdmin(){
        return userPasswordAdmin;
    }
    public void setUserPasswordAdmin(String userPasswordAdmin){
        this.userPasswordAdmin = userPasswordAdmin;
    }

    public boolean verifyFirstTimeAccess(Context context){
        userDAO = userDAO.getInstance(context);
        UserAdmin admin = userDAO.selectUserAdmin(this.userNameAdmin);

        if (admin.getUserPasswordAdmin() == null || admin == null){
            return true;
        }
        return false;
    }

    public int verifyAdminPassword(String userPasswordAdmin, Context context){
        userDAO = userDAO.getInstance(context);
        UserAdmin admin = userDAO.selectUserAdmin(this.userNameAdmin);
        //System.out.println("user from database= " + admin.getUserNameAdmin() + "senha " + admin.getUserPasswordAdmin() );
        if((admin.getUserPasswordAdmin() == null) || admin == null){
            return NO_ADMIN;
        }else if ( admin.getUserPasswordAdmin().equals(userPasswordAdmin)){
            return OK;
        }else {
            return INCORRECT;
        }
    }

}
