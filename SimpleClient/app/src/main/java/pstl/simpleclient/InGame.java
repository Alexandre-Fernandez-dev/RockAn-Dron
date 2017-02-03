package pstl.simpleclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class InGame extends AppCompatActivity {

    String strusername;
    String strserver;
    String strport;

    DatagramSocket clientSocket;
    DatagramPacket sendPacket, receivePacket;
    InetAddress ipServer;
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];
    String str = "";

    String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);

        Intent i = getIntent();
        strusername = i.getStringExtra("username");
        strserver = i.getStringExtra("server");
        strport = i.getStringExtra("port");

        final TextView input_score = (TextView) findViewById(R.id.score);
        final Button sendButton = (Button) findViewById(R.id.send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = input_score.getText().toString();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(score)) {
                            try {
                                clientSocket = new DatagramSocket();
                                ipServer = InetAddress.getByName(strserver);
                                sendData = new String(Home.idClient + " SCORETICK "+score).getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, ipServer, Integer.parseInt(strport));
                                clientSocket.send(sendPacket);
                            } catch (SocketException e) {
                                e.printStackTrace();
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
            }
        });
    }
}
