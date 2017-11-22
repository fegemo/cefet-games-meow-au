package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author gustavo
 */
public class Running extends MiniGame {

    //fundo
    private Texture fundoTexture;
    //personagens
    private Texture catTexture;
    private Cat cat;
    private Texture dogTexture;
    private Array<Dog> dogs;
    //objetos
    private Texture ballTexture;
    private Texture woolTexture;
    private Texture boneTexture;
    private Texture kitTexture;
    private Sprite ball;
    //sprites
    private Array<Sprite> woolArray;
    private Array<Sprite> boneArray;
    private Array<Sprite> kitArray;
    //sons
    private MySound pickupWoolSound;
    private MySound pickupKitSound;
    private MySound finalSound;
    private MySound loseSound;
    private float catSpeed;
    //constantes de velocidade
    private static final float CAT_SPEED_CONSTANT = (float) 0.25;
    private static final float DOG_SPEED_CONSTANT = (float) 0.002;
    // variáveis do desafio - variam com a dificuldade do minigame
    private float minimumdogSpeed;
    private float dogSpeed;
    private int totalWool;
    private int totalBone;
    private int totalKit;

    private boolean blnNextLevel = false;
    private int totalEnemy;
    private final int increase = 3;
    private float fltScale = 1;

    public Running(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 80f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        totalEnemy = 1;
        fundoTexture = assets.get(
                "running/fundo.png", Texture.class);
        catTexture = assets.get(
                "running/cat-run-spritesheet.png", Texture.class);
        cat = new Cat(catTexture);
        dogTexture = assets.get(
                "running/dog-run-spritesheet.png", Texture.class);
        ballTexture = assets.get(
                "running/ball.png", Texture.class);
        woolTexture = assets.get(
                "running/wool.png", Texture.class);
        boneTexture = assets.get(
                "running/bone.png", Texture.class);
        kitTexture = assets.get(
                "running/kit.png", Texture.class);

        pickupWoolSound = new MySound(assets.get(
                "running/pickup_wool.wav", Sound.class));
        pickupKitSound = new MySound(assets.get(
                "running/pickup_kit.wav", Sound.class));
        finalSound = new MySound(assets.get(
                "running/final.wav", Sound.class));
        loseSound = new MySound(assets.get(
                "running/lose.wav", Sound.class));
        setPositions(false);
    }

    protected void setPositions(boolean blnChange) {
        cat.setPosition(0, (viewport.getWorldHeight() - cat.getHeight()) * rand.nextFloat());
        catSpeed = (float) 0.8;
        ball = new Sprite(ballTexture);
        float fltBall = rand.nextFloat();
        if (fltBall > 0.8) {
            fltBall = (float) 0.8;
        }
        ball.setPosition(1240, viewport.getWorldHeight() * fltBall);
        dogSpeed = (float) 0.005;
        if (blnChange) {
            changeLevel();
            cat.setScale(0.5f);
        }

        dogs = new Array<Dog>();
        for (int i = 0; i < totalEnemy; i++) {
            createDogs(blnChange);
        }
        woolArray = new Array<Sprite>();
        for (int i = 0; i < totalWool; i++) {
            createWool();
        }
        boneArray = new Array<Sprite>();
        for (int i = 0; i < totalBone; i++) {
            createBone();
        }
        kitArray = new Array<Sprite>();
        for (int i = 0; i < totalKit; i++) {
            createKit();
        }
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.minimumdogSpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 100, 180);
        this.totalBone = (int) DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 3, 6) + 1;
        this.totalKit = (int) DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 2, 8) + 1;
        this.totalWool = (int) DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 3, 6) + 1;
        if (difficulty >= 0.5) {
            if (rand.nextFloat() >= 0.5) {
                blnNextLevel = true;
            }
        }

    }

    @Override
    public void onHandlePlayingInput() {
        Vector2 pointer = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(pointer);
        cat.setY(Math.min(pointer.y, viewport.getWorldHeight() - cat.getHeight()));
    }

    @Override
    public void onUpdate(float dt) {
        // atualiza a escova (quadro da animação)
        cat.update(dt);
        //colisao do gato com a bola    
        if (cat.getBoundingRectangle().overlaps(ball.getBoundingRectangle())) {
            if (blnNextLevel) {
                showMessage("Vença o desafio");
                setPositions(true);
                blnNextLevel = false;
            } else {
                challengeSolved();
            }

            finalSound.play();
        }

        //se o gato encostar na bola de la, ele aumenta a velocidade
        for (int i = 0; i < this.woolArray.size; i++) {
            if (cat.getBoundingRectangle().overlaps(woolArray.get(i).getBoundingRectangle())) {
                catSpeed += CAT_SPEED_CONSTANT;
                woolArray.removeIndex(i);
                pickupWoolSound.play();
            }

        }
        //se o gato encostar no kit de banho, ele perde velocidade
        for (int i = 0; i < this.kitArray.size; i++) {
            if (cat.getBoundingRectangle().overlaps(kitArray.get(i).getBoundingRectangle())) {
                if (catSpeed > 0.5) {
                    catSpeed -= CAT_SPEED_CONSTANT;
                }
                kitArray.removeIndex(i);
                pickupKitSound.play();
            }
        }

        for (int aux = 0; aux < this.dogs.size; aux++) {
            Dog dog = dogs.get(aux);
            dog.update(dt);
            //colisao com a bola = fim de jogo
            if (dog.getBoundingRectangle().overlaps(ball.getBoundingRectangle())) {
                challengeFailed();
                loseSound.play();
            }
            //se o cachorro encostar no osso, ele aumenta a velocidade
            for (int i = 0; i < this.boneArray.size; i++) {
                if (dog.getBoundingRectangle().overlaps(boneArray.get(i).getBoundingRectangle())) {
                    dogSpeed += DOG_SPEED_CONSTANT;
                    boneArray.removeIndex(i);
                }
            }
            //se o cachorro encostar no kit de banho, ele perde velocidade
            for (int i = 0; i < this.kitArray.size; i++) {
                if (dog.getBoundingRectangle().overlaps(kitArray.get(i).getBoundingRectangle())) {
                    if (dogSpeed > 0.005) {
                        dogSpeed -= DOG_SPEED_CONSTANT;
                    }
                    kitArray.removeIndex(i);
                }
            }
        }
    }

    @Override
    public void onDrawGame() {
        batch.draw(fundoTexture, 0, 0);
        ball.draw(batch);
        for (int i = 0; i < woolArray.size; i++) {
            Sprite sprite = woolArray.get(i);
            sprite.draw(batch);
        }
        for (int i = 0; i < boneArray.size; i++) {
            Sprite sprite = boneArray.get(i);
            sprite.draw(batch);
        }
        for (int i = 0; i < kitArray.size; i++) {
            Sprite sprite = kitArray.get(i);
            sprite.draw(batch);
        }

        Dog firstDog = dogs.get(0);
        
        if(cat.getY() > firstDog.getY()){
            cat.draw(batch);
            firstDog.draw(batch);
        }
        else{
            firstDog.draw(batch);
            cat.draw(batch);
        }
        
        for (int i = 1; i < dogs.size; i++) {
            Dog dog = dogs.get(i);
            dog.draw(batch);
        }
    }

    @Override
    public String getInstructions() {
        return "Pegue a bola na linha de chegada";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    public Vector2 randomPosition() {
        // pega x e y entre 0 e 1
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        // multiplica x e y pela largura e altura da tela
        position.scl(
                viewport.getWorldWidth() - woolTexture.getWidth(),
                viewport.getWorldHeight() - woolTexture.getHeight());

        return position;
    }

    public void changeLevel() {
        totalEnemy *= increase;
        totalBone *= increase;
        totalWool *= increase;
        totalKit *= increase;
        fltScale = (float) 0.5;
    }

    public void createDogs(boolean blnScale) {
        Dog newDog = new Dog(dogTexture);
        if (blnScale) {
            newDog.setScale(0.5f);
        }
        newDog.setOrigin(0, 0);
        newDog.setPosition(0, viewport.getWorldHeight() * rand.nextFloat());
        Vector2 dogGoal = new Vector2(ball.getX() - newDog.getX(), ball.getY() - newDog.getY());
        dogGoal = dogGoal.nor().scl(minimumdogSpeed);
        newDog.setSpeed(dogGoal);
        dogs.add(newDog);

    }

    public void createWool() {
        Sprite wool = new Sprite(woolTexture);
        Vector2 position = randomPosition();
        wool.setPosition(position.x, position.y);
        woolArray.add(wool);
    }

    public void createBone() {
        Sprite bone = new Sprite(boneTexture);
        Vector2 position = randomPosition();
        bone.setPosition(position.x, position.y);
        boneArray.add(bone);
    }

    public void createKit() {
        Sprite kit = new Sprite(kitTexture);
        Vector2 position = randomPosition();
        kit.setPosition(position.x, position.y);
        kitArray.add(kit);
    }

    class Cat extends AnimatedSprite {

        static final int FRAME_WIDTH = 162;
        static final int FRAME_HEIGHT = 142;

        Cat(final Texture catTexture) {
            super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            catTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],
                        frames[0][4],
                        frames[0][5],
                        frames[0][6],
                        frames[0][7]
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
        }

        @Override
        public void update(float dt) {
            super.update(dt);
            if (super.getX() < viewport.getWorldWidth() - cat.getWidth() * fltScale) {
                super.setPosition(super.getX() + catSpeed,
                        super.getY());
            }
        }
    }

    class Dog extends AnimatedSprite {

        private Vector2 speed;

        static final int FRAME_WIDTH = 164;
        static final int FRAME_HEIGHT = 144;

        Dog(final Texture dogTexture) {
            super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            dogTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],
                        frames[0][4],
                        frames[0][5],
                        frames[0][6],
                        frames[0][7]
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
        }

        @Override
        public void update(float dt) {
            super.update(dt);
            if (super.getX() < viewport.getWorldWidth() - dogs.get(0).getWidth() * fltScale) {
                super.setPosition(super.getX() + this.speed.x * dogSpeed,
                        super.getY() + this.speed.y * dogSpeed);
            }
        }

        Vector2 getPosition() {
            return new Vector2(
                    this.getX(),
                    this.getY());
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }
    }

}
