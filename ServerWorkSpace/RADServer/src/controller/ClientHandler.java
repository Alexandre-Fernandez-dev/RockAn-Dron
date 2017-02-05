package controller;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import model.ServerLogger;
import model.ServerModel;
import model.game.Player;
import model.interfaces.ServerEvents;
import view.ServerInput;
import view.ServerOutput;
import view.interfaces.ServerProtocol;

public class ClientHandler extends Thread implements ServerProtocol, ServerEvents {
	
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
		
	private ServerInput si;
	private ServerOutput so;
	//private ServerLogger logger;
	private Client client;
	private InetAddress address;
	private boolean stop = false;
	private ArrayList<String> messages;
	private Object handlerLock = new Object();
	private int id = -1;


	public ClientHandler(InetAddress inetAddress, ServerLogger logger, ServerInput si, ServerOutput so) {
		super();
		//this.logger = logger;
		this.address = inetAddress;
		this.si = si;
		this.so = so;
		this.messages = new ArrayList<String>();
	}
	
	public void receiveMessage(String message) {
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
			String message = messages.remove(0);
			//String[] tmessages = message.split("\n");
			System.out.println(message.length());
			message.replace('\n', '\0');
			System.out.println(message.length());
			si.parseMessage(message/*tmessages[0]*/);
		}
	}

	public ServerInput getSi() {
		return si;
	}

	public ServerOutput getSo() {
		return so;
	}
	
	@Override
	public void CsendConnect(String pseudo) {
		if(ServerModel.clients.containsKey(pseudo) /*|| ServerModel.clientHandlers.containsKey(address)*/) {
			so.SsendConnectBAD();
		} else {
			this.client = new Client(address, new Player(pseudo));
			ServerModel.registerClient(client, this);
			so.SsendConnectOK(this.id);
		}
	}
	
	@Override
	public void CsendNewGame(String gameName, byte nbJoueur, int levelID, long levelLength) {
		if(ServerModel.createGame(gameName, nbJoueur, levelID, levelLength)) {
			so.SsendNewGameOK();
		} else {
			so.SsendNewGameBAD();
		}
	}
	
	@Override
	public void CsendJoinGame(String gameName) {
		if(ServerModel.joinGame(gameName, this)) {
			so.SsendJoinGameOK(ServerModel.games.get(gameName).getGame().getLevelID());
		} else {
			//comme pour le moment pas de parties multiples ne plus envoyer de donn�es a ce client.
			//ServerModel.unregisterClient(client, this);
			so.SsendJoinGameBAD();
		}
	}
	
	@Override
	public void CsendLeaveGame(String gameName) {
		ServerModel.leaveGame(gameName, this);
	}
	
	@Override
	public void CaskGameList() {
		
	}
	
	@Override
	public void CaskGameUserList(String game) {
		
	}
	
	@Override
	public void CsendStartGameOK() {
		ServerModel.receiveStartGameOK();
	}
	
	@Override
	public void CsendScoreTick(byte score) {
		ServerModel.addScore(client, score);
	}
	
	@Override
	public void CsendDisconnect() {
		ServerModel.unregisterClient(client, this);
	}
	
	//EVENTS
	@Override
	public void gameFull() {
		so.SsendStartGame(ServerCore.secTillGameStart);//seule dépendance de servercore
	}
	
	@Override
	public void gameEnd() {
		so.SsendGameEnd(ServerModel.getGame().getWinner().getPseudo());
	}
	
	@Override
	public void gameListChanged() {
		so.SsendGameList(new ArrayList<String>(ServerModel.games.keySet()));
	}
	
	@Override
	public void gameUserListChanged(String gname) {
		so.SsendGameUserList(gname, new ArrayList<String>(ServerModel.games.get(gname).clients.keySet()));
	}
	//ENDEVENTS
	
	public InetAddress getAddress() {
		return address;
	}

	public void stopHandler() {
		this.stop = true;
		synchronized(handlerLock) {
			handlerLock.notify();
		}
	}

	public void setId(int idClient) {
		this.id  = idClient;
	}

	public int getIdentity() {
		return id;
	}

	public Client getClient() {
		return client;
	}

}
