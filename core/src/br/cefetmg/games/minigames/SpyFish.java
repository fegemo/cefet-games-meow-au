package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.collision.Collidable;
import br.cefetmg.games.minigames.Objects.Fish;
import br.cefetmg.games.minigames.Objects.MemoryChip;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import static br.cefetmg.games.screens.BaseScreen.camera;
import com.badlogic.gdx.Gdx;
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

    private ArrayList<MemoryChip> chip;

    private static SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    //elementos de logica
    private Fish fish;

    private int MAX_CHIPS;

    public SpyFish(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 20f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);

        this.texturaFish = assets.get("spy-fish/fish.png", Texture.class);
        this.texturaFundo = assets.get("spy-fish/fundo.png", Texture.class);
        this.texturaMemoCard = assets.get("spy-fish/card.png", Texture.class);

        this.fish = new Fish(texturaFish);

        batch = new SpriteBatch();

    }

    @Override
    protected void challengeSolved() {
        super.challengeSolved();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onStart() {
        chip = new ArrayList<MemoryChip>();
        MAX_CHIPS = 5;
        for (int i = 0; i < MAX_CHIPS; i++) {
            chip.add(new MemoryChip(texturaMemoCard));
        }
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

        //throw new UnsupportedOperationException("Not supported yet."); 
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onHandlePlayingInput() {
        // move o peixe
        this.fish.updateAccordingToTheMouse(getMousePosInGameWorld().x, getMousePosInGameWorld().y);
    }

    @Override
    public void onUpdate(float dt) {

        for (Iterator<MemoryChip> iterator = chip.iterator(); iterator.hasNext();) {
            MemoryChip mc = iterator.next();
            mc.update(dt);
            if (mc.collidesWith(this.fish)) {
                //se o peixe colidir com o cartão de memoria
                iterator.remove();
            }
        }

    }

    @Override
    public void onDrawGame() {

        update(Gdx.graphics.getDeltaTime());
        batch.begin();

        this.fish.render(batch, getMousePosInGameWorld().x, getMousePosInGameWorld().y);

        for (MemoryChip chip : chip) {
            chip.render(batch);
        }
        batch.end();

        this.fish.render_area_collision();
        for (MemoryChip chip : this.chip) {
            //mostra os circulos de colisão
            chip.render_area_collision();
        }

        System.out.println(this.chip);

    }

    @Override
    public String getInstructions() {
        return "Pegue o cartão de memória";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Vector3 getMousePosInGameWorld() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }
}