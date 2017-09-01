/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;
import br.cefetmg.games.Config;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author sarit
 */
public class TheFridgeGame extends MiniGame {
    
    private Texture backgroundTexture;
    private Texture fridgeTexture;
    private Fridge fridge;
   
    public TheFridgeGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 100f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        // objetos de textura
        this.backgroundTexture = screen.assets.get("the-fridge-game/fridge-game-background.png", Texture.class);
        this.fridgeTexture = screen.assets.get("the-fridge-game/open-fridge.png", Texture.class);
        
        // instancias das subclasses da fase
        fridge = new Fridge(fridgeTexture);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        /*
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 120, 220);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 240, 340);
        */
    }

    @Override
    public void onHandlePlayingInput() {
        // obtem a posição do mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        
    }

    @Override
    public void onUpdate(float dt) {
        
    }

    @Override
    public void onDrawGame() {
        fridge.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Pegue o Máximo de Comida que Puder";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
    
    class Fridge extends AnimatedSprite {

        static final int FRAME_WIDTH = 467;
        static final int FRAME_HEIGHT = 547;

        Fridge(final Texture catTexture) {
            super(new Animation(1.0f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            catTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0]
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
            
            setScale(0.5f);
        }

        Vector2 getPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getPosition().dst(enemyX, enemyY);
        }
    }
}
