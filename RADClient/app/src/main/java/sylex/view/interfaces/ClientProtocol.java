package sylex.view.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ClientProtocol {
	//Connection :
	public void CsendConnect(String pseudo);
	public void SsendConnectOK(int idClient);
	public void SsendConnectBAD();
	
	//Gestion des parties
//	public void SsendGameList(List<String> wordList);
	public void SsendGameUserList(/*String game, */List<String> userNames);
	public void CaskGameList();
	public void CaskGameUserList(String game);
	
	//Initialisation
	public void CsendNewGame(String gameName, byte nbJoueur, int levelID, long levelLength);
	public void SsendNewGameOK();
	public void SsendNewGameBAD();
	public void CsendJoinGame(String gameName);
	public void SsendJoinGameOK(int levelid);
	public void SsendJoinGameBAD();
	public void CsendLeaveGame(String gameName);

	public void SsendStartGame(int nbSec);
	public void CsendStartGameOK();
	
	//Jeu
	public void CsendScoreTick(byte score);
	
	//Fin
	public void SsendGameEnd(String winnerUsername);
	
	//D�connection
	public void CsendDisconnect();
}
