package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.collision.Collidable;
import br.cefetmg.games.minigames.Objects.Fish;
import br.cefetmg.games.minigames.Objects.MemoryChip;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import static br.cefetmg.games.screens.BaseScreen.camera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import java.util.Random;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Luiza-Pedro
 */
public class SpyFish extends MiniGame {

    //texturas
    private Texture texturaFish;
    private Texture texturaFundo;
    private Texture texturaMemoCard;
    private Texture textureFishSheet;
    private Texture pointerTexture;

    private ArrayList<MemoryChip> chip;

    private static SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    //elementos de logica
    private Fish fish;

    private int MAX_CHIPS;

    public SpyFish(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10000f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);

        this.texturaFish = assets.get("spy-fish/fish.png", Texture.class);
        this.texturaMemoCard = assets.get("spy-fish/card.png", Texture.class);
        this.texturaFundo = assets.get("spy-fish/ocean.jpeg", Texture.class);
        this.textureFishSheet = assets.get("spy-fish/fishsheet.png", Texture.class);

        this.texturaFundo = assets.get("spy-fish/ocean.jpeg", Texture.class);
        this.pointerTexture = assets.get("spy-fish/pointer.png", Texture.class);

        batch = new SpriteBatch();

    }

    @Override
    protected void onStart() {
        chip = new ArrayList<MemoryChip>();

        for (int i = 0; i < this.MAX_CHIPS; i++) {
            chip.add(new MemoryChip(texturaMemoCard));
        }

        this.fish = new Fish(this.texturaFish);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

        this.MAX_CHIPS = (int) DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 2, 8);
    }

    @Override
    public void onHandlePlayingInput() {
        // move o peixe
        this.fish.updateAccordingToTheMouse(getMousePosInGameWorld().x, getMousePosInGameWorld().y);
        /*Vector3 mause = getMousePosInGameWorld();
        this.fish.updateAccordingToTheMouse(mause.x, mause.y);*/
    }

    @Override
    public void onUpdate(float dt) {
        for (Iterator<MemoryChip> iterator = chip.iterator(); iterator.hasNext();) {
            MemoryChip mc = iterator.next();

            
            if (mc.collidesWith(this.fish)) {
                //se o peixe pegar um cartão de memoria
                iterator.remove();

                if (this.chip.isEmpty()) {
                    // se pegar todos os chips ganha
                    //super.challengeSolved();
                }
            }

            if (mc.getPositionMemoryCard().y < 0) {
                //se um chip não for pego perde
                //super.challengeFailed();
                break;
            }

        }
    }

    private Vector3 getMousePosInGameWorld() {
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        return click;
    }

    @Override
    public void onDrawGame() {
        Vector3 mause = getMousePosInGameWorld();

        update(Gdx.graphics.getDeltaTime());
        batch.begin();
        batch.draw(texturaFundo, 0f, 0f, 1280f, 720f);

        this.fish.render(batch, getMousePosInGameWorld().x, getMousePosInGameWorld().y);
        this.fish.render(batch);
        this.fish.flip(true, false);
        for (MemoryChip chip : chip) {
            chip.render(batch);
        }
        batch.draw(pointerTexture, mause.x, mause.y);
        batch.end();

        this.fish.render_area_collision();
        for (MemoryChip chip : this.chip) {
            //mostra os circulos de colisão
            chip.render_area_collision();
        }
    }

    @Override
    public String getInstructions() {
        return "Pegue o cartão de memória";

    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }

}
