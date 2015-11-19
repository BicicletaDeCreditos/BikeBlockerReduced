package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
//import android.widget.ImageButton;
import android.widget.TextView;

import bikeblocker.bikeblocker.R;

public class CyclingActivity extends Activity {
    private TextView creditsTextView;
    private TextView velocityTextView;
    private TextView caloriesTextView;
    private TextView distanceTextView;
    //private ImageButton finishCyclingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycling);

        creditsTextView = (TextView) findViewById(R.id.creditsTextView);
        velocityTextView  = (TextView) findViewById(R.id.velocityTextView);
        caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);

        //finishCyclingButton = (ImageButton) findViewById(R.id.finishCyclingButton);
    }

    public void setCreditsTextView(String credits){
        creditsTextView.setText(credits);
    }

    public void setVelocityTextView(String velocity){
        velocityTextView.setText(velocity);
    }

    public void setCaloriesTextView(String calories){
        caloriesTextView.setText(calories);
        System.out.println("Dado recebido: " + calories);
    }

    public void setDistanceTextView(String distance){
        distanceTextView.setText(distance);
    }

    public Context returnApplicationContext(){
        return this;
    }

}
