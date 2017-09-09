package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashMap;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Timer;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class JumpTheObstacles extends MiniGame {

    private Texture kongTexture;
    private Kong kong;
    private Texture tartarusTexture;
    private Texture toothTexture;
    private Array<Sound> tartarusAppearingSound;
    private Sound toothBreakingSound;
    private Array<Obstacle> enemies;
    private int numberOfBrokenTeeth;
    
    // variáveis do desafio - variam com a dificuldade do minigame
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private float spawnInterval;

    public JumpTheObstacles(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        kongTexture = assets.get(
                "jump-the-obstacles/kong_walking.png", Texture.class);
        kong = new Kong(kongTexture);
        tartarusTexture = assets.get(
                "jump-the-obstacles/sprite02.png", Texture.class);
        /*toothTexture = assets.get(
                "shoo-the-tartarus/tooth.png", Texture.class);*/
        tartarusAppearingSound = new Array<Sound>(3);
        tartarusAppearingSound.addAll(assets.get(
                "shoo-the-tartarus/appearing1.wav", Sound.class),
                assets.get(
                        "shoo-the-tartarus/appearing2.wav", Sound.class),
                assets.get(
                        "shoo-the-tartarus/appearing3.wav", Sound.class));
        toothBreakingSound = assets.get(
                "shoo-the-tartarus/tooth-breaking.wav", Sound.class);
        enemies = new Array<Obstacle>();
        numberOfBrokenTeeth = 0;

        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnEnemy();
            }

        }, 0, this.spawnInterval);
    }


    private void spawnEnemy() {
        Vector2 cannonballPosition = new Vector2(Gdx.graphics.getWidth() + ((float) Math.random())*200.0f - 100.0f, 0.0f);
        
        Vector2 cannonballSpeed = new Vector2(-this.minimumEnemySpeed, 0.0f);

        Obstacle enemy = new Obstacle(tartarusTexture);
        enemy.setPosition(cannonballPosition.x, cannonballPosition.y);
        enemy.speed = cannonballSpeed;
        enemies.add(enemy);
        
        // toca um efeito sonoro
        Sound sound = tartarusAppearingSound.random();
        //long id = sound.play(0.5f);
        //sound.setPan(id, cannonballPosition.x < viewport.getWorldWidth()
        //        ? -1 : 1, 1);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 300, 400);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 450, 550);
        this.spawnInterval = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 1.0f, 1.5f);
    }

    @Override
    public void onHandlePlayingInput() {
        
        if(Gdx.input.justTouched())
            kong.jump();
    }

    @Override
    public void onUpdate(float dt) {
        
        // atualiza a escova (quadro da animação)
        kong.update(dt);

        // atualiza os inimigos (quadro de animação + colisão com dentes)
        for (int i = 0; i < this.enemies.size; i++) {
            Obstacle obstacle = this.enemies.get(i);
            
            if(obstacle.getX() <= 0){
                enemies.removeIndex(i);
                i--;
            }
            else{
            
                obstacle.update(dt);

                // verifica se este inimigo está colidindo com algum dente
                //for (Tooth tooth : this.teeth) {
                    if (obstacle.getBoundingRectangle()
                            .overlaps(kong.getBoundingRectangle())) {
                        super.challengeFailed();
                    }
                //}
            }
        }
    }

    @Override
    public void onDrawGame() {
        for (Obstacle tart : this.enemies) {
            tart.draw(batch);
        }
        kong.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Pule os obstáculos";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Kong extends MultiAnimatedSprite {

        static final float SPEED = 320.0f;
        
        static final int FRAME_WIDTH = 36;
        static final int FRAME_HEIGHT = 38;
        
        static final float TOTAL_JUMPING_TIME = 0.75f;
        
        static final float X_POSITION = 300.0f;
        
        private Timer jumpTimer;
        JumpState state;

        private Vector2 speed;
        
        private float jumpingTime = 0.0f;
        
        Kong(final Texture kongTexture) {
            super(new HashMap<String, Animation>() {
                {
                    //new Animation(0.1f, new Array<TextureRegion>() {
                    TextureRegion[][] frames = TextureRegion.split(
                            kongTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    
                    Animation walking = new Animation(0.2f,
                            frames[0][0],
                            frames[0][1],
                            frames[0][2],
                            frames[0][1]);
                    walking.setPlayMode(Animation.PlayMode.LOOP);
                    put("walking", walking);
                    
/*                    Animation jumping = new Animation(0.2f,
                            frames[1][0],
                            frames[1][1],
                            frames[1][2],
                            frames[1][3]);
                    jumping.setPlayMode(Animation.PlayMode.NORMAL);
                    put("jumping", jumping);
  */                  
                }
            }, "walking");
            
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
            
            this.setX(X_POSITION);
            
            speed = new Vector2(0.0f, 0.0f);
            
            state = JumpState.NONE;
            
            jumpTimer = new Timer();
            jumpTimer.stop();
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
            
            switch(state){
                case NONE:
                    speed.y = 0.0f;
                    jumpingTime = 0.0f;
                    break;
                case JUMPING:
                    jumpingTime += dt;
                    speed.y = SPEED;
                    
                    if(jumpingTime >= TOTAL_JUMPING_TIME/2.0f)
                        state = JumpState.FALLING;
                    
                    break;
                case FALLING:
                    jumpingTime -= dt;
                    speed.y = -SPEED;
                    
                    if(jumpingTime <= 0.0f)
                        state = JumpState.NONE;
                    
                    break;
            }
            
            super.setPosition(super.getX() + this.speed.x * dt,
                    Math.max(super.getY() + (this.speed.y * dt), 0.0f));
        }
        
        public void jump(){
            if(state == JumpState.NONE){
                state = JumpState.JUMPING;
                
                // toca um efeito sonoro
                Sound sound = tartarusAppearingSound.random();
                //long id = sound.play(0.5f);
                //sound.setPan(id, cannonballPosition.x < viewport.getWorldWidth()
                //        ? -1 : 1, 1);
            }
        }
    }

    class Obstacle extends AnimatedSprite {

        static final float SPEED = 160.0f;
        
        static final int FRAME_WIDTH = 40;
        static final int FRAME_HEIGHT = 40;
        
        static final float MIN_SPACE = 80.0f;
        
        private Vector2 speed;
        
        private float jumpingTime = 0.0f;
        
        Obstacle(final Texture kongTexture) {
            super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            kongTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0]/*,
                        frames[0][1],
                        frames[0][2],
                        frames[0][3]*/
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
        
    }

   
    enum JumpState{
        NONE,
        JUMPING,
        FALLING
    }
}
