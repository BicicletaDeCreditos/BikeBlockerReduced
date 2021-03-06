package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
    private TextView creditsLabelTextView;
    private TextView caloriesLabelTextView;
    private TextView velocityLabelTextView;
    private TextView distanceLabelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycling);

        creditsTextView = (TextView) findViewById(R.id.creditsTextView);
        velocityTextView  = (TextView) findViewById(R.id.velocityTextView);
        caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);

        creditsLabelTextView = (TextView) findViewById((R.id.creditsLabelTextView));
        velocityLabelTextView = (TextView) findViewById(R.id.velocityLabel);
        caloriesLabelTextView = (TextView) findViewById(R.id.caloriesLabel);
        distanceLabelTextView = (TextView) findViewById(R.id.distanceLabel);


        creditsTextView.setText("0");
        velocityTextView.setText("0");
        caloriesTextView.setText("0");
        distanceTextView.setText("0");

        Typeface newTitleFace = Typeface.createFromAsset(getAssets(),"MJF Zhafira Demo.ttf");
        creditsTextView.setTypeface(newTitleFace);
        creditsLabelTextView.setTypeface(newTitleFace);
        velocityLabelTextView.setTypeface(newTitleFace);
        caloriesLabelTextView.setTypeface(newTitleFace);
        distanceLabelTextView.setTypeface(newTitleFace);


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

        startChronometer();

    }

    public void startChronometer() {
        ((Chronometer) findViewById(R.id.chronometer)).start();
    }

    public void stopChronometer() {
        ((Chronometer) findViewById(R.id.chronometer)).stop();
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
        stopChronometer();
        finish();

    }

    @Override
    public void onBackPressed() {
        cancelCycling();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
