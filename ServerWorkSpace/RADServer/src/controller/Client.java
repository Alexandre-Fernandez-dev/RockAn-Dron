package controller;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import model.game.Player;

public class Client {
	private InetAddress clientAdress;
	private Player p;
	
	public Client(InetAddress clientAdress, Player p) {
		super();
		this.clientAdress = clientAdress;
		this.p = p;
	}

	public Player getPlayer() {
		return p;
	}
	
}
