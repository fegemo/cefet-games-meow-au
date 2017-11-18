
package br.cefetmg.games.minigames;

import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.sound.MySound;
import br.cefetmg.games.sound.MyMusic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    
    private Texture backgroundTexture;
    private Texture catTexture;
    private Texture ratsSpriteSheet;
    private Texture fireTexture;
    private Texture rocketTexture;
    private Texture miniExplosionTexture;
    private Texture bigExplosionTexture;
    private Texture primaryWeapon_Texture;
    private Texture secondaryWeapon_Texture;
    
    private MyMusic levelSound;
    private MyMusic ratsSound;
    private MySound bombSound;
    private MySound ratSound;
    private MySound fireSound;
    
    private SwapButton swapButton;
    private Background background;
    private Cat cat;
    private Array<Rat> rats;
    private Array<Fire> fires;
    
    private Vector2 mousePos;
    
    private float countFireTimer;
    private float fireSoundInterval;
    private float fireSoundMinimumInterval;
    private boolean releaseFire;
    private boolean stopAllSounds;
    private boolean changeWeapon;
    
    // variáveis de desafio
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private float percentIgnoreRats;
    private int maxNumRats;
    private int maxNumFires;
    private int countRatsSurvived;
    
    public KillTheRats(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 20f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }
    
    @Override
    protected void onStart() {
        backgroundTexture = assets.get("kill-the-rats/Background_Sewer.png", Texture.class);
        catTexture = assets.get("kill-the-rats/cat_sprite.png", Texture.class);
        ratsSpriteSheet = assets.get("kill-the-rats/ratframes.png", Texture.class);
        fireTexture = assets.get("kill-the-rats/fireball_0.png", Texture.class);
        rocketTexture = assets.get("kill-the-rats/rocket.png", Texture.class);
        miniExplosionTexture = assets.get("kill-the-rats/mini_explosion.png", Texture.class);
        bigExplosionTexture = assets.get("kill-the-rats/big_explosion.png", Texture.class);
        primaryWeapon_Texture = assets.get("kill-the-rats/primary_weapon.png", Texture.class);
        secondaryWeapon_Texture = assets.get("kill-the-rats/secondary_weapon.png", Texture.class);
        
        levelSound = new MyMusic(assets.get("kill-the-rats/JerryFive.mp3", Music.class));
        ratsSound = new MyMusic(assets.get("kill-the-rats/Rats_Ambience.mp3", Music.class));
        ratSound = new MySound(assets.get("kill-the-rats/rat.mp3", Sound.class));
        fireSound = new MySound(assets.get("kill-the-rats/pistol_silenced_walther.mp3", Sound.class));
        bombSound = new MySound(assets.get("kill-the-rats/bomb.mp3", Sound.class));
        
        init();
        initSwapButton();
        initBackground();
        initCat();
        initRat();
        initFire();
    }
    
    private void init() {
        mousePos = new Vector2(0, 0);
        countFireTimer = 0;
        countRatsSurvived = 0;
        //percentIgnoreRats = 0.4f;
        //maxNumRats = 100;
        maxNumFires = 100;
        fireSoundInterval = 0;
        fireSoundMinimumInterval = 6.0f;
        releaseFire = true;
        stopAllSounds = false;
        changeWeapon = false;
        
        levelSound.play();
        levelSound.setVolume(0.8f);
        ratsSound.play();
    }
    
    private void initSwapButton() {
        swapButton = new SwapButton(primaryWeapon_Texture);
        swapButton.setCenter(primaryWeapon_Texture.getWidth()/2.5f, 
                             viewport.getWorldHeight() - primaryWeapon_Texture.getHeight()/3.0f);
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
        
        for (int i = 0; i < maxNumRats; i++) {
            Rat rat = new Rat(ratsSpriteSheet, ratSound);
            //rat.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
            this.rats.add(rat);
        }
    }
    
    private void initFire() {
        fires = new Array<Fire>();
        //TextureRegion[][] frames = TextureRegion.split(fireTexture,
        //        fireTexture.getWidth(), fireTexture.getHeight());
        
        for (int i = 0; i < maxNumFires; i++) {
            Fire fire = new Fire(fireTexture);
            //fire.setCenter(viewport.getWorldWidth() * 0.1f, viewport.getWorldHeight() / 2f);
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
        /*
        // Valores antigos para os parâmetros de dificuldade
        this.maxNumRats = (int) DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 100, 250);
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 2, 6);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 4, 7);
        this.percentIgnoreRats = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 0.75f, 0.3f);
        */
        
        // Valores atualizados para os parâmetros de dificuldade
        this.maxNumRats = (int) DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 80, 160);
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 2, 6);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 4, 7);
        this.percentIgnoreRats = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 0.5f, 0.8f);
    }
    
    @Override
    public void onHandlePlayingInput() {
        // obtem a posição do mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        mousePos = new Vector2(click.x, click.y);
        //cat.setCenter(click.x, click.y);
        
        if (Gdx.input.justTouched())
            swapButton.swap(mousePos);
        
        for (Fire fire : this.fires) {
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
                if (fire.getRocketMode()) {
                    if (fire.getExplodeMode())
                        rat.explode(fire.getPosition());
                }
                else if (fire.getBoundCirle().overlaps(rat.getBoundCirle())) {
                    if (!fire.getExplodeMode())
                        rat.kill();

                    if (rat.getHP() > 1)
                        fire.explode();
                    break;
                }
            }
        }
        
        for (Rat rat : this.rats) {
            rat.update(dt);
        }
        
        if (countRatsSurvived > maxNumRats * percentIgnoreRats) {
            challengeFailed();
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
        swapButton.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Não deixe muitos ratos passarem";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
    
    private class SwapButton extends Sprite {

        static final int FRAME_WIDTH = 256;
        static final int FRAME_HEIGHT = 256;
        
        private float radius;

        SwapButton(final Texture weaponTexture) {
            super(weaponTexture);
            init();
        }
        
        public final void init() {
            setScale(0.4f);
            radius = getWidth()/5;
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

        public Vector2 getPosition() {
            return new Vector2(getX(), getY());
        }
        
        public void swap(Vector2 v) {
            Circle c = new Circle(getX(), getY(), radius);
            
            if (c.contains(v)) {
                changeWeapon = !changeWeapon;
                
                if (changeWeapon)
                    setTexture(secondaryWeapon_Texture);
                else
                    setTexture(primaryWeapon_Texture);
            }
        }
    }
    
    private class Background extends AnimatedSprite {
        
        static final float FRAME_DURATION = 1.0f;
        
        static final int FRAME_WIDTH = 1280;
        static final int FRAME_HEIGHT = 720;
        
        Background(final Texture catTexture) {
            super(new Animation(FRAME_DURATION, new Array<TextureRegion>() {
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
        
        public final void init() {
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
    
    private class Cat extends AnimatedSprite {

        static final float FRAME_DURATION = 0.1f;
        
        static final int FRAME_WIDTH = 118;
        static final int FRAME_HEIGHT = 150;
        
        private float collisionRadius;
        private Circle forceField;
        private boolean enableFieldForce;
        private float fieldForceInterval;

        Cat(final Texture catTexture) {
            super(new Animation(FRAME_DURATION, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            catTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0]
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
            
            reset();
        }
        
        public final void reset() {
            fieldForceInterval = 10f;
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
    
    private class Rat extends MultiAnimatedSprite {
        
        static final float FRAME_DURATION = 0.02f;
        static final float MAX_DIST_EXPLODE = 200.0f;
        static final float EXPLODE_DURATION = 1.0f;
        static final int ROWS = 6;
        static final int COLS = 8;

        private final MySound sound;
        private Color defaultColor;
        private Vector2 direction;
        private float speed;
        private float offset;
        private float wallDist;
        private float time;
        private float collisionRadius;
        private float probabilityFollow;
        private float sumTimer;
        private int countHit;
        private int HP;
        private boolean flipX;
        private boolean surroundPlayer;
        private boolean explodeMode;
        
        public Rat(final Texture ratsSpriteSheet, MySound s) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(ratsSpriteSheet,
                                    ratsSpriteSheet.getWidth()/COLS, ratsSpriteSheet.getHeight()/ROWS);
                    Animation walking = new Animation<TextureRegion>(FRAME_DURATION, frames[0]); // todas as colunas da linha 0
                    walking.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                    put("walking", walking);
                }
            }, "walking");
            
            sound = s;
            init();
            reset();
        }
        
        public final void init() {
            defaultColor = getColor();
            time = 0;
            flipX = false;
            collisionRadius = 15;
            offset = 10;
            wallDist = 80;
            direction = new Vector2();
            speed = 1;
            minimumEnemySpeed = 2f;
            maximumEnemySpeed = 5f;
        }
        
        public final void reset() {
            setColor(defaultColor);
            float posY = (float) Math.random() * (viewport.getWorldHeight() - 2*wallDist) + wallDist;
            setPosition(viewport.getWorldWidth() + getWidth(), posY);
            setRotation(90);
            direction.x = -1;
            direction.y = -0.5f + (float) Math.random();
            direction.nor();
            speed = (float) Math.random() * maximumEnemySpeed + minimumEnemySpeed;
            surroundPlayer = false;
            explodeMode = false;
            countHit = 0;
            HP = 1;
            probabilityFollow = 0.001f;
            sumTimer = 0;
            
            sound.stop();
            sound.play(0.1f);
        }
        
        public void kill() {
            countHit++;
            if (countHit >= HP)
                reset();
        }
        
        public int getHP() {
            return HP;
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
                countHit++;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }
        
        public void lookAhead() {
            double angle = Math.atan(direction.y / direction.x);
            angle += (direction.x > 0) ? -Math.PI/2 : Math.PI/2;
            angle *= 180 / Math.PI;
            
            setRotation((float) angle);
        }

        public void setDirection(Vector2 v) {
            direction.x = v.x - getX();
            direction.y = v.y - getY();
            direction.nor();
            
            lookAhead();
        }
        
        public void explode(Vector2 v) {
            float dist = getPosition().dst2(v);
            
            if (dist <= MAX_DIST_EXPLODE*MAX_DIST_EXPLODE) {
                explodeMode = true;
                direction = getPosition().sub(v);
                direction.nor();
                speed += 1;
                
                setColor(Color.valueOf("FF4500"));
            }
        }
        
        public void walk(Vector2 direction) {
            Vector2 normalized = new Vector2(direction);
            normalized.scl(speed);
            
            normalized.x += getX();
            normalized.y += getY();
            
            setPosition(normalized.x, normalized.y);
        }
        
        // cálculo do vetor tangente a um circulo a partir de uma posição qualquer
        public Vector2 tangentForceField(Circle c) {
            float distance = cat.getPosition().dst(getPosition());
            
            float a = c.radius*c.radius / (distance);
            Vector2 aux = new Vector2(getPosition());
            // vetor que aponta do centro do circulo para a posicao atual
            aux.sub(cat.getPosition());
            aux.nor().scl(a); // o tamanho do vetor é limitado à projeção ortogonal do ponto de intersecção com a tangente
            
            // altura do triangulo retângulo formado pelos pontos "intersectionPoint", posição atual e o centro do circulo
            float h = (float) Math.sqrt(a*(distance - a));
            Vector2 intersectionPoint = new Vector2(-aux.y, aux.x); // rotaciona em +90 graus
            intersectionPoint.nor().scl(h); // vetor que representa a altura do triangulo retângulo
            // vetor que aponta do centro do circulo para o ponto de intercecção com a tangente
            return intersectionPoint.add(aux).add(c.x, c.y);
            
            //Vector2 tangent = new Vector2(intersectionPoint);
            //return tangent.sub(getPosition()).nor();
        }
        
        private void behaviorMove() {
            if (surroundPlayer) {
                HP = 3;
                speed = maximumEnemySpeed;
                Circle c = new Circle(cat.getForceField());
                setDirection(tangentForceField(c));
                walk(direction);
            }
            else {
                if (getY() < wallDist || getY() > viewport.getWorldHeight() - wallDist)
                    direction.y *= -0.8f;
                
                lookAhead();
                walk(direction);
                
                surroundPlayer = (Math.random() < probabilityFollow);
            }
        }
        
        private void updateAnimation(float dt) {
            if (stopAllSounds)
                sound.stop();
            
            time += dt;
            if (time >= 2*COLS*FRAME_DURATION) {
                flipX = !flipX;
                time = 0;
            }
            
            if (flipX)
                setFlip(true, false);
        }
        
        @Override
        public void update(float dt) {
            super.update(dt);
            
            updateAnimation(dt);
            
            if (explodeMode) {
                float rotate = (float)Math.random() * 10;
                setRotation(getRotation() + rotate);
                speed += 0.2f;
                
                walk(direction);
                
                sumTimer += dt;
                if (sumTimer > EXPLODE_DURATION)
                    reset();
                
                return;
            }
            
            behaviorMove();
            
            if (getX() < 0) {
                countRatsSurvived++;
                reset();
            }
        }
    }
    
    private class Fire extends MultiAnimatedSprite {

        static final float FRAME_DURATION = 0.05f;
        //static final float weaponChangeTimer = 10.0f;
        static final float EXPLODE_DURATION = 1.0f;
        static final float ARC_HEIGHT = 100.0f;
        
        private MySound sound;
        private float fireInterval;
        private float speed;
        private float offset;
        private float collisionRadius;
        private float a, b, c;
        private float sumTimer;
        private boolean launched;
        private boolean rocketMode;
        private boolean explodeMode;
        private Vector2 direction;
        private Vector2 explodePos;
        Circle arcCircle;

        public Fire(final Texture fireTexture) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(fireTexture, 64, 64);
                    Animation fireball = new Animation<TextureRegion>(FRAME_DURATION, frames[0]); // todas as colunas da linha 0
                    fireball.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                    put("fireball", fireball);
                    
                    frames = TextureRegion.split(rocketTexture, 360, 720);
                    Animation rocket = new Animation<TextureRegion>(FRAME_DURATION, frames[0]); // todas as colunas da linha 0
                    rocket.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                    put("rocket", rocket);
                    
                    frames = TextureRegion.split(miniExplosionTexture, 96, 96);
                    Animation miniExplosion = new Animation<TextureRegion>(FRAME_DURATION, frames[0]); // todas as colunas da linha 0
                    miniExplosion.setPlayMode(Animation.PlayMode.LOOP);
                    put("miniExplosion", miniExplosion);
                    
                    frames = TextureRegion.split(bigExplosionTexture, 96, 96);
                    Animation bigExplosion = new Animation<TextureRegion>(FRAME_DURATION, 
                            frames[0][0], frames[0][1], frames[0][2], frames[0][3], frames[0][4],
                            frames[1][0], frames[1][1], frames[2][2], frames[1][3], frames[1][4],
                            frames[2][0], frames[2][1], frames[1][2], frames[2][3], frames[2][4]);
                    bigExplosion.setPlayMode(Animation.PlayMode.LOOP);
                    put("bigExplosion", bigExplosion);
                }
            }, "fireball");
            
            init();
            reset();
        }
        
        public final void init() {
            sound = fireSound;
            fireInterval = 2.0f;
            offset = 10;
            collisionRadius = 10;
            rocketMode = false;
            a = b = c = 0;
            arcCircle = new Circle();
        }
        
        public final void reset() {
            setScale(1.0f);
            sound = fireSound;
            direction = new Vector2(0, 0);
            setPosition(cat.getX(), cat.getY());
            speed = 20;
            launched = false;
            explodeMode = false;
            sumTimer = 0;
            
            if (rocketMode)
                fireInterval = 60.0f;
            else
                fireInterval = 2.0f;
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
        
        public Boolean getRocketMode() {
            return rocketMode;
        }
        
        public Boolean getExplodeMode() {
            return explodeMode;
        }
        
        public Circle getArcCircle() {
            return arcCircle;
        }
        
        public void explode() {
            explodeMode = true;
            
            if (rocketMode) {
                setScale(4.0f);
                startAnimation("bigExplosion");
                sound = bombSound;
                sound.play(0.5f);
            }
            else
                startAnimation("miniExplosion");
        }
        
        public void lookAhead() {
            double angle = Math.atan(direction.y / direction.x);
            if (rocketMode)
                angle += (direction.x > 0) ? -Math.PI/2 : Math.PI/2;
            else
                angle += (direction.x > 0) ? Math.PI : 0;
            angle *= 180 / Math.PI;
            
            if (explodeMode)
                setRotation(0);
            else
                setRotation((float) angle);
        }
        
        public void setDirection(Vector2 v) {
            if (launched) return;
            
            direction.x = v.x - getX();
            direction.y = v.y - getY();
            
            lookAhead();
        }
        
        public void follow() {
            lookAhead();
            
            //if (direction.len() < offset)
            //    return;
            
            Vector2 normalized = new Vector2(direction);
            normalized.nor(); // normaliza o vetor
            normalized.scl(speed);
            
            normalized.x += getX();
            normalized.y += getY();
            
            setPosition(normalized.x, normalized.y);
        }
        
        /*
        public void parabole() {
            float x1 = getX();
            float x2 = mousePos.x;
            
            a = -1;
            b = -(x1 + x2); // soma das raizes da equação
            c = -(x1 * x2); // produto das raizes da equação
            
            float delta = b*b - 4*a*c;
            float paraboleHeight = -delta/(4*a);
            float height = viewport.getWorldHeight() / 3;
            
            float newHeight = height / paraboleHeight;
            a *= newHeight;
            b *= newHeight;
            c *= newHeight;
            c += getY();
            //c += mousePos.y;
        }
        */
        
        public Vector2 tangentCircle(Circle c) {
            Vector2 radiusVec = getPosition().sub(c.x, c.y);
            radiusVec.nor();
            radiusVec.scl(c.radius);
            
            if (explodePos.x < cat.getPosition().x)
                radiusVec.scl(-1f);
            
            Vector2 intersectionPoint = new Vector2(radiusVec.y, -radiusVec.x); // rotaciona em -90 graus
            return intersectionPoint.nor();
        }
        
        public void arc(float height) {
            Vector2 distVec = (new Vector2(explodePos)).sub(getPosition());
            float halfDist = distVec.len() / 2;
            
            if (height >= halfDist)
                height = halfDist;
            
            float radius = (halfDist*halfDist + height*height) / (2*height);
            
            distVec.nor();
            distVec.scl(halfDist);
            Vector2 dir = new Vector2(distVec.y, -distVec.x); // rotaciona -90 graus
            dir.nor();
            dir.scl(radius - height);
            
            // centro da circunferencia
            Vector2 center = distVec.add(getPosition()).add(dir);
            arcCircle = new Circle(center.x, center.y, radius);
        }
        
        public void trajectoryCurve(float x) {
            /*
            x += speed; // obtem a abscissa do próximo ponto
            float y = (a*x*x - b*x + c);
            
            // calcula o vetor que inicia na posição atual e aponta para a próxima posição
            direction.x = x - getX();
            direction.y = y - getY();
            //setPosition(x+speed, y);
            */
            
            direction = tangentCircle(arcCircle);
            
            Circle circle = new Circle(getX(), getY(), Math.max(getWidth(), getHeight()));
            if (circle.contains(explodePos)) {
                explode();
            }
        }
        
        public void enableOnceFollow() {
            if (releaseFire && !launched) {
                if (fireSoundInterval >= fireSoundMinimumInterval) {
                    sound.play(0.2f);
                    fireSoundInterval = 0;
                }
                
                explodePos = mousePos;
                //parabole();
                arc(ARC_HEIGHT);
                setDirection(mousePos);
                
                launched = true;
                releaseFire = false;
            }
        }
        
        private void updateWeapon() {
            if (launched) {
                if (rocketMode)
                    trajectoryCurve(getX());
                follow();
            }
            
            //if (!changeWeapon && getTime() >= weaponChangeTimer)
                //changeWeapon = true;
            
            if (!rocketMode && changeWeapon) {
                rocketMode = true;
                startAnimation("rocket");
                reset();
            }
            else if (rocketMode && !changeWeapon) {
                rocketMode = false;
                startAnimation("fireball");
                reset();
            }
        }
        
        @Override
        public void update(float dt) {
            super.update(dt);
            
            fireSoundInterval += dt;
            if (stopAllSounds)
                sound.stop();
            
            if (explodeMode) {
                sumTimer += dt;
                
                if (sumTimer >= EXPLODE_DURATION) {
                    if (rocketMode)
                        startAnimation("rocket");
                    else
                        startAnimation("fireball");
                    
                    reset();
                }
            }
            else {
                updateWeapon();
            
                if (countFireTimer >= fireInterval) {
                    countFireTimer = 0;
                    releaseFire = true;
                }
                if (!releaseFire)
                    countFireTimer += dt;

                if (getX() < 0 || getX() > viewport.getWorldWidth() || getY() < 0 || getY() > viewport.getWorldHeight())
                    reset();
            }
        }
    }
}
