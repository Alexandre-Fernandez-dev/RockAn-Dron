package fr.upmc.lexteksylux.radclient.interfaces;

/**
 * Created by Alexiram on 14/02/2017.
 */

public interface GameEventReceiver {

    public void onGameScoreUpdate(byte amount);

    public void onGameClose();

}
