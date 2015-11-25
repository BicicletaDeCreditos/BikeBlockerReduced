package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;
import bikeblocker.bikeblocker.util.MonitorCyclingData;
import bikeblocker.bikeblocker.util.StartBluetoothConnection;

public class CyclingActivity extends Activity {
    private TextView creditsTextView;
    private TextView velocityTextView;
    private TextView caloriesTextView;
    private TextView distanceTextView;
    private ImageButton finishCyclingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycling);

        creditsTextView = (TextView) findViewById(R.id.creditsTextView);
        velocityTextView  = (TextView) findViewById(R.id.velocityTextView);
        caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);

        creditsTextView.setText("0");
        velocityTextView.setText("0");
        caloriesTextView.setText("0");
        distanceTextView.setText("0");

        finishCyclingButton = (ImageButton) findViewById(R.id.finishCyclingButton);
        finishCyclingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCycling();
            }
        });

        try {
            startMonitor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setCreditsTextView(String credits){
        creditsTextView.setText(credits);
        System.out.println("Credits: " + credits);
    }

    public void setVelocityTextView(String velocity){
        velocityTextView.setText(velocity);
        System.out.println("Velocity: " + velocity);
    }

    public void setCaloriesTextView(String calories){
        caloriesTextView.setText(calories);
        System.out.println("Calorias: " + calories);
    }

    public void setDistanceTextView(String distance){
        distanceTextView.setText(distance);
        System.out.println("Distance: " + distance);
    }

    public void cancelCycling(){
        updateCredits();
        finish();
    }

    @Override
    public void onBackPressed() {
        cancelCycling();
        super.onBackPressed();
    }

    private void updateCredits(){
        User user = UserDAO.getInstance(getApplicationContext()).selectUser();
        if (user == null){
            Toast.makeText(getApplicationContext(), "There's no registered user. Credits not saved.", Toast.LENGTH_SHORT).show();
            return;
        }

        int actualCredits = user.getCredits();
        user.setCredits(actualCredits + Integer.parseInt(creditsTextView.getText().toString()));
        UserDAO.getInstance(getApplicationContext()).editUserInformation(user);
        System.out.println("Quantidade atual: " + UserDAO.getInstance(getApplicationContext()).selectUser().getCredits());
    }

    public void startMonitor() throws InterruptedException {
        MonitorCyclingData monitor = new MonitorCyclingData(this, StartBluetoothConnection.getBluetoothConnection(),
                StartBluetoothConnection.getHandler());
        monitor.startMonitoring();
    }

}
