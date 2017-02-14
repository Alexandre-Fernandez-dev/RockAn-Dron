package fr.upmc.lexteksylux.radclient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.upmc.lexteksylux.radclient.interfaces.GameEventReceiver;

/**
 * Created by Alexiram on 20/01/2017.
 */

public class RAD extends Game {
    private final int nbSec;
    private final GameEventReceiver sr;
    public SpriteBatch batch;
    public BitmapFont font;

    public RAD(int nbSec, GameEventReceiver sr) {
        this.nbSec = nbSec;
        this.sr = sr;
    }

    public void create() {
        //batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        this.setScreen(new GameScreen(this, nbSec, sr));
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        //batch.dispose();
        font.dispose();
    }
}
