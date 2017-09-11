/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 *
 * @author Alberto
 */
public class GatoFantasma extends MiniGame{
    
    private Array<Sprite> enemies;
    private Sprite target;
    private Texture catsTexture;
    private Texture targetTexture;
    private Texture fundoTexture;
    private int enemiesKilled;
    private int spawnedEnemies;

    private float initialEnemyScale;
    private float maxEnemyScale;
    private int totalEnemies;
    private float spawnInterval;
    
    public GatoFantasma(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
         enemies = new Array<Sprite>();
        catsTexture = assets.get(
                "gato-fantasma/gato-fantasma.png", Texture.class);
        targetTexture = assets.get(
                "gato-fantasma/target.png", Texture.class);
        fundoTexture = assets.get(
                "gato-fantasma/fundo.jpg", Texture.class);
        target = new Sprite(targetTexture);
        target.setOriginCenter();
        enemiesKilled = 0;
        spawnedEnemies = 0;
        scheduleEnemySpawn();
    }

    private void scheduleEnemySpawn() {
        Timer.Task t = new Timer.Task() {
            @Override
            public void run() {
                spawnEnemy();
                if (++spawnedEnemies < totalEnemies) {
                    scheduleEnemySpawn();
                }
            }
        };
        // spawnInterval * 15% para mais ou para menos
        float nextSpawnMillis = this.spawnInterval
                * (rand.nextFloat() / 3 + 0.15f);
        timer.scheduleTask(t, nextSpawnMillis);
    }
    private void spawnEnemy() {
        // pega x e y entre 0 e 1
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        // multiplica x e y pela largura e altura da tela
        position.scl(
                viewport.getWorldWidth() - catsTexture.getWidth()
                * initialEnemyScale,
                viewport.getWorldHeight()
                - catsTexture.getHeight() * initialEnemyScale);

        Sprite enemy = new Sprite(catsTexture);
        enemy.setPosition(position.x, position.y);
        enemy.setScale(initialEnemyScale);
        enemies.add(enemy);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.initialEnemyScale = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 1.15f, 0.8f);
        this.maxEnemyScale = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.15f, 0.4f);
        this.spawnInterval = DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.totalEnemies = (int) Math.ceil(maxDuration / spawnInterval) - 3;
    }

    @Override
    public void onHandlePlayingInput() {
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
    }

    @Override
    public void onUpdate(float dt) {
        for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            // aumenta só até x% do tamanho da imagem
            if (sprite.getScaleX() < maxEnemyScale) {
                sprite.setScale(sprite.getScaleX() + 0.3f * dt);
            }
        }
        enemies.sort(new Comparator<Sprite>() {
            @Override
            public int compare(Sprite b1, Sprite b2) {
                //TODO testar nulos 
                return b1.getScaleX()>b2.getScaleX()?-1:b1.getScaleX()==b2.getScaleX()?0:1;
            }
        });
    }

    
    @Override
    public void onDrawGame() {

        for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            sprite.draw(batch);
        }
        target.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Detenha os gatos fantasmas";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
}
