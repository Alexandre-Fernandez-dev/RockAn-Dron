package fr.upmc.lexteksylux.radclient;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import fr.upmc.lexteksylux.radclient.interfaces.GameEventReceiver;
import fr.upmc.lexteksylux.radclient.model.RADGame;
import fr.upmc.lexteksylux.radclient.model.RADGoal;

public class GameScreen implements Screen {
    private final long prestart;
    private final int nbSecTillStart;
    private final GameEventReceiver sr;
    private Sound dropSound;
    private Music rainMusic;
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Texture background;
    private Texture goalImage;
    private Texture goalImage_red;
    private RADGame gameLogic;
    private Array<Rectangle> goals;
    private BitmapFont font;
    private BitmapFont fontCenter;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private int speed = 350;
    final RAD game;

    int width = 480;
    int heigth = 800;
    private boolean started = false;
    private int score = 0;

    public GameScreen(final RAD game, int nbSec, GameEventReceiver sr) {
        this.sr = sr;
        this.nbSecTillStart = nbSec;
        this.game = game;
        font = new BitmapFont();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        fontCenter = generator.generateFont(parameter);
        //generator.dispose();

        goalImage = new Texture(Gdx.files.internal("goal.png"));
        goalImage_red = new Texture(Gdx.files.internal("goal_red.png"));
        // Load background
        background = new Texture(Gdx.files.internal("bk.png"));


        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, heigth);
        batch = new SpriteBatch();

        /**
         * Nouveau code !
         */
        this.prestart = System.currentTimeMillis();
        this.gameLogic = new RADGame();
        this.goals = new Array<Rectangle>();
        //spawnGoals();
    }

    private void spawnGoals() {
        while(gameLogic.getLevel().getGoals().size() > 0 && gameLogic.getLevel().getGoals().get(0).getAt()+ gameLogic.getStartedAT()<System.currentTimeMillis()+(1000*heigth/speed)) {
            RADGoal goal = gameLogic.getLevel().getGoals().remove(0);
            goal.x = 12 + goal.getWay() * 120;
            goal.y = 70 + speed*(gameLogic.getStartedAT()+goal.getAt() - System.currentTimeMillis())/1000; //(goal.getAt()+ gameLogic.getStartedAT() - System.currentTimeMillis()) * 800/1000;//bullshit
            goal.width = 96;
            goal.width = 64;
            System.out.println("\n\nSPAWNED !" + goal.x + " " + goal.y + "\n\n");
            goals.add(goal);
        }
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        batch.begin();
        batch.draw(background, 0, 0);
        font.draw(batch, "SCORE : " + score, 10, heigth-10);

        if(System.currentTimeMillis() - prestart < nbSecTillStart * 1000) {
            glyphLayout.setText(fontCenter, "STARTING IN " + (nbSecTillStart - (System.currentTimeMillis() - prestart)/1000));
            fontCenter.draw(batch, glyphLayout, width/2F-glyphLayout.width/2F, heigth/2F-glyphLayout.height/2F);
        } else if(started == false) {
            started = true;
            gameLogic.start();
            spawnGoals();
        }
        if(started) {
            //batch.draw(bucketImage, bucket.x, bucket.y);
            Iterator<Rectangle> it = goals.iterator();
            while (it.hasNext()) {
                RADGoal goal = (RADGoal) it.next();
                long relTime = System.currentTimeMillis() - gameLogic.getStartedAT() - ((RADGoal) goal).getAt();
                if (relTime > -80 && relTime < 20) {
                    goal.tapable = true;
                    batch.draw(goalImage_red, goal.x, goal.y);
                } else {
                    if (relTime > 200)
                        goal.tapable = false;
                    batch.draw(goalImage, goal.x, goal.y);
                }
                font.draw(batch, String.valueOf(relTime), goal.x, goal.y);//String.valueOf(((RADGoal)goal).getAt()+System.currentTimeMillis()-game.getStartedAT())
            }
        }
        if(gameLogic.isEnded()) {
            glyphLayout.setText(fontCenter, gameLogic.getWinner() + " WINS !");
            fontCenter.draw(batch, glyphLayout, width / 2F - glyphLayout.width / 2F, heigth / 2F - glyphLayout.height / 2F);
        }
        batch.end();

        if(started) {
            //TODO : considérer utiliser inputProcessor a la place http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/InputProcessor.
            // process user input (2 times because of multitouch)
            for (int i = 0; i < 2; i++) {
                if (Gdx.input.isTouched(i)) {
                    Vector3 touchPos = new Vector3();
                    touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                    camera.unproject(touchPos);
                    if (touchPos.x < width / 4) {
                        removeGoalIfTouch(goals, 0);
                    } else if (touchPos.x < width / 2) {
                        removeGoalIfTouch(goals, 1);
                    } else if (touchPos.x < 3 * width / 4) {
                        removeGoalIfTouch(goals, 2);
                    } else {
                        removeGoalIfTouch(goals, 3);
                    }
                }
            }

            // spawns the new goals which have not been displayed
            spawnGoals();

            // move the goals, remove any that are beneath the bottom edge of
            // the screen.
            Iterator<Rectangle> iter = goals.iterator();
            while (iter.hasNext()) {
                Rectangle goal = iter.next();
                goal.y -= speed * Gdx.graphics.getDeltaTime();
                if (goal.y + 64 < 0) iter.remove();
            }

            if (started && System.currentTimeMillis() > gameLogic.getLevel().getLength() + gameLogic.getStartedAT()) {
                if(gameLogic.isEnded()) {
                    if (Gdx.input.isTouched()) {
                        this.hide();
                        this.dispose();
                        sr.onGameClose();
                    }
                }
            }
        }
    }

    private void removeGoalIfTouch(Array<Rectangle> goals, int i) {
        Iterator<Rectangle> it = goals.iterator();
        while (it.hasNext()) {
            RADGoal goal = (RADGoal)it.next();
            if (goal.tapable) {
                if (((RADGoal) goal).getWay() == i) {
                    it.remove();
                    score++;
                    //gestion temporaire du scoreobserver (plus tard le mettre dans un thread séparé avec updates régulières
                    if(sr != null)
                        sr.onGameScoreUpdate((byte)1);
                }
            } else {
                break;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        font.dispose();
        fontCenter.dispose();
        background.dispose();
        goalImage.dispose();
        goalImage_red.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
    }

    public void onGameEnd(String winner) {
        this.gameLogic.endGame(winner);
    }
}