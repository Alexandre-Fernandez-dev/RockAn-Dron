package fr.upmc.lexteksylux.radclient.model;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Alexiram on 20/01/2017.
 */

public class RADLevel {

    private final LinkedList<RADGoal> goals = new LinkedList<RADGoal>();
    private final long length;
    private final String filename;

    public RADLevel(String s) {
        this.filename = s;
        this.length = 15000;
    }

    public void load() {//chargement d'un niveau
        goals.add(new RADGoal(1, 3000));
        goals.add(new RADGoal(2, 3000));
        goals.add(new RADGoal(3, 3200));
        goals.add(new RADGoal(1, 4000));
        goals.add(new RADGoal(2, 4000));
        goals.add(new RADGoal(1, 5000));
        goals.add(new RADGoal(2, 5600));
        goals.add(new RADGoal(0, 6000));
        goals.add(new RADGoal(2, 6200));
        goals.add(new RADGoal(1, 7000));
        goals.add(new RADGoal(2, 7000));
        goals.add(new RADGoal(3, 8200));
        goals.add(new RADGoal(1, 8000));
        goals.add(new RADGoal(2, 9000));
        goals.add(new RADGoal(1, 10000));
        goals.add(new RADGoal(2, 10600));
        goals.add(new RADGoal(0, 11000));
        goals.add(new RADGoal(2, 12200));

    }

    public LinkedList<RADGoal> getGoals() {
        return goals;
    }

    public long getLength() {
        return length;
    }

}
