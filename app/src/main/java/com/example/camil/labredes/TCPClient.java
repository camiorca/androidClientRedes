package com.example.camil.labredes;

import android.location.Location;
import android.os.SystemClock;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Created by camil on 20/09/2016.
 */
public class TCPClient {

    //private static Socket clienteTCPSocket;
    private static boolean running;
    private Location location;
    private static String ipAddress;

    public TCPClient(String ipAddress){
        running = true;
        this.ipAddress = ipAddress;
    }

    public void run(final String message){
        try {
            Log.e("TCPClient", "IP Address: " + ipAddress);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    try{
                        running = true;
                        Log.e("TCPClient", "Running biiih!");

                        Socket clienteTCPSocket = new Socket(ipAddress, 5000);
                        Log.e("TCPClient", "Is it null?:" + clienteTCPSocket);
                        DataInputStream input = new DataInputStream(clienteTCPSocket.getInputStream());
                        DataOutputStream output = new DataOutputStream(clienteTCPSocket.getOutputStream());

                        while (running) {
                            output.writeBytes(message + '\n');
                            SystemClock.sleep(1000);
                        }

                        output.writeBytes("END" + "\n");
                        clienteTCPSocket.close();
                        Log.e("TCPClient", "Running stopped with 'running' value at " + running);
                    }
                    catch (Exception exc){
                        Log.e("TCPClientError", "Error found: " + exc.getLocalizedMessage());
                    }
                }
            });
            t.start();
        }
        catch (Exception e){
            Log.e("TCPClient", "Error al conectar: " + e.getMessage());
        }
    }

    public void stop(){
        this.running = false;
        Log.e("TCPClientValue", "TCP running = " + running);
    }
}
