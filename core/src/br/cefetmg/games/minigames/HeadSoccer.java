package br.cefetmg.games.minigames;

import br.cefetmg.games.Player;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import java.util.ArrayList;

/**
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */

public class HeadSoccer extends MiniGame {
    private Texture backgroundTexture;
    private Texture catTexture;
    private Texture goalLeftTexture;
    private Texture goalRightTexture;
    private Player cat;
    private Sprite background;
    private Sprite goalLeft;
    private Sprite goalRight;
    private float gravity;
    private ArrayList objects;

    
    public class Ball {
        private Texture ballTexture;
        private Sprite ball;
        
        public Ball() {
            ballTexture = assets.get("head-soccer/ball.png", Texture.class);
            backgroundTexture = assets.get("head-soccer/Arena.png", Texture.class);
            ball = new Sprite(ballTexture);
            ball.setSize(100, 100);
            ball.setPosition(590, 81);
        }

        public Vector2 getPositionBall() {
            Vector2 position = new Vector2(ball.getX(), ball.getY());
            return position;
        }

        public void setPositionBall(float x, float y) {
           ball.setPosition(x, y);
        }
        
        
    }
    
    public HeadSoccer(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        objects = new ArrayList();
        catTexture = assets.get("head-soccer/cat.png", Texture.class);
        Ball ball = new Ball();
        goalLeftTexture = assets.get("head-soccer/goalLeft.png", Texture.class);
        goalRightTexture = assets.get("head-soccer/goalRight.png", Texture.class);
        background = new Sprite(backgroundTexture);
        
        
        goalLeft = new Sprite(goalLeftTexture);
        goalLeft.setPosition(-45, 75);
        goalRight = new Sprite(goalRightTexture);
        goalRight.setPosition(1135, 75);
        cat = new Player(new Vector2(463.5f, 81f), new Vector2(30, 81), new Vector2(1235, 209), catTexture, batch, 3, 4, 100, 100);
        objects.add(cat);
        gravity = 4;
    }
    
    @Override
    protected void configureDifficultyParameters(float difficulty) {

    }
    

    @Override
    public void onHandlePlayingInput() {
        /*
        // atualiza a posição do alvo de acordo com o mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        this.target.setPosition(click.x - this.target.getWidth() / 2,
                click.y - this.target.getHeight() / 2);

        // verifica se matou um inimigo
        if (Gdx.input.justTouched()) {
            // itera no array de inimigos
            for (int i = 0; i < enemies.size; i++) {
                Sprite sprite = enemies.get(i);
                // se há interseção entre o retângulo da sprite e do alvo,
                // o tiro acertou
                if (sprite.getBoundingRectangle().overlaps(
                        target.getBoundingRectangle())) {
                    // contabiliza um inimigo morto
                    this.enemiesKilled++;
                    // remove o inimigo do array
                    this.enemies.removeValue(sprite, true);
                    cariesDyingSound.play();
                    // se tiver matado todos os inimigos, o desafio
                    // está resolvido
                    if (this.enemiesKilled >= this.totalEnemies) {
                        super.challengeSolved();
                    }

                    // pára de iterar, porque senão o tiro pode pegar em mais
                    // de um inimigo
                    break;
                }
            }
        }
        */
    }

    @Override
    public void onUpdate(float dt) {
        //Gdx.graphics.getDeltaTime()
        /*
        // vai diminuindo o tamanho das cáries existentes
        for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            // diminui só até x% do tamanho da imagem
            if (sprite.getScaleX() > minimumEnemyScale) {
                sprite.setScale(sprite.getScaleX() - 0.3f * dt);
            }
        }
        */
        cat.updateMoviment();
    }

    @Override
    public String getInstructions() {
        return "Acerte as cáries";
    }

    @Override
    public void onDrawGame() {
        background.draw(batch);
        cat.draw();
        //ball.draw(batch);
        goalLeft.draw(batch);
        goalRight.draw(batch);
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

}
