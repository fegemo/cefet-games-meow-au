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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Circle;
import java.util.ArrayList;
import java.util.Random;

public class HitTheGopher extends MiniGame {

    private Texture hammerTexture;
    private Texture gopherTexture;
    private Texture gopherHitTexture;
    private Texture backgroundTexture;
    private MySound hitSound;
    private Array<Gopher> enemies;
    private Hammer hammer;
    
    // variáveis do desafio - variam com a dificuldade do minigame
    private float spawnInterval;
    private float showedInterval;
    private int gophersAmount;
    
    private boolean showingAny;
        
    static final float SPAN = 50.0f;
    
    static final float Y_POSITION = 125.0f;
    
    public HitTheGopher(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        hammerTexture = assets.get(
                "hit-the-gopher/hammer.png", Texture.class);
        gopherTexture = assets.get(
                "hit-the-gopher/gopher.png", Texture.class);
        gopherHitTexture = assets.get(
                "hit-the-gopher/gopher-hit.png", Texture.class);
        backgroundTexture = assets.get(
                "hit-the-gopher/background.png", Texture.class);
        hitSound = new MySound(assets.get("hit-the-gopher/smw_kick.wav", Sound.class));
        this.showingAny = false;

        this.enemies = new Array<Gopher>();
        
        for(int i=0; i<this.gophersAmount; i++){
            Gopher gopher = new Gopher(gopherTexture);
            gopher.setPosition(getGopherX(i+1, this.gophersAmount, SPAN), Y_POSITION);
            gopher.startAnimation("hidden");
            gopher.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            this.enemies.add(gopher);
        }

        hammer = new Hammer(hammerTexture);
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2);
        hammer.setPosition(Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2);
        
        hammer.setOriginCenter();
        
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnEnemy();
            }

        }, 0, this.spawnInterval);
    }

    private void spawnEnemy() {
        
        if(!showingAny){
            Random random = new Random();
            int gopher_index = random.nextInt(gophersAmount);

            this.enemies.get(gopher_index).setState(GopherState.SHOWING);
            showingAny = true;
        }
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.gophersAmount = (int) DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 2.0f, 5.0f);
        this.spawnInterval = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 1.0f, 3.0f);
        this.showedInterval = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 1.0f, 3.0f);
        
    }

    @Override
    public void onHandlePlayingInput() {
        
        if (Gdx.input.justTouched()) {
            
            hammer.state = HammerState.ANIMATION;
            hammer.click();
            hitSound.play();
            
            for(Gopher gopher : this.enemies){
                boolean hit = hammer.getBoundingRectangle().overlaps(gopher.getBoundingRectangle());
                
                if(gopher.state == GopherState.SHOWED || gopher.state == GopherState.HIDING_MISS){
                    if(hit){
                        gopher.setState(GopherState.HIDING_HIT);
                    }
                }
            }            
        }
        
    }

    @Override
    public void onUpdate(float dt) {
        hammer.update(dt);

        // atualiza os inimigos (quadro de animação + colisão com dentes)
        for (int i = 0; i < this.enemies.size; i++) {
            Gopher gopher = this.enemies.get(i);

            gopher.update(dt);            
        }
        
    }

    @Override
    public void onDrawGame() {
        //Background
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        for (Gopher gopher : this.enemies) {
            gopher.draw(batch);
        }
        
        hammer.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Acerte os castores";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    float getGopherX(int gopher_number, int gopher_amount, float span){
        float screen_center = Gdx.graphics.getWidth()/2.0f;
        float gopher_width = Gopher.FRAME_WIDTH;
        float gopher_center = gopher_width/2.0f;

        float x_position;
        float middle = (gopher_amount + 1)/2;
        
        if(gopher_amount % 2 == 1){
            x_position  = screen_center - gopher_center - (span+gopher_width)*(gopher_number-middle);
        }
        else{
            x_position  = screen_center - gopher_width - span/2.0f
                    - (span+gopher_width)*(gopher_amount/2-1)
                    + (span+gopher_width)*(gopher_number-1);                    
        }

        return x_position;
    }
    
    class Gopher extends MultiAnimatedSprite {

        static final int FRAME_WIDTH = 168;
        static final int FRAME_HEIGHT = 142;

        static final float TOTAL_SHOWED_TIME = 2.0f;
        static final float TOTAL_SHOWING_TIME = 0.5f;
        static final float TOTAL_HIDDEN_TIME = 0.5f;
        static final float TOTAL_HIDING_TIME = 0.5f;

        private float accumulated;
        
        private GopherState state;
        
        Gopher(final Texture gopherTexture) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            gopherTexture, FRAME_WIDTH, FRAME_HEIGHT);

                    TextureRegion[][] hitFrames = TextureRegion.split(
                            gopherHitTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    
                    Animation hidden = new Animation(TOTAL_HIDDEN_TIME,
                            frames[0][0]
                    );

                    hidden.setPlayMode(Animation.PlayMode.NORMAL);
                    put("hidden", hidden);
                    
                    Animation showing = new Animation(TOTAL_SHOWING_TIME/3,
                            frames[0][0],
                            frames[0][1],
                            frames[0][2]
                    );

                    showing.setPlayMode(Animation.PlayMode.NORMAL);
                    put("showing", showing);

                    Animation showed = new Animation(TOTAL_SHOWED_TIME/3,
                            frames[0][2],
                            frames[1][0],
                            frames[1][2],
                            frames[0][2]
                    );

                    showed.setPlayMode(Animation.PlayMode.NORMAL);
                    put("showed", showed);
                    
                    Animation hiding_hit = new Animation(TOTAL_HIDING_TIME/3,
                            hitFrames[1][1],
                            hitFrames[0][1],
                            hitFrames[0][0]
                    );
                    hiding_hit.setPlayMode(Animation.PlayMode.NORMAL);
                    put("hiding-hit", hiding_hit);
                    
                    Animation hiding_miss = new Animation(TOTAL_HIDING_TIME/3,
                            frames[1][1],
                            frames[0][1],
                            frames[0][0]
                    );
                    hiding_miss.setPlayMode(Animation.PlayMode.NORMAL);
                    put("hiding-miss", hiding_miss);


                }
            }, "hidden");

            super.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
            super.setAutoUpdate(false);

            state = GopherState.HIDDEN;

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
            
            // Lógica de randomização
            
            switch(state){
                case HIDDEN:
                    
                    break;
                case HIDING_HIT:
                    if(accumulated >= TOTAL_HIDING_TIME){
                        setState(GopherState.HIDDEN);
                        showingAny = false;
                    }
                    else{
                        accumulated += dt;
                    }
                    break;
                case HIDING_MISS:
                    if(accumulated >= TOTAL_HIDING_TIME){
                        setState(GopherState.HIDDEN);
                        
                        challengeFailed();
                    }
                    else{
                        accumulated += dt;
                    }
                    break;
                case SHOWED:
                    if(accumulated >= showedInterval){
                        setState(GopherState.HIDING_MISS);
                    }
                    else{
                        accumulated += dt;
                    }
                    break;
                case SHOWING:
                    if(accumulated >= TOTAL_SHOWING_TIME){
                        setState(GopherState.SHOWED);
                    }
                    else{
                        accumulated += dt;
                    }
                    break;
                default:
                    
                    break;
            }
            
        }

        public void setState(GopherState gs){
            this.state = gs;
            accumulated = 0.0f;
            
            switch(state){
                case HIDDEN:
                    this.startAnimation("hidden");
                    break;
                case HIDING_HIT:
                    this.startAnimation("hiding-hit");
                    break;
                case HIDING_MISS:
                    this.startAnimation("hiding-miss");
                    break;
                case SHOWED:
                    this.startAnimation("showed");
                    break;
                case SHOWING:
                    this.startAnimation("showing");
                    break;
                default:
                    
                    break;
            }
            
        }
        
        public Circle getCollisionCircle() {
            Vector2 position = new Vector2(this.getX(), this.getY());
            Vector2 center = position.add(FRAME_WIDTH / 2.0f, FRAME_HEIGHT / 2.0f);

            return new Circle(center, (FRAME_WIDTH + FRAME_HEIGHT) / 4.0f);
        }
        
        
    }

    class Hammer extends MultiAnimatedSprite {

        static final int FRAME_WIDTH = 70;
        static final int FRAME_HEIGHT = 60;

        private HammerState state;
        
        private static final float CLICK_TIME = 0.5f;
        
        private float accumulated;
        
        Hammer(final Texture hammerTexture) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            hammerTexture, FRAME_WIDTH, FRAME_HEIGHT);

                    Animation normal = new Animation(0.01f,
                            frames[0][0]
                    );

                    normal.setPlayMode(Animation.PlayMode.NORMAL);
                    put("normal", normal);
                    
                    Animation click = new Animation(CLICK_TIME,
                            frames[0][1],
                            frames[0][2],
                            frames[0][3],
                            frames[0][2],
                            frames[0][1],
                            frames[0][0]
                    );

                    click.setPlayMode(Animation.PlayMode.NORMAL);
                    put("click", click);

                }
            }, "normal");

            this.state = HammerState.IDLE;
            accumulated = 0.0f;
            
            super.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
            super.setAutoUpdate(false);

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

            Vector2 position = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(position);
            this.setCenter(position.x, position.y);

            switch(state){
                case ANIMATION:
                    if(accumulated >= CLICK_TIME){
                        startAnimation("normal");
                        accumulated = 0.0f;
                        state = HammerState.IDLE;
                    }
                    else{
                        accumulated += dt;
                    }
                    break;
                case IDLE:
                    break;
                default:
                    
                    break;
            }
        }

        public void click(){
            this.startAnimation("click");
            accumulated = 0.0f;
            state = HammerState.ANIMATION;
        }
        
        public Circle getCollisionCircle() {
            Vector2 position = new Vector2(this.getX(), this.getY());
            Vector2 center = position.add(FRAME_WIDTH / 2.0f, FRAME_HEIGHT / 2.0f);

            return new Circle(center, (FRAME_WIDTH + FRAME_HEIGHT) / 4.0f);
        }
        
    }

    enum HammerState{
        ANIMATION,
        IDLE
    }
    
    enum GopherState{
        SHOWED,
        SHOWING,
        HIDDEN,
        HIDING_HIT,
        HIDING_MISS
    }
}
