package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashMap;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class CatchThatHomework extends MiniGame {

    private Texture catTexture;
    private Cat cat;

    public CatchThatHomework(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        catTexture = assets.get("catch-that-homework/cat-spritesheet.png", Texture.class);
        
        TextureRegion[][] frames = TextureRegion.split(catTexture,
                Cat.FRAME_WIDTH, Cat.FRAME_HEIGHT);
        
        cat = new Cat(frames[0][0], 0 + 200);
        cat.setCenter(
            viewport.getWorldWidth() / 2f,
            cat.height);
        cat.setScale(5);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
    }

    @Override
    public void onHandlePlayingInput() {
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        cat.setCenter(click.x, cat.height);
    }

    @Override
    public void onUpdate(float dt) {
    }

    @Override
    public void onDrawGame() {
        this.cat.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Catch that homework!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Cat extends Sprite {
        private final int lives = 1;
        private final float height;

        static final int FRAME_WIDTH = 50;
        static final int FRAME_HEIGHT = 50;

        public Cat(TextureRegion texture, float height) {
            super(texture);
            this.height = height;
        }
    }
}
