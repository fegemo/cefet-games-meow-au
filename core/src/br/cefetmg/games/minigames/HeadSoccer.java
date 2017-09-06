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

/**
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class HeadSoccer extends MiniGame {
    private Texture backgroundTexture;
    private Texture catTexture;
    private Texture ballTexture;
    private Texture goalLeftTexture;
    private Texture goalRightTexture;
    private Player cat;
    private Sprite background;
    private Sprite ball;
    private Sprite goalLeft;
    private Sprite goalRight;

    public HeadSoccer(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        backgroundTexture = assets.get("head-soccer/Arena.png", Texture.class);
        catTexture = assets.get("head-soccer/cat.png", Texture.class);
        ballTexture = assets.get("head-soccer/ball.png", Texture.class);
        goalLeftTexture = assets.get("head-soccer/goalLeft", Texture.class);
        //goalRightTexture = assets.get("head-soccer/goalRight.png", Texture.class);
        background = new Sprite(backgroundTexture);
        ball = new Sprite(ballTexture);
        ball.setSize(100, 100);
        ball.setPosition(590, 81);
        //goalLeft = new Sprite(goalLeftTexture);
        //goalRight = new Sprite(goalRightTexture);
        cat = new Player(new Vector2(463.5f, 81f), new Vector2(30, 81), new Vector2(1235, 209), catTexture, batch, 3, 4, 100, 100);
        //scheduleEnemySpawn();
    }

    private void scheduleEnemySpawn() {
        Task t = new Task() {
            @Override
            public void run() {
                ;
            }
        };
    }

    
    @Override
    protected void configureDifficultyParameters(float difficulty) {
        /*
        this.initialEnemyScale = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 1.15f, 0.8f);
        this.minimumEnemyScale = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.15f, 0.4f);
        this.spawnInterval = DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.totalEnemies = (int) Math.ceil(maxDuration / spawnInterval) - 3;
        */
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
        ball.draw(batch);
        //goalLeft.draw(batch);
        //goalRight.draw(batch);
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

}
