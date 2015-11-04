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
    public static final String TABLE_NAME = "user_apps";
    public static final String APPID_COLUMN = "app_id";
    public static final String APPNAME_COLUMN = "app_name";
    public static final String CREDITSHOUR_COLUMN = "credits_hour";

    private SQLiteDatabase database = null;
    private static AppDAO appDAO = null;
    List<HashMap<String, String>> app_list;

    private AppDAO(Context context) {
        DatabaseHelper persistenceHelper = DatabaseHelper.getInstance(context);
        database = persistenceHelper.getWritableDatabase();
    }

    public static AppDAO getInstance(Context context) {
        if (appDAO == null) {
            appDAO = new AppDAO(context.getApplicationContext());
        }
        return appDAO;
    }

    public void closeDatabaseConnection() {
        if ((database != null) && (database.isOpen())) {
            database.close();
        }
    }

    public void saveApp(App app) {
        ContentValues values = generateContentValuesApp(app);
        database.insert(TABLE_NAME, null, values);
    }

    public void editAppInformation(App app) {
        ContentValues values = generateContentValuesApp(app);
        database.update(TABLE_NAME, values, APPID_COLUMN + " = " + app.getAppID(), null);
    }

    public void deleteApp(App app) {
        database.delete(TABLE_NAME, APPID_COLUMN + " = " + app.getAppID(), null);
    }

    public ContentValues generateContentValuesApp(App app) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(APPNAME_COLUMN, app.getAppName());
        contentValues.put(CREDITSHOUR_COLUMN, app.getCreditsPerHour());

        return contentValues;
    }

    public List<HashMap<String, String>> selectAllApps() {
        app_list = new ArrayList<HashMap<String, String>>();

        try {
            Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);

            while (cursor.moveToNext()){
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("app_name", cursor.getString(cursor.getColumnIndex(APPNAME_COLUMN)));
                map.put("credits_hour", cursor.getString(cursor.getColumnIndex(CREDITSHOUR_COLUMN)));
                app_list.add(map);
            }
        }catch (Exception e){
            System.out.println("Exception on get user_apps." + e.toString());
        }
        return app_list;
    }

    public List<String> getAppsNameFromDatabase() {
        List<String> appsNameList = new ArrayList<String>();
        String queryAll = "SELECT * FROM " + TABLE_NAME;

        try {
            Cursor cursor = database.rawQuery(queryAll, null);
            String appName;
            while (cursor.moveToNext()){
                ContentValues contentValues = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
                appName = contentValues.getAsString(APPNAME_COLUMN);
                if(!appsNameList.contains(appName)){
                    appsNameList.add(appName);
                }
            }
        }catch (Exception e){
            System.out.println("Exception on get all apps name." + e.toString());
        }
        return appsNameList;
    }

    public App selectApp(String app_name) {
        String queryApp = "SELECT * FROM " + TABLE_NAME + " where " + APPNAME_COLUMN + " = ? ";

        App app = null;
        try{
            Cursor cursor = database.rawQuery(queryApp, new String[]{app_name});

            if (cursor.moveToFirst()) {
                ContentValues contentValues = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
                app = contentValuesApp(contentValues);
            }
        }catch (Exception e){
            System.out.println("Exception on get the app.");
        }
        return app;
    }

    public App contentValuesApp(ContentValues contentValues) {
        App app = new App();

        app.setAppID(contentValues.getAsInteger(APPID_COLUMN));
        app.setAppName(contentValues.getAsString(APPNAME_COLUMN));
        app.setCreditsPerHour(contentValues.getAsInteger(CREDITSHOUR_COLUMN));

        return app;
    }
}
