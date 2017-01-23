package fr.upmc.lexteksylux.radclient.model;

/**
 * Created by Alexiram on 20/01/2017.
 */

public class RADGame {

    private RADLevel level;
    private long startedAT;

    public RADGame() {
        level = new RADLevel("level1.txt");
        level.load();
    }

    public void start() {
        startedAT = System.currentTimeMillis();
    }

    public RADLevel getLevel() {
        return level;
    }

    public long getStartedAT() {
        return startedAT;
    }
}
