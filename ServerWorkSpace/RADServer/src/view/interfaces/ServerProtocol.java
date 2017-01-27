package view.interfaces;

import java.util.Collection;

public interface ServerProtocol {
	//Connection :
	public default void CsendConnect(String pseudo){}
	public default void SsendConnectOK(){}
	public default void SsendConnectBAD(){}
	
	//Initialisation
	public default void CsendNewGame(byte nbJoueur, int levelID, long levelLength){}
	public default void CsendJoinGame(){}
	public default void SsendJoinGameOK(int levelid){}
	public default void SsendJoinGameBAD(){}
	public default void SsendStartGame(int nbSec){}
	public default void CsendStartGameOK(){}
	
	//Jeu
	public default void CsendScoreTick(byte score){}
	
	//Fin
	public default void SsendGameEnd(byte idClientWinner){}
	
	//Déconnection
	public default void CsendDisconnect(){}
}
