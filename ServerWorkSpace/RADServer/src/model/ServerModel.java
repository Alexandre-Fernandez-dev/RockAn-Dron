package model;

import java.net.InetAddress;
import java.util.HashMap;

import controller.Client;
import controller.HandleClient;
import model.game.Game;
import model.game.Player;
import model.interfaces.ServerEvents;

public class ServerModel {
	static private Game game;
	public static HashMap<InetAddress, HandleClient> clientHandlers;
	public static HashMap<String, Client> clients;
	
	public ServerModel() {
		clientHandlers = new HashMap<InetAddress, HandleClient>();
		clients = new HashMap<String, Client>();
	}
	
	public static boolean isGameCreated() {
		return game!=null;
	}
	
	public static synchronized void createGame(byte nbJoueur, int levelID, long levelLength) {
		game = new Game(nbJoueur, levelID, levelLength);
		System.out.println("CREATE GAME");
	}

	public static synchronized boolean joinGame(Client client) {
		boolean result = game.addPlayer(client.getPlayer());
		if(result && game.isFull())
			notifyGameFull();
		System.out.println(client.getPlayer().getPseudo() + " JOINED GAME");
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

	public static synchronized void registerClient(Client client, HandleClient handleClient) {
		clientHandlers.put(handleClient.getAddress(), handleClient);
		clients.put(client.getPlayer().getPseudo(), client);
		System.out.println(client.getPlayer().getPseudo());
		System.out.println(" REGISTERED");
	}
	
	public static synchronized void unregisterClient(Client client, HandleClient handleClient) {
		clientHandlers.remove(handleClient.getAddress(), handleClient);
		clients.remove(client.getPlayer().getPseudo(), client);
		System.out.println(client.getPlayer().getPseudo());
		System.out.println(" UNREGISTERED");
	}
	
	public static void addScore(Client client, byte score) {//probablement pas besoin de synchroniser
		client.getPlayer().addScore(score);
		System.out.println("SCORE OF " + client.getPlayer().getPseudo());
		System.out.println(" += " + score);
	}

	public static void deleteGame() {
		ServerModel.game = null;
		System.out.println("DELETE GAME");
	}
	
	public static synchronized void notifyGameFull() {
		clientHandlers.values().forEach(ServerEvents::gameFull);
		System.out.println("GAME FULL");
	}
	
	public static synchronized void notifyGameEnd() {
		synchronized(ServerCore.serverLock) {
			clientHandlers.values().forEach(ServerEvents::gameEnd);
			System.out.println("GAME END");
			ServerCore.serverLock.notify();
			deleteGame();
		}
	}

	public static Game getGame() {
		return game;
	}

	

}
