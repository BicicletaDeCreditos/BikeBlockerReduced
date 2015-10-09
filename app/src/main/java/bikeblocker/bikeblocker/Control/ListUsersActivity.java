package bikeblocker.bikeblocker.Control;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.R;

public class ListUsersActivity extends ListActivity {
    public static final String USER_NAME = "user_name";
    private ListView contactListView;
    private CursorAdapter userAdapter;
    UserDAO userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userdao = UserDAO.getInstance(getApplicationContext());

        contactListView = getListView();

        setListAdapter(getListUsers());
    }

    @Override
    protected void onResume(){
        super.onResume();
        setListAdapter(getListUsers());
    }

    public SimpleAdapter getListUsers(){
        String[] from ={"name"};
        int[] to = { R.id.user_name };
        if(userdao.selectAllUsers().isEmpty()){
            return new SimpleAdapter(this, null, R.layout.activity_list_no_users, null, null);
        }else {
            return new SimpleAdapter(this, userdao.selectAllUsers(), R.layout.activity_list_users, from, to);
        }
    }

    public void startViewUserActivity(View view){
        Intent viewUser = new Intent(this, ViewUserActivity.class);

        viewUser.putExtra(USER_NAME, ((TextView) view).getText());
        startActivity(viewUser);
    }
}
