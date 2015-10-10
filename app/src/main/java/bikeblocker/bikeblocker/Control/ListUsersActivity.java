package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;

public class ListUsersActivity extends Activity {
    //public static final String USER_NAME = "user_name";
    private ListView usersListView;
    UserDAO userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userdao = UserDAO.getInstance(getApplicationContext());

        setContentView(R.layout.activity_list_users);

        usersListView = (ListView) findViewById(R.id.listViewUsers);
        usersListView.setAdapter(getListUsers());
        usersListView.setOnItemClickListener(startViewUserActivity(this));

        if(getListUsers().isEmpty()){
            Toast.makeText(getApplicationContext(), "There is no user registered!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        usersListView.setAdapter(getListUsers());
        if(getListUsers().isEmpty()){
            Toast.makeText(getApplicationContext(), "There is no user registered!", Toast.LENGTH_LONG).show();
        }
    }

    public SimpleAdapter getListUsers(){
        String[] from ={"name"};
        int[] to = new int[]{ android.R.id.text1 };

        return new SimpleAdapter(this, userdao.selectAllUsers(), android.R.layout.activity_list_item, from, to);
    }

    public AdapterView.OnItemClickListener startViewUserActivity(final Context context){
        return (new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View view, int position, long id) {
                Intent viewUser = new Intent(context, ViewUserActivity.class);

                viewUser.putExtra("name", ((TextView) view).getText());
                startActivity(viewUser);
            }
        });
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
