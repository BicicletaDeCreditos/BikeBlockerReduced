package bikeblocker.bikeblocker.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "BIKEBLOCKER";
    public static final int VERSION = 3;
    protected static final String SCRIPT_COMMAND_CREATION_USERTABLE = "CREATE TABLE IF NOT EXISTS user (" +
            "  _id int NOT NULL PRIMARY KEY," +
            "  username VARCHAR(15) NOT NULL ," +
            "  password VARCHAR(15) NOT NULL," +
            "  name VARCHAR(15) NOT NULL," +
            "  credits INT NULL);";
    protected static final String SCRIPT_COMMAND_CREATION_APPSTABLE =
            "CREATE TABLE user_apps (" +
                    "  app_id integer primary key autoincrement," +
                    "  app_name text NOT NULL," +
                    "  credits_hour INT NOT NULL);";
    protected static final String SCRIPT_CREATION=
            "CREATE TABLE session_app (" +
            "  sessionapp_id INT NOT NULL PRIMARY KEY," +
            "  spent_credits INT NULL," +
            "  duration INT NULL," +
            "  apps_app_id INT NOT NULL," +
            "  CONSTRAINT fk_session_app_apps1" +
            "    FOREIGN KEY (apps_app_id)" +
            "    REFERENCES user_apps (app_id)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION);";

    protected static final String SCRIPT_COMMAND_DELETION_DATABASE =
            "DROP TABLE IF EXISTS user ;" +
            "DROP TABLE IF EXISTS user_apps ;";
    protected  static final String SCRIPT_DELETION =
            "DROP TABLE IF EXISTS session_app ;";

    private static DatabaseHelper databaseHelper = null;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static DatabaseHelper getInstance(Context context){
        if (databaseHelper == null){
            databaseHelper = new DatabaseHelper(context.getApplicationContext());
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_COMMAND_DELETION_DATABASE);
        db.execSQL(SCRIPT_COMMAND_CREATION_USERTABLE);
        db.execSQL(SCRIPT_COMMAND_CREATION_APPSTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL(this.SCRIPT_COMMAND_DELETION_DATABASE);
       onCreate(db);
    }
}
