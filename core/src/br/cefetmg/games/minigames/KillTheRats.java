/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author acces
 */
public class KillTheRats extends MiniGame {
    
    private Texture catTexture;
    private Texture fireTexture;
    private Cat cat;
    private Array<Fire> fires;
    
    private float countTimer;
    private boolean releaseFire;
    
    // variáveis de desafio
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private int maxNumFires;
    
    public KillTheRats(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 100f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }
    
    @Override
    protected void onStart() {
        catTexture = assets.get("kill-the-rats/lakitu.png", Texture.class);
        fireTexture = assets.get("kill-the-rats/rocket.png", Texture.class);
        
        cat = new Cat(catTexture);
        fires = new Array<Fire>();
        
        maxNumFires = 100;
        countTimer = 0;
        releaseFire = true;
        
        initCat();
        initFire();
    }
    
    private void initCat() {
        cat.setCenter(viewport.getWorldWidth() * 0.1f, viewport.getWorldHeight() / 2f);
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
            fire.updateMoves(dt);
        }
    }
    
    @Override
    public void onDrawGame() {
        for (Fire fire : this.fires) {
            fire.draw(batch);
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

        static final int FRAME_WIDTH = 200;
        static final int FRAME_HEIGHT = 259;

        Cat(final Texture catTexture) {
            super(new Animation(1.0f, new Array<TextureRegion>() {
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
    
    class Fire extends AnimatedSprite {

        static final float fireInterval = 3.0f;
        
        private float speed;
        private float offset;
        private boolean launched;
        private Vector2 direction;

        public Fire(final Texture fireTexture) {
            super(new Animation(1.0f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            fireTexture, fireTexture.getWidth(), fireTexture.getHeight());
                    super.addAll(new TextureRegion[]{
                        frames[0][0]
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
            
            defineProperties();
            reset();
        }
        
        public void defineProperties() {
            setScale(0.1f);
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
        
        public void setDirection(Vector2 v) {
            if (launched) return;
            
            direction.x = v.x - getX();
            direction.y = v.y - getY();
            
            double angle = Math.atan(direction.y / direction.x);
            angle += (direction.x > 0) ? -Math.PI/2 : Math.PI/2;
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
        
        public void updateMoves(float dt) {
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
