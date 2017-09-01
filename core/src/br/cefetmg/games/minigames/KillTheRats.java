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
        maxNumFires = 10;
        
        initializeFire();
    }
    
    private void initializeFire() {
        TextureRegion[][] frames = TextureRegion.split(fireTexture,
                Fire.FRAME_WIDTH, Fire.FRAME_HEIGHT);
        
        Fire fire = new Fire(frames[0][0]);
        fire.setCenter(
                viewport.getWorldWidth() * 0.1f,
                viewport.getWorldHeight() / 2f);
        this.fires.add(fire);
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
        cat.setCenter(click.x, click.y);
        
        for (Fire fire : this.fires) {
            fire.setDirection(new Vector2(click.x, click.y));
            
            if (Gdx.input.isTouched()) {
                fire.follow();
            }
        }
    }
    
    @Override
    public void onUpdate(float dt) {
        cat.update(dt);
    }
    
    @Override
    public void onDrawGame() {
        cat.draw(batch);
        
        for (Fire fire : this.fires) {
            fire.draw(batch);
        }
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
            
            setScale(0.5f);
        }

        Vector2 getPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getPosition().dst(enemyX, enemyY);
        }
    }
    
    class Fire extends Sprite {

        static final int FRAME_WIDTH = 360;
        static final int FRAME_HEIGHT = 720;
        
        private float speed;
        private float offset;
        private Vector2 direction;

        public Fire(TextureRegion fireTexture) {
            super(fireTexture);
            init();
        }
        
        public void init() {
            setScale(0.1f);
            direction = new Vector2(0, 0);
            speed = 10;
            offset = 10;
        }
        
        public void setDirection(Vector2 v) {
            float posX = getX() + FRAME_WIDTH / 2;
            float posY = getY() + FRAME_HEIGHT / 2;
            
            direction.x = v.x - posX;
            direction.y = v.y - posY;
            
            double angle = Math.atan(direction.y / direction.x);
            angle += (direction.x > 0) ? -Math.PI/2 : Math.PI/2;
            angle *= 180 / Math.PI;
            
            setRotation((float) angle);
        }
        
        public void follow() {
            if (direction.len() < offset)
                return;
            
            direction.nor(); // normaliza o vetor
            float newPosX = getX() + direction.x * speed;
            float newPosY = getY() + direction.y * speed;
            
            setPosition(newPosX, newPosY);
        }
    }
}
