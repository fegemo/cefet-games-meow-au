package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashMap;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class CatchThatHomework extends MiniGame {

    private Texture catTexture;
    private Cat cat;
    private Texture homeworkTexture;
    private Array<Homework> homeworks;
    private Sound backgroundMusic;
    private float speedMultiplier;
    private Texture backgroundImage;
    
    private float spawnInterval = 1;

    public CatchThatHomework(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        catTexture = assets.get("catch-that-homework/cat-sprite.png", Texture.class);
        homeworkTexture = assets.get("catch-that-homework/homework.png", Texture.class);
        backgroundMusic = assets.get("catch-that-homework/bensound-sexy.mp3", Sound.class);
        backgroundImage = assets.get("catch-that-homework/valley.png", Texture.class);

        cat = new Cat(catTexture, 0 + 200);
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
        cat.setCenter(click.x, cat.height);
    }

    @Override
    public void onUpdate(float dt) {
        for (Homework homework : homeworks) {
            homework.update(dt);
        }

        for (Homework homework : homeworks) {
//            Colisão com o gato
            if (homework.getBoundingRectangle()
                    .overlaps(cat.getBoundingRectangle())) {
                homeworks.removeValue(homework, true);
//                Colisão com o chão
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
        return "Catch that homework!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Cat extends Sprite {
        private final int lives = 1;
        private final float height;

        static final int FRAME_WIDTH = 50;
        static final int FRAME_HEIGHT = 50;

        public Cat(Texture texture, float height) {
            super(texture);
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
