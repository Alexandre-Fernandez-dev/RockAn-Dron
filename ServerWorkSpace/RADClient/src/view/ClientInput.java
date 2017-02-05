package view;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import view.interfaces.ClientProtocol;

public class ClientInput {
	ClientProtocol handler;
	boolean stop = false;
	
	public ClientInput() {
		super();
	}
	
	public void init(ClientProtocol handler) {
		this.handler = handler;
	}
	
	public boolean parseMessage(String message) {
		List<String> wordList;
		String words[] = message.split(" ");
		switch(words[0]) {
			case "CONNECTOK" :
				if(words.length == 2)
					handler.SsendConnectOK(Integer.parseInt(words[1]));
				break;
			case "CONNECTBAD" :
				if(words.length == 1)
					handler.SsendConnectBAD();
				break;
			case "GAMELIST" :
				wordList = Arrays.asList(words);
				wordList.remove(0);
				handler.SsendGameList(wordList);
				break;
			case "GAMEULIST" :
				if(words.length >= 2) {
					wordList = Arrays.asList(words);
					wordList.remove(0);
					String gameName = wordList.remove(0);
					handler.SsendGameUserList(gameName, wordList);
				}
				break;
			case "NEWGAMEOK" :
				if(words.length == 1)
					handler.SsendNewGameOK();
				break;
			case "NEWGAMEBAD" :
				if(words.length == 1)
					handler.SsendNewGameBAD();
				break;
			case "JOINGAMEOK" :
				if(words.length == 2)
					handler.SsendJoinGameOK(Integer.parseInt(words[1]));
				break;
			case "JOINGAMEBAD" :
				if(words.length == 1)
					handler.SsendJoinGameBAD();
				break;
			case "STARTGAME" :
				if(words.length == 2)
					handler.SsendStartGame(Integer.parseInt(words[1]));
				break;
			case "GAMEEND" :
				if(words.length == 2)
					handler.SsendGameEnd(words[1]);
				break;
		}
		return false;
	}
	
}
