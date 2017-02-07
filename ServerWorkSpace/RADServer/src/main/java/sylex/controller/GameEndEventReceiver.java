package sylex.controller;

import sylex.view.interfaces.ServerProtocol;
import sylex.model.ServerModel;
import sylex.model.game.Game;

public class GameEndEventReceiver extends Thread {
	
	Game game;
	private ServerModel model;
	
	public GameEndEventReceiver(ServerModel model, Game game) {
		this.model = model;
		this.game = game;
	}
	
	@Override
	public void run() {
		synchronized(game.gameLock) {
			try {
				game.gameLock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.calculateWinner();
			ServerModel.notifyGameEnd();
		}
	}

}
