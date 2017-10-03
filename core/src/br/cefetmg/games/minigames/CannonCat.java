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
    public int i = 0;
    public int cannonDirectionIndex = 0;
    public int c = 0;
    public int tiro = 0;
    public int[] cat_x = new int[N_GATOS];
    public int[] cat_y = new int[N_GATOS];
    public int[] cookie_x = new int[MUNICAO];
    public int[] cookie_y = new int[MUNICAO];
    public int center_x = 1200 / 2;
    public int center_y = 700 / 2;
    public double velocidade = 1;
    public double dificuldade = 1;
    public double tempo1 = System.currentTimeMillis();
    public double tempo2 = System.currentTimeMillis();
    public final static int MUNICAO = 10;
    public final static int N_GATOS = 8;
    private ObjectMap<Direction, Texture> cannonTextures;

    public CannonCat(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        background = assets.get("cannon-cat/background.png", Texture.class);

        cat = assets.get("cannon-cat/cat.png", Texture.class);
        cookie = assets.get("cannon-cat/biscoito.png", Texture.class);
        
        cannonTextures =  new ObjectMap<Direction, Texture>();
        cannonTextures.put(Direction.EAST, assets.get("cannon-cat/cannon_right.png", Texture.class));
        cannonTextures.put(Direction.SOUTH_EAST, assets.get("cannon-cat/cannon_down+right.png", Texture.class));
        cannonTextures.put(Direction.SOUTH, assets.get("cannon-cat/cannon_down.png", Texture.class));
        cannonTextures.put(Direction.SOUTH_WEST, assets.get("cannon-cat/cannon_down+left.png", Texture.class));
        cannonTextures.put(Direction.WEST, assets.get("cannon-cat/cannon_left.png", Texture.class));
        cannonTextures.put(Direction.NORTH_WEST, assets.get("cannon-cat/cannon_up+left.png", Texture.class));
        cannonTextures.put(Direction.NORTH, assets.get("cannon-cat/cannon_up.png", Texture.class));
        cannonTextures.put(Direction.NORTH_EAST, assets.get("cannon-cat/cannon_up+right.png", Texture.class));
                
        for (i = 0; i < N_GATOS; i++) {
            cat_x[i] = (int) (200 * Math.cos((i * Math.PI) / 4)) + 1280 / 2;
            cat_y[i] = (int) ((-1) * 200 * Math.sin((i * Math.PI) / 4)) + 720 / 2;
        }
        for (i = 0; i < MUNICAO; i++) {
            cookie_x[i] = 300;
            cookie_y[i] = 200 + (-1) * i * 20;
        }
        tiro = MUNICAO;
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        //velocidade = 1+3*difficulty;
        this.velocidade = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 0, 1);
    }

    @Override
    public void onHandlePlayingInput() {
        if (Gdx.input.justTouched()) {

            if (tiro > 0) {
                cat_x[cannonDirectionIndex] = 0;
                tiro--;
            } else {
                super.challengeFailed();
            }

            cookie_x[tiro] = -500;
        }
    }

    @Override
    public void onUpdate(float dt) {
        tempo2 = System.currentTimeMillis();
        if (tempo2 - tempo1 > (100 - velocidade * 50)) {
            tempo1 = System.currentTimeMillis();
            cannonDirectionIndex++;
            cannonDirectionIndex = cannonDirectionIndex % N_GATOS;
        }

        for (i = 0; i < N_GATOS; i++) {
            if (cat_x[i] == 0) {
                c++;
            }
        }
        if (c == N_GATOS) {
            super.challengeSolved();
        } else {
            c = 0;
        }
        if (tiro <= 0 && c < N_GATOS) {
            super.challengeFailed();
        }

    }

    @Override
    public void onDrawGame() {
        batch.draw(background, 0, 0);
        for (i = 0; i < N_GATOS; i++) {
            batch.draw(cat, cat_x[i], cat_y[i]);
        }
        for (i = 0; i < MUNICAO; i++) {
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
