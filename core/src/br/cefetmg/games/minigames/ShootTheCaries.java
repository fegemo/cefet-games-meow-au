package br.cefetmg.games.minigames;

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
import br.cefetmg.games.sound.MySound;

/**
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class ShootTheCaries extends MiniGame {

    private Array<Sprite> enemies;
    private Sprite target;
    private Texture cariesTexture;
    private Texture targetTexture;
    private MySound cariesAppearingSound;
    private MySound cariesDyingSound;
    private int enemiesKilled;
    private int spawnedEnemies;

    private float initialEnemyScale;
    private float minimumEnemyScale;
    private int totalEnemies;
    private float spawnInterval;

    public ShootTheCaries(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        enemies = new Array<Sprite>();
        cariesTexture = assets.get(
                "shoot-the-caries/caries.png", Texture.class);
        targetTexture = assets.get(
                "shoot-the-caries/target.png", Texture.class);
        cariesAppearingSound = new MySound(assets.get(
                "shoot-the-caries/caries1.mp3", Sound.class));
        cariesDyingSound = new MySound(assets.get(
                "shoot-the-caries/caries2.mp3", Sound.class));
        target = new Sprite(targetTexture);
        target.setOriginCenter();
        enemiesKilled = 0;
        spawnedEnemies = 0;
        scheduleEnemySpawn();
    }

    private void scheduleEnemySpawn() {
        Task t = new Task() {
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
            if (sprite.getScaleX() > minimumEnemyScale) {
                sprite.setScale(sprite.getScaleX() - 0.3f * dt);
            }
        }
    }

    @Override
    public String getInstructions() {
        return "Acerte as cáries";
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
    public boolean shouldHideMousePointer() {
        return true;
    }

}