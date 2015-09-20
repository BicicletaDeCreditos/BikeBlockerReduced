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

    public static String SCRIPT_COMMAND_CREATION_TABLE = "CREATE TABLE "
            + TABLE_NAME + " (" + USERNAME_COLUMN
            + " VARCHAR(255) NOT NULL PRIMARY KEY," + USERPASSWORD_COLUMN + ");";

    public static final String SCRIPT_COMMAND_DELETION_TABLE = "DROP TABLE IF EXISTS"
            + TABLE_NAME;

    private SQLiteDatabase database = null;
    private static UserAdminDAO userDAO = null;

    private UserAdminDAO(Context context) {
        DatabaseHelper persistenceHelper = DatabaseHelper
                .getInstance(context);
        database = persistenceHelper.getWritableDatabase();
    }

    /*
    Metodo para obter instancia de UserAdminDAO para acesso as rotinas de manipulacao de dados
     */
    public static UserAdminDAO getInstance(Context context) {
        if (userDAO == null) {
            userDAO = new UserAdminDAO(context.getApplicationContext());
        }
        return userDAO;
    }

    /*
     * Método responsável por salvar um usuário.
     */
    public void saveUser(UserAdmin user) {
        ContentValues values = generateContentValuesUser(user);
        database.insert(TABLE_NAME, null, values);
    }

    /*
     * Método responsável por deletar um usuário.
     */
    private void deleteUser(UserAdmin user) {
        String[] valuesToReplace = { String.valueOf(user.getUserNameAdmin()) };
        database.delete(TABLE_NAME, USERNAME_COLUMN + " = ?", valuesToReplace);
    }

    /*
     * Método responsável por editar informações de um determinado
     * usuário.
     */
    public void editUserInformations(UserAdmin user) {
        ContentValues values = generateContentValuesUser(user);

        String[] valuesToReplace = { String.valueOf(user.getUserNameAdmin()) };

        database.update(TABLE_NAME, values, USERNAME_COLUMN + " = ?",
                valuesToReplace);
    }

    /*
     * Método responsável por encerrar a conexão com
     * o banco de dados.
     */
    public void closeDatabaseConnection() {
        if ((database != null) && (database.isOpen())) {
            database.close();
        }
    }

    /*
     * Método responsável por relacionar cada atributo da instância de User
     * a uma determinada coluna de uma entrada da base de dados.
     */
    public ContentValues generateContentValuesUser(UserAdmin user) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(USERNAME_COLUMN, user.getUserNameAdmin());
        contentValues.put(USERPASSWORD_COLUMN, user.getUserPasswordAdmin());

        return contentValues;
    }

    /*
    Metodo responsavel por recuperar o UserAdmin do banco de dados
     */
    public UserAdmin selectUserAdmin(String username) {
        String queryUser = "SELECT * FROM " + TABLE_NAME + " where "
                + USERNAME_COLUMN + " = ?";
        UserAdmin user = recoverByQuery(queryUser, username);
        return user;
    }
    /*
     * Método responsável por realizar a consulta e
     * recuperar o usuario admin.
     * "queryUser" = query a ser utilizada
     * "value" = valor a ser utilizado como chave de busca
     */
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

    /*
     * Método responsável por preencher uma instância de UserAdmin com o valor de 'password'
     * contentValues = instância da Classe ContentValues
     */
    public UserAdmin contentValuesUser(ContentValues contentValues) {
        UserAdmin user = new UserAdmin();

        user.setUserPasswordAdmin(contentValues.getAsString(USERPASSWORD_COLUMN));

        return user;
    }
}
