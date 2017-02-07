package sylex.model.game;

import java.util.ArrayList;
import java.util.Date;

import sylex.controller.ServerCore;

public class Game extends Thread {
	private long timeStarted;
	private long levelLength;
	private ArrayList<Player> players;
	private int nbPlayer;
	private int levelID;
	private int ready = 0;
	public Object gameLock = new Object();
	private Player winner;
	
	@Override
	public void run() {
		synchronized(gameLock) {
			try {
				Thread.sleep(ServerCore.secTillGameStart*1000);
			} catch (InterruptedException e) {
				System.out.println("Stop forced");
				e.printStackTrace();
			}
			this.timeStarted = System.currentTimeMillis();
			try {
				Thread.sleep(levelLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finish();
		}
	}
	
	private void finish() {
		synchronized(gameLock) {
			gameLock.notify();
		}
		
	}
	
	public void stopGame() {
		this.interrupt();
		finish();
	}

	public Game(long levelLenght, long nbPlayer) {
		this.levelLength = levelLenght;
	}
	
	public Game(byte nbJoueur, int levelID, long levelLength) {
		players = new ArrayList<Player>();
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
	

	public boolean removePlayer(Player player) {
		players.remove(player);
		return true;
	}
	
	public boolean isFull() {
		return nbPlayer==players.size();
	}
	
	public boolean isEmpty() {
		return players.size()==0;
	}

	public int getLevelID() {
		return levelID;
	}

	public int getReady() {
		return ready;
	}

	public void incReady() {
		this.ready++;
	}

	public boolean ready() {
		return ready==players.size();
	}

	public void calculateWinner() {
		int score = 0;
		for(Player p : players) {
			if(p.score > score) {
				this.winner = p;
				score = p.score;
			}
		}
		
	}

	public Player getWinner() {
		return winner;
	}

}
