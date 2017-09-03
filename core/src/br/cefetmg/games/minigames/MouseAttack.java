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
import com.badlogic.gdx.utils.Timer.Task;
import br.cefetmg.games.minigames.util.Cat;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author
 */
public class MouseAttack extends MiniGame {

    private Array<Sprite> enemies;
    private Texture catTexture;
    private Cat2 cat;


    private int enemiesKilled;
    private int spawnedEnemies;
    private Sprite target;
    private Texture cariesTexture;
    private Texture targetTexture;
    private Sound cariesAppearingSound;
    private Sound cariesDyingSound;
    private float initialEnemyScale;
    private float minimumEnemyScale;
    private int totalEnemies;
    private float spawnInterval;
    private float posX, posY;
    private float ScreenWidth;
    private float ScreenHeight;

    public MouseAttack(BaseScreen screen,
                       MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        
        catTexture = assets.get("MouseAttack/sprite-cat.png",Texture.class);

        cat = new Cat2(catTexture);
        
        ScreenHeight = Gdx.graphics.getHeight();
        ScreenWidth = Gdx.graphics.getWidth();
        posX = (float)(ScreenWidth*0.2);
        posY = (float)(ScreenHeight*0.5);
        cat.setPosition(50, 50);
        
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnEnemy();
            }

        }, 0, this.spawnInterval);

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
        /*Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
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
        cariesAppearingSound.play(0.5f);*/
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
        Vector3 Posi;
        Posi = new Vector3(posX, posY, 0);
        viewport.unproject(Posi);
        cat.setCenter(Posi.x, Posi.y);
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
        }*/
    }

    @Override
    public void onUpdate(float dt) {
        cat.update(dt);
        // vai diminuindo o tamanho das cáries existentes
        /*for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            // diminui só até x% do tamanho da imagem
            if (sprite.getScaleX() > minimumEnemyScale) {
                sprite.setScale(sprite.getScaleX() - 0.3f * dt);
            }
        }*/
    }

    @Override
    public String getInstructions() {
        return "Acerte as cáries";
    }

    @Override
    public void onDrawGame() {
        cat.draw(batch);
       /*for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            sprite.draw(batch);
        }
        target.draw(batch);*/
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
    class Cat2 extends AnimatedSprite{
        static final int FRAME_WIDTH = 50;
        static final int FRAME_HEIGHT = 50;
        TextureRegion[][] quadrosDaAnimacao;
        Texture spriteSheet;
        
        
        Animation chutar;
        
        public Cat2(final Texture cat) {
            
            /*this.spriteSheet = spriteSheet;
        
            spriteSheet = new Texture("MouseAttack/sprite-cat2.png");
            
            quadrosDaAnimacao = TextureRegion.split(spriteSheet, 50, 50);
            chutar = new Animation(0.1f,
            quadrosDaAnimacao[0][0], // 1ª linha, 1ª coluna
            quadrosDaAnimacao[0][1], // idem, 2ª coluna
            quadrosDaAnimacao[0][2],
            quadrosDaAnimacao[0][3]);*/
            
             super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            cat, 50, 50);
                    super.addAll(new TextureRegion[]{
                        frames[4][0],
                        frames[4][1],
                        frames[4][2],
                        frames[4][3],
                        frames[4][4],
                        frames[4][5],
                        frames[4][6],
                        frames[4][7],
                        frames[4][8]
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            //chutar.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            super.setAutoUpdate(false);
            //this.cat = cat;
        }

       
        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }
    }

}
