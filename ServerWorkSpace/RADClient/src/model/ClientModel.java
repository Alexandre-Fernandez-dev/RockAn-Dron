package model;

import java.util.ArrayList;
import java.util.List;

public class ClientModel {
	
	public static boolean inGame = false;
	public static ArrayList<String> games;
	public static ArrayList<String> clientsInGame;
	public static String gameName;
	public static int idClient;
	private static int idLevel;
	
	
	public static void registerConnection(int idClient) {
		ClientModel.idClient = idClient;
		//mettre a jour l'affichage
	}


	public static void showBadConnect() {
		//mettre a jour l'affichage
	}


	public static void gameEnd(String winnerName) {
		//mettre a jour l'affichage et autres
	}

	public static void updateGameList(List<String> gameList) {
		ClientModel.games = new ArrayList<String>(gameList);
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


	public static void startGame(int nbSec) {
		// lancer l'écran du jeu et attendre nbSec
	}
	
}
