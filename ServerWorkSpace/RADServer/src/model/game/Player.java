package model.game;

import controller.interfaces.ClientHandler;

public class Player {
	
	int score=0;
	ClientHandler client;
	private String pseudo;
	
	public Player(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPseudo() {
		return pseudo;
	}
	
	public void addScore(byte sc) {
		this.score += sc;
	}
}
