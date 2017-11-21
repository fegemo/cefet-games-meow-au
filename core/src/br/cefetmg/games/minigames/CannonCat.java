package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.ArrayList;

/**
 * @author Kael
 */
public class CannonCat extends MiniGame {

    private Texture background;
    private Texture cat, cookie;
    public int cannonDirectionIndex = 0;
    public int remainingShots;
    private int remainingCats;
    public int[] cookie_x = new int[MAX_AMMO];
    public int[] cookie_y = new int[MAX_AMMO];
    public int center_x = 1200 / 2;
    public int center_y = 700 / 2;
    public double velocidade = 1;
    public double dificuldade = 1;
    public double tempo1 = System.currentTimeMillis();
    public double tempo2 = System.currentTimeMillis();
    public final static int MAX_AMMO = 10;
    public final static int NUMBER_OF_CATS = 8;
    private ObjectMap<Direction, Texture> cannonTextures;

    private MySound backgroundMusic;
    private static ArrayList<Cat> cats;
    private static ArrayList<Projectile> projectiles;

    private static final float CAT_SPEED_SCALE = 2f,
            PROJECTILE_SPEED_SCALE = 2f;
    public final int CHALLENGE_FAILED = 0,
            CHALLENGE_SOLVED = 1;

    public CannonCat(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        background = assets.get("cannon-cat/background.png", Texture.class);

        cat = assets.get("cannon-cat/cat.png", Texture.class);
        cookie = assets.get("cannon-cat/biscoito.png", Texture.class);

        cannonTextures = new ObjectMap<Direction, Texture>();
        cannonTextures.put(Direction.EAST, assets.get("cannon-cat/cannon_right.png", Texture.class));
        cannonTextures.put(Direction.SOUTH_EAST, assets.get("cannon-cat/cannon_down+right.png", Texture.class));
        cannonTextures.put(Direction.SOUTH, assets.get("cannon-cat/cannon_down.png", Texture.class));
        cannonTextures.put(Direction.SOUTH_WEST, assets.get("cannon-cat/cannon_down+left.png", Texture.class));
        cannonTextures.put(Direction.WEST, assets.get("cannon-cat/cannon_left.png", Texture.class));
        cannonTextures.put(Direction.NORTH_WEST, assets.get("cannon-cat/cannon_up+left.png", Texture.class));
        cannonTextures.put(Direction.NORTH, assets.get("cannon-cat/cannon_up.png", Texture.class));
        cannonTextures.put(Direction.NORTH_EAST, assets.get("cannon-cat/cannon_up+right.png", Texture.class));

        backgroundMusic = new MySound(assets.get("cannon-cat/background-music.mp3", Sound.class));
        backgroundMusic.loop();

        cats = new ArrayList<Cat>();
        projectiles = new ArrayList<Projectile>();

        for (int i = 0; i < NUMBER_OF_CATS; i++) {
            Vector2 startPoint = new Vector2((int) (200 * Math.cos((i * Math.PI) / 4)) + 1280 / 2, (int) ((-1) * 200 * Math.sin((i * Math.PI) / 4)) + 720 / 2);
            Vector2 finalPoint = new Vector2(0, i * 2 * cat.getHeight());

            cats.add(new Cat(new Sprite(cat), startPoint, finalPoint));
        }

        for (int i = 0; i < MAX_AMMO; i++) {
            cookie_x[i] = 300;
            cookie_y[i] = 200 + (-1) * i * 20;
        }
        remainingShots = MAX_AMMO;
        remainingCats = NUMBER_OF_CATS;
    }

    private void challengeFinished(int challengeResult) {
        backgroundMusic.stop();

        if (challengeResult == CHALLENGE_FAILED) {
            super.challengeFailed();
        } else if (challengeResult == CHALLENGE_SOLVED) {
            super.challengeSolved();
        }
    }

    private void checkColision() {

    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        //velocidade = 1+3*difficulty;
        this.velocidade = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 0, 1);
    }

    @Override
    public void onHandlePlayingInput() {
        if (Gdx.input.justTouched()) {
            if (remainingShots-- > 0) {

                //Dispara-se um projétil
                Vector2 startPoint = new Vector2(center_x, center_y);
                Vector2 finalPoint = new Vector2(cats.get(cannonDirectionIndex).getX(), cats.get(cannonDirectionIndex).getY());
                projectiles.add(new Projectile(new Sprite(cookie), startPoint, finalPoint));

                checkColision();

                /*if (cat_x[cannonDirectionIndex] != 0) {
                    cat_x[cannonDirectionIndex] = 0;
                    remainingCats--;
                }*/
                if (remainingCats == 0) {
                    challengeFinished(CHALLENGE_SOLVED);
                } else if (remainingShots == 0) {
                    challengeFinished(CHALLENGE_FAILED);
                }
            } else {
                challengeFinished(CHALLENGE_FAILED);
            }

            cookie_x[remainingShots] = -500;
        }
    }

    @Override
    public void onUpdate(float dt) {
        tempo2 = System.currentTimeMillis();
        if (tempo2 - tempo1 > (100 - velocidade * 50)) {
            tempo1 = System.currentTimeMillis();
            cannonDirectionIndex++;
            cannonDirectionIndex = cannonDirectionIndex % NUMBER_OF_CATS;
        }

        updateCatsAndProjectilesLists();
    }

    @Override
    public void onDrawGame() {
        batch.draw(background, 0, 0);
        for (int i = 0; i < cats.size(); i++) {
            cats.get(i).draw(batch);
        }
        for (int i = 0; i < MAX_AMMO; i++) {
            batch.draw(cookie, cookie_x[i], cookie_y[i]);
        }
        for (int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).draw(batch);
        }

        //Desenham as posições do canhão de maneira a girar no sentido horário
        Direction currentDirection = Direction.valueOf(cannonDirectionIndex);
        batch.draw(cannonTextures.get(currentDirection), center_x, center_y);
    }

    public void updateCatsAndProjectilesLists() {
        for (int i = 0; i < cats.size(); i++) {
            for (int j = 0; j < projectiles.size(); j++) {
                //Move-se o projétil
                projectiles.get(j).updateCurrentPosition(PROJECTILE_SPEED_SCALE);
                
                //Averigua colisão
                if (cats.get(i).getState() == Cat.START_STATE) {
                    if (cats.get(i).getSprite().getBoundingRectangle().overlaps(projectiles.get(j).getSprite().getBoundingRectangle())) {
                        cats.get(i).setState(Cat.MOVING_STATE);
                        projectiles.remove(j);
                    }
                }
            }
            //Move-se o gato se este estiver em movimento
            cats.get(i).updateCurrentPosition(CAT_SPEED_SCALE);
        }
    }

    @Override
    public String getInstructions() {
        return "Alimente todos os gatinhos!";

    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    private static enum Direction {
        EAST(0),
        SOUTH_EAST(1),
        SOUTH(2),
        SOUTH_WEST(3),
        WEST(4),
        NORTH_WEST(5),
        NORTH(6),
        NORTH_EAST(7);

        private final int index;

        private Direction(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static Direction valueOf(int index) {
            Direction found = null;
            for (Direction d : Direction.values()) {
                if (d.getIndex() == index) {
                    found = d;
                    break;
                }
            }
            return found;
        }
    };

    public static class Object {

        private Vector2 currentPosition;
        private Vector2 finalPosition;
        private Sprite sprite;

        public Object(Sprite sprite, Vector2 startPoint, Vector2 finalPoint) {
            this.sprite = sprite;
            currentPosition = new Vector2(startPoint);
            finalPosition = new Vector2(finalPoint);

            this.sprite.setPosition(currentPosition.x, currentPosition.y);
        }

        public void draw(SpriteBatch batch) {
            batch.draw(sprite, currentPosition.x, currentPosition.y);
        }

        public Vector2 getCurrentPosition() {
            return currentPosition;
        }

        public Vector2 getFinalPosition() {
            return finalPosition;
        }

        public Sprite getSprite() {
            return sprite;
        }

        public float getX() {
            return currentPosition.x;
        }

        public float getY() {
            return currentPosition.y;
        }

        public void updateCurrentPosition(float speed) {
            //Direção do projétil
            Vector2 direction = (new Vector2(finalPosition)).sub(currentPosition).nor();

            //Projétil caminha em direção ao destino
            currentPosition.mulAdd(direction, speed);
            this.sprite.setPosition(currentPosition.x, currentPosition.y);

        }
    };

    public static class Projectile extends Object {

        public Projectile(Sprite sprite, Vector2 startPoint, Vector2 finalPoint) {
            super(sprite, startPoint, finalPoint);
        }
    };

    public static class Cat extends Object {

        public static final int START_STATE = 0,
                MOVING_STATE = 1,
                FINAL_STATE = 2;

        private int state = START_STATE;

        public Cat(Sprite sprite, Vector2 startPoint, Vector2 finalPoint) {
            super(sprite, startPoint, finalPoint);
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void updateCurrentPosition(float speed) {
            if (this.state == MOVING_STATE) {
                super.updateCurrentPosition(speed);
            }
        }
    };
}
