package sylex.view;

import sylex.view.interfaces.ServerProtocol;

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
                /*case "AGLIST" :
                  if(words.length == 1)
                  handler.CaskGameList();
                  break;
                  case "AGULIST" :
                  if(words.length == 2)
                  handler.CaskGameUserList(words[1]);
                  break;
                  case "NEWGAME" :
                  if(words.length == 5) {
                  System.out.println(words[3].length());
                  handler.CsendNewGame(words[1], Byte.parseByte(words[2]), Integer.parseInt(words[3]), Long.parseLong(words[4]));
                  }
                  break;
                  case "JOINGAME" :
                  if(words.length == 2)
                  handler.CsendJoinGame(words[1]);
                  break;
                  case "LEAVEGAME" :
                  if(words.length == 1)
                  handler.CsendLeaveGame();
                  break;*/
            case "AULIST" :
                if(words.length == 1)
                    handler.CaskUserList();
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
