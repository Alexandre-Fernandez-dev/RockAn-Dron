package sylex.view;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import sylex.view.interfaces.ClientProtocol;

public class ClientOutput extends Thread implements ClientProtocol {
	DatagramSocket sock;
	InetAddress clientAdress;
	int port;
	private int id = -1;
	ArrayList<String> messages = new ArrayList<String>();
	Object handlerLock = new Object();
	boolean stop = false;
	
	public ClientOutput(DatagramSocket sock, InetAddress clientAdress, int port) {
		this.sock = sock;
		this.clientAdress = clientAdress;
		this.port = port;
		this.start();
	}

	public void sendMessage(String message) {
		this.messages.add(0, message);
		synchronized(handlerLock) {
			handlerLock.notify();
		}
	}

	public ArrayList<String> getMessages() {
		return messages;
	}

	@Override
	public void run() {
		while(!stop) {
			if(this.messages.size() == 0) {
				synchronized(handlerLock) {
					try {
						handlerLock.wait();
						if(stop == true) continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			send(messages.remove(0));
		}
	}

	public void send(String message) {
		//message += "\n";
		DatagramPacket p = new DatagramPacket(message.getBytes(), message.getBytes().length, clientAdress, port);
		try {
			sock.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//METHODES PROTOCOLES UTILES (CLIENT ENVOIE)
	@Override
	public synchronized void CaskGameList() {
		sendMessage(id + " AGLIST");
	}
	
	@Override
	public synchronized void CaskGameUserList(String gameName) {
		sendMessage(id + " AGULIST " + gameName);
	}
	
	@Override
	public synchronized void CsendConnect(String userName) {
		sendMessage("CONNECT " + userName);
	}

	@Override
	public synchronized void CsendReadyReceive() {
		Log.e("E", "READYRECEIVE");
		sendMessage(id + " READYRECEIVE");
	}

	@Override
	public synchronized void CsendDisconnect() {
		sendMessage(id + " DISCONNECT");
	}
	
	@Override
	public synchronized void CsendJoinGame(String gameName) {
		sendMessage(id + " JOINGAME " + gameName);
	}

	@Override
	public synchronized void CsendLeaveGame(String gameName) {
		sendMessage(id + " LEAVEGAME " + gameName);
	}

	@Override
	public synchronized void CsendNewGame(String gameName, byte nbJoueur, int levelID, long levelLength) {
		sendMessage(id + " NEWGAME " + gameName + " " + nbJoueur + " " + levelID + " " + levelLength);
	}

	@Override
	public synchronized void CsendScoreTick(byte score) {
		sendMessage(id + " SCORETICK " + score);
	}

	@Override
	public synchronized void CsendStartGameOK() {
		sendMessage(id + " STARTGAMEOK");
	}



	public void setId(int idClient) {
		this.id = idClient;
	}



	//METHODES PROTOCOLE INUTILES(SERVEUR ENVOIE)
	@Override
	public void SsendConnectOK(int idClient) {

	}

	@Override
	public void SsendConnectBAD() {

	}


	@Override
	public void SsendGameUserList(/*String game, */List<String> userNames) {

	}


	@Override
	public void SsendJoinGameOK(int levelid) {

	}

	@Override
	public void SsendJoinGameBAD() {

	}

	@Override
	public void SsendStartGame(int nbSec) {

	}

	@Override
	public void SsendNewGameOK() {

	}

	@Override
	public void SsendNewGameBAD() {

	}

	@Override
	public void SsendGameEnd(String winnerUsername) {

	}


}
