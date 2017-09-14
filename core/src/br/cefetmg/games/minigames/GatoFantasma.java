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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
public class GatoFantasma extends MiniGame {

    private Array<Sprite> enemies;
    private Sprite target;
    private Texture catsTexture;
    private Texture targetTexture;
    private Texture fundoTexture;
    private int enemiesKilled;
    private int spawnedEnemies;
    private int cont;
    private float initialEnemyScale;
    private int totalEnemies;
    private float spawnInterval;

    public GatoFantasma(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        cont = 0;
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
<<<<<<< HEAD
        Vector2 position = new Vector2(rand.nextInt() % (720 - 565) + 565f, 360);

        Sprite enemy = new Sprite(catsTexture);
=======
        Vector2 position = new Vector2(rand.nextInt()%(720-535)+535f,rand.nextInt()%(370-230)+230f);
        TextureRegion tr = new TextureRegion(catsTexture);
        TextureRegion t2 = new TextureRegion(catsTexture, tr.getRegionWidth()/2, tr.getRegionHeight());
        Sprite enemy = new Sprite(t2);
>>>>>>> cc0a0f0bd55a9b2deecf67ccd1a79743c39c07fb
        enemy.setPosition(position.x, position.y);
        enemy.setScale(initialEnemyScale);
        enemies.add(enemy);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.initialEnemyScale = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 0.05f, 0.25f);
        this.spawnInterval = DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.totalEnemies = (int) (20 * DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 0.f, 1.0f)) + 3;
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
<<<<<<< HEAD
            System.out.println("X:" + click.x + "   Y:" + click.y);
=======
             //       System.out.println("X:"+click.x+"   Y:"+click.y);
>>>>>>> cc0a0f0bd55a9b2deecf67ccd1a79743c39c07fb

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
            // diminui só até x% do tamanho da imagem
<<<<<<< HEAD
            if (sprite.getScaleY() < 2.0f) {
                System.out.println(sprite.getScaleY() + "enemies" + enemies.size);
=======
            if (sprite.getScaleY()<2.0f ) {
>>>>>>> cc0a0f0bd55a9b2deecf67ccd1a79743c39c07fb
                sprite.setScale(sprite.getScaleX() + 0.3f * dt);
            } else {
                //perdeu playboy
                challengeFailed();
            }
        }
        enemies.sort(new Comparator<Sprite>() {
            @Override
            public int compare(Sprite b1, Sprite b2) {
                //TODO testar nulos 
                return b1.getScaleX() > b2.getScaleX() ? -1 : b1.getScaleX() == b2.getScaleX() ? 0 : 1;
            }
        });
    }

    @Override
    public void onDrawGame() {
        batch.draw(fundoTexture, 0, 0);
        for (int i = enemies.size - 1; i >= 0; i--) {
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
