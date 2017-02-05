package model;

import java.util.HashMap;

import controller.Client;
import controller.ClientHandler;
import model.game.Game;
import model.interfaces.ServerEvents;

public class ServerModel {
	static private Game game;
	//public static HashMap<InetAddress, HandleClient> clientHandlers;
	public static HashMap<Integer, ClientHandler> clientHandlers;
	public static HashMap<String, Client> clients;
	
	public static HashMap<String, GameModel> games;
	
	static int idClients = 0;
	
	public ServerModel() {
		//clientHandlers = new HashMap<InetAddress, HandleClient>();
		clientHandlers = new HashMap<Integer, ClientHandler>();
		clients = new HashMap<String, Client>();
		games = new HashMap<String, GameModel>();
	}
	
	public static synchronized boolean createGame(String gameName, byte nbJoueur, int levelID, long levelLength) {
		if(!games.containsKey(gameName)) {
			GameModel gm = new GameModel(new Game(nbJoueur, levelID, levelLength), gameName);
			games.put(gameName, gm);
			notifyGameListChanged();
			System.out.println("CREATE GAME");
			return true;
		} else {
			System.out.println("CREATE GAME BAD");
			return false;
		}
	}
	
	public static synchronized boolean joinGame(String gameName, ClientHandler handleClient) {
		GameModel g = games.get(gameName);
		if(g == null) {
			return false;
		}
		boolean result = g.joinGame(handleClient);
		System.out.println(handleClient.getClient().getPlayer().getPseudo() + " JOINED GAME");
		return result;
	}
	
	public static synchronized boolean leaveGame(String gameName, ClientHandler handleClient) {
		GameModel gm = games.get(gameName);
		if(gm == null) {
			return false;
		}
		boolean result = gm.leaveGame(handleClient);
		if(gm.getGame().isEmpty()) {
			games.remove(gameName);
			notifyGameListChanged();
		}
		System.out.println(handleClient.getClient().getPlayer().getPseudo() + " LEAVED GAME");
		return result;
	}
	
	public static synchronized void receiveStartGameOK() {
		game.incReady();
		System.out.println("RECEIVED START GAME OK");
		if(game.ready()) {
			game.start();
			System.out.println("START GAME");
		}
	}

	public static synchronized void registerClient(Client client, ClientHandler handleClient) {
		handleClient.setId(idClients);
		clientHandlers.put(idClients++, handleClient);
		clients.put(client.getPlayer().getPseudo(), client);
		System.out.println(client.getPlayer().getPseudo());
		System.out.println(" REGISTERED");
	}
	
	public static synchronized void unregisterClient(Client client, ClientHandler handleClient) {
		//clientHandlers.remove(handleClient.getAddress(), handleClient);
		clientHandlers.remove(handleClient.getIdentity(), handleClient);
		clients.remove(client.getPlayer().getPseudo(), client);
		System.out.println(client.getPlayer().getPseudo());
		System.out.println(" UNREGISTERED");
	}
	
	public static void addScore(Client client, byte score) {//probablement pas besoin de synchroniser
		client.getPlayer().addScore(score);
		System.out.println("SCORE OF " + client.getPlayer().getPseudo());
		System.out.println(" += " + score);
	}
	
	public static synchronized void notifyGameEnd() {
		//synchronized(ServerCore.serverLock) {
			clientHandlers.values().forEach(ServerEvents::gameEnd);
			System.out.println("GAME END");
			//ServerCore.serverLock.notify();
		//}
	}
	
	public static synchronized void notifyGameListChanged() {
			clientHandlers.values().forEach(ServerEvents::gameListChanged);
			System.out.println("GAME LIST CHANGED");
	}

	public static Game getGame() {
		return game;
	}

	public void stopServer() {
		for(ClientHandler h : clientHandlers.values()) {
			h.stopHandler();
			try {
				h.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(GameModel g : games.values()) {
			g.getGame().stopGame();
			try {
				g.getGame().join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
