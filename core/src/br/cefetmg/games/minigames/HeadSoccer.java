package br.cefetmg.games.minigames;

import br.cefetmg.games.Bot;
import static br.cefetmg.games.Config.*;
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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import java.util.ArrayList;
import java.util.Iterator;

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
    private Ball ball;
    private Bot bot;
    private Player tmpCat;
    private Ball tmpBall;
    private Bot tmpBot;
    private Sprite background;
    private Sprite goalLeft;
    private Sprite goalRight;
    private ArrayList objects;

    public class Ball {

        private Texture ballTexture;
        private Sprite ball;
        private Circle circle;
        private Vector2 speed;
        private SpriteBatch batch;
        private float mass;
        
        public Ball(SpriteBatch batch) {
            this.batch = batch;
            
            ballTexture = assets.get("head-soccer/ball.png", Texture.class);
            ball = new Sprite(ballTexture);
            ball.setSize(60, 60);
            ball.setPosition(590, FLOOR);
            
            circle = new Circle(620, 111, 30);
            speed = new Vector2(0, 0);
            mass = 1;
        }

        public Vector2 getPositionBall() {
            Vector2 position = new Vector2(ball.getX(), ball.getY());
            return position;
        }

        public void setPositionBall(float x, float y) {
            ball.setPosition(x, y);
            circle.setPosition(x + circle.radius, y + circle.radius);
        }
        
        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }
        
        public void actionGravity(float value) {
            speed.set(speed.x, speed.y - value);
        }
        
        public void draw(){
            ball.draw(batch);
        }

    }

    public HeadSoccer(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        
        backgroundTexture = assets.get("head-soccer/Arena.png", Texture.class);
        goalLeftTexture = assets.get("head-soccer/goalLeft.png", Texture.class);
        goalRightTexture = assets.get("head-soccer/goalRight.png", Texture.class);
        catTexture = assets.get("head-soccer/cat.png", Texture.class);
        
        goalLeft = new Sprite(goalLeftTexture);
        goalRight = new Sprite(goalRightTexture);
        background = new Sprite(backgroundTexture);
        
        goalLeft.setPosition(INITIALXLEFTGOAL, INITIALYGOAL);
        goalRight.setPosition(INITIALXRIGHTGOAL, INITIALYGOAL);

        objects = new ArrayList();
        ball = new Ball(batch);
        cat = new Player(new Vector2(463.5f, FLOOR), new Vector2(30, FLOOR), new Vector2(1245, 209),
                catTexture, batch, 3, 4, 100, 100,10);
        
        objects.add(cat);
        objects.add(ball);
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
        for (Object o : objects) {
            if (o instanceof Ball) {
                tmpBall = (Ball) o;
                tmpBall.actionGravity(dt * GRAVITY);
            } else if (o instanceof Player) {
                tmpCat = (Player) o;
                tmpCat.actionGravity(dt * GRAVITY);
            } else if (o instanceof Bot) {
                tmpBot = (Bot) o;
            }

        }

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
        return "Faça um gol sem levar gol";
    }

    @Override
    public void onDrawGame() {
        background.draw(batch);
        goalLeft.draw(batch);
        goalRight.draw(batch);
        cat.draw();
        ball.draw();
        
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

}
