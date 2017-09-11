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

/**
 *
 * @author Alberto
 */
public class GatoFantasma extends MiniGame{
    
    private Array<Sprite> enemies;
    private Sprite target;
    private Texture catsTexture;
    private Texture targetTexture;
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
                "", Texture.class);
        targetTexture = assets.get(
                "", Texture.class);
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
                viewport.getWorldWidth() - cariesTexture.getWidth()
                * initialEnemyScale,
                viewport.getWorldHeight()
                - cariesTexture.getHeight() * initialEnemyScale);

        Sprite enemy = new Sprite(cariesTexture);
        enemy.setPosition(position.x, position.y);
        enemy.setScale(initialEnemyScale);
        enemies.add(enemy);

        // toca um efeito sonoro
        cariesAppearingSound.play(0.5f);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.initialEnemyScale = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 1.15f, 0.8f);
        this.minimumEnemyScale = DifficultyCurve.LINEAR_NEGATIVE
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
    }

    @Override
    public void onUpdate(float dt) {
        // vai diminuindo o tamanho das cáries existentes
        for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            // diminui só até x% do tamanho da imagem
            if (sprite.getScaleX() < maxEnemyScale) {
                sprite.setScale(sprite.getScaleX() + 0.3f * dt);
            }
        }
        enemies.sort();
    }

    private boolean comp(Sprite a, Sprite b){ return a.getScaleX()>b.getScaleX();}
    
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
