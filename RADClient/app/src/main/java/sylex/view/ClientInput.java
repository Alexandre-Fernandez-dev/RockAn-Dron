package sylex.view;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sylex.view.interfaces.ClientProtocol;

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
				Log.d("D", "Received : \"" + words[0] + "\"");
				if(words.length == 2) {
					Log.d("D", "Received : \"" + words[1] + "\"");
					handler.SsendConnectOK(Integer.parseInt(words[1]));
				}
				break;
			case "CONNECTBAD" :
				if(words.length == 1)
					handler.SsendConnectBAD();
				break;
            /*
			case "GAMELIST" :
				wordList = new ArrayList(Arrays.asList(words));
				wordList.remove(0);
				handler.SsendGameList(wordList);
				break;
			case "GAMEULIST" :
				if(words.length >= 2) {
					wordList = new ArrayList(Arrays.asList(words));
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
            */
            case "ULIST" :
                if (words.length > 1) {
                    ArrayList<String> users = new ArrayList<>();
                    for (int i = 1; i < words.length; i++) {
                        users.add(words[i]);
                    }
                    handler.SsendGameUserList(users);
                }
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
