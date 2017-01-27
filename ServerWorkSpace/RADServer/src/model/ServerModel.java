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
	
	public static boolean isGameCreated() {
		return game==null;
	}
	
	public static synchronized void createGame(byte nbJoueur, int levelID, long levelLength) {
		game = new Game(nbJoueur, levelID, levelLength);
	}

	public static synchronized boolean joinGame(Client client) {
		boolean result = game.addPlayer(client.getPlayer());
		if(result && game.isFull())
			notifyGameFull();
		return result;
	}

	public static synchronized void registerClient(Client client, HandleClient handleClient) {
		clientHandlers.put(handleClient.getAddress(), handleClient);
		clients.put(client.getPlayer().getPseudo(), client);
	}
	
	public static synchronized void unregisterClient(Client client, HandleClient handleClient) {
		clientHandlers.remove(handleClient.getAddress(), handleClient);
		clients.remove(client.getPlayer().getPseudo(), client);
	}
	
	public static void addScore(Client client, byte score) {//probablement pas besoin de synchroniser
		client.getPlayer().addScore(score);
	}

	public static void deleteGame() {
		ServerModel.game = null;
	}
	
	public static synchronized void notifyGameFull() {
		clientHandlers.values().forEach(ServerEvents::gameFull);
	}

	public static Game getGame() {
		return game;
	}

}
