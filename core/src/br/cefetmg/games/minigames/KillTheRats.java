/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author acces
 */
public class KillTheRats extends MiniGame {
    
    private Texture catTexture;
    private Texture ratsSpriteSheet;
    private Texture fireTexture;
    private Cat cat;
    private Array<Rat> rats;
    private Array<Fire> fires;
    
    private float countTimer;
    private boolean releaseFire;
    
    // variáveis de desafio
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private int maxNumRats;
    private int maxNumFires;
    
    public KillTheRats(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 20f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }
    
    @Override
    protected void onStart() {
        catTexture = assets.get("kill-the-rats/lakitu.png", Texture.class);
        ratsSpriteSheet = assets.get("kill-the-rats/ratframes.png", Texture.class);
        fireTexture = assets.get("kill-the-rats/fireball_0.png", Texture.class);
        
        cat = new Cat(catTexture);
        rats = new Array<Rat>();
        fires = new Array<Fire>();
        
        maxNumRats = 100;
        maxNumFires = 100;
        countTimer = 0;
        releaseFire = true;
        
        initCat();
        initRat();
        initFire();
    }
    
    private void initCat() {
        cat.setCenter(viewport.getWorldWidth() * 0.1f, viewport.getWorldHeight() / 2f);
    }
    
    private void initRat() {
        for (int i = 0; i < maxNumRats; i++) {
            Rat rat = new Rat(ratsSpriteSheet);
            //rat.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
            this.rats.add(rat);
        }
    }
    
    private void initFire() {
        //TextureRegion[][] frames = TextureRegion.split(fireTexture,
        //        fireTexture.getWidth(), fireTexture.getHeight());
        
        for (int i = 0; i < maxNumFires; i++) {
            Fire fire = new Fire(fireTexture);
            //fire.setCenter(viewport.getWorldWidth() * 0.1f, viewport.getWorldHeight() / 2f);
            this.fires.add(fire);
        }
    }
    
    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 120, 220);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 240, 340);
    }
    
    @Override
    public void onHandlePlayingInput() {
        // obtem a posição do mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        //cat.setCenter(click.x, click.y);
        
        for (Fire fire : this.fires) {
            fire.setDirection(new Vector2(click.x, click.y));
            
            if (Gdx.input.isTouched()) {
                fire.enableOnceFollow();
            }
        }
    }
    
    @Override
    public void onUpdate(float dt) {
        cat.update(dt);
        
        for (Fire fire : this.fires) {
            fire.update(dt);
        }
        
        for (Rat rat : this.rats) {
            rat.update(dt);
        }
    }
    
    @Override
    public void onDrawGame() {
        for (Fire fire : this.fires) {
            fire.draw(batch);
        }
        
        for (Rat rat : this.rats) {
            rat.draw(batch);
        }
        
        cat.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Ataque os Ratos";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
    
    class Cat extends AnimatedSprite {

        static final float frameDuration = 1.0f;
        
        static final int FRAME_WIDTH = 200;
        static final int FRAME_HEIGHT = 259;

        Cat(final Texture catTexture) {
            super(new Animation(frameDuration, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            catTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
            
            reset();
        }
        
        public void reset() {
            setScale(0.6f);
            setPosition(getOriginX(), getOriginY());
        }
        
        @Override
        public void setPosition(float x, float y) {
            super.setPosition(x - super.getWidth()/2, y - super.getHeight()/2);
        }
        
        @Override
        public float getX() {
            return super.getX() + super.getWidth() / 2;
        }
        
        @Override
        public float getY() {
            return super.getY() + super.getHeight() / 2;
        }

        Vector2 getPosition() {
            return new Vector2(
                    this.getX() + this.getWidth(),
                    this.getY() + this.getHeight());
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getPosition().dst(enemyX, enemyY);
        }
    }
    
    class Rat extends MultiAnimatedSprite {

        private Vector2 direction;
        private float speed;
        private float minSpeed;
        private float maxSpeed;
        private float offset;
        private float time;
        private boolean flipX;
        private int numCollisions;
        
        static final float frameDuration = 0.02f;
        static final int ROWS = 6;
        static final int COLS = 8;

        public Rat(final Texture ratsSpriteSheet) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(ratsSpriteSheet,
                                    ratsSpriteSheet.getWidth()/COLS, ratsSpriteSheet.getHeight()/ROWS);
                    Animation walking = new Animation(frameDuration,
                            frames[0][0], frames[0][1],
                            frames[0][2], frames[0][3],
                            frames[0][4], frames[0][5],
                            frames[0][6], frames[0][7]);
                    walking.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                    put("walking", walking);
                }
            }, "walking");
            
            init();
            reset();
        }
        
        public void init() {
            time = 0;
            flipX = false;
            numCollisions = 0;
            offset = 10;
            direction = new Vector2();
            speed = 1;
            minSpeed = 1f;
            maxSpeed = 5f;
        }
        
        public void reset() {
            float posY = (float) Math.random() * viewport.getWorldHeight();
            setPosition(viewport.getWorldWidth() + getWidth(), posY);
            setRotation(90);
            direction.x = -1;
            direction.y = 0;
            speed = (float) Math.random() * maxSpeed + minSpeed;
        }
        
        @Override
        public void setPosition(float x, float y) {
            super.setPosition(x - super.getWidth()/2, y - super.getHeight()/2);
        }
        
        @Override
        public float getX() {
            return super.getX() + super.getWidth() / 2;
        }
        
        @Override
        public float getY() {
            return super.getY() + super.getHeight() / 2;
        }
        
        public Vector2 getPosition() {
            return new Vector2(getX(), getY());
        }
        
        public Rectangle getBoundRect() {
            return new Rectangle(getX(), getY(), getWidth(), getHeight());
        }
        
        public Circle getBoundCirle() {
            Vector2 pos = getPosition().add(direction);
            return new Circle(pos, Math.max(getWidth(), getHeight()));
        }
        
        public void verifyCollision(Circle c) {
            if (getBoundCirle().overlaps(c))
                numCollisions++;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        @Override
        public void update(float dt) {
            super.update(dt);
            
            time += dt;
            if (time >= 2*COLS*frameDuration) {
                flipX = !flipX;
                time = 0;
            }
            
            if (flipX)
                setFlip(true, false);
            
            Vector2 newPos = new Vector2(direction);
            newPos.scl(speed);
            setPosition(getX() + newPos.x, getY() + newPos.y);
            
            if (getX() < 0)
                reset();
        }
        
        public void setDirection(Vector2 v) {
            direction.x = v.x - getX();
            direction.y = v.y - getY();
            
            double angle = Math.atan(direction.y / direction.x);
            angle += (direction.x > 0) ? Math.PI : 0;
            angle *= 180 / Math.PI;
            
            setRotation((float) angle);
        }
        
        public void follow() {
            if (direction.len() < offset)
                return;
            
            Vector2 normalized = new Vector2(direction);
            normalized.nor(); // normaliza o vetor
            normalized.scl(speed);
            
            normalized.x += getX();
            normalized.y += getY();
            
            setPosition(normalized.x, normalized.y);
        }
    }
    
    class Fire extends AnimatedSprite {

        static final float fireInterval = 6.0f;
        static final float frameDuration = 0.1f;
        
        static final int WIDTH = 64;
        static final int HEIGHT = 64;
        
        private float speed;
        private float offset;
        private boolean launched;
        private Vector2 direction;

        public Fire(final Texture fireTexture) {
            super(new Animation(frameDuration, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            fireTexture, WIDTH, HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0], frames[0][1],
                        frames[0][2], frames[0][3],
                        frames[0][4], frames[0][5],
                        frames[0][6], frames[0][7]
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
            
            defineProperties();
            reset();
        }
        
        public void defineProperties() {
            //setScale(0.1f);
            offset = 10;
        }
        
        public void reset() {
            direction = new Vector2(0, 0);
            setPosition(cat.getX(), cat.getY());
            speed = 20;
            launched = false;
        }
        
        @Override
        public void setPosition(float x, float y) {
            super.setPosition(x - super.getWidth()/2, y - super.getHeight()/2);
        }
        
        @Override
        public float getX() {
            return super.getX() + super.getWidth() / 2;
        }
        
        @Override
        public float getY() {
            return super.getY() + super.getHeight() / 2;
        }
        
        public Vector2 getPosition() {
            return new Vector2(getX(), getY());
        }
        
        public Rectangle getBoundRect() {
            return new Rectangle(getX(), getY(), getWidth(), getHeight());
        }
        
        public Circle getBoundCirle() {
            return new Circle(getPosition(), Math.max(getWidth(), getHeight()));
        }
        
        public void setDirection(Vector2 v) {
            if (launched) return;
            
            direction.x = v.x - getX();
            direction.y = v.y - getY();
            
            double angle = Math.atan(direction.y / direction.x);
            angle += (direction.x > 0) ? Math.PI : 0;
            angle *= 180 / Math.PI;
            
            setRotation((float) angle);
        }
        
        public void follow() {
            if (direction.len() < offset)
                return;
            
            Vector2 normalized = new Vector2(direction);
            normalized.nor(); // normaliza o vetor
            normalized.scl(speed);
            
            normalized.x += getX();
            normalized.y += getY();
            
            setPosition(normalized.x, normalized.y);
        }
        
        public void enableOnceFollow() {
            if (releaseFire && !launched) {
                launched = true;
                releaseFire = false;
            }
        }
        
        @Override
        public void update(float dt) {
            super.update(dt);
            
            if (launched)
                follow();
            
            if (countTimer > fireInterval) {
                countTimer = 0;
                releaseFire = true;
            }
            if (!releaseFire)
                countTimer += dt;
            
            if (getX() < 0 || getX() > viewport.getWorldWidth() || getY() < 0 || getY() > viewport.getWorldHeight())
                reset();
        }
    }
}
