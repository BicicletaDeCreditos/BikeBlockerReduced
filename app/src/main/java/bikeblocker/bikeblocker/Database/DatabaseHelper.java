package bikeblocker.bikeblocker.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "BIKEBLOCKER";
    public static final int VERSION = 1;

    private static DatabaseHelper databaseHelper = null;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    /*
    * Obter uma instancia de DatabaseHelper para facilitar insercao, delecao e consultas ao banco
    */
    public static DatabaseHelper getInstance(Context context){
        if (databaseHelper == null){
            databaseHelper = new DatabaseHelper(context.getApplicationContext());
        }
        return databaseHelper;
    }

    /*
    * Metodo responsavel por executar o comando SQL para criacao das tabelas do banco
    * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserAdminDAO.SCRIPT_COMMAND_CREATION_TABLE);
    }

    /*
    Metodo responsavel por atualizar o banco.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL(UserAdminDAO.SCRIPT_COMMAND_DELETION_TABLE);
       onCreate(db);
    }
}
