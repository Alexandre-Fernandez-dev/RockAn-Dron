package model.game;

import java.util.ArrayList;
import java.util.Date;

public class Game {
	private long timeStarted;
	private long levelLenght;
	private ArrayList<Player> players;
	private int nbPlayer;
	
	public Game(long levelLenght, long nbPlayer) {
		this.levelLenght = levelLenght;
	}
	
	public boolean addPlayer(Player p) {
		if(players.size()<nbPlayer) {
			players.add(p);
			return true;
		}
		return false;
	}
	
	public void startGame() {
		this.timeStarted = System.currentTimeMillis();
	}

}
