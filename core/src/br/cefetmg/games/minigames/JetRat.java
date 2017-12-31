package br.cefetmg.games.minigames;

import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;

import java.util.HashMap;
import java.util.Random;

import static br.cefetmg.games.Config.WORLD_HEIGHT;
import static br.cefetmg.games.Config.WORLD_WIDTH;

import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.math.Rectangle;

public class JetRat extends MiniGame {

    private Calopsita mouse;
    private Texture mouseTexture;
    private Texture tubeTexture;
    private Texture cattubeTexture;
    private Texture bg1;
    private Array<Tube> enemies;

    // variáveis do desafio - variam com a dificuldade do minigame
    private float minimumEnemySpeed;
    private float screenWidth;
    private float screenHeight;
    int srcX, troca;
    private MyMusic meon;
    private MySound jet;
    private float nextJetSoundPlay = 0;
    int difficulty;
    
    public JetRat(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
         
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
        
    }

    @Override
    protected void onStart() {
 
        troca = 0;
        mouseTexture = assets.get("jet-rat/jatmouse.png", Texture.class);
        cattubeTexture = assets.get("jet-rat/tubecat.png", Texture.class);
        bg1 = assets.get("jet-rat/background.png", Texture.class);
        bg1.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        tubeTexture = assets.get("jet-rat/tube.png", Texture.class);
        meon = new MyMusic(assets.get("jet-rat/meon.mp3", Music.class));
        jet = new MySound(assets.get("jet-rat/kruncha-angry.mp3", Sound.class));
        mouse = new Calopsita(mouseTexture);
        mouse.setScale(0.5f);
        
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();

        mouse.setPosition(screenWidth/8, screenHeight/4);

        enemies = new Array<Tube>();

        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnEnemy();
            }

        }, 0, (float) Math.random() + 0.7f);
        srcX = 0;
        meon.setVolume(0.2f);
        meon.play();
    }

    private void spawnEnemy() {

        float Ddwown = (float) Math.random() * viewport.getScreenHeight() * 0.3f;

        Vector2 tubeGoal = new Vector2(-screenWidth, Ddwown);
        Vector2 tubePosition = new Vector2();
        int dist=(int) (150-(150-this.minimumEnemySpeed));
        
        Vector2 tubeSpeed = tubeGoal
                .sub(tubePosition)
                .nor()
                .scl(dist);
        Tube enemy = new Tube(cattubeTexture);
        enemy.setSize(new Random().nextInt(this.difficulty)+3);

        enemy.setPosition(WORLD_WIDTH, 10 * enemy.getSize());
        enemy.setSpeed(tubeSpeed);
        enemy.startAnimation("dormindo");
        enemies.add(enemy);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 70, 140);
        this.difficulty = (int) (Math.ceil(DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 1, 14))-1);
    }
    
    @Override
    public void onHandlePlayingInput() {

        if (Gdx.input.isTouched()) {
            if(mouse.getY() <= screenHeight/2){
                mouse.acceleration.y = 4.0f*Math.abs(Calopsita.ACCELERATION_Y);
            }
            else{
                mouse.acceleration.y = Math.abs(Calopsita.ACCELERATION_Y);
            }
            mouse.acceleration.x = 30.0f;
            
            if(!mouse.fuel){
                mouse.startAnimation("fuel");
            }
            mouse.fuel = true;

            if (nextJetSoundPlay <= 0) {
                jet.play(0.5f);
                nextJetSoundPlay = 0.5f;
            } else {
                nextJetSoundPlay -= Gdx.graphics.getDeltaTime();
            }
        }
        else{
            if(mouse.getY() >= screenHeight/2){
                mouse.acceleration.y = -2.0f*Math.abs(Calopsita.ACCELERATION_Y);
            }
            else{
                mouse.acceleration.y = -Math.abs(Calopsita.ACCELERATION_Y);
            }
            
            mouse.acceleration.x = -30.0f;
            
            if(mouse.fuel){
                mouse.startAnimation("nofuel");
            }
            mouse.fuel = false;
        }
    }

    @Override
    public void onUpdate(float dt) {
        if (this.getState().equals(MiniGameState.PLAYER_SUCCEEDED)) {
            meon.stop();
        }
        if(this.isPaused()==true)
           meon.pause();
       
        mouse.update(dt);
        
        if ((mouse.getY() + mouse.getHeight() / 2 > WORLD_HEIGHT) || 
                (mouse.getY() + mouse.getHeight() < 0)) {
            super.challengeFailed();
            meon.stop();
        }
        
        for (Tube tube : this.enemies) {
            
            if (mouse.getBoundingRectangle().overlaps(tube.getColliderRectangle())) {

                super.challengeFailed();
                meon.stop();
                tube.startAnimation("acordado");
            }
            tube.setPosition(tube.getX() - 5, tube.getY());
            
            tube.update(dt);
        }

     
    }
   
    
    @Override
    public void onDrawGame() {
        batch.draw(bg1, 0, 0, srcX, 0, WORLD_WIDTH, WORLD_HEIGHT);

        for (Tube tube : this.enemies) {
            for (int i = 0; i < tube.getOriginY()/60; i++) {
                batch.draw(tubeTexture, tube.getX(), 60 * i);
            }
            tube.draw(batch);
        }

        mouse.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Voe!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
    public 

    class Calopsita extends MultiAnimatedSprite {

        static final int FRAME_WIDTH = 131;//131
        static final int FRAME_HEIGHT = 156;//156

        static final float MAX_SPEED_Y = 250.0f;
        static final float ACCELERATION_Y = 300.0f;
        
        static final float MAX_SPEED_X = 100.0f;
        static final float ACCELERATION_X = 50.0f;
                
        private Vector2 speed;
        private Vector2 acceleration;
        
        boolean fuel;
        
        public Calopsita(final Texture toothbrushTexture) {
            super(new HashMap<String, Animation>() {
                {
                    
                    TextureRegion[][] frames = TextureRegion
                            .split(toothbrushTexture,
                                    FRAME_WIDTH, FRAME_HEIGHT);
                    Animation sleep = new Animation(0.1f,
                            frames[0][0],
                            frames[0][1],
                            frames[0][2],
                            frames[0][3]
                            
                    );
                    Animation acordado = new Animation(0.1f,
                            frames[0][4],
                            frames[0][5],
                            frames[0][6],
                            frames[0][7]
                    );
                    sleep.setPlayMode(Animation.PlayMode.LOOP);
                    acordado.setPlayMode(Animation.PlayMode.LOOP);
                    put("fuel", sleep);
                    put("nofuel", acordado);

                }
            }, "nofuel");
                    
            this.fuel = false;
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
            
            this.speed = new Vector2(0.0f, 0.0f);
            this.acceleration = new Vector2(0.0f, -ACCELERATION_Y);
            
        }

        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX() + this.getWidth(),
                    this.getY() + this.getHeight());
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }
        
        @Override
        public void update(float dt) {

            super.update(dt);
            super.setPosition(super.getX() + this.speed.x * dt,
                    super.getY() + this.speed.y * dt);
            
            this.speed.x = Math.max(Math.min(this.speed.x+this.acceleration.x*dt, Calopsita.MAX_SPEED_X), 0.0f);
            this.speed.y = Math.max(Math.min(this.speed.y+this.acceleration.y*dt, Calopsita.MAX_SPEED_Y), -Calopsita.MAX_SPEED_Y);
            
        }
    }

    class Tube extends MultiAnimatedSprite {

        private Vector2 speed;

        private static final int FRAME_WIDTH = 220;
        private static final int FRAME_HEIGHT = 390;
        private int size;
        private final Rectangle colliderRectangle;
        private static final int COLLIDER_PADDING_TOP = 50;

        public Tube(final Texture tubesSpritesheet) {

            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(tubesSpritesheet,
                                    FRAME_WIDTH, FRAME_HEIGHT);
                    Animation sleep = new Animation(0.1f,
                            frames[0][0],
                            frames[1][0],
                            frames[2][0]
                    );
                    Animation acordado = new Animation(0.1f,
                            frames[0][1],
                            frames[1][1],
                            frames[2][1]
                    );
                    sleep.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                    acordado.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                    put("dormindo", sleep);
                    put("acordado", acordado);

                }
            }, "dormindo");
             
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            super.setAutoUpdate(false);
            colliderRectangle = new Rectangle(super.getBoundingRectangle());
        }

        public void changePicture() {
            this.startAnimation("acordado");
        }

        @Override
        public void update(float dt) {

            super.update(dt);
            setPosition(super.getX() + this.speed.x * dt,
                    super.getY() + this.speed.y * dt);
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }
        
        public Rectangle getColliderRectangle() {
            return colliderRectangle;
        }
        
        private void updateColliderRectangle() {
            colliderRectangle.set(getBoundingRectangle());
            colliderRectangle.setHeight(
                    colliderRectangle.getY() + 
                    colliderRectangle.getHeight()
                            - COLLIDER_PADDING_TOP);
            colliderRectangle.setY(0);
        }

        @Override
        public void setPosition(float x, float y) {
            super.setPosition(x, y);
            updateColliderRectangle();
        }
        
        

    }

}
