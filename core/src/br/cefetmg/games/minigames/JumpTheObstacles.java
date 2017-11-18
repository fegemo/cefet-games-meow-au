package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashMap;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.math.Circle;

public class JumpTheObstacles extends MiniGame {

    private Texture dogTexture;
    private Dog dog;
    private Texture obstaclesTexture;
    private Texture backgroundTexture;
    private Array<MySound> obstaclesAppearingSound;
    private MySound jumpSound;
    private Array<Obstacle> enemies;

    // variáveis do desafio - variam com a dificuldade do minigame
    private float minimumEnemySpeed;
    private float spawnInterval;

    static final float Y_POSITION = 250.0f;
    
    public JumpTheObstacles(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        dogTexture = assets.get(
                "jump-the-obstacles/dog.png", Texture.class);
        dog = new Dog(dogTexture);
        obstaclesTexture = assets.get(
                "jump-the-obstacles/obstacles.png", Texture.class);
        backgroundTexture = assets.get(
                "jump-the-obstacles/background.png", Texture.class);
        obstaclesAppearingSound = new Array<MySound>(3);
        obstaclesAppearingSound.addAll(new MySound(assets.get(
                "jump-the-obstacles/appearing1.wav", Sound.class)),
                new MySound(assets.get(
                        "jump-the-obstacles/appearing2.wav", Sound.class)),
                new MySound(assets.get(
                        "jump-the-obstacles/appearing3.wav", Sound.class)));
        jumpSound = new MySound(assets.get("jump-the-obstacles/29-extra-life-balloon.mp3", Sound.class));
        enemies = new Array<Obstacle>();

        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnEnemy();
            }

        }, 0, this.spawnInterval);
    }

    private void spawnEnemy() {
        Vector2 cannonballPosition = new Vector2(viewport.getWorldWidth() + Math.min(0.0f, ((float) Math.random()) * 200.0f - 100.0f), 0.0f);

        Vector2 cannonballSpeed = new Vector2(-this.minimumEnemySpeed, 0.0f);

        Obstacle enemy = new Obstacle(obstaclesTexture);
        enemy.setPosition(cannonballPosition.x, cannonballPosition.y);
        enemy.speed = cannonballSpeed;
        enemy.setY(Y_POSITION);
        enemies.add(enemy);

        // toca um efeito sonoro
        MySound sound = obstaclesAppearingSound.random();
        long id = sound.play(0.5f);
        sound.setPan(id, cannonballPosition.x < viewport.getWorldWidth()
                ? -1 : 1, 1);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 400, 1200);
        this.spawnInterval = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.9f, 1.2f);
    }

    @Override
    public void onHandlePlayingInput() {

        if (Gdx.input.justTouched()) {
            dog.jump();
        }
    }

    @Override
    public void onUpdate(float dt) {
        // atualiza a escova (quadro da animação)
        dog.update(dt);

        // atualiza os inimigos (quadro de animação + colisão com dentes)
        for (int i = 0; i < this.enemies.size; i++) {
            Obstacle obstacle = this.enemies.get(i);

            if (obstacle.getX() <= 0) {
                enemies.removeIndex(i);
                i--;
            } else {

                obstacle.update(dt);

                // verifica se este inimigo está colidindo com algum dente
                if (obstacle.getCollisionCircle().overlaps(dog.getCollisionCircle())) {
                    super.challengeFailed();
                }

            }
        }
    }

    @Override
    public void onDrawGame() {
        //Background
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        for (Obstacle tart : this.enemies) {
            tart.draw(batch);
        }
        dog.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Pule os obstáculos";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Dog extends MultiAnimatedSprite {

        static final float SPEED = 750.0f;
        
        static final int FRAME_WIDTH = 120;
        static final int FRAME_HEIGHT = 130;

        static final float TOTAL_JUMPING_TIME = 0.75f;

        static final float X_POSITION = 250.0f;

        private JumpState state;

        private final Vector2 speed;

        private final Vector2 force;
        
        Dog(final Texture kongTexture) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            kongTexture, FRAME_WIDTH, FRAME_HEIGHT);

                    Animation walking = new Animation(0.05f,
                            frames[1][0],
                            frames[1][0],
                            frames[1][0],
                            frames[1][0],
                            frames[1][0],
                            frames[1][0],
                            frames[1][0],
                            frames[1][0],
                            frames[1][1],
                            frames[1][2],
                            frames[1][3],
                            frames[1][4],
                            frames[1][5],
                            frames[1][6],
                            frames[1][7],
                            frames[1][8]
                    );

                    walking.setPlayMode(Animation.PlayMode.LOOP_RANDOM);
                    put("walking", walking);

                    Animation jumping = new Animation(Dog.TOTAL_JUMPING_TIME / 6.0f,
                            frames[0][0],
                            frames[0][1],
                            frames[0][2],
                            frames[0][4],
                            frames[0][4],
                            frames[0][5],
                            frames[0][6],
                            frames[0][7],
                            frames[0][8],
                            frames[0][0]
                    );
                    jumping.setPlayMode(Animation.PlayMode.NORMAL);
                    put("jumping", jumping);

                }
            }, "walking");

            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);

            //this.setX(X_POSITION);
            this.setPosition(X_POSITION, Y_POSITION);
            
            speed = new Vector2(0.0f, 0.0f);
            force = new Vector2(0.0f, 0.0f);

            state = JumpState.NONE;

        }

        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }

        @Override
        public void update(float dt) {
            super.update(dt);

            switch (state) {
                case NONE:
                    speed.y = 0.0f;
                    force.y = 0.0f;
                    break;
                case JUMPING:                               
                    super.setPosition(super.getX() + this.speed.x * dt,
                        Math.max(super.getY() + (this.speed.y * dt), Y_POSITION));
                    this.speed.y += this.force.y;//*dt;
                    if (super.getY() <= Y_POSITION) {
                        state = JumpState.NONE;
                        startAnimation("walking");
                    }
                    break;
            }
        }

        public void jump() {
            if (state == JumpState.NONE) {
                state = JumpState.JUMPING;
                speed.y = SPEED;
                force.y = -2*SPEED/(TOTAL_JUMPING_TIME/Gdx.graphics.getDeltaTime());
                
                // toca um efeito sonoro
                MySound sound = jumpSound;
                long id = sound.play(0.5f);
                sound.setPan(id, dog.getX(), 0.25f);
                startAnimation("jumping");
            }
        }

        public Circle getCollisionCircle() {
            Vector2 position = new Vector2(this.getX(), this.getY());
            Vector2 center = position.add(FRAME_WIDTH / 2.0f, FRAME_HEIGHT / 2.0f);

            return new Circle(center, (FRAME_WIDTH + FRAME_HEIGHT) / 4.0f);
        }
    }

    class Obstacle extends AnimatedSprite {

        static final float SPEED = 160.0f;

        static final int FRAME_WIDTH = 100;
        static final int FRAME_HEIGHT = 100;

        static final float MIN_SPACE = 80.0f;

        private Vector2 speed;

        Obstacle(final Texture kongTexture) {
            super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            kongTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][5],
                        frames[0][4],
                        frames[0][3],
                        frames[0][2],
                        frames[0][1],
                        frames[0][0]
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);

            //this.setX(Gdx.graphics.getWidth());
            speed = new Vector2(-SPEED, 0.0f);

        }

        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }

        @Override
        public void update(float dt) {
            super.update(dt);

            super.setPosition(Math.max(super.getX() + this.speed.x * dt, 0.0f),
                    super.getY() + this.speed.y * dt);
        }

        public Circle getCollisionCircle() {
            Vector2 position = new Vector2(this.getX(), this.getY());
            Vector2 center = position.add(FRAME_WIDTH / 2.0f, FRAME_HEIGHT / 2.0f - 10);

            return new Circle(center, 30);
        }

    }

    enum JumpState {
        NONE,
        JUMPING
    }
}
