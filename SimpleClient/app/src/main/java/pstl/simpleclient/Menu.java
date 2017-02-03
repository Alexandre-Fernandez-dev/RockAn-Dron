package pstl.simpleclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Menu extends AppCompatActivity {

    String strusername;
    String strserver;
    String strport;

    DatagramSocket clientSocket;
    DatagramPacket sendPacket, receivePacket;
    InetAddress ipServer;
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent i = getIntent();
        strusername = i.getStringExtra("username");
        strserver = i.getStringExtra("server");
        strport = i.getStringExtra("port");

        final Button newgame = (Button) findViewById(R.id.newgame);
        final Button joingame = (Button) findViewById(R.id.joingame);

        newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creategame = new Intent(Menu.this, NewGame.class);
                creategame.putExtra("username", strusername);
                creategame.putExtra("server", strserver);
                creategame.putExtra("port", strport);
                startActivity(creategame);
            }
        });

        joingame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            clientSocket = new DatagramSocket();
                            ipServer = InetAddress.getByName(strserver);
                            sendData = new String(Home.idClient + " JOINGAME").getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, ipServer, Integer.parseInt(strport));
                            clientSocket.send(sendPacket);
                            receivePacket = new DatagramPacket(receiveData, receiveData.length);

                            while (!str.contains("JOINGAME")) {
                                clientSocket.receive(receivePacket);
                                str = new String(receivePacket.getData(), 0, receivePacket.getLength());

                                if (str.equals("JOINGAME BAD"))
                                    throw new GameException("Full party");

                                else {
                                    /* Parsing: JOINGAME OK X */
                                    int level = Integer.parseInt(str.substring(str.indexOf(' ')+1));

                                    receivePacket.setLength(receiveData.length);

                                    /* Waiting for STARTGAME message */

                                    clientSocket.receive(receivePacket);
                                    str = new String(receivePacket.getData(), 0, receivePacket.getLength());

                                    /* Parsing: STARTGAME X */
                                    int delay = Integer.parseInt(str.substring(str.indexOf(' ')+1));
                                    Thread.sleep(delay*1000);

                                    sendData = new String(Home.idClient + " STARTGAMEOK").getBytes();
                                    sendPacket = new DatagramPacket(sendData, sendData.length, ipServer, Integer.parseInt(strport));
                                    clientSocket.send(sendPacket);

                                    /* GAME STARTS */
                                    Intent ingame = new Intent(Menu.this, InGame.class);
                                    ingame.putExtra("username", strusername);
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
                        } catch (GameException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }
}
