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

import static java.lang.System.out;

public class Home extends AppCompatActivity {

    String strusername;
    String strserver;
    String strport;

    static int idClient = -1;

    DatagramSocket clientSocket;
    DatagramPacket sendPacket, receivePacket;
    InetAddress ipServer;
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final TextView input_username = (TextView) findViewById(R.id.username);
        final TextView input_server = (TextView) findViewById(R.id.server);
        final TextView input_port = (TextView) findViewById(R.id.port);
        final Button connect = (Button) findViewById(R.id.connect);

        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                strusername = input_username.getText().toString();
                strserver = input_server.getText().toString();
                strport = input_port.getText().toString();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(strusername)
                                && !TextUtils.isEmpty(strserver)
                                && !TextUtils.isEmpty(strport)) {
                            try {
                                clientSocket = new DatagramSocket();
                                ipServer = InetAddress.getByName(strserver);
                                sendData = new String("CONNECT "+strusername).getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, ipServer, Integer.parseInt(strport));
                                clientSocket.send(sendPacket);
                                receivePacket = new DatagramPacket(receiveData, receiveData.length);

                                while (!str.contains("CONNECTOK") && !str.contains("CONNECTBAD")) {
                                    clientSocket.receive(receivePacket);
                                    str = new String(receivePacket.getData(), 0, receivePacket.getLength());

                                    if (str.equals("CONNECTBAD"))
                                        throw new ServerException("BAD CONNECTION");

                                    else {
                                        int indexSpace = str.indexOf(" ");
                                        idClient = Integer.parseInt(str.substring(indexSpace+1, str.length()-1));
                                        Intent menu = new Intent(Home.this, Menu.class);
                                        menu.putExtra("input_username", strusername);
                                        menu.putExtra("server", strserver);
                                        menu.putExtra("port", strport);
                                        startActivity(menu);
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
