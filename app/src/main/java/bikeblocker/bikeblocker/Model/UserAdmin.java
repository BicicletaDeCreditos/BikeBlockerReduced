package bikeblocker.bikeblocker.Model;


import android.content.Context;

import java.io.Serializable;

import bikeblocker.bikeblocker.Database.UserAdminDAO;

public class UserAdmin implements Serializable {
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

        if (admin.getUserNameAdmin() == null){
            return true;
        }
        return false;
    }

    public UserAdmin verifyAdminPassword(String userPasswordAdmin, Context context){
        userDAO = userDAO.getInstance(context);
        UserAdmin admin = userDAO.selectUserAdmin(this.userNameAdmin);
        System.out.println("ADMIN = "+ admin.getUserPasswordAdmin() +"  "+ admin.getUserNameAdmin());

        if(admin == null){
            return null;
        }else if (admin.getUserPasswordAdmin().equals(userPasswordAdmin) ){
            return admin;
        }else {
            return null;
        }
    }

}
