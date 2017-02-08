package sylex.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sylex.androidClient.LobbyActivity;
import sylex.androidClient.interfaces.ConnectEventReceiver;
import sylex.androidClient.interfaces.RoomEventReceiver;
import sylex.controller.ServerHandler;

public class ClientModel {
	
	public static boolean inGame = false;
	public static ArrayList<String> games = new ArrayList<String>();
	public static ArrayList<String> clientsInGame = new ArrayList<String>();
	public static String gameName = new String("");
	public static int idClient = -1;
	private static int idLevel = -1;
	private static ServerHandler handler = null;

	//events receiver
	private static ConnectEventReceiver ceo;
	//private static Object celock = new Object();
	private static RoomEventReceiver reo;
	private static Object relock = new Object();


	public static void setHandler(ServerHandler handler) {
		ClientModel.handler = handler;
	}

	//BIND ACTIVITY EVENTS
	public static void bindConnectEvent(ConnectEventReceiver ceo) {
		ClientModel.ceo = ceo;
		//synchronized (celock) {
		//	celock.notify();
		//}
	}

	public static void bindRoomEvent(RoomEventReceiver reo) {
		ClientModel.reo = reo;
		Log.d("D", "\n\ndone\n\n");
		synchronized (relock) {
			relock.notify();
		}
	}

	//ACTIONS QUI IMPLIQUENT UN ENVOI DE PAQUET :

	public static void Connect(String username) {
		handler.CsendConnect(username);
	}

	//ACTIONS APPLIQUEES A LA RECEPTION DES PAQUETS :

	public static void registerConnection(int idClient) {
		ClientModel.idClient = idClient;
		ceo.onConnectOK();
		//mettre a jour l'affichage
	}

	public static void updateGameList(List<String> gameList) {
		ClientModel.games = new ArrayList<String>(gameList);
		Log.d("D", "\n\n Received Game List : " + games.toString() + "\n\n");
		if(reo != null) {//ce message peut être reçu avant la création de son receveur
			reo.onGameListChanged();
		} else {//le message doit être stocké jusqu'a la création du receveur
			new Thread() {
				public void run() {
					synchronized (relock) {
						try {
							relock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					reo.onGameListChanged();
				}
			}.start();
		}
	}


	public static void showBadConnect() {
		//mettre a jour l'affichage
		ceo.onConnectBAD();
	}


	public static void gameEnd(String winnerName) {
		//mettre a jour l'affichage et autres
	}

	public static void updateGameUserList(String game, List<String> userList) {
		ClientModel.gameName = game;
		ClientModel.clientsInGame = new ArrayList<String>(userList);
	}


	public static void showBadJoin() {
		//mettre a jour l'affichage
	}


	public static void registerJoin(int idLevel) {
		ClientModel.idLevel = idLevel;
	}


	public static void showBadNew() {
		//mettre a jour l'affichage
	}


	public static void registerNew() {
		//Peut être rien ici
	}

	public static void sendNew(String gameName, byte playerCount, int level) {
		handler.CsendNewGame(gameName, playerCount, level, 15000);
	}


	public static void startGame(int nbSec) {
		// lancer l'écran du jeu et attendre nbSec
	}

}
