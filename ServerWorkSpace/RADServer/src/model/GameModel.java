package model;

import java.util.HashMap;

import controller.Client;
import controller.ClientHandler;
import model.game.Game;
import model.interfaces.ServerEvents;

public class GameModel {

	private Game game;
	public HashMap<Integer, ClientHandler> clientHandlers;
	public HashMap<String, Client> clients;
	private String name;

	public GameModel(Game game, String name) {
		this.game = game;
		this.name = name;
		this.clientHandlers = new HashMap<Integer, ClientHandler>();
		this.clients = new HashMap<String, Client>();
	}
	
	public synchronized boolean joinGame(ClientHandler hclient) {
		boolean result = game.addPlayer(hclient.getClient().getPlayer());
		if(result)
			registerClient(hclient.getClient(), hclient);
		if(result && game.isFull())
			notifyGameFull();
		System.out.println(hclient.getClient().getPlayer().getPseudo() + " JOINED GAME");
		return result;
	}
	
	public boolean leaveGame(ClientHandler handleClient) {
		boolean result = game.removePlayer(handleClient.getClient().getPlayer());
		if(result)
			unregisterClient(handleClient.getClient(), handleClient);
		System.out.println(handleClient.getClient().getPlayer().getPseudo() + " LEAVED GAME");
		return result;
	}
	
	public synchronized void registerClient(Client client, ClientHandler handleClient) {
		clientHandlers.put(handleClient.getIdentity(), handleClient);
		clients.put(client.getPlayer().getPseudo(), client);
		System.out.println(client.getPlayer().getPseudo());
		System.out.println(" REGISTERED IN GAME");
		notifyGameUserListChanged();
	}
	
	public synchronized void unregisterClient(Client client, ClientHandler handleClient) {
		clientHandlers.remove(handleClient.getIdentity(), handleClient);
		clients.remove(client.getPlayer().getPseudo(), client);
		System.out.println(client.getPlayer().getPseudo());
		System.out.println(" UNREGISTERED IN GAME");
		notifyGameUserListChanged();
	}
	
	public synchronized void notifyGameUserListChanged() {
		clientHandlers.values().forEach(c->c.gameUserListChanged(this.name));
		System.out.println("GAME ULIST CHANGED");
	}
	
	
	public synchronized void notifyGameFull() {
		clientHandlers.values().forEach(ServerEvents::gameFull);
		System.out.println("GAME FULL");
	}

	public Game getGame() {
		return game;
	}

}
