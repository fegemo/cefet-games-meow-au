package br.cefetmg.games.minigames;

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

public class CucumberMadness extends MiniGame {

    private Texture catTexture;
    private Cat cat;
    private Texture veggieTexture;
    private Array<Veggie> veggies;
    private Array<Texture> veggieTextures;
    private Sound backgroundMusic;
    private float speedMultiplier;
    private float spawnIntervalMultiplier;
    private float spawnInterval = 1;
    private Texture backgroundImage;

    public CucumberMadness(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        catTexture = assets.get("cucumber-madness/cat-sprite.png", Texture.class);
        backgroundImage = assets.get("cucumber-madness/background.png", Texture.class);
        veggieTextures = new Array<Texture>();
        veggies = new Array<Veggie>();
        veggieTextures.addAll(
            assets.get("cucumber-madness/carrot.png", Texture.class),
            assets.get("cucumber-madness/onion.png", Texture.class),
            assets.get("cucumber-madness/tomato.png", Texture.class),
            assets.get("cucumber-madness/potato.png", Texture.class));
        backgroundMusic = assets.get("cucumber-madness/bensound-jazzcomedy.mp3", Sound.class);

        cat = new Cat(catTexture, 0 + 200);
        cat.setCenter(
            viewport.getWorldWidth() / 2f,
            cat.height);
        cat.setScale(3);

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
        this.speedMultiplier = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 1f, 2f);
        this.spawnIntervalMultiplier = DifficultyCurve.S.getCurveValueBetween(difficulty, 1f, 0.5f);
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
        cat.setCenter(click.x, click.y);
    }

    @Override
    public void onUpdate(float dt) {
        for (Veggie veggie : veggies) {
            veggie.update(dt);
        }

        for (Veggie veggie : veggies) {
//            Colisão veggie x cat
            if (veggie.getBoundingRectangle()
                    .overlaps(cat.getBoundingRectangle())) {
                super.challengeFailed();
            } else if (veggie.getX() + veggie.FRAME_WIDTH / 2f > viewport.getWorldWidth() || veggie.getX() < 0) {
                Vector2 speed = veggie.getSpeed();
                veggie.setSpeed(new Vector2(-speed.x, speed.y));
            } else if (veggie.getY() + veggie.FRAME_HEIGHT / 2f > viewport.getWorldHeight() || veggie.getY() < 0) {
                Vector2 speed = veggie.getSpeed();
                veggie.setSpeed(new Vector2(speed.x, -speed.y));
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
        return "dodge the veggies";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Cat extends Sprite {
        private final int lives = 9;
        private final float height;

        static final int FRAME_WIDTH = 50;
        static final int FRAME_HEIGHT = 50;

        public Cat(Texture texture, float height) {
            super(texture);
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
