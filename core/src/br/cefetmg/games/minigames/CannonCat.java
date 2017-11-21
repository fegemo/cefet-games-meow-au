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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
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
    public int[] cookie_x = new int[MAX_AMMO];
    public int[] cookie_y = new int[MAX_AMMO];
    public int center_x = 1200 / 2;
    public int center_y = 700 / 2;
    public double tempo1 = System.currentTimeMillis();
    public double tempo2 = System.currentTimeMillis();
    public final static int MAX_AMMO = 10;
    public final static int NUMBER_OF_CATS = 8;
    private ObjectMap<Direction, Texture> cannonTextures;

    private MySound backgroundMusic;
    private static ArrayList<Cat> cats;
    private static ArrayList<Projectile> projectiles;
    private static ArrayList<Vector2> catsStartPoints;

    private static final float CAT_SPEED_SCALE = 4f,
            PROJECTILE_SPEED_SCALE = 2f;
    public final int CHALLENGE_FAILED = 0,
            CHALLENGE_SOLVED = 1;
    public double TEMPO_MUDANCA_DIRECAO_CANHAO;

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
        catsStartPoints = new ArrayList<Vector2>();

        for (int i = 0; i < NUMBER_OF_CATS; i++) {
            Vector2 startPoint = new Vector2((int) (200 * Math.cos((i * Math.PI) / 4)) + 1280 / 2, (int) ((-1) * 200 * Math.sin((i * Math.PI) / 4)) + 720 / 2);
            Vector2 finalPoint;

            if (i < NUMBER_OF_CATS / 2) {
                finalPoint = new Vector2(0, i * 2 * cat.getHeight());
            } else {
                finalPoint = new Vector2(viewport.getScreenWidth() - cat.getWidth(), (i - NUMBER_OF_CATS / 2) * 2 * cat.getHeight());
            }

            cats.add(new Cat(new Sprite(cat), startPoint, finalPoint));
            catsStartPoints.add(new Vector2(startPoint));
        }

        for (int i = 0; i < MAX_AMMO; i++) {
            cookie_x[i] = 300;
            cookie_y[i] = 200 + (-1) * i * 20;
        }

        remainingShots = MAX_AMMO;
    }

    private void challengeFinished(int challengeResult) {
        backgroundMusic.stop();

        if (challengeResult == CHALLENGE_FAILED) {
            super.challengeFailed();
        } else if (challengeResult == CHALLENGE_SOLVED) {
            super.challengeSolved();
        }
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        if (difficulty <= 0.25) { //Fácil: Canhão super lento e longuíssimo tempo de jogo
            TEMPO_MUDANCA_DIRECAO_CANHAO = 750;
            super.maxDuration *= 2;
        } else if (difficulty <= 0.5) { //Médio: Canhão lento e longo tempo de jogo
            TEMPO_MUDANCA_DIRECAO_CANHAO = 500;
            super.maxDuration *= 1.5;
        } else if (difficulty <= 0.75) { //Difícil: Canhão normal e tempo de jogo normal
            TEMPO_MUDANCA_DIRECAO_CANHAO = 250;
        } else { //Impossível: Canhão rápido e tempo de jogo baixo
            TEMPO_MUDANCA_DIRECAO_CANHAO = 100;
            super.maxDuration /= 1.5;
        }
    }

    @Override
    public void onHandlePlayingInput() {
        if (Gdx.input.justTouched()) {
            if (remainingShots-- > 0) {
                //Dispara-se um projétil
                Vector2 startPoint = new Vector2(center_x, center_y);
                Vector2 finalPoint = new Vector2(catsStartPoints.get(cannonDirectionIndex));
                projectiles.add(new Projectile(new Sprite(cookie), startPoint, finalPoint));
            } else if (projectiles.size() == 0){
                challengeFinished(CHALLENGE_FAILED);
            }

            if (remainingShots >= 0) {
                cookie_x[remainingShots] = -500;
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        tempo2 = System.currentTimeMillis();
        if (tempo2 - tempo1 > TEMPO_MUDANCA_DIRECAO_CANHAO) {
            tempo1 = System.currentTimeMillis();
            cannonDirectionIndex++;
            cannonDirectionIndex = cannonDirectionIndex % NUMBER_OF_CATS;
        }

        updateCatsAndProjectilesLists();

        if (Cat.howManyStartStates(cats) == 0) {
            challengeFinished(CHALLENGE_SOLVED);
        } else if (remainingShots == 0 && projectiles.size() == 0) {
            challengeFinished(CHALLENGE_FAILED);
        }
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
        ArrayList<Projectile> removableProjectile = new ArrayList<Projectile>();

        //Move-se gato se estiverem em movimento
        for (int i = 0; i < cats.size(); i++) {
            cats.get(i).updateCurrentPosition(CAT_SPEED_SCALE);
        }

        //Move-se projéteis
        for (int i = 0; i < projectiles.size(); i++) {
            if (projectiles.get(i).updateCurrentPosition(PROJECTILE_SPEED_SCALE)) {
                removableProjectile.add(projectiles.get(i));
            }
        }

        //Detecta colisão entre projétil e gato
        for (int i = 0; i < cats.size(); i++) {
            for (int j = 0; j < projectiles.size(); j++) {
                //Averigua colisão
                if (cats.get(i).getState() == Cat.START_STATE) {
                    if (cats.get(i).getSprite().getBoundingRectangle().overlaps(projectiles.get(j).getSprite().getBoundingRectangle())) {
                        cats.get(i).setState(Cat.MOVING_STATE);
                        projectiles.remove(j);
                    }
                }
            }
        }

        //Remove-se os projéteis que já alcançaram seu destino final
        for (int i = 0; i < removableProjectile.size(); i++) {
            int cat = Cat.getCat(cats, removableProjectile.get(i).getFinalPosition());

            //Movimento gato
            if (cat >= 0) {
                cats.get(cat).setState(Cat.MOVING_STATE);
            }

            //Remove projétil
            if (projectiles.contains(removableProjectile.get(i))) {
                projectiles.remove(removableProjectile.get(i));
            }
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

        private final float CIRCLE_RADIUS = 1.5f;

        private Circle finalPointLimitCircle;

        public Object(Sprite sprite, Vector2 startPoint, Vector2 finalPoint) {
            this.sprite = sprite;
            currentPosition = new Vector2(startPoint);
            finalPosition = new Vector2(finalPoint);
            finalPointLimitCircle = new Circle();

            this.sprite.setPosition(currentPosition.x, currentPosition.y);
            finalPointLimitCircle.set(finalPosition.x, finalPosition.y, CIRCLE_RADIUS);
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

        public boolean updateCurrentPosition(float speed) {
            //Direção do projétil
            Vector2 direction = (new Vector2(finalPosition)).sub(currentPosition).nor();

            //Projétil caminha em direção ao destino
            currentPosition.mulAdd(direction, speed);
            sprite.setPosition(currentPosition.x, currentPosition.y);

            if (rectAndCircleOverlap(sprite.getBoundingRectangle(), finalPointLimitCircle)) {
                currentPosition.set(finalPosition);
                return true;
            }
            return false;
        }

        /**
         * Verifica se um círculo e um retângulo colidem.
         *
         * @param c círculo
         * @param r retângulo
         * @return true se há colisão, false caso contrário
         */
        public static final boolean rectAndCircleOverlap(Rectangle r, Circle c) {
            //Vetor que liga os centros das figuras geométricas
            Vector2 vectorConnectingCenters = new Vector2((r.x + r.width / 2) - c.x, (r.y + r.height / 2) - c.y);
            //Vector interno ao retângulo, paralelo ao eixo Y e de comprimento ALTURA/2
            Vector2 vectorYAxisRectangle = new Vector2(0, (r.y + r.height) - (r.y + r.height / 2));

            //Distância unidimensional entre as coordenadas Y do retângulo e do círculo
            float distanceYBetweenCenters = (new Vector2(0, (r.y + r.height / 2) - c.y)).len();

            //Limita-se o tamanho do vetor 'vectorYAxisRectangle' garantindo que ele está dentro do retângulo & 
            //não ultrapassa a altura em relação ao centro do círculo
            Vector2 vectorClampedY = vectorYAxisRectangle.clamp(0, Math.min(distanceYBetweenCenters, r.height / 2));

            //Se houver colisão, a subtração de vetores abaixo resultará em um vetor paralelo ao eixo X
            if (vectorConnectingCenters.sub(vectorClampedY).len() <= r.height / 2 + c.radius) {
                return true; //Utliza-se altura pois o retângulo do tiro está em pé
            }
            return false;
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

        public static int getCat(ArrayList<Cat> cats, Vector2 finalPosition) {
            for (int i = 0; i < cats.size(); i++) {
                if (cats.get(i).getCurrentPosition().equals(finalPosition)) {
                    return i;
                }
            }

            return -1;
        }

        public int getState() {
            return state;
        }

        public static int howManyStartStates(ArrayList<Cat> cats) {
            int startState = 0;

            for (int i = 0; i < cats.size(); i++) {
                if (cats.get(i).getState() == START_STATE) {
                    startState++;
                }
            }

            return startState;
        }

        public void setState(int state) {
            this.state = state;
        }

        public boolean updateCurrentPosition(float speed) {
            if (this.state == MOVING_STATE) {
                return super.updateCurrentPosition(speed);
            }
            return false;
        }
    };
}
