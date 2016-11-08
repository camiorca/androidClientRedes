package com.example.camil.labredes;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener{

    private final Context context = this;

    private Button startButton;
    private Button stopButton;
    private EditText ipAddressText;
    private RadioButton tcpRadio;
    private RadioButton udpRadio;
    private Location location;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddressText = (EditText) findViewById(R.id.ipAddressText);
        tcpRadio = (RadioButton) findViewById(R.id.tcpRadioButton);
        udpRadio = (RadioButton) findViewById(R.id.udpRadioButton);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);
        stopButton.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        //Getting IP Address
        String ip = ipAddressText.getText().toString();
        if(ip.isEmpty()){
            Toast.makeText(this,"Por favor ingresa una dirección IP válida",Toast.LENGTH_SHORT).show();
        }

        Log.e("MainClass", "Info available: " + getInfo());

        //Initialize workflow
        TCPClient tcp = new TCPClient(ip);
        UDPClient udp = new UDPClient(ip);

        switch (v.getId()){
            case R.id.startButton:
                stopButton.setEnabled(true);
                startButton.setEnabled(false);
                if(tcpRadio.isChecked()){
                    tcp.run(getInfo());
                    Log.e("MainTCP","TCP Running");
                    Toast.makeText(this,"Iniciando envio de datos...",Toast.LENGTH_LONG).show();
                }
                if(udpRadio.isChecked()){
                    udp.run(getInfo());
                    Log.e("MainUDP","UDP Running");
                    Toast.makeText(this,"Iniciando envio de datos...",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.stopButton:
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                if(tcpRadio.isChecked()){
                    tcp.stop();
                    Log.e("MainTCP","TCP Stopped");
                    Toast.makeText(this,"Deteniendo envio de datos...",Toast.LENGTH_LONG).show();
                }
                if(udpRadio.isChecked()){
                    udp.stop();
                    Log.e("MainUDP","UDP Stopped");
                    Toast.makeText(this,"Deteniendo envio de datos...",Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    private String getInfo(){
        String res = "";

        //Values
        long time = 1000*60*1;
        long distance = 10;

        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean gpsUp = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkUp = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(gpsUp == false && networkUp == false){

        }
        else{
            boolean getLocation = true;
            if (networkUp == true) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,time,distance, this);
                Log.d("Network", "Network");
                if (locationManager != null) {

                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                    else
                    {
                        System.out.println("Error");
                    }
                }

            }
            // if GPS Enabled get lat/long using GPS Services
            if (gpsUp == true) {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,time,distance, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
        }

        res = latitude + "==" + longitude + "=="+(location.getAltitude()+2640)+"=="+location.getSpeed();

        return res;

    }

    //Needed methods, never used tho, do not attempt to change them, or i will strike down
    //with furious anger.
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }
}
