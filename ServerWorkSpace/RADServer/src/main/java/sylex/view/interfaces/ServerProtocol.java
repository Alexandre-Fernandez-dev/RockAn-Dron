package sylex.view.interfaces;

import java.util.List;

public interface ServerProtocol {
    //Connection :
    public default void CsendConnect(String pseudo){}
    public default void SsendConnectOK(int idClient){}
    public default void CsendReadyReceive(){}
    public default void SsendConnectBAD(){}

    //Gestion des parties
    /*public default void SsendGameList(List<String> gameNames){}
      public default void SsendGameUserList(String game, List<String> userNames){}
      public default void CaskGameList(){}
      public default void CaskGameUserList(String game){}*/

    //Initialisation
    /*public default void CsendNewGame(String gameName, byte nbJoueur, int levelID, long levelLength){}
      public default void SsendNewGameOK(){}
      public default void SsendNewGameBAD(){}
      public default void CsendJoinGame(String gameName){}
      public default void SsendJoinGameOK(int levelid){}
      public default void SsendJoinGameBAD(){}
      public default void CsendLeaveGame(){}*/

    //Gestion d'une seule partie
    public default void CaskUserList(){}
    public default void SsendUserList(List<String> userNames){}

    public default void SsendStartGame(int nbSec){}
    public default void CsendStartGameOK(){}

    //Jeu
    public default void CsendScoreTick(byte score){}

    //Fin
    public default void SsendGameEnd(String winnerUsername){}

    //D�connection
    public default void CsendDisconnect(){}
}
