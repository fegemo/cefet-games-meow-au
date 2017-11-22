package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer.Task;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.audio.Music;

public class RainingCats extends MiniGame {

    private Sprite player;
    private Sprite backGround;
    private Sprite arrow;
    private Array<Sprite> cats;
    private int spawnedCats, totalCats;
    private int helpedCats;
    private float spawnInterval;
    private float speed;
    private boolean ground;
    private boolean jump;
    private float gravity;

    private MyMusic music;
    private MySound pop;

    private Texture playerTexture;
    private Texture arrowTexture;
    private Texture sakamoto, sakamoto2;
    private Texture bgTexture;

    public RainingCats(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 50f,/*Tempo maximo da fase*/
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        cats = new Array<Sprite>();
        spawnedCats = 0;
        helpedCats = 0;
        ground = true;
        jump = false;

        bgTexture = assets.get("raining-cats/sakamoto2.jpg", Texture.class);
        playerTexture = assets.get("raining-cats/player.png", Texture.class);
        sakamoto = assets.get("raining-cats/sakamoto.png", Texture.class);
        sakamoto2 = assets.get("raining-cats/sakamoto1.png", Texture.class);
        arrowTexture = assets.get("raining-cats/arrow.png", Texture.class);

        music =  new MyMusic(assets.get("raining-cats/music.mp3", Music.class));
        pop = new MySound(assets.get("raining-cats/Pop.mp3", Sound.class));

        arrow = new Sprite(arrowTexture);
        arrow.setScale(0.08f);
        arrow.setOrigin(0, 0);
        arrow.setPosition(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2);

        backGround = new Sprite(bgTexture);
        backGround.setOrigin(0, 0);
        backGround.setScale(viewport.getWorldWidth() / backGround.getWidth(), viewport.getWorldHeight() / backGround.getHeight());
        backGround.setPosition(0, 0);

        player = new Sprite(playerTexture);
        player.setScale(0.4f);
        player.setOrigin(0, 0);
        player.setPosition(viewport.getWorldWidth() * 0.2f, viewport.getWorldHeight() * .1f);
        music.play();
        music.setVolume(.4f);
        scheduleCatsSpawn();

    }

    private void scheduleCatsSpawn() {
        Task t = new Task() {
            @Override
            public void run() {
                spawnCats();
                if (++spawnedCats < totalCats) {
                    scheduleCatsSpawn();
                }
            }
        };
        float nextSpawnMillis = this.spawnInterval
                * (rand.nextFloat() / 3 + 0.15f);
        timer.scheduleTask(t, nextSpawnMillis);

    }

    private void spawnCats() {
        Sprite cat;
        if (rand.nextInt() % 3 == 0) {
            cat = new Sprite(sakamoto);
            cat.setScale(0.3f);
        } else {
            cat = new Sprite(sakamoto2);
            cat.setScale(0.3f);
        }
        cat.setCenter(0, 0);
        cat.setPosition((viewport.getWorldWidth() * (0.05f + rand.nextFloat() * 0.55f)),
                viewport.getWorldHeight() - viewport.getWorldHeight() * 0.1f);
        cats.add(cat);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

        this.speed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 2.5f, 7);
        this.spawnInterval = DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 2f);
        this.totalCats = (int) Math.ceil(maxDuration / spawnInterval) - 3;

    }

    @Override
    public void onHandlePlayingInput() {
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        arrow.setPosition(click.x - arrow.getWidth() / 2 * arrow.getScaleX(), click.y - arrow.getHeight() / 2 * arrow.getScaleY());

        if ((player.getX() - player.getWidth() / 2 * player.getScaleX())
                < click.x - arrow.getWidth() / 2 * arrow.getScaleX()) {
            player.setX(player.getX() + (click.x - arrow.getWidth() / 2 * arrow.getScaleX() - player.getX()) / 6);
            if (player.getX() + player.getWidth() * player.getScaleX() >= viewport.getWorldWidth()) {
                player.setX(viewport.getWorldWidth() - player.getWidth() * player.getScaleX());
            }
        } else if (player.getX() + player.getWidth() / 2 * player.getScaleX()
                > click.x - arrow.getWidth() / 2 * arrow.getScaleX()) {
            player.setX(player.getX() - (player.getX() - click.x - arrow.getWidth() / 2 * arrow.getScaleX()) / 6);

            if (player.getX() <= 0) {
                player.setX(0);
            }
        }

        if (Gdx.input.justTouched()) {
            if (ground) {
                jump = true;
                gravity = 20;
            }
        }

    }

    @Override
    public void onUpdate(float dt) {
        for (int i = 0; i < cats.size; i++) {
            Sprite sprite = cats.get(i);
            if (sprite.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
                pop.play();
                this.cats.removeValue(sprite, true);
                this.helpedCats++;
                if (this.helpedCats >= this.totalCats) {
                    music.stop();
                    super.challengeSolved();
                }
            }
        }

        if (jump) {
            ground = false;
            gravity -= 1.7;
            player.setY(player.getY() + gravity);
            if (player.getY() < viewport.getWorldHeight() * .1f) {
                player.setY(viewport.getWorldHeight() * .1f);
                jump = false;
                ground = true;
            }
        }

        for (int i = 0; i < cats.size; i++) {
            cats.get(i).setPosition(cats.get(i).getX(), cats.get(i).getY() - speed);
            cats.get(i).rotate(7 * speed / 3);
            if (cats.get(i).getY() <= -100) {
                music.stop();
                super.challengeFailed();
            }
        }
    }

    @Override
    public String getInstructions() {
        return "Salve os gatos";
    }

    @Override
    public void onDrawGame() {

        backGround.draw(batch);
        player.draw(batch);

        for (int i = 0; i < cats.size; i++) {
            Sprite sprite = cats.get(i);
            sprite.draw(batch);
        }
        arrow.draw(batch);

    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

}
