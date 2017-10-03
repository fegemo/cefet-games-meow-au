package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * @author Kael
 */
public class CannonCat extends MiniGame {

    private Texture background;
    private Texture cat, cookie;
    public int cannonDirectionIndex = 0;
    public int remainingShots;
    private int remainingCats;
    public int[] cat_x = new int[NUMBER_OF_CATS];
    public int[] cat_y = new int[NUMBER_OF_CATS];
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

        for (int i = 0; i < NUMBER_OF_CATS; i++) {
            cat_x[i] = (int) (200 * Math.cos((i * Math.PI) / 4)) + 1280 / 2;
            cat_y[i] = (int) ((-1) * 200 * Math.sin((i * Math.PI) / 4)) + 720 / 2;
        }
        for (int i = 0; i < MAX_AMMO; i++) {
            cookie_x[i] = 300;
            cookie_y[i] = 200 + (-1) * i * 20;
        }
        remainingShots = MAX_AMMO;
        remainingCats = NUMBER_OF_CATS;
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        //velocidade = 1+3*difficulty;
        this.velocidade = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 0, 1);
    }

    @Override
    public void onHandlePlayingInput() {
        if (Gdx.input.justTouched()) {

            if (remainingShots > 0) {
                remainingShots--;
                if (cat_x[cannonDirectionIndex] != 0) {
                    cat_x[cannonDirectionIndex] = 0;
                    remainingCats--;
                }
                if (remainingCats == 0) {
                    super.challengeSolved();
                } else if (remainingShots == 0) {
                    super.challengeFailed();
                }
            } else {
                super.challengeFailed();
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
    }

    @Override
    public void onDrawGame() {
        batch.draw(background, 0, 0);
        for (int i = 0; i < NUMBER_OF_CATS; i++) {
            batch.draw(cat, cat_x[i], cat_y[i]);
        }
        for (int i = 0; i < MAX_AMMO; i++) {
            batch.draw(cookie, cookie_x[i], cookie_y[i]);
        }
        //Desenham as posições do canhão de maneira a girar no sentido horário
        Direction currentDirection = Direction.valueOf(cannonDirectionIndex);
        batch.draw(cannonTextures.get(currentDirection), center_x, center_y);
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
    }
}
