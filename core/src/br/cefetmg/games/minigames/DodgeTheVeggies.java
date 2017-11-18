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
import com.badlogic.gdx.math.Rectangle;
import java.util.HashMap;

public class DodgeTheVeggies extends MiniGame {

    private Texture catSpritesheet;
    private Cat cat;
    private Texture veggieTexture;
    private Texture faintedCatTexture;
    private Array<Veggie> veggies;
    private Array<Texture> veggieTextures;
    private MySound backgroundMusic;
    private float speedMultiplier;
    private float spawnIntervalMultiplier;
    private final float spawnInterval = 1;
    private Texture backgroundImage;

    public DodgeTheVeggies(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void challengeFailed() {
        boolean isFlipX = cat.isFlipX();
        cat.setRegion(faintedCatTexture);
        cat.flip(isFlipX, false);
        super.challengeFailed();
    }

    @Override
    protected void onStart() {
        catSpritesheet = assets.get("dodge-the-veggies/cat-spritesheet.png", Texture.class);
        backgroundImage = assets.get("dodge-the-veggies/background.png", Texture.class);
        faintedCatTexture = assets.get("dodge-the-veggies/fainted-cat-texture.png", Texture.class);
        veggieTextures = new Array<Texture>();
        veggies = new Array<Veggie>();
        veggieTextures.addAll(
            assets.get("dodge-the-veggies/carrot.png", Texture.class),
            assets.get("dodge-the-veggies/onion.png", Texture.class),
            assets.get("dodge-the-veggies/tomato.png", Texture.class),
            assets.get("dodge-the-veggies/potato.png", Texture.class));
        backgroundMusic = new MySound(assets.get("dodge-the-veggies/bensound-jazzcomedy.mp3", Sound.class));

        cat = new Cat(catSpritesheet, 200);
        cat.setCenter(
            viewport.getWorldWidth() / 2f,
            cat.height);
        cat.setScale(0.3f);

        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnVeggies();
            }

        }, 0, this.spawnIntervalMultiplier * this.spawnInterval);

        backgroundMusic.play();
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.speedMultiplier = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 1f, 2f) * 0.5f;
        this.spawnIntervalMultiplier = DifficultyCurve.S.getCurveValueBetween(difficulty, 1f, 0.5f) * 1.5f;
    }

    private void spawnVeggies() {
        int index = MathUtils.random(0, veggieTextures.size - 1);

        Vector2 direction;
        Vector2 position = new Vector2();

        direction = new Vector2(MathUtils.random(-1, 1) * 300, MathUtils.random(-1, 1) * 300);
        if (direction.x != 0f || direction.y != 0f) {
            Veggie veggie = new Veggie(veggieTextures.get(index), direction.scl(this.speedMultiplier));

            boolean appearFromSides = MathUtils.randomBoolean();
            if (appearFromSides) {
                position.x = MathUtils.randomBoolean()
                        ? 0
                        : viewport.getWorldWidth() - veggie.FRAME_WIDTH;
                position.y = MathUtils.random(
                        0,
                        viewport.getWorldHeight() - veggie.FRAME_HEIGHT);
            } else {
                position.y = MathUtils.randomBoolean()
                        ? 0
                        : viewport.getWorldHeight() - veggie.FRAME_HEIGHT;
                position.x = MathUtils.random(
                        0,
                        viewport.getWorldWidth() - veggie.FRAME_WIDTH);
            }

            veggie.setPosition(position.x, position.y);
            veggies.add(veggie);
        }
    }

    @Override
    public void onHandlePlayingInput() {
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);

        if (!cat.isFlipX() && Gdx.input.getDeltaX() < 0
                || cat.isFlipX() && Gdx.input.getDeltaX() > 0) {
            cat.flipFrames(true, false);
        }

        Rectangle viewportRectangle = new Rectangle(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        if (viewportRectangle.contains(cat.getBoundingRectangle())) {
           cat.setCenter(click.x, click.y);
        } else {
            challengeFailed();
        }
    }

    @Override
    public void onUpdate(float dt) {
        for (Veggie veggie : veggies) {
            veggie.update(dt);
        }
        
        cat.update(dt);

        for (Veggie veggie : veggies) {
            // Colisão veggie x cat
            if (veggie.getBoundingRectangle()
                    .overlaps(cat.getBoundingRectangle())) {
                challengeFailed();
            } else if (veggie.getX() + veggie.FRAME_WIDTH / 2f > viewport.getWorldWidth() || veggie.getX() < 0) {
                Vector2 speed = veggie.getSpeed();
                if(veggie.getX()*speed.x < 0){
                    veggie.setSpeed(new Vector2(-speed.x, speed.y));
                }
            } else if (veggie.getY() + veggie.FRAME_HEIGHT / 2f > viewport.getWorldHeight() || veggie.getY() < 0) {
                Vector2 speed = veggie.getSpeed();
                if(veggie.getY()*speed.y < 0){
                    veggie.setSpeed(new Vector2(speed.x, -speed.y));
                }
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
        this.cat.draw(batch);

        for (Veggie veggie : veggies) {
            veggie.draw(batch);
        }
    }

    @Override
    public String getInstructions() {
        return "Desvie dos vegetais";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Cat extends MultiAnimatedSprite {
        private final int lives = 9;
        private final float height;
        static final int FRAME_WIDTH = 497;
        static final int FRAME_HEIGHT = 291;

        public Cat(Texture texture, float height) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(catSpritesheet,
                                    FRAME_WIDTH, FRAME_HEIGHT);
                    Animation walking = new Animation(0.2f,
                            frames[0][0],
                            frames[0][1],
                            frames[0][2],
                            frames[1][0],
                            frames[1][1],
                            frames[1][2],
                            frames[2][0],
                            frames[2][1]);
                    walking.setPlayMode(Animation.PlayMode.LOOP);
                    put("walking", walking);
                }
            }, "walking");
            this.height = height;
        }
    }

    class Veggie extends Sprite {

        private int FRAME_WIDTH;
        private int FRAME_HEIGHT;
        private Vector2 speed;

        public Veggie(Texture texture, Vector2 speed) {
            super(texture);
            this.speed = speed;

//            carrot
            if (texture == veggieTextures.get(0)) {
                FRAME_WIDTH = 34;
                FRAME_HEIGHT = 55;
//                onion
            } else if (texture == veggieTextures.get(1)) {
                FRAME_WIDTH = 38;
                FRAME_HEIGHT = 54;
//                tomato
            } else if (texture == veggieTextures.get(2)) {
                FRAME_WIDTH = 40;
                FRAME_HEIGHT = 49;
//                potato
            } else if (texture == veggieTextures.get(3)) {
                FRAME_WIDTH = 28;
                FRAME_HEIGHT = 60;
            }
        }

        public void update(float dt) {
            this.setPosition(this.getX() - this.speed.x * dt,
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
