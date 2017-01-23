package fr.upmc.lexteksylux.radclient.model;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Alexiram on 20/01/2017.
 */

public class RADGoal extends Rectangle {

    private final int way;
    private final long at;
    public boolean tapable = false;

    public RADGoal(int way, long at) {
        this.way = way;
        this.at = at;
    }

    public long getAt() {
        return at;
    }

    public int getWay() {
        return way;
    }
}
