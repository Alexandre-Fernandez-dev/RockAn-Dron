package model.game;

import java.util.ArrayList;
import java.util.Date;

public class Game {
	private long timeStarted;
	private long levelLength;
	private ArrayList<Player> players;
	private int nbPlayer;
	private int levelID;
	
	public Game(long levelLenght, long nbPlayer) {
		this.levelLength = levelLenght;
	}
	
	public Game(byte nbJoueur, int levelID, long levelLength) {
		this.nbPlayer = nbJoueur;
		this.levelID = levelID;
		this.levelLength = levelLength;
	}

	public boolean addPlayer(Player p) {
		if(players.size()<nbPlayer) {
			players.add(p);
			return true;
		}
		return false;
	}
	
	public boolean isFull() {
		return nbPlayer==players.size();
	}
	
	public void startGame() {
		this.timeStarted = System.currentTimeMillis();
	}

	public int getLevelID() {
		return levelID;
	}

}
