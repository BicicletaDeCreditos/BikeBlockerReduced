package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import bikeblocker.bikeblocker.R;
import bikeblocker.bikeblocker.util.StartBluetoothConnection;

public class StartConnectionActivity extends Activity {

    private BluetoothAdapter bluetoothAdapter; // mudar para bluetoothconnection
    private ImageButton startButton;
    private StartBluetoothConnection startBluetoothConnection;
    private AlertDialog builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_connection);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        startButton = (ImageButton) findViewById(R.id.startButton);

        startBluetoothConnection = new StartBluetoothConnection(this);
    }

    public void connectToBikeBlocker(View view){
        // ligar bluetooth
        startBluetoothConnection.turnBluetoothOn();
        // procurar por dispositivo bluetooth da bike
        // verificar se o dispositivo ja foi pareado alguma vez na vida
        // SIM: conectar com o dispositivo
        // NAO: parear com o dispositivo e conectar com o dispositivo
        // Exibir mensagem que esta tentando estabelecer conexao com a bike enquanto o processo esta rodando
        // Iniciar uma nova activity quando conexao foi estabelecida
    }

    public void askForEnablingBluetooth(String requestEnableString){
        Intent turnOn = new Intent(requestEnableString);
        startActivityForResult(turnOn, 0);
    }

    public void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void showConnectionDialog(){
        AlertDialog.Builder  myBuilder = new AlertDialog.Builder(this);
        myBuilder.setMessage("Connecting to BikeBlocker Bluetooth device...");
        builder = myBuilder.create();
        builder.show();
    }

    public void dismissDialog(){
        builder.dismiss();
    }
}
