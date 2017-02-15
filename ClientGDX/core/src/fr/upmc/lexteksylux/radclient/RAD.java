package fr.upmc.lexteksylux.radclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.upmc.lexteksylux.radclient.interfaces.GameEventReceiver;
import fr.upmc.lexteksylux.radclient.interfaces.ServerGameEndEventReceiver;

/**
 * Created by Alexiram on 20/01/2017.
 */

public class RAD extends Game implements ServerGameEndEventReceiver {
    private final int nbSec;
    private final GameEventReceiver sr;
    public SpriteBatch batch;
    public BitmapFont font;
    private GameScreen gs;

    public RAD(int nbSec, GameEventReceiver sr) {
        this.nbSec = nbSec;
        this.sr = sr;
    }

    public void create() {
        //batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        gs = new GameScreen(this, nbSec, sr);
        this.setScreen(gs);
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        //batch.dispose();
        font.dispose();
    }

    @Override
    public void onGameEnd(String winner) {
        gs.onGameEnd(winner);
    }
}
