package view;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Set;

import view.interfaces.ClientProtocol;

public class ClientOutput implements ClientProtocol {
	DatagramSocket sock;
	InetAddress clientAdress;
	int port;
	private int id;
	
	public ClientOutput(DatagramSocket sock, InetAddress clientAdress, int port) {
		this.sock = sock;
		this.clientAdress = clientAdress;
		this.port = port;
	}

	public void send(String message) {
		message += "\n";
		DatagramPacket p = new DatagramPacket(message.getBytes(), message.getBytes().length, clientAdress, port);
		try {
			sock.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void CaskGameList() {
		send(id + " AGLIST");
	}
	
	@Override
	public synchronized void CaskGameUserList(String gameName) {
		send(id + " AGULIST " + gameName);
	}
	
	@Override
	public synchronized void CsendConnect(String userName) {
		send("CONNECT " + userName);
	}
	
	@Override
	public synchronized void CsendDisconnect() {
		send(id + " DISCONNECT");
	}
	
	@Override
	public synchronized void CsendJoinGame(String gameName) {
		send(id + " JOINGAME " + gameName);
	}

	@Override
	public synchronized void CsendLeaveGame(String gameName) {
		send(id + " LEAVEGAME " + gameName);
	}
	
	@Override
	public synchronized void CsendNewGame(String gameName, byte nbJoueur, int levelID, long levelLength) {
		send(id + " NEWGAME " + gameName + " " + nbJoueur + " " + levelID + " " + levelLength);
	}
	
	@Override
	public synchronized void CsendScoreTick(byte score) {
		send(id + " SCORETICK " + score);
	}
	
	@Override
	public synchronized void CsendStartGameOK() {
		send(id + " STARTGAMEOK");
	}

	public void setId(int idClient) {
		this.id = idClient;
	}
	
	
}
