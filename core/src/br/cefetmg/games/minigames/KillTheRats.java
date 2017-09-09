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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    
    private Texture backgroundTexture;
    private Texture catTexture;
    private Texture ratsSpriteSheet;
    private Texture fireTexture;
    
    private Sound levelSound;
    private Sound ratsSound;
    private Array<Sound> ratSound;
    private Array<Sound> fireSound;
    
    private Background background;
    private Cat cat;
    private Array<Rat> rats;
    private Array<Fire> fires;
    
    private Vector2 mousePos;
    
    private float countTimer;
    private boolean releaseFire;
    private boolean stopAllSounds;
    
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
        backgroundTexture = assets.get("kill-the-rats/Background_Sewer.png", Texture.class);
        catTexture = assets.get("kill-the-rats/lakitu.png", Texture.class);
        ratsSpriteSheet = assets.get("kill-the-rats/ratframes.png", Texture.class);
        fireTexture = assets.get("kill-the-rats/fireball_0.png", Texture.class);
        
        levelSound = assets.get("kill-the-rats/JerryFive.mp3", Sound.class);
        ratsSound = assets.get("kill-the-rats/Rats_Ambience.mp3", Sound.class);
        
        init();
        initBackground();
        initCat();
        initRat();
        initFire();
    }
    
    private void init() {
        mousePos = new Vector2(0, 0);
        maxNumRats = 100;
        maxNumFires = 100;
        countTimer = 0;
        releaseFire = true;
        stopAllSounds = false;
        
        levelSound.play();
        ratsSound.play(3.0f);
    }
    
    private void initBackground() {
        background = new Background(backgroundTexture);
        background.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
    }
    
    private void initCat() {
        cat = new Cat(catTexture);
        cat.setCenter(viewport.getWorldWidth() * 0.2f, viewport.getWorldHeight() / 2f);
    }
    
    private void initRat() {
        rats = new Array<Rat>();
        ratSound = new Array<Sound>();
        
        for (int i = 0; i < maxNumRats; i++) {
            ratSound.add(assets.get("kill-the-rats/FieldRat.mp3", Sound.class));
            Rat rat = new Rat(ratsSpriteSheet, ratSound.get(i));
            //rat.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
            this.rats.add(rat);
        }
    }
    
    private void initFire() {
        fires = new Array<Fire>();
        //TextureRegion[][] frames = TextureRegion.split(fireTexture,
        //        fireTexture.getWidth(), fireTexture.getHeight());
        fireSound = new Array<Sound>();
        
        for (int i = 0; i < maxNumFires; i++) {
            Fire fire = new Fire(fireTexture);
            //fire.setCenter(viewport.getWorldWidth() * 0.1f, viewport.getWorldHeight() / 2f);
            fireSound.add(assets.get("kill-the-rats/pistol_silenced_walther.mp3", Sound.class));
            fire.setSound(fireSound.get(i));
            this.fires.add(fire);
        }
    }
    
    @Override
    protected void onEnd() {
        stopAllSounds = true;
        levelSound.stop();
        ratsSound.stop();
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
        mousePos = new Vector2(click.x, click.y);
        //cat.setCenter(click.x, click.y);
        
        for (Fire fire : this.fires) {
            fire.setDirection(mousePos);
            
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
            
            for (Rat rat : this.rats) {
                if (fire.getBoundCirle().overlaps(rat.getBoundCirle())) {
                    rat.reset();
                    //fire.reset();
                    break;
                }
            }
        }
        
        for (Rat rat : this.rats) {
            rat.update(dt);
        }
    }
    
    @Override
    public void onDrawGame() {
        background.draw(batch);
        
        for (Fire fire : this.fires) {
            if (fire.getLaunched())
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
    
    class Background extends AnimatedSprite {
        
        static final float frameDuration = 1.0f;
        
        static final int FRAME_WIDTH = 1280;
        static final int FRAME_HEIGHT = 720;
        
        Background(final Texture catTexture) {
            super(new Animation(frameDuration, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            catTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[] {
                        frames[0][0]
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
            
            init();
        }
        
        public void init() {
            setAlpha(0.95f);
            setPosition(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
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
    }
    
    class Cat extends AnimatedSprite {

        static final float frameDuration = 1.0f;
        
        static final int FRAME_WIDTH = 200;
        static final int FRAME_HEIGHT = 259;
        
        private float collisionRadius;
        private Circle forceField;
        private boolean enableFieldForce;
        private float fieldForceInterval;

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
            fieldForceInterval = 7f;
            collisionRadius = 50;
            setScale(0.6f);
            setPosition(getOriginX(), getOriginY());
            forceField = new Circle(getX(), getY(), collisionRadius*3);
            enableFieldForce = false;
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
        
        @Override
        public void update(float dt) {
            super.update(dt);
            
            forceField.x = getX();
            forceField.y = getY();
            
            if (getTime() > fieldForceInterval)
                enableFieldForce = true;
            
            if (enableFieldForce) {
                
            }
        }
        
        public Rectangle getBoundRect() {
            return new Rectangle(getX(), getY(), getWidth(), getHeight());
        }
        
        public Circle getBoundCirle() {
            return new Circle(getPosition(), collisionRadius);
        }
        
        public Circle getForceField() {
            return this.forceField;
        }
        
        public boolean getEnableFieldForce() {
            return enableFieldForce;
        }
    }
    
    class Rat extends MultiAnimatedSprite {

        private Sound sound;
        private Vector2 direction;
        private float speed;
        private float minSpeed;
        private float maxSpeed;
        private float offset;
        private float wallDist;
        private float time;
        private float collisionRadius;
        private float probabilityFollow;
        private int numCollisions;
        private boolean flipX;
        private boolean folowPlayer;
        
        static final float frameDuration = 0.02f;
        static final int ROWS = 6;
        static final int COLS = 8;

        public Rat(final Texture ratsSpriteSheet, Sound s) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(ratsSpriteSheet,
                                    ratsSpriteSheet.getWidth()/COLS, ratsSpriteSheet.getHeight()/ROWS);
                    Animation walking = new Animation(frameDuration, frames[0]); // todas as colunas da linha 0
                    walking.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                    put("walking", walking);
                }
            }, "walking");
            
            sound = s;
            init();
            reset();
        }
        
        public void init() {
            time = 0;
            flipX = false;
            collisionRadius = 15;
            numCollisions = 0;
            offset = 10;
            wallDist = 80;
            direction = new Vector2();
            speed = 1;
            minSpeed = 2f;
            maxSpeed = 5f;
        }
        
        public void reset() {
            float posY = (float) Math.random() * (viewport.getWorldHeight() - 2*wallDist) + wallDist;
            setPosition(viewport.getWorldWidth() + getWidth(), posY);
            setRotation(90);
            direction.x = -1;
            direction.y = -1 + (float) Math.random() * 2;
            direction.nor();
            speed = (float) Math.random() * maxSpeed + minSpeed;
            folowPlayer = false;
            probabilityFollow = 0.001f;
            
            sound.stop();
            sound.play(0.2f);
        }
        
        public void setSound(Sound s) {
            sound = s;
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
            Vector2 pos = new Vector2(direction).scl(offset);
            pos.add(getPosition());
            return new Circle(pos, collisionRadius);
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

        public void setDirection(Vector2 v) {
            direction.x = v.x - getX();
            direction.y = v.y - getY();
            direction.nor();
            
            double angle = Math.atan(direction.y / direction.x);
            angle += (direction.x > 0) ? -Math.PI/2 : Math.PI/2;
            angle *= 180 / Math.PI;
            
            setRotation((float) angle);
        }
        
        public void walk(Vector2 direction) {
            Vector2 normalized = new Vector2(direction);
            normalized.scl(speed);
            
            normalized.x += getX();
            normalized.y += getY();
            
            setPosition(normalized.x, normalized.y);
        }
        
        // cálculo do vetor tangente ao campo de força
        public Vector2 tangentForceField() {
            Circle forceField = cat.getForceField();
            float distance = cat.getPosition().dst(getPosition());
            
            float a = forceField.radius*forceField.radius / (distance);
            Vector2 aux = new Vector2(getPosition());
            // vetor que aponta do centro de "forceField" para a posicao atual
            aux.sub(cat.getPosition());
            aux.nor().scl(a); // o tamanho do vetor é limitado à projeção ortogonal do ponto de intersecção com a tangente
            
            // altura do triangulo retângulo formado pelos pontos "intersectionPoint", posição atual e o centro de "forceField"
            float h = (float) Math.sqrt(a*(distance - a));
            Vector2 intersectionPoint = new Vector2(-aux.y, aux.x); // rotaciona em +90 graus
            intersectionPoint.nor().scl(h); // vetor que representa a altura do triangulo retângulo
            // vetor que aponta do centro de "forceField" para o ponto de intercecção com a tangente
            intersectionPoint.add(aux).add(forceField.x, forceField.y);
            
            Vector2 tangent = new Vector2(intersectionPoint);
            return tangent.sub(getPosition()).nor();
        }
        
        @Override
        public void update(float dt) {
            super.update(dt);
            
            if (stopAllSounds)
                sound.stop();
            
            time += dt;
            if (time >= 2*COLS*frameDuration) {
                flipX = !flipX;
                time = 0;
            }
            
            if (flipX)
                setFlip(true, false);
            
            if (folowPlayer)
                setDirection(cat.getPosition());
            else {
                folowPlayer = (Math.random() < probabilityFollow);
            }
            
            if (cat.getEnableFieldForce()) {
                folowPlayer = true;
                walk(tangentForceField());
            }
            else {
                walk(direction);
            }
            
            if (getX() < 0)
                reset();
        }
    }
    
    class Fire extends AnimatedSprite {

        static final float fireInterval = 2.0f;
        static final float frameDuration = 0.1f;
        
        static final int WIDTH = 64;
        static final int HEIGHT = 64;
        
        private Sound sound;
        private float speed;
        private float offset;
        private float collisionRadius;
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
            
            init();
            reset();
        }
        
        public void init() {
            //setScale(0.1f);
            offset = 10;
            collisionRadius = 10;
        }
        
        public void reset() {
            direction = new Vector2(0, 0);
            setPosition(cat.getX(), cat.getY());
            speed = 20;
            launched = false;
        }
        
        public void setSound(Sound s) {
            sound = s;
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
            Vector2 pos = new Vector2(direction).nor().scl(2*offset);
            pos.add(getPosition());
            return new Circle(pos, collisionRadius);
        }
        
        public Boolean getLaunched() {
            return launched;
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
                
                sound.play(0.2f);
            }
        }
        
        @Override
        public void update(float dt) {
            super.update(dt);
            
            if (stopAllSounds)
                sound.stop();
            
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
