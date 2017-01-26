package view.interfaces;

import java.util.Collection;

public interface ServerProtocol {
	//Connection :
	default void CsendConnect(String pseudo){}
	default void SsendConnectOK(byte idClient){}
	default void SsendConnectBAD(){}
	
	//Initialisation
	default void CsendNewGame(byte nbJoueur, int levelID, long levelLenght){}
	default void CsendJoinGame(){}
	default void SsendJoinGameOK(int levelid){}
	default void SsendJoinGameBAD(){}
	default void SsendStartGame(int nbSec){}
	default void CsendStartGameOK(){}
	
	//Jeu
	default void CsendScoreTick(byte score){}
	
	//Fin
	default void SsendGameEnd(byte idClientWinner){}
	
	//Déconnection
	default void CsendDisconnect(){}
}
