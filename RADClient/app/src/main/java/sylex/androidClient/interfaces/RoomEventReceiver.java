package sylex.androidClient.interfaces;

/**
 * Created by alexiram on 07/02/17.
 */

public interface RoomEventReceiver {

    public void onGameUserListChanged();
    public void onJoinGameOk();
    public void onJoinGameBad();

}
