package com.example.camil.labredes;

import android.location.Location;
import android.os.SystemClock;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by CO6 on 20/09/2016.
 */
public class UDPClient {

    private static boolean running;
    private Location location;
    private static String ipAddress;

    public UDPClient(String ipAddress){
        running = true;
        this.ipAddress = ipAddress;
    }

    public void run(final String message){
        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    try{
                        running = true;
                        Log.e("UDPClient", "Running biiih!");

                        byte[] receiveData = new byte[1024];
                        byte[] sendData;

                        DatagramSocket clienteUDPSocket = new DatagramSocket();
                        InetAddress IPAddress = InetAddress.getByName(ipAddress);
                        Log.e("UDPClient","IPAddress: " + IPAddress);

                        while(running){
                            sendData = message.getBytes();
                            Log.e("UDPClient", "Message: " + message + "\n" + "MessageBytes: " + sendData);
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 7777);
                            clienteUDPSocket.send(sendPacket);
                            SystemClock.sleep(1000);
                        }
                    }
                    catch (Exception exc){
                        Log.e("UDPClientError", "Error: " + exc.getMessage());
                    }
                }
            });
            t.start();
        }
        catch (Exception e){
            Log.e("UDPClientError", "Error: " +e.getMessage() );
        }
    }

    public void stop(){
        this.running = false;
        Log.e("UDPClientValue", "UDP running = " + running);
    }

}
