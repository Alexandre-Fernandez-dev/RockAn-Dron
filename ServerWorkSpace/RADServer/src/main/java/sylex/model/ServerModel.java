package sylex.model;

import java.util.HashMap;

import sylex.controller.Client;
import sylex.controller.ClientHandler;
import sylex.model.game.Game;
import sylex.model.interfaces.ServerEvents;

public class ServerModel {
    //static private Game game;

    public static HashMap<Integer, ClientHandler> clientHandlers;
    public static HashMap<String, Client> clients;

    public static HashMap<String, GameModel> games;

    private static int idClients = 0;

    public ServerModel() {
        //clientHandlers = new HashMap<InetAddress, HandleClient>();
        clientHandlers = new HashMap<Integer, ClientHandler>();
        clients = new HashMap<String, Client>();
        games = new HashMap<String, GameModel>();
        initDefaultGame();
    }

    public static void initDefaultGame() {
        if(!games.isEmpty())
            games.remove("DEF"); //détruire la partie "proprement"
        createGame("DEF", (byte)4, 1, 16000);
    }

    public static synchronized boolean createGame(String gameName, byte nbJoueur, int levelID, long levelLength) {
        if(!games.containsKey(gameName)) {
            GameModel gm = new GameModel(new Game(nbJoueur, levelID, levelLength), gameName);
            games.put(gameName, gm);
            //notifyGameListChanged();
            System.out.println("CREATE GAME");
            return true;
        } else {
            System.out.println("CREATE GAME BAD");
            return false;
        }
    }

    public static synchronized boolean joinGame(String gameName, ClientHandler handleClient) {
        GameModel g = games.get(gameName);
        if(g == null) {
            return false;
        }
        boolean result = g.joinGame(handleClient);
        return result;
    }

    public static synchronized boolean leaveGame(String gameName, ClientHandler handleClient) {
        GameModel gm = games.get(gameName);
        if(gm == null) {
            return false;
        }
        boolean result = gm.leaveGame(handleClient);
        if(gm.getGame().isEmpty()) {
            //games.remove(gameName); PARTIE UNIQUE
            //notifyGameListChanged();
        }
        System.out.println(handleClient.getClient().getPlayer().getPseudo() + " LEAVED GAME");
        return result;
    }

    public static synchronized void receiveStartGameOK(String gameName) {
        GameModel g = games.get(gameName);
        g.addReady();
        System.out.println("RECEIVED START GAME OK");
    }
    
    public static synchronized int registerClient(Client client, ClientHandler handleClient) {
        int ret = idClients;
        handleClient.setId(idClients);
        clientHandlers.put(idClients++, handleClient);
        clients.put(client.getPlayer().getPseudo(), client);
        System.out.println(client.getPlayer().getPseudo());
        System.out.println(" REGISTERED");
        notifyUserListChanged();
        //partie unique
        joinGame("DEF", handleClient);
        return ret;
    }

    public static synchronized void unregisterClient(Client client, ClientHandler handleClient) {
        //clientHandlers.remove(handleClient.getAddress(), handleClient);
        clientHandlers.remove(handleClient.getIdentity(), handleClient);
        clients.remove(client.getPlayer().getPseudo(), client);
        System.out.println(client.getPlayer().getPseudo());
        System.out.println(" UNREGISTERED");
        notifyUserListChanged();
        //partie unique
        leaveGame("DEF", handleClient);
    }

    public static void addScore(String gameName, Client client, byte score) {//probablement pas besoin de synchroniser
        GameModel g = games.get(gameName);
        g.addScore(client.getPlayer(), score);
        System.out.println("SCORE OF " + client.getPlayer().getPseudo());
        System.out.println(" += " + score);
    }


    /*public static synchronized void notifyGameListChanged() {
      clientHandlers.values().forEach(ServerEvents::gameListChanged);
      System.out.println("GAME LIST CHANGED");
      }*/

    public static synchronized void notifyUserListChanged() {
        clientHandlers.values().forEach(ServerEvents::userListChanged);
        System.out.println("GAME LIST CHANGED");
    }

    public void stopServer() {
        for(ClientHandler h : clientHandlers.values()) {
            h.stopHandler();
            try {
                h.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(GameModel g : games.values()) {
            g.getGame().stopGame();
            try {
                g.getGame().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

	public static int getIdClients() {
		return idClients;
	}
}
