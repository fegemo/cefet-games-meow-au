package br.cefetmg.games.minigames;

import static br.cefetmg.games.Config.*;
import br.cefetmg.games.minigames.CatAvoider.Colision;
import br.cefetmg.games.minigames.CatAvoider.Obstacle;
import static br.cefetmg.games.minigames.HeadSoccer.Player.FLOOR;
import static br.cefetmg.games.minigames.HeadSoccer.Player.GRAVITY;
import static br.cefetmg.games.minigames.HeadSoccer.Player.INITIALXLEFTGOAL;
import static br.cefetmg.games.minigames.HeadSoccer.Player.INITIALXRIGHTGOAL;
import static br.cefetmg.games.minigames.HeadSoccer.Player.INITIALYGOAL;
import static br.cefetmg.games.minigames.HeadSoccer.Player.JUMP;
import static br.cefetmg.games.minigames.HeadSoccer.Player.convertToRad;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer.Task;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.sound.MyMusic;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class HeadSoccer extends MiniGame {

    private boolean goalP, goalB;
    private float floorBall, reflected, limit, lastCollisonTime, cheatBot;
    private Texture backgroundTexture;
    private Texture catTexture;
    private Texture goalLeftTexture;
    private Texture goalRightTexture;
    private Texture botTexture;
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
    private Rectangle player_Rect;
    private Sprite background;
    private Sprite goalLeft;
    private Sprite goalRight;
    private Sprite TraveD;
    private Sprite TraveE;
    private Sprite bottomE;
    private Sprite bottomD;
    private Sprite bottomJ;
    private Sprite bottomK;
    private ArrayList objects;
    private Rectangle bot_Rect;
    
    private boolean isOver;
    protected MyMusic backgroundMusic;

    public HeadSoccer(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        cheatBot = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 8f, 25f);
    }

    @Override
    protected void onStart() {
        reflected = 0.6f;
        goalP = false;
        goalB = false;
        lastCollisonTime = 0;
        backgroundTexture = assets.get("head-soccer/Arena.png", Texture.class);
        goalLeftTexture = assets.get("head-soccer/goalLeft.png", Texture.class);
        goalRightTexture = assets.get("head-soccer/goalRight.png", Texture.class);

        bottomE = new Sprite(assets.get("head-soccer/bottomL.png", Texture.class));
        bottomD = new Sprite(assets.get("head-soccer/bottomD.png", Texture.class));
        bottomJ = new Sprite(assets.get("head-soccer/bottomJ.png", Texture.class));
        bottomK = new Sprite(assets.get("head-soccer/bottomK.png", Texture.class));

        bottomE.setSize(100, 55);
        bottomE.setPosition(100, 0);
        bottomD.setSize(100, 55);
        bottomD.setPosition(270, 0);

        bottomJ.setSize(100, 55);
        bottomJ.setPosition(870, 0);
        bottomK.setSize(100, 55);
        bottomK.setPosition(1040, 0);

        botTexture = assets.get("head-soccer/cat2.png", Texture.class);
        catTexture = assets.get("head-soccer/cat1.png", Texture.class);

        TraveD = new Sprite(assets.get("head-soccer/TraveD.png", Texture.class));
        TraveE = new Sprite(assets.get("head-soccer/TraveE.png", Texture.class));
        goalLeft = new Sprite(goalLeftTexture);
        goalRight = new Sprite(goalRightTexture);
        background = new Sprite(backgroundTexture);

        goalLeft.setPosition(INITIALXLEFTGOAL, INITIALYGOAL);
        goalRight.setPosition(INITIALXRIGHTGOAL, INITIALYGOAL);

        TraveD = new Sprite(assets.get("head-soccer/TraveD.png", Texture.class));
        TraveE = new Sprite(assets.get("head-soccer/TraveE.png", Texture.class));
        TraveD.setPosition(INITIALXRIGHTGOAL, INITIALYGOAL);
        TraveE.setPosition(INITIALXLEFTGOAL, INITIALYGOAL);

        objects = new ArrayList();

        ball = new Ball(batch, 8, 6);
        cat = new Player(new Vector2(463.5f, FLOOR), new Vector2(30, FLOOR), new Vector2(1245, 209),
                catTexture, batch, 3, 4, 100, 100, 2);
        bot = new Bot(new Vector2(663.5f, FLOOR), new Vector2(30, FLOOR), new Vector2(1245, 209),
                botTexture, batch, 3, 4, 100, 100, 2);
        bot.changeDificulty(cheatBot);

        objects.add(cat);
        objects.add(ball);
        objects.add(bot);

        limit = 0.2f;
        int width = 12;
        int height = 12;
        int widthscreen = 1280;
        int heightscreen = 720;
        floorBall = 69;

        Texture obstacleTexture = assets.get("avoider/grey.png", Texture.class);
        backgroundLeft = new Obstacle(obstacleTexture, new Vector2(0, height + floorBall), width, heightscreen - width - floorBall);
        backgroundRight = new Obstacle(obstacleTexture, new Vector2(widthscreen - width, height + floorBall), width, heightscreen - width - floorBall);
        backgroundTop = new Obstacle(obstacleTexture, new Vector2(0, heightscreen - width), widthscreen, height);
        backgroundDown = new Obstacle(obstacleTexture, new Vector2(0, floorBall), widthscreen, height);

        leftGoal = new Obstacle(obstacleTexture, new Vector2(99, floorBall), width, 190);
        goalCrossLeft = new Obstacle(obstacleTexture, new Vector2(0, 284), 150, height);
        rightGoal = new Obstacle(obstacleTexture, new Vector2(1184, floorBall), width, 190);
        goalCrossRight = new Obstacle(obstacleTexture, new Vector2(1141, 284), 150, height);
        backgroundMusic =  new MyMusic(assets.get("head-soccer/soccer.mp3", Music.class));
    }

    private void finilizeGame() {
        Task t = new Task() {
            @Override
            public void run() {
                finishGame();
            }
        };
        timer.scheduleTask(t, 0.5f);
    }

    public void finishGame() {
        if (goalB) {
            super.challengeFailed();
            isOver = true;
        }
        if (goalP) {
            super.challengeSolved();
            isOver = true;
        }
    }

    public void verifyCollision(float dt) {
        player_Rect = cat.getSprite_Player().getBoundingRectangle();
        bot_Rect = bot.getSprite_Bot().getBoundingRectangle();
        if (lastCollisonTime != 0) {
            lastCollisonTime += dt;
        }
        if (lastCollisonTime > 0.05f) {
            lastCollisonTime = 0;
        }
        if (lastCollisonTime == 0) {
            //Colisão bot e player
            if (Colision.rectsOverlap(bot_Rect, player_Rect)) {
                float x = bot.speed.x;
                float y = bot.speed.y;
                if (bot.speed.x + cat.speed.x > bot.maxSpeed) {
                    bot.speed.set(bot.maxSpeed, bot.speed.y);
                } else if (bot.speed.x + cat.speed.x < -bot.maxSpeed) {
                    bot.speed.set(-bot.maxSpeed, bot.speed.y);
                } else {
                    bot.speed.add(cat.speed.x, 0);
                }

                if (cat.speed.x + x > cat.maxSpeed) {
                    cat.speed.set(cat.maxSpeed, cat.speed.y);
                } else if (cat.speed.x + x < -cat.maxSpeed) {
                    cat.speed.set(-cat.maxSpeed, cat.speed.y);
                } else {
                    cat.speed.add(bot.speed.x, 0);
                }
            }
            //Colisão pé jogador bola
            if (Colision.collideCircleWithRotatedRectangle(ball.circle, cat.sprite_Shoes.getBoundingRectangle(), cat.getRotation_angle())) {
                if (cat.movingFoot == true && cat.footDown == false) {
                    lastCollisonTime += dt;
                    float x = (float) (cat.kick_power * Math.cos(cat.getRotation_angle() * convertToRad));
                    float y = (float) Math.abs(cat.kick_power * Math.sin(cat.getRotation_angle() * convertToRad));
                    ball.speed.y = 0;
                    ball.speed.add(x, y);
                }
            }
            //Colisão jogador bola
            if (Colision.rectCircleOverlap(player_Rect, ball.circle) != null) {
                lastCollisonTime += dt;
                float x = Colision.rectCircleOverlap(player_Rect, ball.circle).x;
                float y = Colision.rectCircleOverlap(player_Rect, ball.circle).y;
                if (cat.walking) {
                    ball.setSpeed(ball.getSpeed().add(cat.getSpeed()).scl(cat.getMass()));
                } else if (y == player_Rect.y || y == player_Rect.y + player_Rect.height) {
                    ball.setSpeed(ball.getSpeed().x, ball.getSpeed().y * -1);
                } else {
                    ball.setSpeed(ball.getSpeed().x * -1, ball.getSpeed().y);
                }
            }

            //Colisão pé bot bola
            if (Colision.collideCircleWithRotatedRectangle(ball.circle, bot.sprite_Shoes.getBoundingRectangle(), bot.getRotation_angle())) {
                if (bot.movingFoot == true && bot.footDown == false) {
                    lastCollisonTime += dt;
                    float x = (float) (bot.kick_power * Math.cos(bot.getRotation_angle() * convertToRad));
                    float y = (float) Math.abs(bot.kick_power * Math.sin(bot.getRotation_angle() * convertToRad));
                    ball.speed.y = 0;
                    ball.speed.add(x, y);
                }
            }
            //Colisão bot bola
            if (Colision.rectCircleOverlap(bot_Rect, ball.circle) != null) {
                lastCollisonTime += dt;
                float x = Colision.rectCircleOverlap(bot_Rect, ball.circle).x;
                float y = Colision.rectCircleOverlap(bot_Rect, ball.circle).y;
                if (bot.walking) {
                    ball.setSpeed(ball.getSpeed().add(bot.getSpeed()).scl(bot.getMass()));
                } else if (y == bot_Rect.y || y == bot_Rect.y + bot_Rect.height) {
                    ball.setSpeed(ball.getSpeed().x, ball.getSpeed().y * -1);
                } else {
                    ball.setSpeed(ball.getSpeed().x * -1, ball.getSpeed().y);
                }
            }

            //Colisão chão e bola
            if (Colision.rectCircleOverlap(backgroundDown.getBounds(), ball.circle) != null) {
                lastCollisonTime += dt;
                if (ball.movingV) {
                    ball.setSpeed(ball.getSpeed().x * reflected, ball.getSpeed().y * -reflected);
                } else if (ball.movingH) {
                    if (ball.getSpeed().x > 0) {
                        ball.setSpeed(ball.getSpeed().add(-2.25f * dt, 0));
                    } else {
                        ball.setSpeed(ball.getSpeed().add(2.25f * dt, 0));
                    }
                }
            } //Colisão teto e bola
            else if (Colision.rectCircleOverlap(backgroundTop.getBounds(), ball.circle) != null) {
                lastCollisonTime += dt;
                ball.setSpeed(ball.getSpeed().x * reflected, ball.getSpeed().y * -reflected);
            }
            //Colisão lateral esquerda e bola
            if (Colision.rectCircleOverlap(backgroundLeft.getBounds(), ball.circle) != null) {
                lastCollisonTime += dt;
                ball.setSpeed(ball.getSpeed().x * -1, ball.getSpeed().y * reflected);
            }//Colisão lateral direita e bola
            else if (Colision.rectCircleOverlap(backgroundRight.getBounds(), ball.circle) != null) {
                lastCollisonTime += dt;
                ball.setSpeed(ball.getSpeed().x * -1, ball.getSpeed().y * reflected);
            }
            //Colisão travessão esquerdo e bola
            if (Colision.rectCircleOverlap(goalCrossLeft.getBounds(), ball.circle) != null) {
                lastCollisonTime += dt;
                float x = Colision.rectCircleOverlap(goalCrossLeft.getBounds(), ball.circle).x;
                float y = Colision.rectCircleOverlap(goalCrossLeft.getBounds(), ball.circle).y;

                if (goalCrossRight.getBounds().y + goalCrossRight.getBounds().height == y) {
                    ball.setSpeed(ball.getSpeed().x, ball.getSpeed().y * -1);
                } else if (goalCrossRight.getBounds().y == y) {
                    ball.setSpeed(ball.getSpeed().x, ball.getSpeed().y * -1);
                } else {
                    ball.setSpeed(ball.getSpeed().x * -1, ball.getSpeed().y);
                }
            }//Colisão gol esquerdo e bola 
            if (Colision.rectCircleOverlap(leftGoal.getBounds(), ball.circle) != null) {
                lastCollisonTime += dt;
                goalB = true;
                finilizeGame();
                //Gol na esquerda
            }
            //Colisão travessão direito e bola
            if (Colision.rectCircleOverlap(goalCrossRight.getBounds(), ball.circle) != null) {
                lastCollisonTime += dt;
                float x = Colision.rectCircleOverlap(goalCrossRight.getBounds(), ball.circle).x;
                float y = Colision.rectCircleOverlap(goalCrossRight.getBounds(), ball.circle).y;
                if (goalCrossRight.getBounds().y + goalCrossRight.getBounds().height == y) {
                    ball.setSpeed(ball.getSpeed().x, ball.getSpeed().y * -1);
                } else if (goalCrossRight.getBounds().y == y) {
                    ball.setSpeed(ball.getSpeed().x, ball.getSpeed().y * -1);
                } else {
                    ball.setSpeed(ball.getSpeed().x * -1, ball.getSpeed().y * 1);
                }
            }//Colisão gol esquerdo e bola 
            if (Colision.rectCircleOverlap(rightGoal.getBounds(), ball.circle) != null) {
                lastCollisonTime += dt;
                goalP = true;
                finilizeGame();
                //Gol na Direita
            }

            if (ball.getSpeed().x < limit && ball.getSpeed().x > -limit) {
                lastCollisonTime += dt;
                ball.setSpeed(0, ball.getSpeed().y);
            }
            if (ball.getSpeed().y < limit && ball.getSpeed().y > -limit) {
                lastCollisonTime += dt;
                ball.setSpeed(ball.getSpeed().x, 0);
            }
        }
    }

    @Override
    public void onHandlePlayingInput() {

        // atualiza a posição do alvo de acordo com o mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        if (Gdx.input.isTouched()) {
            if (click.x > bottomD.getX() && click.x < bottomD.getX() + bottomD.getWidth()) {
                if (click.y > bottomD.getY() && click.y < bottomD.getY() + bottomD.getHeight()) {
                    cat.right = true;
                }
            } else if (click.x > bottomE.getX() && click.x < bottomE.getX() + bottomE.getWidth()) {
                if (click.y > bottomE.getY() && click.y < bottomE.getY() + bottomE.getHeight()) {
                    cat.left = true;
                }
            }

            if (click.x > bottomJ.getX() && click.x < bottomJ.getX() + bottomJ.getWidth()) {
                if (click.y > bottomJ.getY() && click.y < bottomJ.getY() + bottomJ.getHeight()) {
                    cat.jump = true;
                }
            }
            if (click.x > bottomK.getX() && click.x < bottomK.getX() + bottomK.getWidth()) {
                if (click.y > bottomK.getY() && click.y < bottomK.getY() + bottomK.getHeight()) {
                    cat.kick = true;
                }
            }
        }

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
                tmpBot.actionGravity(dt * GRAVITY);
            }
        }

        cat.updateMoviment();
        cat.moveFoot(dt);
        bot.updateMovement(dt, new Vector2(ball.ball.getX(), ball.ball.getY()), new Vector2(cat.sprite_Player.getX(), cat.sprite_Player.getY()));
        bot.moveFoot(dt);
        verifyCollision(dt);
        ball.updateMovement();
    }

    @Override
    public String getInstructions() {
        return "Faça um gol sem levar gol";
    }

    @Override
    public void onDrawGame() {
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        background.draw(batch);
        goalLeft.draw(batch);
        goalRight.draw(batch);
        cat.draw();
        bot.draw();
        ball.draw();
        TraveD.draw(batch);
        TraveE.draw(batch);
        bottomE.draw(batch);
        bottomD.draw(batch);
        bottomJ.draw(batch);
        bottomK.draw(batch);
        
        if (isOver) {
            backgroundMusic.stop();
        }
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
    
        @Override
    protected void onEnd() {
        isOver = true;
        backgroundMusic.stop();
    }

    // <editor-fold desc="Classes internas da HeadSoccer" defaultstate="collapsed">
    public class Ball {

        private Texture ballTexture;
        public Sprite ball;
        public boolean movingH;
        public boolean movingV;
        private Circle circle;
        public Vector2 speed;
        private float ballStep;
        private SpriteBatch batch;
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

    class Bot {

        public boolean walking, up, down, left, right, orientation, footUp, footDown, movingFoot, jump;
        private float botStep, playerWidth, playerHeight;
        public Sprite sprite_Bot, sprite_Shoes;
        public Vector2 position, positionMin, positionMax, speed, footPosition;
        private Texture botTexture;
        private SpriteBatch batch;
        public float maxSpeed;
        private float mass, aTime;
        private float rotation_angle, initial_angle, final_angle, correctionX, correctionY, aCorrectionX, aCorrectionY;
        public float kick_power;
        private int moveAlone;
        private int oldOrientation;
        private float oldTime, decisionPeriod;

        public Bot(Vector2 positionInicial, Vector2 positionMin, Vector2 positionMax, Texture botTexture, SpriteBatch batch, float botStep, float maxSpeed, float sizeX, float sizeY, float mass) {
            this.speed = new Vector2(0, 0);
            this.walking = false;
            this.positionMin = positionMin;
            this.positionMax = positionMax;
            this.batch = batch;
            this.botStep = botStep;
            this.botTexture = botTexture;
            this.maxSpeed = maxSpeed;
            this.mass = mass;
            aTime = 0.3f;
            footUp = false;
            footDown = false;
            movingFoot = false;
            jump = false;
            kick_power = 20;
            oldTime = 0;
            decisionPeriod = 0.2f;
            this.sprite_Bot = new Sprite(botTexture);
            this.sprite_Bot.setSize(sizeX, sizeY);
            this.sprite_Bot.setFlip(false, false);
            this.sprite_Shoes = new Sprite(new Texture("head-soccer/shoes.png"));
            this.sprite_Shoes.setSize(0.7f * sizeX, 0.3f * sizeY);
            this.sprite_Shoes.setOriginCenter();
            this.sprite_Shoes.setRotation(27);
            this.sprite_Shoes.setFlip(true, false);
            this.initial_angle = -27;
            final_angle = 45;

            correctionY = -sprite_Shoes.getHeight() / 2;

            aCorrectionX = 0;
            aCorrectionY = 0;

            oldOrientation = 0;
            this.playerWidth = sprite_Bot.getWidth();
            this.playerHeight = sprite_Bot.getHeight();

            this.position = positionInicial;
            footPosition = new Vector2(positionInicial.x, positionInicial.y - sprite_Shoes.getHeight() / 2);

            orientation = false;//true= Direita false = left
            correctionX = sprite_Bot.getWidth() / 3;

            sprite_Bot.setPosition(positionInicial.x, positionInicial.y);
            sprite_Shoes.setPosition(footPosition.x, footPosition.y);
        }

        public Sprite getSprite_Bot() {
            return sprite_Bot;
        }

        public float getRotation_angle() {
            return rotation_angle;
        }

        public void changeDificulty(float difficulty) {
            maxSpeed = maxSpeed * difficulty / 10;
            botStep = botStep * difficulty / 10;
            kick_power = kick_power * difficulty / 10;
        }

        public void moveFoot(float dt) {
            if (oldOrientation == 0 || (oldOrientation == 1 && orientation)
                    || (oldOrientation == 2 && orientation == false)) {
                if (movingFoot == true) {

                    if (oldOrientation == 0) {
                        if (orientation == true) {
                            oldOrientation = 1;
                        } else {
                            oldOrientation = 2;
                        }
                    }

                    float prop = dt / aTime;
                    float limitX = playerWidth / 1.5f;
                    float limitY = 20;
                    float speedUp = 2.5f;
                    rotation_angle = sprite_Shoes.getRotation();
                    if (orientation == true) {
                        if (footDown == false) {
                            if (rotation_angle - prop * speedUp * initial_angle < final_angle) {
                                sprite_Shoes.setRotation(rotation_angle - prop * speedUp * initial_angle);
                                if (aCorrectionX + prop * limitX < limitX) {
                                    aCorrectionX += prop * limitX;
                                }
                                if (aCorrectionY + prop * limitY < limitY) {
                                    aCorrectionY += prop * limitY;
                                }
                            } else {
                                aCorrectionX = limitX;
                                aCorrectionY = limitY;
                                sprite_Shoes.setRotation(final_angle);
                                footDown = true;
                            }
                        } else if (rotation_angle + prop * speedUp * initial_angle > initial_angle) {
                            sprite_Shoes.setRotation(rotation_angle + prop * speedUp * initial_angle);
                            if (aCorrectionX - prop * limitX > 0) {
                                aCorrectionX -= prop * limitX;
                            }
                            if (aCorrectionY - prop * limitY > 0) {
                                aCorrectionY -= prop * limitY;
                            }
                        } else {
                            aCorrectionX = 0;
                            aCorrectionY = 0;
                            sprite_Shoes.setRotation(initial_angle);
                            footUp = true;
                        }
                    } else if (footDown == false) {
                        if (rotation_angle + prop * speedUp * initial_angle > -final_angle) {
                            sprite_Shoes.setRotation(rotation_angle + prop * speedUp * initial_angle);
                            if (aCorrectionX - prop * limitX > -limitX) {
                                aCorrectionX -= prop * limitX;
                            }
                            if (aCorrectionY + prop * limitY < limitY) {
                                aCorrectionY += prop * limitY;
                            }
                        } else {
                            aCorrectionX = -limitX;
                            aCorrectionY = limitY;
                            sprite_Shoes.setRotation(-final_angle);
                            footDown = true;
                        }
                    } else if (rotation_angle - prop * speedUp * initial_angle < -initial_angle) {
                        sprite_Shoes.setRotation(rotation_angle - prop * speedUp * initial_angle);
                        if (aCorrectionX + prop * limitX < 0) {
                            aCorrectionX += prop * limitX;
                        }
                        if (aCorrectionY - prop * limitY > 0) {
                            aCorrectionY -= prop * limitY;
                        }
                    } else {
                        aCorrectionX = 0;
                        aCorrectionY = 0;
                        sprite_Shoes.setRotation(-initial_angle);
                        footUp = true;
                    }
                    if (footUp) {
                        footUp = false;
                        footDown = false;
                        movingFoot = false;
                        oldOrientation = 0;
                    }

                }
            } else {
                footUp = false;
                footDown = false;
                movingFoot = false;
                oldOrientation = 0;
                aCorrectionX = 0;
                aCorrectionY = 0;
                if (orientation) {
                    sprite_Shoes.setRotation(initial_angle);
                } else {
                    sprite_Shoes.setRotation(-initial_angle);
                }
            }
        }

        public Sprite getSprite_Shoes() {
            return sprite_Shoes;
        }

        public void updateState(float dt, Vector2 ball, Vector2 player) {
            if (oldTime > 0) {
                oldTime += dt;
            }
            if (oldTime > decisionPeriod) {
                oldTime = 0;
            }
            if (oldTime == 0) {
                float x = sprite_Bot.getX();
                float y = sprite_Bot.getY() + playerHeight;

                if (ball.x > x + playerWidth && ball.y > y) {
                    moveAlone = 1;
                } else if (ball.x > x && ball.y <= y) {
                    moveAlone = 2;
                } else if (ball.x < x && ball.y > y) {
                    moveAlone = 3;
                } else {
                    moveAlone = 4;
                }
                if (Math.abs(ball.x - x) < playerWidth && movingFoot == false && ball.x < x) {
                    movingFoot = true;
                }
                if (Math.abs(ball.x - x) < playerWidth / 4 && ball.x > x) {
                    jump = true;
                }
            }
        }

        public void updateMovement(float dt, Vector2 ball, Vector2 player) {
            updateState(dt, ball, player);
            float x = sprite_Bot.getX(), y = sprite_Bot.getY();
            rotation_angle = sprite_Shoes.getRotation();
            walking = false;
            up = false;
            down = false;
            left = false;
            right = false;
            switch (moveAlone) {
                case 3:
                case 4:
                    if (speed.x > -1 * maxSpeed) {
                        speed.x -= 1;
                    }
                    if (orientation == true) {
                        this.sprite_Bot.setFlip(false, false);
                        this.sprite_Shoes.setFlip(true, false);
                        this.sprite_Shoes.setRotation(27);
                        correctionX = sprite_Bot.getWidth() / 3;

                        orientation = false;
                    }
                    break;
                case 1:
                case 2:
                    if (speed.x < maxSpeed) {
                        speed.x += 1;
                    }
                    if (orientation == false) {
                        correctionX = 0;
                        this.sprite_Bot.setFlip(true, false);
                        this.sprite_Shoes.setFlip(false, false);
                        this.sprite_Shoes.setRotation(-27);
                        orientation = true;
                    }
                    break;
                default:
                    speed.x = 0;
                    break;
            }

            if (moveAlone == 1 || moveAlone == 3 || jump == true) {
                if (sprite_Bot.getY() == FLOOR) {
                    speed.y = JUMP;
                }
            }

            float botStepx = botStep * (speed.x / maxSpeed);
            float botStepy = botStep * (speed.y / maxSpeed);

            if (speed.x < 0) {
                if (x + botStepx > positionMin.x) {
                    x += botStepx;
                }
                walking = true;
                left = true;
            } else if (speed.x > 0) {
                if (x + botStepx < positionMax.x - playerWidth) {
                    x += botStepx;
                }
                walking = true;
                right = true;
            }

            if (y > FLOOR) {
                if (speed.y > 0) {
                    y += botStepy;
                    walking = true;
                    up = true;
                } else if (speed.y < 0) {
                    y += botStepy;
                    walking = true;
                    down = true;

                }
            } else if (y == FLOOR) {
                if (speed.y > 0) {
                    y += botStepy;
                    walking = true;
                    up = true;
                }
            }

            if (y < FLOOR) {
                y = FLOOR;
            }

            sprite_Bot.setPosition(x, y);
            sprite_Shoes.setPosition(x + correctionX + aCorrectionX, y - sprite_Shoes.getHeight() / 2 + correctionY + aCorrectionY);
        }

        public void actionGravity(float value) {
            speed.set(speed.x, speed.y - value);
        }

        public void draw() {
            sprite_Bot.draw(batch);
            sprite_Shoes.draw(batch);
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public float getMass() {
            return mass;
        }
    }

    class Player {

        public static final int WORLD_WIDTH = 1280;
        public static final int WORLD_HEIGHT = 720;
        public static final int FLOOR = 81;
        public static final int JUMP = 7;
        public static final int GRAVITY = 10;
        public static final float convertToRad = 3.14159265359f / 180;

        public static final int INITIALXLEFTGOAL = -45;
        public static final int INITIALXRIGHTGOAL = 1135;
        public static final int INITIALYGOAL = 75;

        public boolean walking, orientation, footUp, footDown, movingFoot, left, right, jump, kick;
        private float playerStep, playerWidth, playerHeight;
        public Sprite sprite_Player, sprite_Shoes;
        public Vector2 position, positionMin, positionMax, speed, footPosition;
        private Texture playerTexture;
        private SpriteBatch batch;
        public float maxSpeed;
        private float mass, aTime;
        private float rotation_angle, initial_angle, final_angle, correctionX, correctionY, aCorrectionX, aCorrectionY;
        public float kick_power;
        private int oldOrientation;

        public Player(Vector2 positionInicial, Vector2 positionMin, Vector2 positionMax, Texture playerTexture, SpriteBatch batch, float playerStep, float maxSpeed, float sizeX, float sizeY, float mass) {
            this.speed = new Vector2(0, 0);
            this.walking = false;
            this.positionMin = positionMin;
            this.positionMax = positionMax;
            this.batch = batch;
            this.playerStep = playerStep;
            this.playerTexture = playerTexture;
            this.maxSpeed = maxSpeed;
            this.mass = mass;
            aTime = 0.3f;
            footUp = false;
            footDown = false;
            movingFoot = false;
            kick_power = 20;
            this.sprite_Player = new Sprite(playerTexture);
            this.sprite_Player.setSize(sizeX, sizeY);
            this.sprite_Player.setFlip(true, false);
            this.sprite_Shoes = new Sprite(new Texture("head-soccer/shoes.png"));
            this.sprite_Shoes.setSize(0.7f * sizeX, 0.3f * sizeY);
            this.sprite_Shoes.setOriginCenter();
            this.sprite_Shoes.setRotation(-27);
            this.initial_angle = -27;
            final_angle = 45;

            correctionY = -sprite_Shoes.getHeight() / 2;

            aCorrectionX = 0;
            aCorrectionY = 0;

            oldOrientation = 0;
            this.playerWidth = sprite_Player.getWidth();
            this.playerHeight = sprite_Player.getHeight();

            this.position = positionInicial;
            footPosition = new Vector2(positionInicial.x, positionInicial.y - sprite_Shoes.getHeight() / 2);

            orientation = true;//true= Direita false = left
            correctionX = 0;

            sprite_Player.setPosition(positionInicial.x, positionInicial.y);
            sprite_Shoes.setPosition(footPosition.x, footPosition.y);
        }

        public Sprite getSprite_Player() {
            return sprite_Player;
        }

        public float getRotation_angle() {
            return rotation_angle;
        }

        public void moveFoot(float dt) {
            if (oldOrientation == 0 || (oldOrientation == 1 && orientation)
                    || (oldOrientation == 2 && orientation == false)) {
                if (movingFoot == true) {

                    if (oldOrientation == 0) {
                        if (orientation == true) {
                            oldOrientation = 1;
                        } else {
                            oldOrientation = 2;
                        }
                    }

                    float prop = dt / aTime;
                    float limitX = playerWidth / 1.5f;
                    float limitY = 20;
                    float speedUp = 2.5f;
                    rotation_angle = sprite_Shoes.getRotation();
                    if (orientation == true) {
                        if (footDown == false) {
                            if (rotation_angle - prop * speedUp * initial_angle < final_angle) {
                                sprite_Shoes.setRotation(rotation_angle - prop * speedUp * initial_angle);
                                if (aCorrectionX + prop * limitX < limitX) {
                                    aCorrectionX += prop * limitX;
                                }
                                if (aCorrectionY + prop * limitY < limitY) {
                                    aCorrectionY += prop * limitY;
                                }
                            } else {
                                aCorrectionX = limitX;
                                aCorrectionY = limitY;
                                sprite_Shoes.setRotation(final_angle);
                                footDown = true;
                            }
                        } else if (rotation_angle + prop * speedUp * initial_angle > initial_angle) {
                            sprite_Shoes.setRotation(rotation_angle + prop * speedUp * initial_angle);
                            if (aCorrectionX - prop * limitX > 0) {
                                aCorrectionX -= prop * limitX;
                            }
                            if (aCorrectionY - prop * limitY > 0) {
                                aCorrectionY -= prop * limitY;
                            }
                        } else {
                            aCorrectionX = 0;
                            aCorrectionY = 0;
                            sprite_Shoes.setRotation(initial_angle);
                            footUp = true;
                        }
                    } else if (footDown == false) {
                        if (rotation_angle + prop * speedUp * initial_angle > -final_angle) {
                            sprite_Shoes.setRotation(rotation_angle + prop * speedUp * initial_angle);
                            if (aCorrectionX - prop * limitX > -limitX) {
                                aCorrectionX -= prop * limitX;
                            }
                            if (aCorrectionY + prop * limitY < limitY) {
                                aCorrectionY += prop * limitY;
                            }
                        } else {
                            aCorrectionX = -limitX;
                            aCorrectionY = limitY;
                            sprite_Shoes.setRotation(-final_angle);
                            footDown = true;
                        }
                    } else if (rotation_angle - prop * speedUp * initial_angle < -initial_angle) {
                        sprite_Shoes.setRotation(rotation_angle - prop * speedUp * initial_angle);
                        if (aCorrectionX + prop * limitX < 0) {
                            aCorrectionX += prop * limitX;
                        }
                        if (aCorrectionY - prop * limitY > 0) {
                            aCorrectionY -= prop * limitY;
                        }
                    } else {
                        aCorrectionX = 0;
                        aCorrectionY = 0;
                        sprite_Shoes.setRotation(-initial_angle);
                        footUp = true;
                    }
                    if (footUp) {
                        footUp = false;
                        footDown = false;
                        movingFoot = false;
                        oldOrientation = 0;
                    }

                }
            } else {
                footUp = false;
                footDown = false;
                movingFoot = false;
                oldOrientation = 0;
                aCorrectionX = 0;
                aCorrectionY = 0;
                if (orientation) {
                    sprite_Shoes.setRotation(initial_angle);
                } else {
                    sprite_Shoes.setRotation(-initial_angle);
                }
            }
        }

        public Sprite getSprite_Shoes() {
            return sprite_Shoes;
        }

        public void updateMoviment() {
            float x = sprite_Player.getX(), y = sprite_Player.getY();
            rotation_angle = sprite_Shoes.getRotation();
            walking = false;

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) || left) {
                if (speed.x > -1 * maxSpeed) {
                    speed.x -= 1;
                }
                if (orientation == true) {
                    this.sprite_Player.setFlip(false, false);
                    this.sprite_Shoes.setFlip(true, false);
                    this.sprite_Shoes.setRotation(27);
                    correctionX = sprite_Player.getWidth() / 3;

                    orientation = false;
                }
                left = false;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) || right) {
                if (speed.x < maxSpeed) {
                    speed.x += 1;
                }
                if (orientation == false) {
                    correctionX = 0;
                    this.sprite_Player.setFlip(true, false);
                    this.sprite_Shoes.setFlip(false, false);
                    this.sprite_Shoes.setRotation(-27);
                    orientation = true;
                }
                right = false;
            } else {
                speed.x = 0;
            }

            if ((Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) && y == FLOOR || jump) {
                if (sprite_Player.getY() == FLOOR) {
                    speed.y = JUMP;
                }
                jump = false;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || kick) {
                if (movingFoot == false) {
                    movingFoot = true;
                }
                kick = false;
            }

            float playerStepx = playerStep * (speed.x / maxSpeed);
            float playerStepy = playerStep * (speed.y / maxSpeed);

            if (speed.x < 0) {
                if (x + playerStepx > positionMin.x) {
                    x += playerStepx;
                }
                walking = true;
            } else if (speed.x > 0) {
                if (x + playerStepx < positionMax.x - playerWidth) {
                    x += playerStepx;
                }
                walking = true;
            }

            if (y > FLOOR) {
                if (speed.y > 0) {
                    y += playerStepy;
                    walking = true;
                } else if (speed.y < 0) {
                    y += playerStepy;
                    walking = true;

                }
            } else if (y == FLOOR) {
                if (speed.y > 0) {
                    y += playerStepy;
                    walking = true;
                }
            }

            if (y < FLOOR) {
                y = FLOOR;
            }

            sprite_Player.setPosition(x, y);
            sprite_Shoes.setPosition(x + correctionX + aCorrectionX, y - sprite_Shoes.getHeight() / 2 + correctionY + aCorrectionY);
        }

        public void actionGravity(float value) {
            speed.set(speed.x, speed.y - value);
        }

        public void draw() {
            sprite_Player.draw(batch);
            sprite_Shoes.draw(batch);
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public float getMass() {
            return mass;
        }
    }

    // </editor-fold>
}
