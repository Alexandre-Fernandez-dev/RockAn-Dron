package model;

import java.net.InetAddress;

public class Game {
	private int port;
	private InetAddress player1, player2;
	private long score1, score2;
	
	public Game(int port, InetAddress player1, InetAddress player2) {
		super();
		this.port = port;
		this.player1 = player1;
		this.player2 = player2;
	}
}
