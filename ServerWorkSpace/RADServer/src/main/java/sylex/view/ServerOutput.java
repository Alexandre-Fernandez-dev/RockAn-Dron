package sylex.view;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import sylex.view.interfaces.ServerProtocol;

public class ServerOutput implements ServerProtocol {
    DatagramSocket sock;
    InetAddress clientAdress;
    int port;

    public ServerOutput(DatagramSocket sock, InetAddress clientAdress, int port) {
        this.sock = sock;
        this.clientAdress = clientAdress;
        this.port = port;
    }

    public void send(String message) {
        DatagramPacket p = new DatagramPacket(message.getBytes(), message.getBytes().length, clientAdress, port);
        try {
            System.out.println("SEND " + message + " " + p.getAddress() + " " + p.getPort());
            sock.send(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void SsendConnectOK(int idClient) {
        //System.out.println("SEND CONNECT OK" + sock.getInetAddress() + " " + sock.getPort());
        send("CONNECTOK " + idClient);
    }

    @Override
    public synchronized void SsendConnectBAD() {
        send("CONNECTBAD");
    }


    /* 
       @Override
       public synchronized void SsendGameList(List<String> set) {
       StringBuilder sb = new StringBuilder("GAMELIST");
       for(String gname : set)
       sb.append(" " + gname);
       send(sb.toString());
       }

       @Override
       public synchronized void SsendGameUserList(String game, List<String> usernames) {
       StringBuilder sb = new StringBuilder("GAMEULIST");
       sb.append(" " + game);
       for(String uname : usernames)
       sb.append(" " + uname);
       send(sb.toString());
       }

       @Override
       public synchronized void SsendNewGameOK() {
       send("NEWGAMEOK");
       }

       @Override
       public synchronized void SsendNewGameBAD() {
       send("NEWGAMEBAD");
       }

       @Override
       public synchronized void SsendJoinGameOK(int levelid) {
       send("JOINGAMEOK " + levelid);
       }

       @Override
       public synchronized void SsendJoinGameBAD() {
       send("JOINGAMEBAD");
       }
    */

    @Override
    public synchronized void SsendUserList(List<String> usernames) {
        //System.out.println("SEND USER LIST");
        StringBuilder sb = new StringBuilder("ULIST");
        for(String uname : usernames)
            sb.append(" " + uname);
        send(sb.toString());
    } 

    @Override
    public synchronized void SsendStartGame(int nbSec) {
        send("STARTGAME " + nbSec);
    }

    @Override
    public synchronized void SsendGameEnd(String winnerUsername) {
        send("GAMEEND " + winnerUsername);
    }

}
