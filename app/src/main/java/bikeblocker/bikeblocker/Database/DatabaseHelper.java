package bikeblocker.bikeblocker.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "BIKEBLOCKER";
    public static final int VERSION = 3;
    protected static final String SCRIPT_COMMAND_CREATION_ADMINTABLE = "CREATE TABLE IF NOT EXISTS userAdmin (" +
            "  useradmin VARCHAR(6) NOT NULL PRIMARY KEY," +
            "  adminpassword VARCHAR(15));";
    protected static final String SCRIPT_COMMAND_CREATION_USERTABLE = "CREATE TABLE IF NOT EXISTS user (" +
            "  username VARCHAR(15) NOT NULL PRIMARY KEY," +
            "  password VARCHAR(15) NOT NULL," +
            "  name VARCHAR(15) NOT NULL," +
            "  credits INT NULL);";
    protected static final String SCRIPT_COMMAND_CREATION_APPSTABLE =
            "CREATE TABLE apps (" +
                    "  app_id INT NOT NULL PRIMARY KEY," +
                    "  app_name VARCHAR(15) NOT NULL," +
                    "  credits_hour INT NOT NULL," +
                    "  user_username VARCHAR(15) NOT NULL," +
                    "  CONSTRAINT fk_apps_user" +
                    "    FOREIGN KEY (user_username)" +
                    "    REFERENCES user (username)" +
                    "    ON DELETE NO ACTION" +
                    "    ON UPDATE NO ACTION);";
    protected static final String SCRIPT_CREATION=
            "CREATE TABLE webpages (" +
            "  webpages_id INT NOT NULL PRIMARY KEY," +
            "  webpage_name VARCHAR(15) NULL," +
            "  url VARCHAR(100) NULL," +
            "  credits_hour INT NULL," +
            "  user_username MEDIUMTEXT NOT NULL," +
            "  CONSTRAINT fk_webpages_user1" +
            "    FOREIGN KEY (user_username)" +
            "    REFERENCES user (username)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION);" +
            "CREATE TABLE session_web (" +
            "  sessionweb_id INT NOT NULL PRIMARY KEY," +
            "  spent_credits INT NULL," +
            "  duration INT NULL," +
            "  webpages_webpages_id INT NOT NULL," +
            "  CONSTRAINT fk_session_web_webpages1" +
            "    FOREIGN KEY (webpages_webpages_id)" +
            "    REFERENCES webpages (webpages_id)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION);" +
            "CREATE TABLE session_app (" +
            "  sessionapp_id INT NOT NULL PRIMARY KEY," +
            "  spent_credits INT NULL," +
            "  duration INT NULL," +
            "  apps_app_id INT NOT NULL," +
            "  CONSTRAINT fk_session_app_apps1" +
            "    FOREIGN KEY (apps_app_id)" +
            "    REFERENCES apps (app_id)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION);";

    protected static final String SCRIPT_COMMAND_DELETION_DATABASE = "DROP TABLE IF EXISTS userAdmin ;" +
            "DROP TABLE IF EXISTS user ;";
    protected  static final String SCRIPT_DELETION =
            "DROP TABLE IF EXISTS webpages ;" +
            "DROP TABLE IF EXISTS apps ;" +
            "DROP TABLE IF EXISTS session_app ;" +
            "DROP TABLE IF EXISTS session_web ;";

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
        db.execSQL(this.SCRIPT_COMMAND_DELETION_DATABASE);
        db.execSQL(SCRIPT_COMMAND_CREATION_ADMINTABLE);
        db.execSQL(SCRIPT_COMMAND_CREATION_USERTABLE);
        db.execSQL(SCRIPT_COMMAND_CREATION_APPSTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL(this.SCRIPT_COMMAND_DELETION_DATABASE);
       onCreate(db);
    }
}
