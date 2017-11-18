package br.cefetmg.games.minigames;

import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;

public class CatchThatHomework extends MiniGame {

    private Texture catSpritesheet;
    private Cat cat;
    private Texture homeworkTexture;
    private Array<Homework> homeworks;
    private MySound backgroundMusic;
    private float speedMultiplier;
    private Texture backgroundImage;

    private final float spawnInterval = 1;

    public CatchThatHomework(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        catSpritesheet = assets.get("catch-that-homework/cat-spritesheet.png", Texture.class);
        homeworkTexture = assets.get("catch-that-homework/homework.png", Texture.class);
        backgroundMusic = new MySound(assets.get("catch-that-homework/bensound-sexy.mp3", Sound.class));
        backgroundImage = assets.get("catch-that-homework/valley.png", Texture.class);

        cat = new Cat(catSpritesheet, 200);
        cat.setCenter(
                viewport.getWorldWidth() / 2f,
                cat.height);
        cat.setScale(5);

        homeworks = new Array<Homework>();

        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnHomework();
            }

        }, 0, this.spawnInterval);

        backgroundMusic.play();
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.speedMultiplier = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 1f, 3f);
    }

    private void spawnHomework() {
        Homework homework = new Homework(homeworkTexture, new Vector2(0, 300));
        homework.setScale(3);
        homework.speed.scl(this.speedMultiplier);
        homework.setPosition(MathUtils.random(0, viewport.getWorldWidth()), viewport.getWorldHeight());
        homeworks.add(homework);

    }

    @Override
    public void onHandlePlayingInput() {
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        if (click.x > 0 && click.x < viewport.getWorldWidth()) {
            cat.setCenter(click.x, cat.height);
        }
    }

    @Override
    public void onUpdate(float dt) {
        for (Homework homework : homeworks) {
            homework.update(dt);
        }

        cat.update(dt);

        for (Homework homework : homeworks) {
            // Colisão com o gato
            if (homework.getBoundingRectangle()
                    .overlaps(cat.getBoundingRectangle())) {
                homeworks.removeValue(homework, true);
                // Colisão com o chão
            } else if (homework.getY() < 0) {
                super.challengeFailed();
            }
        }
    }

    @Override
    public void onEnd() {
        backgroundMusic.stop();
    }

    @Override
    public void onDrawGame() {
        batch.draw(backgroundImage, 0, 0);
        cat.draw(batch);

        for (Homework homework : homeworks) {
            homework.draw(batch);
        }

    }

    @Override
    public String getInstructions() {
        return "Pegue o dever de casa!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Cat extends MultiAnimatedSprite {
        private final float height;

        static final int FRAME_WIDTH = 22;
        static final int FRAME_HEIGHT = 34;
        
        public Cat(final Texture catSpritesheet, float height) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(catSpritesheet,
                                    FRAME_WIDTH, FRAME_HEIGHT);
                    Animation walking = new Animation(0.2f,
                            frames[0][0],
                            frames[1][0],
                            frames[1][1],
                            frames[1][2],
                            frames[0][3],
                            frames[1][3]);
                    walking.setPlayMode(Animation.PlayMode.LOOP);
                    put("walking", walking);
                }
            }, "walking");
            this.height = height;
        }
    }

    class Homework extends Sprite {

        static final int FRAME_WIDTH = 32;
        static final int FRAME_HEIGHT = 32;
        private Vector2 speed;

        public Homework(Texture texture, Vector2 speed) {
            super(texture);
            this.speed = speed;
        }

        public void update(float dt) {
            this.setPosition(this.getX(),
                    this.getY() - this.speed.y * dt);
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }
    }
}
