package pstl.simpleclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NewGame extends AppCompatActivity {

    String strusername;
    String strserver;
    String strport;

    DatagramSocket clientSocket;
    DatagramPacket sendPacket, receivePacket;
    InetAddress ipServer;
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];
    String str = "";

    String nbP;
    String levelID;
    String length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        Intent i = getIntent();
        strusername = i.getStringExtra("username");
        strserver = i.getStringExtra("server");
        strport = i.getStringExtra("port");

        final TextView input_nbplayer = (TextView) findViewById(R.id.nbplayer);
        final TextView input_level = (TextView) findViewById(R.id.level);
        final TextView input_length = (TextView) findViewById(R.id.length);
        final Button create = (Button) findViewById(R.id.create);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nbP = input_nbplayer.getText().toString();
                levelID = input_level.getText().toString();
                length = input_length.getText().toString();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(nbP)
                                && !TextUtils.isEmpty(levelID)
                                && !TextUtils.isEmpty(length)) {
                            try {
                                clientSocket = new DatagramSocket();
                                ipServer = InetAddress.getByName(strserver);
                                sendData = new String(Home.idClient + " NEWGAME "+nbP+" "+levelID+" "+length).getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, ipServer, Integer.parseInt(strport));
                                clientSocket.send(sendPacket);
                                receivePacket = new DatagramPacket(receiveData, receiveData.length);

                                while (!str.contains("NEWGAME")) {
                                    clientSocket.receive(receivePacket);
                                    str = new String(receivePacket.getData(), 0, receivePacket.getLength());

                                    if (str.equals("NEWGAME BAD"))
                                        throw new ServerException("CREATING GAME FAIL");

                                    else {
                                        Intent ingame = new Intent(NewGame.this, InGame.class);
                                        ingame.putExtra("input_username", strusername);
                                        ingame.putExtra("server", strserver);
                                        ingame.putExtra("port", strport);
                                        startActivity(ingame);
                                    }

                                    receivePacket.setLength(receiveData.length);
                                }
                            } catch (SocketException e) {
                                e.printStackTrace();
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ServerException e) {
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
