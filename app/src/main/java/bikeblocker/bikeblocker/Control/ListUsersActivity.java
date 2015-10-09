package bikeblocker.bikeblocker.Control;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;

public class ListUsersActivity extends ListActivity {
    public static final String USER_NAME = "user_name"; 	// for a value passed between activities during Intents
    private ListView contactListView; 				// reference for built-in ListView (set in onCreate)
    private CursorAdapter userAdapter; 			// Adapter that exposes data from a Cursor to a ListView widget (Android Doco.)
    UserDAO userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userdao = UserDAO.getInstance(getApplicationContext());

        contactListView = getListView(); 									// get ref to the built-in ListView from ListActivity inherited method

        setListAdapter(getListUsers()); 		// connect list view and adapter using ListActivity inherited method
    }

    //executes each time the Activity regains the focus (including the first time) after being partially or fully hidden
    @Override
    protected void onResume(){
        super.onResume();
        setListAdapter(getListUsers());
    }

    public SimpleAdapter getListUsers(){
        String[] from ={"name"};
        int[] to = { R.id.user_name };
        return new SimpleAdapter(this, userdao.selectAllUsersNEW(), R.layout.activity_list_users, from, to);
    }

    public void startViewUserActivity(View view){
        Intent viewUser = new Intent(this, ViewUserActivity.class);

        viewUser.putExtra(USER_NAME, ((TextView) view).getText());
        startActivity(viewUser);
    }

    public void startAddNewUserActivity(View view){
        Intent intent = new Intent(this, AddNewUserActivity.class);
        startActivity(intent);
    }

    public void startListUsersActivity(View view){
        Intent intent = new Intent(this, ListUsersActivity.class);
        startActivity(intent);
    }

    public void startChangePasswordActivity(View view){
        Intent intent = new Intent(this, RegisterPasswordActivity.class);
        startActivity(intent);
    }
}
