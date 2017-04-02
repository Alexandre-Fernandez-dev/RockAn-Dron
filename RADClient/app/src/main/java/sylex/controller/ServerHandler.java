package sylex.controller;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sylex.model.ClientModel;
import sylex.view.ClientInput;
import sylex.view.ClientOutput;
import sylex.view.interfaces.ClientProtocol;

public class ServerHandler extends Thread implements ClientProtocol {

	private ClientInput ci;
	private ClientOutput co;

	private boolean stop = false;
	private ArrayList<String> messages;
	private Object handlerLock = new Object();
	private int id = -1;


	public ServerHandler(ClientInput ci, ClientOutput co) {
		super();
		//this.logger = logger;
		this.ci = ci;
		this.co = co;
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
			ci.parseMessage(message/*tmessages[0]*/);
		}
	}

	public ClientInput getCi() {
		return ci;
	}

	public ClientOutput getCo() {
		return co;
	}

	@Override
	public synchronized void SsendConnectBAD() {
		ClientModel.showBadConnect();
	}

	@Override
	public synchronized void SsendConnectOK(int idClient) {
		ClientModel.registerConnection(idClient);
		co.setId(idClient);
		co.CaskGameList();
	}

	@Override
	public synchronized void SsendGameEnd(String winnerName) {
		ClientModel.gameEnd(winnerName);
	}

	@Override
	public synchronized void SsendGameUserList(/*String game, */List<String> userList) {
		ClientModel.updateGameUserList(/*game, */userList);
	}

	@Override
	public synchronized void SsendJoinGameBAD() {
		ClientModel.showBadJoin();
	}

	@Override
	public synchronized void SsendJoinGameOK(int idLevel) {
		ClientModel.registerJoin(idLevel);
	}

	@Override
	public synchronized void SsendNewGameBAD() {
		ClientModel.showBadNew();
	}

	@Override
	public synchronized void SsendNewGameOK() {
		ClientModel.registerNew();
	}

	@Override
	public synchronized void SsendStartGame(int nbSec) {
		ClientModel.startGame(nbSec);
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


	//ServerHandler -> OUTPUT
	@Override
	public void CsendConnect(String pseudo) {
		co.CsendConnect(pseudo);
	}

	@Override
	public void CaskGameList() {
		co.CaskGameList();
	}

	@Override
	public void CaskGameUserList(String game) {
		co.CaskGameUserList(game);
	}

	@Override
	public void CsendNewGame(String gameName, byte nbJoueur, int levelID, long levelLength) {
		co.CsendNewGame(gameName, nbJoueur, levelID, levelLength);
	}

	@Override
	public void CsendJoinGame(String gameName) {
		co.CsendJoinGame(gameName);
	}

	@Override
	public void CsendLeaveGame(String gameName) {
		co.CsendLeaveGame(gameName);
	}

	@Override
	public void CsendStartGameOK() {
		co.CsendStartGameOK();
	}

	@Override
	public void CsendScoreTick(byte score) {
		co.CsendScoreTick(score);
	}

	@Override
	public void CsendDisconnect() {
		co.CsendDisconnect();
	}
}
