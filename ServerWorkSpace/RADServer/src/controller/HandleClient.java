package controller;

import java.net.DatagramSocket;
import java.net.InetAddress;

import model.ServerLogger;
import model.ServerModel;
import model.game.Player;
import model.interfaces.ServerEvents;
import view.ServerInput;
import view.ServerOutput;
import view.interfaces.ServerProtocol;

public class HandleClient implements Runnable, ServerProtocol, ServerEvents {
	
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	
	int secTillGameStart = 5;
	
	private ServerInput si;
	private ServerOutput so;
	private ServerLogger logger;
	private Client client;
	private InetAddress address;
	private boolean stop = false;

	public HandleClient(InetAddress inetAddress, ServerLogger logger, ServerInput si, ServerOutput so) {
		super();
		this.logger = logger;
		this.address = inetAddress;
		this.si = si;
		this.so = so;
	}

	@Override
	public void run() {
		while(!stop) {
			if(client.getMessages().size() == 0) {
				try {
					client.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			String message = client.getMessages().get(0);
			si.parseMessage(message);
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
		if(ServerModel.clients.containsKey(pseudo) || ServerModel.clientHandlers.containsKey(address)) {
			so.SsendConnectBAD();
		} else {
			this.client = new Client(address, new Player(pseudo));
			ServerModel.registerClient(client, this);
			so.SsendConnectOK();
		}
	}
	
	@Override
	public void CsendNewGame(byte nbJoueur, int levelID, long levelLength) {
		if(!ServerModel.isGameCreated()) {
			ServerModel.createGame(nbJoueur, levelID, levelLength);
			so.SsendConnectOK();
		} else {
			so.SsendConnectBAD();
		}
	}
	
	@Override
	public void CsendJoinGame() {
		if(ServerModel.joinGame(this.client)) {
			so.SsendJoinGameOK(ServerModel.getGame().getLevelID());
		} else {
			//comme pour le moment pas de parties multiples ne plus envoyer de données a ce client.
			ServerModel.unregisterClient(client, this);
			so.SsendJoinGameBAD();
		}
	}
	
	@Override
	public void CsendScoreTick(byte score) {
		ServerModel.addScore(client, score);
	}
	
	@Override
	public void CsendDisconnect() {
		ServerModel.unregisterClient(client, this);
	}

	public InetAddress getAddress() {
		return address;
	}

	@Override
	public void gameFull() {
		so.SsendStartGame(secTillGameStart);
	}
	
	@Override
	public void gameEnd() {
		//so.sendgameend
		
	}

	public void receiveMessage(String message) {
		client.receiveMessage(message);
		client.notify();
	}

	public void stopHandler() {
		this.stop = true;
	}
}
