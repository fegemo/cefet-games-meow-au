package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static java.lang.Math.max;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author
 */
public class MouseAttack extends MiniGame {

    private Array<Sprite> enemies;
    private Texture catTexture;
    private Cat2 cat;

    private Texture mosterTexture;
    private Monster monster;

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
    
    private static final int NUMBER_OF_TILED_BACKGROUND_TEXTURE = 7;
    private TextureRegion background;

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
        
        background = new TextureRegion(new Texture("menu-background.png"));
        // configura a textura para repetir caso ela ocupe menos espaço que o
        // espaço disponível
        background.getTexture().setWrap(
                Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // define a largura da região de desenho de forma que ela seja repetida
        // um número de vezes igual a NUMBER_OF_TILED_BACKGROUND_TEXTURE 
        background.setRegionWidth(
                background.getTexture().getWidth()
                * NUMBER_OF_TILED_BACKGROUND_TEXTURE);
        // idem para altura, porém será repetida um número de vezes igual a 
        // NUMBER_OF_TILED_BACKGROUND_TEXTURE * razãoDeAspecto
        background.setRegionHeight(
                (int) (background.getTexture().getHeight()
                * NUMBER_OF_TILED_BACKGROUND_TEXTURE
                / Config.DESIRED_ASPECT_RATIO));
        
        
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
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        // multiplica x e y pela largura e altura da tela
        position.scl(
                viewport.getWorldWidth() - mosterTexture.getWidth()
                * initialEnemyScale,
                viewport.getWorldHeight()
                - mosterTexture.getHeight() * initialEnemyScale);

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
        System.out.println("chegou aqui1");
        cat.update(dt);
        cat.update();
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
        return "Mate todos os monstros!";
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
        
        float tempoDaAnimacao;
        
        Animation power;        
        Animation socar;
        Animation chutar;
        Animation morrer;
        Animation parado;
        
        int x = 0;
        
        public Cat2(final Texture cat) {
            
             super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            cat, 50, 50);
                    super.addAll(new TextureRegion[]{
                        frames[4][0]
                    });
                }
            }));
            
            quadrosDaAnimacao = TextureRegion.split(cat, 50, 50);
            
            chutar = new Animation(0.1f,
            quadrosDaAnimacao[4][0], 
            quadrosDaAnimacao[4][1], 
            quadrosDaAnimacao[4][2],
            quadrosDaAnimacao[4][3],
            quadrosDaAnimacao[4][4],
            quadrosDaAnimacao[4][5],
            quadrosDaAnimacao[4][6],
            quadrosDaAnimacao[4][7],
            quadrosDaAnimacao[4][8],
            quadrosDaAnimacao[4][9]);
            
            power = new Animation(0.1f,
            quadrosDaAnimacao[1][0], 
            quadrosDaAnimacao[1][1], 
            quadrosDaAnimacao[1][2],
            quadrosDaAnimacao[1][3],
            quadrosDaAnimacao[1][4],
            quadrosDaAnimacao[1][5]);
            
            morrer = new Animation(0.1f,
            quadrosDaAnimacao[3][0], 
            quadrosDaAnimacao[3][1], 
            quadrosDaAnimacao[3][2],
            quadrosDaAnimacao[3][3]);
            
            parado = new Animation(0.1f,
            quadrosDaAnimacao[0][0]);
            
            socar = new Animation(0.1f,
            quadrosDaAnimacao[5][5], 
            quadrosDaAnimacao[5][6], 
            quadrosDaAnimacao[5][7],
            quadrosDaAnimacao[5][8],
            quadrosDaAnimacao[5][9]);
            
            
            //super.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
            //chutar.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            //super.setAutoUpdate(false);
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
        
        public void changeAnimation(){
            
            x++;
            
            if(x%2==0)
                super.setAnimation(power);
            else
                super.setAnimation(parado);
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            
            
            
           
            
        }
        public void update(){
            
            tempoDaAnimacao += Gdx.graphics.getDeltaTime();
           if(Gdx.input.isButtonPressed(Buttons.LEFT)){
                 Gdx.input.setInputProcessor(new InputAdapter() {
                 public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                     if (button == Buttons.LEFT) {
                         changeAnimation();
                         return true;
                     }
                     return false;
                 }
             });
           }
            
        /*if (Gdx.input.) {
            super.setAnimation(power);
                  
        }
        else{
        }*/
        }
    }

}
