package view;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import view.interfaces.ServerProtocol;

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
		message += "\n";
		DatagramPacket p = new DatagramPacket(message.getBytes(), port, clientAdress, port);
		try {
			sock.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void SsendConnectOK() {
		send("CONNECTOK");
	}

	@Override
	public synchronized void SsendConnectBAD() {
		send("CONNECTBAD");
	}

	@Override
	public synchronized void SsendJoinGameOK(int levelid) {
		send("JOINGAMEOK " + levelid);
	}

	@Override
	public synchronized void SsendJoinGameBAD() {
		send("JOINGAMEBAD");
	}

	@Override
	public synchronized void SsendStartGame(int nbSec) {
		send("STARTGAME " + nbSec);
	}

	@Override
	public synchronized void SsendGameEnd(byte idClientWinner) {
		send("GAMEEND " + idClientWinner);
	}

}
