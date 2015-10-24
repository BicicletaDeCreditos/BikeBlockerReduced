package bikeblocker.bikeblocker.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bikeblocker.bikeblocker.Model.User;

public class UserDAO {

    public static final String TABLE_NAME = "user";
    public static final String USERNAME_COLUMN = "username";
    public static final String PASSWORD_COLUMN = "password";
    public static final String NAME_COLUMN = "name";
    public static final String CREDITS_COLUMN = "credits";

    List<HashMap<String, String>> user_list;


    private SQLiteDatabase database = null;
    private static UserDAO userDAO = null;

    private UserDAO(Context context) {
        DatabaseHelper persistenceHelper = DatabaseHelper
                .getInstance(context);
        database = persistenceHelper.getWritableDatabase();
    }

    public static UserDAO getInstance(Context context) {
        if (userDAO == null) {
            userDAO = new UserDAO(context.getApplicationContext());
        }
        return userDAO;
    }

    public void saveUser(User user) {
        ContentValues values = generateContentValuesUser(user);
        database.insert(TABLE_NAME, null, values);
    }

    public void deleteUser(User user) {
        String[] valuesToReplace = {String.valueOf(user.getUsername())};
        database.delete(TABLE_NAME, USERNAME_COLUMN + " = ?", valuesToReplace);
    }

    public void editUserInformations(User user) {
        ContentValues values = generateContentValuesUser(user);
        database.update(TABLE_NAME, values, USERNAME_COLUMN + " = " + user.getUsername(), null);
    }

    public void closeDatabaseConnection() {
        if ((database != null) && (database.isOpen())) {
            database.close();
        }
    }

    public ContentValues generateContentValuesUser(User user) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(USERNAME_COLUMN, user.getUsername());
        contentValues.put(PASSWORD_COLUMN, user.getPassword());
        contentValues.put(NAME_COLUMN, user.getName());
        contentValues.put(CREDITS_COLUMN, user.getCredits());

        return contentValues;
    }

    public User selectUser(String name) {
        String queryUser = "SELECT * FROM " + TABLE_NAME + " where " + NAME_COLUMN + " = ?";

        User user = null;
        try{
            Cursor cursor = database.rawQuery(queryUser, new String[]{name});

            if (cursor.moveToFirst()) {
                user = new User();
                ContentValues contentValues = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
                user = contentValuesUser(contentValues);
            }
        }catch (Exception e){
            System.out.println("Exeption on get one user.");
        }
        return user;

    }

    public List<HashMap<String, String>> selectAllUsers() {
        User user;
        user_list = new ArrayList<HashMap<String, String>>();

        try {
            Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);

            while (cursor.moveToNext()){
                HashMap map = new HashMap();
                map.put("name", cursor.getString(cursor.getColumnIndex("name")));
                map.put("user", cursor.getString(cursor.getColumnIndex("username")));
                user_list.add(map);
            }
        }catch (Exception e){
            System.out.println("Exception on get all users.");

        }
        return user_list;
    }

    public User contentValuesUser(ContentValues contentValues) {
        User user = new User();

        user.setPassword(contentValues.getAsString(PASSWORD_COLUMN));
        user.setUsername(contentValues.getAsString(USERNAME_COLUMN));
        user.setName(contentValues.getAsString(NAME_COLUMN));
        user.setCredits(contentValues.getAsInteger(CREDITS_COLUMN));

        return user;
    }
}
