package view;

import java.io.IOException;
import java.net.DatagramPacket;

import view.interfaces.ServerProtocol;

public class ServerInput {
	ServerProtocol handler;
	boolean stop = false;
	
	public ServerInput() {
		super();
	}
	
	public void init(ServerProtocol handler) {
		this.handler = handler;
	}
	
	public boolean parseMessage(String message) {
		String words[] = message.split(" ");
		switch(words[0]) {
			case "CONNECT" :
				if(words.length == 2)
					handler.CsendConnect(words[1]);
				break;
			case "NEWGAME" :
				if(words.length == 4) {
					System.out.println(words[3].length());
					handler.CsendNewGame(Byte.parseByte(words[1]), Integer.parseInt(words[2]), Long.parseLong(words[3]));
				}
				break;
			case "JOINGAME" :
				if(words.length == 1)
					handler.CsendJoinGame();
				break;
			case "STARTGAMEOK" :
				if(words.length == 1)
					handler.CsendStartGameOK();
				break;
			case "SCORETICK" :
				if(words.length == 2)
					handler.CsendScoreTick(Byte.parseByte(words[1]));
				break;
			case "DISCONNECT" :
				if(words.length == 1)
					handler.CsendDisconnect();
				break;
		}
		return false;
	}
	
}
