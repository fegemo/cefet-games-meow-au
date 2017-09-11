package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.collision.Collidable;
import br.cefetmg.games.minigames.Objects.Fish;
import br.cefetmg.games.minigames.Objects.MemoryChip;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.Random;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;

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

    private static int MAX_CHIPS;

    private static float diff;

    public SpyFish(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10000, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);

        this.texturaFish = assets.get("spy-fish/fish.png", Texture.class);
        this.texturaFundo = assets.get("spy-fish/fundo.png", Texture.class);
        this.texturaMemoCard = assets.get("spy-fish/card.png", Texture.class);
        this.fish = new Fish(texturaFish);

        batch = new SpriteBatch();

        diff = difficulty;

    }

    @Override
    protected void challengeSolved() {
        super.challengeSolved();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onStart() {
        if (diff == 0.047425874f) {

            chip = new ArrayList<MemoryChip>();
            MAX_CHIPS = 5;
            for (int i = 0; i < MAX_CHIPS; i++) {
                chip.add(new MemoryChip(texturaMemoCard));
            }
        } else {
            chip = new ArrayList<MemoryChip>();
            MAX_CHIPS = 100;
            for (int i = 0; i < MAX_CHIPS; i++) {
                chip.add(new MemoryChip(texturaMemoCard));
            }
        }
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

        //throw new UnsupportedOperationException("Not supported yet."); 
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onHandlePlayingInput() {
        /*Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        if(Gdx.input.isTouched())
            control.update(click);
        else
            control.update();
         */// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onUpdate(float dt) {
        /*fish.update(dt);
        memoryCard.update();*/

    }

    @Override
    public void onDrawGame() {

        update(Gdx.graphics.getDeltaTime());
        batch.begin();
        batch.draw(texturaFish, 0, 0);
        batch.draw(texturaFundo, 100, 100);
        this.fish.draw(batch);
        for (MemoryChip chip : chip) {
            chip.render(batch, this.fish);
        }
        batch.end();

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
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}