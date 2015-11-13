package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

import bikeblocker.bikeblocker.Model.App;
import bikeblocker.bikeblocker.R;

public class ConnectBluetoothActivity extends Activity {
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver mReceiver;
    private ListView devicesListView;
    private ArrayAdapter<String> adapter;
    private BluetoothDevice bluetoothDevice;
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-9000-00905A9B34FB");
    private InputStream inputStream = null;
    private BluetoothSocket bluetoothSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_bluetooth);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        devicesListView = (ListView) findViewById(R.id.devicesListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        devicesListView.setAdapter(adapter);
        devicesListView.setOnItemClickListener(bikeblockerClickListener());

        turnBluetoothOn();
    }

    private AdapterView.OnItemClickListener bikeblockerClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder  builder = new AlertDialog.Builder(ConnectBluetoothActivity.this);

                builder.setTitle("Connect to BikeBlocker Bluetooth device.");
                builder.setPositiveButton(R.string.connect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        connect();
                        showStartDialog();
                    }
                });

                builder.setNegativeButton(R.string.button_cancel, null);
                builder.show();
            }
        };
    }

    public void turnBluetoothOn() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(getVisible, 0);

            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
        }
        else {
            Toast.makeText(getApplicationContext(), "Bluetooth is already on", Toast.LENGTH_LONG).show();
        }
    }

    public void turnBluetoothOff(View view) {
        adapter.clear();
        bluetoothAdapter.disable();
        bluetoothDevice = null;
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
    }

    public void listDevices(View view){
        adapter.clear();
        bluetoothDevice = null;
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        if(bluetoothAdapter.startDiscovery()){
            mReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        String deviceInfo = device.getName() + "\n" + device.getAddress();
                        if(adapter.getPosition(deviceInfo) < 0){
                            adapter.add(deviceInfo);
                            if(device.getName().contains("Beatriz")){//change to bikeblocker
                                bluetoothDevice = device;
                            }
                        }
                    }
                }
            };
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);
        }
    }

    private void connect(){
        bluetoothAdapter.cancelDiscovery();
        try{
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            // Attempt to connect to a remote device.
            bluetoothSocket.connect();
            Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_SHORT).show();
        } catch(IOException e){
            Log.e("ERRO AO CONECTAR", "O erro foi " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Conexão não foi estabelecida", Toast.LENGTH_SHORT).show();

        }
    }

    private void showStartDialog(){

    }

    //Colocar isso em uma thread quando nova atividade iniciar
    private void receberDados(){
        // Verifica se há conexão estabelecida com o Bluetooth.
        if(bluetoothSocket != null){

            //textoRecebido.setText("");

            try{

                // Get the input stream associated with this socket.
                inputStream = bluetoothSocket.getInputStream();
                int available = inputStream.available();
                if (available > 0) {
                    // Cria um buffer para ler a "sujeira"
                    byte[] uselessBuffer = new byte[available];

                    // Lê a "sujeira"
                    inputStream.read(uselessBuffer);
                }

                byte[] msgBuffer = new byte[1];

                // Reads bytes from this stream and stores them in the byte array
                inputStream.read(msgBuffer);

               // textoRecebido.setText(new String(msgBuffer));

                Toast.makeText(getApplicationContext(), "Mensagem foi recebida", Toast.LENGTH_LONG).show();

            } catch(IOException e){
                Log.e("ERRO AO RECEBER MENSAGEM", "O erro foi" + e.getMessage());
                Toast.makeText(getApplicationContext(), "Mensagem não recebida", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth não está conectado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mReceiver != null) {
                this.unregisterReceiver(mReceiver);
            }
        } catch (IllegalArgumentException e) {
            mReceiver = null;
        }
    }

}
