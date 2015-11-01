package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.R;

public class ListUsersActivity extends Activity {
    private ListView usersListView;
    UserDAO userdao;

    @Override
         protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userdao = UserDAO.getInstance(getApplicationContext());

        setContentView(R.layout.activity_list_users);

        usersListView = (ListView) findViewById(R.id.listViewUsers);
        usersListView.setAdapter(getListUsers());

        if(getListUsers().isEmpty()){
            Toast.makeText(getApplicationContext(), "There is no user registered!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return;
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
        int[] to = new int[]{ R.id.user_name };
        return new SimpleAdapter(this, userdao.selectAllUsers(), R.layout.users, from, to);
    }

    public void startViewUserActivity(View view) {
        Intent viewUser = new Intent(this, ViewUserActivity.class);
        viewUser.putExtra("user_name", ((TextView) view).getText());
        startActivity(viewUser);
    }

    public void startAddNewUserActivity(View view){
        Intent intent = new Intent(this, AddNewUserActivity.class);
        startActivity(intent);
    }

    public void startChangePasswordActivity(View view){
        Intent intent = new Intent(this, RegisterPasswordActivity.class);
        startActivity(intent);
    }
}
