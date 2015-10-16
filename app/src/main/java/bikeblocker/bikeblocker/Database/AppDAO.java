package bikeblocker.bikeblocker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bikeblocker.bikeblocker.Model.App;

public class AppDAO {
    public static final String TABLE_NAME = "apps";
    public static final String APPID_COLUMN = "app_id";
    public static final String APPNAME_COLUMN = "app_name";
    public static final String CREDITSHOUR_COLUMN = "credits_hour";
    public static final String USER_COLUMN = "user_username";

    private SQLiteDatabase database = null;
    private static AppDAO appDAO = null;
    List<HashMap<String, String>> app_list;

    private AppDAO(Context context) {
        DatabaseHelper persistenceHelper = DatabaseHelper
                .getInstance(context);
        database = persistenceHelper.getWritableDatabase();
    }

    public static AppDAO getInstance(Context context) {
        if (appDAO == null) {
            appDAO = new AppDAO(context.getApplicationContext());
        }
        return appDAO;
    }

    public void saveApp(App app) {
        ContentValues values = generateContentValuesApp(app);
        database.insert(TABLE_NAME, null, values);
    }

    public void deleteApp(App app) {
        String[] valuesToReplace = {String.valueOf(app.getAppID())};
        database.delete(TABLE_NAME, APPID_COLUMN + " = ?", valuesToReplace);
    }

    public void editAppInformations(App app) {
        ContentValues values = generateContentValuesApp(app);
        database.update(TABLE_NAME, values, APPID_COLUMN + " = " + app.getAppID(), null);
    }

    public void closeDatabaseConnection() {
        if ((database != null) && (database.isOpen())) {
            database.close();
        }
    }

    public ContentValues generateContentValuesApp(App app) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(APPID_COLUMN, app.getAppID());
        contentValues.put(APPNAME_COLUMN, app.getAppName());
        contentValues.put(CREDITSHOUR_COLUMN, app.getCreditsPerHour());
        contentValues.put(USER_COLUMN, app.getUser());

        return contentValues;
    }

    public App selectApp(String appID) {
        String queryApp = "SELECT * FROM " + TABLE_NAME + " where "
                + APPID_COLUMN + " = ?";

        Cursor cursor = database.rawQuery(queryApp, new String[]{appID});

        App app = null;

        if (cursor.moveToFirst()) {
            app = new App();
            ContentValues contentValues = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
            app = contentValuesApp(contentValues);
        }
        return app;

    }

    public List<HashMap<String, String>> selectAllApps(String username) {
        String queryAllApps = "SELECT * FROM " + TABLE_NAME + " where "
                + USER_COLUMN + " = ?";
        App app;
        app_list = new ArrayList<HashMap<String, String>>();

        try {
            Cursor cursor = database.rawQuery(queryAllApps, new String[]{username});

            while (cursor.moveToNext()){
                HashMap map = new HashMap();
                map.put("appName", cursor.getString(cursor.getColumnIndex("app_name")));
                map.put("credits_hour", cursor.getString(cursor.getColumnIndex("credits_hour")));
                app_list.add(map);
            }
        }catch (Exception e){
            System.out.println("Exception on get apps.");

        }
        return app_list;
    }

    public App contentValuesApp(ContentValues contentValues) {
        App app = new App();

        app.setAppID(contentValues.getAsInteger(APPID_COLUMN));
        app.setAppName(contentValues.getAsString(APPNAME_COLUMN));
        app.setCreditsPerHour(contentValues.getAsInteger(CREDITSHOUR_COLUMN));
        app.setUser(contentValues.getAsString(USER_COLUMN));

        return app;
    }



}
