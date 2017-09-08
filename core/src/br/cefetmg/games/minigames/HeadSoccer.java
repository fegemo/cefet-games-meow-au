package br.cefetmg.games.minigames;

import br.cefetmg.games.Colision;
import br.cefetmg.games.Bot;
import static br.cefetmg.games.Config.*;
import br.cefetmg.games.Obstacle;
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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class HeadSoccer extends MiniGame {

    private float floorBall, reflected;
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
    private Obstacle leftGoal;
    private Obstacle goalCrossLeft;
    private Obstacle rightGoal;
    private Obstacle goalCrossRight;
    private Obstacle backgroundLeft;
    private Obstacle backgroundRight;
    private Obstacle backgroundTop;
    private Obstacle backgroundDown;
    private Sprite background;
    private Sprite goalLeft;
    private Sprite goalRight;
    private ArrayList objects;
    private ArrayList obstacles;

    public class Ball {

        private Texture ballTexture;
        private Sprite ball;
        public boolean movingH;
        public boolean movingV;
        private Circle circle;
        public Vector2 speed;
        private float ballStep;
        private SpriteBatch batch;
        private float mass;
        private final float maxSpeed;

        public Ball(SpriteBatch batch, float maxSpeed, float ballStep) {
            this.batch = batch;
            ballTexture = assets.get("head-soccer/ball.png", Texture.class);
            ball = new Sprite(ballTexture);
            ball.setSize(60, 60);
            ball.setPosition(590, FLOOR);
            this.maxSpeed = maxSpeed;
            this.ballStep = ballStep;

            movingH = false;
            movingV = false;
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

        public float getWidht() {
            return ball.getWidth();
        }

        public void setSpeed(Vector2 speed) {
            float x, y;
            x = speed.x;
            y = speed.y;

            if (speed.x > maxSpeed) {
                x = maxSpeed;
            } else if (speed.x < -1 * maxSpeed) {
                x = -maxSpeed;
            }

            if (speed.y > maxSpeed) {
                y = maxSpeed;
            } else if (speed.y < -1 * maxSpeed) {
                y = -maxSpeed;
            }

            this.speed = speed.set(x, y);
        }

        public void actionGravity(float value) {
            speed.set(speed.x, speed.y - value);
        }

        public void updateMovement() {
            float x = ball.getX(), y = ball.getY();
            float propx = speed.x / maxSpeed;

            if (propx > 1) {
                propx = 1;
            } else if (propx < -1) {
                propx = -1;
            }

            float propy = speed.y / maxSpeed;

            if (propy > 1) {
                propy = 1;
            } else if (propy < -1) {
                propy = -1;
            }

            float ballStepx = ballStep * propx;
            float ballStepy = ballStep * propy;

            if (speed.x < 0) {
                if (x + ballStepx > 0) {
                    x += ballStepx;
                    movingH = true;
                }
            } else if (speed.x > 0) {
                if (x + ballStepx < WORLD_WIDTH - circle.radius) {
                    x += ballStepx;
                    movingH = true;
                }
            }

            if (y > FLOOR) {
                if (speed.y > 0 || speed.y < 0) {
                    y += ballStepy;
                    movingV = true;
                }
            } else if (y == FLOOR) {
                if (speed.y > 0) {
                    y += ballStepy;
                    movingV = true;
                }
            }

            if (y < FLOOR) {
                y = FLOOR;
            }
            if (speed.x > -0.2 && speed.x < 0.2) {
                speed.x = 0;
                movingH = false;
            }

            setPositionBall(x, y);
        }

        public void draw() {
            ball.draw(batch);
        }

        private void setSpeed(float x, float y) {
            this.speed.set(x, y);
        }

    }

    public HeadSoccer(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        reflected = 0.90f;
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

        ball = new Ball(batch, 8, 6);
        cat = new Player(new Vector2(463.5f, FLOOR), new Vector2(30, FLOOR), new Vector2(1245, 209),
                catTexture, batch, 3, 4, 100, 100, 2);

        objects.add(cat);
        objects.add(ball);

        int width = 7;
        int height = 7;
        int widthscreen = 1280;
        int heightscreen = 720;
        floorBall = 74;

        backgroundLeft = new Obstacle(batch, new Vector2(0, height + floorBall), width, heightscreen - width - floorBall);
        backgroundRight = new Obstacle(batch, new Vector2(widthscreen - width, height + floorBall), width, heightscreen - width - floorBall);
        backgroundTop = new Obstacle(batch, new Vector2(0, heightscreen - width), widthscreen, height);
        backgroundDown = new Obstacle(batch, new Vector2(0, floorBall), widthscreen, height);

        leftGoal = new Obstacle(batch, new Vector2(99, floorBall), width, 173);
        goalCrossLeft = new Obstacle(batch, new Vector2(0, 246), 127, height);
        rightGoal = new Obstacle(batch, new Vector2(842, floorBall), width, 173);
        goalCrossRight = new Obstacle(batch, new Vector2(1164, 264), 127, height);

    }

    public void verifyCollision(float dt) {
        if (Colision.rectCircleOverlap(cat.getSprite_Player().getBoundingRectangle(), ball.circle) != null) {
            ball.setSpeed(ball.getSpeed().add(cat.getSpeed()).scl(cat.getMass()));
        }
        if (Colision.rectCircleOverlap(backgroundDown.getRec(), ball.circle) != null) {
            if (ball.movingV) {
                ball.setSpeed(ball.getSpeed().x * reflected, ball.getSpeed().y * -reflected);
            } else if (ball.movingH) {
                if (ball.getSpeed().x > 0) {
                    ball.setSpeed(ball.getSpeed().add(-2.25f * dt, 0));
                } else {
                    ball.setSpeed(ball.getSpeed().add(2.25f * dt, 0));
                }
            }
        } else if (Colision.rectCircleOverlap(backgroundTop.getRec(), ball.circle) != null) {
            ball.setSpeed(ball.getSpeed().x * reflected, ball.getSpeed().y * -reflected);
        }

        if (Colision.rectCircleOverlap(backgroundLeft.getRec(), ball.circle) != null) {
            ball.setSpeed(ball.getSpeed().x * -reflected, ball.getSpeed().y * reflected);
        } else if (Colision.rectCircleOverlap(backgroundRight.getRec(), ball.circle) != null) {
            ball.setSpeed(ball.getSpeed().x * -reflected, ball.getSpeed().y * reflected);
        }

        if (Colision.rectCircleOverlap(goalCrossLeft.getRec(), ball.circle) != null) {
            float x = Colision.rectCircleOverlap(goalCrossLeft.getRec(), ball.circle).x;
            float y = Colision.rectCircleOverlap(goalCrossLeft.getRec(), ball.circle).y;
            if(goalCrossLeft.getRec().y + goalCrossLeft.getRec().height == y || goalCrossLeft.getRec().y == y)
                ball.setSpeed(ball.getSpeed().x * reflected, ball.getSpeed().y * -reflected);
            else{
                ball.setSpeed(ball.getSpeed().x * -reflected, ball.getSpeed().y * reflected);
            }
        } else if (Colision.rectCircleOverlap(leftGoal.getRec(), ball.circle) != null) {
            //Gol na esquerda
        } else if (Colision.rectCircleOverlap(goalCrossRight.getRec(), ball.circle) != null) {
            float x = Colision.rectCircleOverlap(goalCrossRight.getRec(), ball.circle).x;
            float y = Colision.rectCircleOverlap(goalCrossRight.getRec(), ball.circle).y;
            if(goalCrossRight.getRec().y + goalCrossRight.getRec().height == y || goalCrossRight.getRec().y == y)
                ball.setSpeed(ball.getSpeed().x * reflected, ball.getSpeed().y * -reflected);
            else{
                ball.setSpeed(ball.getSpeed().x * -reflected, ball.getSpeed().y * reflected);
            }
        } else if (Colision.rectCircleOverlap(rightGoal.getRec(), ball.circle) != null) {
            //Gol na Direita
        }

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
        verifyCollision(dt);
        ball.updateMovement();
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

    public void resize(int width, int height) {
        float widthscreen = viewport.getScreenWidth();
        float heightscreen = viewport.getScreenHeight();
        float diferenceWidth = width / widthscreen;
        float diferenceHeight = height / heightscreen;
        float w = 5 * diferenceWidth;
        float h = 5 * diferenceHeight;
        widthscreen = viewport.getScreenWidth() * diferenceWidth;
        heightscreen = viewport.getScreenHeight() * diferenceHeight;
        floorBall = 81 - Math.round(ball.getWidht() / 2);

        backgroundLeft = new Obstacle(batch, new Vector2(0, h + floorBall), w, heightscreen - w - floorBall);
        backgroundRight = new Obstacle(batch, new Vector2(widthscreen - w, h + floorBall), w, heightscreen - w - floorBall);
        backgroundTop = new Obstacle(batch, new Vector2(0, heightscreen - w), widthscreen, h);
        backgroundDown = new Obstacle(batch, new Vector2(0, floorBall), widthscreen, h);

        leftGoal = new Obstacle(batch, new Vector2(70 * diferenceWidth, floorBall), w, 120 * diferenceHeight);
        goalCrossLeft = new Obstacle(batch, new Vector2(0, 185 * diferenceHeight), 90 * diferenceWidth, h);
        rightGoal = new Obstacle(batch, new Vector2(842 * diferenceWidth, floorBall), w, 120 * diferenceHeight);
        goalCrossRight = new Obstacle(batch, new Vector2(827 * diferenceWidth, 185 * diferenceHeight), 90 * diferenceWidth, h);
        this.viewport.update(width, height, true);
    }

}
