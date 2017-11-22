package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import java.util.Comparator;

/**
 *
 * @author Luiza-Pedro
 */
public class PhantomCat extends MiniGame {

    private Array<Sprite> enemies;
    private Sprite target;
    private Texture catsTexture;
    private Texture targetTexture;
    private Texture fundoTexture;
    private MySound dieCat;
    private MyMusic fundo;
    private int enemiesKilled;
    private int spawnedEnemies;
    private float initialEnemyScale;
    private int totalEnemies;
    private float spawnInterval;
    //variavel que define se o jogo acabou ou não
    //true acabou
    //false não acabou ainda
    private boolean END;

    public PhantomCat(BaseScreen screen, MiniGameStateObserver observer,
            float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        enemies = new Array<Sprite>();
        catsTexture = assets.get(
                "phantom-cat/gato-fantasma.png", Texture.class);
        targetTexture = assets.get(
                "phantom-cat/target.png", Texture.class);
        fundoTexture = assets.get(
                "phantom-cat/fundo.jpg", Texture.class);
        dieCat = new MySound( Gdx.audio.newSound(Gdx.files.internal("phantom-cat/cat.mp3")));
        fundo =  new MyMusic(Gdx.audio.newMusic(Gdx.files.internal("phantom-cat/fundo.mp3")));
        fundo.play();
        fundo.setVolume(.2f);
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
        Vector2 position = new Vector2(
                rand.nextInt() % (720 - 535) + 535f,
                rand.nextInt() % (370 - 230) + 230f);
        TextureRegion tr = new TextureRegion(catsTexture);
        TextureRegion t2 = new TextureRegion(catsTexture,
                tr.getRegionWidth() / 2,
                tr.getRegionHeight());
        Sprite enemy = new Sprite(t2);
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
        this.totalEnemies = (int) (20 * DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 0.f, 1.0f)) + 3;
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
            for (int i = 0; i < enemies.size; i++) {
                Sprite sprite = enemies.get(i);
                // se há interseção entre o retângulo da sprite e do alvo,
                // o tiro acertou

                if (sprite.getBoundingRectangle().overlaps(
                        target.getBoundingRectangle())) {
                    dieCat.play(0.05f);
                    // contabiliza um inimigo morto
                    this.enemiesKilled++;
                    // remove o inimigo do array
                    this.enemies.removeValue(sprite, true);
                    // se tiver matado todos os inimigos, o desafio
                    // está resolvido
                    if (this.enemiesKilled >= this.totalEnemies) {
                        this.END = true;
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
            if (sprite.getScaleY() < 2.0f) {
                sprite.setScale(sprite.getScaleX() + 0.3f * dt);
            } else {
                this.END = true;
                challengeFailed();
            }
        }
        enemies.sort(new Comparator<Sprite>() {
            @Override
            public int compare(Sprite b1, Sprite b2) {
                //TODO testar nulos 
                return b1.getScaleX() > b2.getScaleX()
                        ? -1
                        : b1.getScaleX() == b2.getScaleX()
                                ? 0
                                : 1;
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
        
        if (isPaused()) {
            //pause na musica
            fundo.pause();
        }else if ( !isPaused() && !this.END){
            //resume na musica
            fundo.play();
        }else{
            // stop na musica no fim do jogo
            fundo.stop();
        }

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
