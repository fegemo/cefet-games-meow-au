package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author Adriel
 */
public class UnderwaterCat extends MiniGame {

    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private float spawnInterval;
    private int totalFish;
    private Texture background;
    private MyMusic backgroundMusic;
    private MyMusic swimmingAmbientSound;
    private MySound gotFishSound;

    Animation<TextureRegion> swimmingAnimation;
    private Array<Fish> toCapture;
    private Array<Spiky> enemies;
    private Texture swimCatTexture;
    private Texture fish1Texture;
    private Texture fish2Texture;
    private Texture fish3Texture;
    private Texture spikyTexture;
    TheCat mainCharacter;

    private boolean isOver;

    private int numberEaten = 0;
    private int fishToEat;
    private float time = 0;

    public UnderwaterCat(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        backgroundMusic = new MyMusic(assets.get("underwater-cat/water.mp3", Music.class));
        swimmingAmbientSound =  new MyMusic(assets.get("underwater-cat/swim.wav", Music.class));
        gotFishSound = new MySound(assets.get("underwater-cat/eat.wav", Sound.class));
        swimCatTexture = assets.get(
                "underwater-cat/swimcatspritesheet.png", Texture.class);
        fish1Texture = assets.get(
                "underwater-cat/fish1.png", Texture.class);
        fish2Texture = assets.get(
                "underwater-cat/fish2.png", Texture.class);
        fish3Texture = assets.get(
                "underwater-cat/fish3.png", Texture.class);

        spikyTexture = assets.get(
                "underwater-cat/fish5.png", Texture.class);

        mainCharacter = new TheCat(swimCatTexture);
        toCapture = new Array<Fish>();
        enemies = new Array<Spiky>();
        background = assets.get(
                "underwater-cat/background.bmp", Texture.class);
        numberEaten = 0;
        isOver = false;
        this.time = 0;
        initializeFish(fishToEat);
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                spawnEnemy();
            }
        }, 0, this.spawnInterval);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 120, 220);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 240, 340);
        this.spawnInterval = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.25f, 1.5f);
        this.fishToEat = (int) Math.ceil(DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 2, 5)) + 1;
    }

    @Override
    public String getInstructions() {
        return "Mate a fome, mas sem espinhos!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    @Override
    public void onDrawGame() {
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        batch.draw(background, 0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());
        for (Fish fish : this.toCapture) {
            fish.draw(batch);
        }
        for (Spiky s : this.enemies) {
            s.draw(batch);
        }
        mainCharacter.draw(batch);
        swimmingAmbientSound.setLooping(true);
        swimmingAmbientSound.play();

        if (isOver) {
            backgroundMusic.stop();
            swimmingAmbientSound.stop();
        }

    }

    @Override
    public void onUpdate(float dt) {
        // atualiza a animação  do gato 
        mainCharacter.update(dt);
        // atualiza a animação  dos peixes
        for (Fish fish : this.toCapture) {
            fish.update(dt);
        }

        // verifica se personagemPrincipalEstáSobreAlgumPeixe        
        for (int i = 0; i < this.toCapture.size; i++) {
            Fish f = this.toCapture.get(i);
            f.update(dt);
            if (f.getBoundingRectangle()
                    .overlaps(mainCharacter.getBoundingRectangle())) {
                toCapture.removeValue(toCapture.get(i), true);
                eatFish();
            }
        }

        // verifica se este inimigo está colidindo com personagem principal        
        for (int i = 0; i < this.enemies.size; i++) {
            Spiky s = this.enemies.get(i);
            s.update(dt);
            Rectangle bounds = mainCharacter.getBoundingRectangle();
            bounds.setHeight(bounds.getHeight() - 50);
            bounds.setWidth(bounds.getWidth() - 70);
            if (s.getBoundingRectangle()
                    .overlaps(bounds)) {
                isOver = true;
                super.challengeFailed();
            }
        }
    }

    private void eatFish() {
        numberEaten++;
        gotFishSound.play();
        if (numberEaten == fishToEat) {
            isOver = true;
            super.challengeSolved();
        }
    }

    @Override
    protected void onEnd() {
        isOver = true;
        backgroundMusic.stop();
        swimmingAmbientSound.stop();
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        mainCharacter.setCenter(click.x, click.y);
        if (this.numberEaten >= this.fishToEat) {
            super.challengeSolved();
        }
    }

    private TextureRegion[] workFrames(final Texture texture, int rows, int columns) {
        TextureRegion[][] tmp = TextureRegion.split(texture,
                texture.getWidth() / columns,
                texture.getHeight() / rows);

        TextureRegion[] swimFrames = new TextureRegion[columns * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                swimFrames[index++] = tmp[i][j];
            }
        }
        return swimFrames;

    }

    private void initializeFish(int qtdePeixes) {
        int tipoPeixe = 1;
        Vector3 posicao = new Vector3();
        for (int i = 0; i <= qtdePeixes; i++) {
            switch (tipoPeixe) {
                case 1:
                    createFish(fish1Texture, tipoPeixe);
                    break;
                case 2:
                    createFish(fish2Texture, tipoPeixe);
                    break;
                case 3:
                    createFish(fish3Texture, tipoPeixe);
                    break;
            }
            if (tipoPeixe <= 3) {
                tipoPeixe++;
            } else {
                tipoPeixe = 1;
            }
        }
    }

    private void createFish(Texture spritesFish, int tipo) {

        Fish fish;
        if (tipo == 1) {
            fish = new Fish(spritesFish, 6, 8);
            createFishAux(fish);
        }
        if (tipo == 2) {
            fish = new Fish(spritesFish, 7, 7);
            createFishAux(fish);
        }
        if (tipo == 3) {
            fish = new Fish(spritesFish, 8, 6);
            createFishAux(fish);
        }

    }

    private void createFishAux(Fish fish) {
        Vector3 posicao = new Vector3();
        float randomWidth = MathUtils.random(viewport.getWorldWidth() - 200, 200);
        float randomHeight = MathUtils.random(viewport.getWorldHeight() - 200, 200);

        fish.setCenter(randomWidth, randomHeight);
        this.toCapture.add(fish);
    }

    private void spawnEnemy() {
        Vector2 goalCenter = new Vector2();
        Vector2 spikyGoal = this.mainCharacter
                .getBoundingRectangle()
                .getCenter(goalCenter);
        Vector2 spikyPosition = new Vector2();
        boolean appearFromSides = MathUtils.randomBoolean();
        if (appearFromSides) {
            spikyPosition.x = MathUtils.randomBoolean()
                    ? -Spiky.FRAME_WIDTH
                    : viewport.getWorldWidth();
            spikyPosition.y = MathUtils.random(
                    -Spiky.FRAME_HEIGHT,
                    viewport.getWorldHeight());
        } else {
            spikyPosition.y = MathUtils.randomBoolean()
                    ? -Spiky.FRAME_HEIGHT
                    : viewport.getWorldHeight();
            spikyPosition.x = MathUtils.random(
                    -Spiky.FRAME_WIDTH,
                    viewport.getWorldWidth());
        }
        Vector2 spikySpeed = spikyGoal.sub(spikyPosition).nor().scl(this.minimumEnemySpeed);

        Spiky enemy = new Spiky(spikyTexture);
        enemy.setPosition(spikyPosition.x, spikyPosition.y);
        enemy.setSpeed(spikySpeed);
        enemies.add(enemy);
    }

    // Classes utilizadas
    class TheCat extends AnimatedSprite {

        TheCat(final Texture swimCatTexture) {

            super(new Animation<TextureRegion>(0.2f, workFrames(swimCatTexture, 8, 6)));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
        }

        Vector2 getPosition() {
            return new Vector2(
                    this.getX() + this.getWidth(),
                    this.getY() + this.getHeight());
        }
    }

    class Fish extends AnimatedSprite {

        Fish(final Texture fishTexture, int rows, int columns) {

            super(new Animation<TextureRegion>(0.2f, workFrames(fishTexture, rows, columns)));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
        }

        Vector2 getPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getDistanceTo(float otherbodyX, float otherbodyY) {
            return getPosition().dst(otherbodyX, otherbodyY);
        }

    }

    class Spiky extends AnimatedSprite {

        private Vector2 speed;
        static final int FRAME_WIDTH = 79;
        static final int FRAME_HEIGHT = 87;

        Spiky(final Texture spikyTexture) {
            super(new Animation<TextureRegion>(0.2f, workFrames(spikyTexture, 4, 12)));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
        }

        @Override
        public void update(float dt) {
            super.update(dt);
            super.setPosition(super.getX() + this.speed.x * dt,
                    super.getY() + this.speed.y * dt);
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }

    }
}
