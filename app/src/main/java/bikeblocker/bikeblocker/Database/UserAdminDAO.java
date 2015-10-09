package bikeblocker.bikeblocker.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import bikeblocker.bikeblocker.Model.UserAdmin;

public class UserAdminDAO {

    public static final String TABLE_NAME = "userAdmin";
    public static final String USERNAME_COLUMN = "useradmin";
    public static final String USERPASSWORD_COLUMN = "adminpassword";

    private SQLiteDatabase database = null;
    private static UserAdminDAO userDAO = null;

    private UserAdminDAO(Context context) {
        DatabaseHelper persistenceHelper = DatabaseHelper
                .getInstance(context);
        database = persistenceHelper.getWritableDatabase();
    }

    public static UserAdminDAO getInstance(Context context) {
        if (userDAO == null) {
            userDAO = new UserAdminDAO(context.getApplicationContext());
        }
        return userDAO;
    }

    public void saveUser(UserAdmin user) {
        ContentValues values = generateContentValuesUser(user);
        database.insert(TABLE_NAME, null, values);
    }

    private void deleteUser(UserAdmin user) {
        String[] valuesToReplace = { String.valueOf(user.getUserNameAdmin()) };
        database.delete(TABLE_NAME, USERNAME_COLUMN + " = ?", valuesToReplace);
    }

    public void editUserInformations(UserAdmin user) {
        ContentValues values = generateContentValuesUser(user);

        String[] valuesToReplace = { String.valueOf(user.getUserNameAdmin()) };

        database.update(TABLE_NAME, values, USERNAME_COLUMN + " = ?",
                valuesToReplace);
    }

    public void closeDatabaseConnection() {
        if ((database != null) && (database.isOpen())) {
            database.close();
        }
    }

    public ContentValues generateContentValuesUser(UserAdmin user) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(USERNAME_COLUMN, user.getUserNameAdmin());
        contentValues.put(USERPASSWORD_COLUMN, user.getUserPasswordAdmin());

        return contentValues;
    }

    public UserAdmin selectUserAdmin(String username) {
        String queryUser = "SELECT * FROM " + TABLE_NAME + " where "
                + USERNAME_COLUMN + " = ?";
        UserAdmin user = recoverByQuery(queryUser, username);
        return user;
    }

    private UserAdmin recoverByQuery(String queryUser, String value) {
        Cursor cursor = database.rawQuery(queryUser, new String[] { value });

        UserAdmin user = new UserAdmin();

        if (cursor.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
            user = contentValuesUser(contentValues);
        }

        return user;
    }

    public UserAdmin contentValuesUser(ContentValues contentValues) {
        UserAdmin user = new UserAdmin();

        user.setUserPasswordAdmin(contentValues.getAsString(USERPASSWORD_COLUMN));

        return user;
    }
}
