package controller;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import model.game.Player;

public class Client {
	private InetAddress clientAdress;
	private ArrayList<String> messages;
	private Player p;
	
	public Client(InetAddress clientAdress, Player p) {
		super();
		this.clientAdress = clientAdress;
		this.messages = new ArrayList<String>();
		this.p = p;
	}

	public Player getPlayer() {
		return p;
	}

	public void receiveMessage(String message) {
		this.messages.add(0, message);
	}

	public ArrayList<String> getMessages() {
		return messages;
	}
	
}
