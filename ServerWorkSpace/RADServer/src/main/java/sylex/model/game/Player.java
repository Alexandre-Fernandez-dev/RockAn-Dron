package sylex.model.game;


public class Player {
	
	int score=0;
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
