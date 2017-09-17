package br.cefetmg.games.minigames;

import br.cefetmg.games.Colision;
import static br.cefetmg.games.Config.*;
import br.cefetmg.games.Obstacle;
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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

public class CatAvoider extends MiniGame {

    public CatAvoider(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }
    
    private Texture backgroundTexture; //backgroud texture of the world
    private Sprite background;//sprite created for the background
    
    private Texture limits;//texture created for the screen limits
    private float limitsWidth;//width of the screen limits
    private float limitsHeight;//height of the screen limits
    private Obstacle down;//scren limit down
    private Obstacle up;//screen limit up
    private Obstacle left;//screen limit left
    private Obstacle right;//screen limit right
    
    protected Random randomGenerator = new Random();//objetct to generate random numbers
    
    class Mouse {
        protected Sprite mouse;//sprite for the mouse(playable character)
        protected Vector2 position;//vector to get the mouse positon on the screen
        
        public void getPosition() {
            position.x = Gdx.input.getX()*WORLD_WIDTH/viewport.getScreenWidth();
            position.y = WORLD_HEIGHT - (Gdx.input.getY()*WORLD_HEIGHT/viewport.getScreenHeight());
        }
    }
    
    class Cat {
        protected Rectangle rect;//rectangle to enclose the cat and treat the collision
        protected Texture texture;//texture for the non playable character ninja cat
        protected Sprite sprite;//sprite of the non non playable character ninja cat
        protected Vector2 direction;//vetor to get the cat direction on the screen
        protected float speed = 10;//variable used to set the cat ninja speed
        protected float delta = 10;//variable used to set the delta of displacemento of the cat ninja in the screen per period of time
        protected char moveType; //variable to set the type of moviment of the cat (jump or random)
        protected int state;//variable to indicate the type of moviment of the cat (jump or random)
        protected final int randomState = 0;
        protected final int jumpState = 1;
        protected Mouse mouse = new Mouse();
        
        public void lookAhead() {
        double angle = Math.atan(direction.y / direction.x);

            angle += (direction.x > 0) ? -Math.PI / 2 : Math.PI / 2;
            angle *= 180 / Math.PI;
            sprite.setRotation((float) angle);
        }
        
        public void setDirection() {
            direction.x = mouse.position.x - (sprite.getX() + sprite.getWidth()/2);
            direction.y = mouse.position.y - (sprite.getY() + sprite.getHeight()/2);
            lookAhead();
        }
  
        public void incrementX(float delta) {
        if((sprite.getX()+limitsWidth+sprite.getWidth()+delta)<WORLD_WIDTH && (moveType=='D' || moveType=='U'))
            sprite.setX(sprite.getX()+delta);
        }
    
        public void decrementX(float delta) {
            if((sprite.getX()-limitsWidth-sprite.getWidth()-delta>0) && (moveType=='D' || moveType=='U'))
                sprite.setX(sprite.getX()-delta);
        }
    
        public void incrementY(float delta) {
            if((sprite.getY()+limitsHeight+sprite.getHeight()+delta)<WORLD_HEIGHT && (moveType=='L' || moveType=='R'))
                sprite.setY(sprite.getY()+delta);
        }
    
        public void decrementY(float delta) {
            if((sprite.getY()-limitsHeight-sprite.getHeight()-delta)>0 && (moveType=='L' || moveType=='R'))
                sprite.setY(sprite.getY()-delta);
        }
    
        public void randomMovementX(int signal) {
            if(signal==0) {
                incrementX(delta);
            }
            else if(signal==1){
                decrementX(delta);
            }
        }
    
        public void randomMovementY(int signal) {
            if(signal==0) {
                incrementY(delta);
            }
            else if(signal==1){
                decrementY(delta);
            }
        }
    
        //tratar a colisão em Random moviment
        public void moveRandom(int signal) {
            final int move = 1;
            int movement = move;
            
            if(movement==move) {
                if(moveType=='D' || moveType=='U') {
                    randomMovementX(signal);
                }
                if(moveType=='L' || moveType=='R') {
                    randomMovementY(signal);
                }
            }
        }
    
        public void move(){
            int changeState = randomGenerator.nextInt(30);
            int signal = 0;
            if (state==jumpState) {
                jump();
                verifyCollision();
                signal = randomGenerator.nextInt(2);
            }
            else {
                moveRandom(signal);
            }
        }

        public void jump() {
            Vector2 normalized = new Vector2(direction);
            normalized.nor();
            normalized.scl(speed);

            normalized.x += sprite.getX();
            normalized.y += sprite.getY();

            sprite.setPosition(normalized.x, normalized.y);
            System.out.println("Mouse: "+mouse.position.x +" "+ mouse.position.y);
            System.out.println("Cat: "+sprite.getX() +" "+ sprite.getY());
        }

        public void reflect() {
            direction.x = mouse.position.x - (sprite.getX() + sprite.getWidth()/2);
            direction.y = mouse.position.y - (sprite.getY() + sprite.getHeight()/2);
            lookAhead();
        }

        public void verifyCollision() {
            cat.rect = cat.sprite.getBoundingRectangle();
            /*
            //collision cat mouse
            if (Colision.rectCircleOverlap(catRect, ball.circle) != null) {

            }
             */
            
            /**collision cat floor*/
            if (Colision.rectsOverlap(down.getRec(), rect)) {
                reflect();
                moveType = 'D';
                state = randomState;
            }
            
            /**collision cat roof*/
            else if (Colision.rectsOverlap(up.getRec(), rect)) {
                reflect();
                moveType = 'U';
                state = randomState;
            }

            /**collision cat left wall*/
            if (Colision.rectsOverlap(left.getRec(), rect)) {
                reflect();
                moveType = 'L';
                state = randomState;
            }
            
            /**collision cat right wall*/
            else if (Colision.rectsOverlap(right.getRec(), rect)) {
                reflect();
                moveType = 'R';
                state = randomState;
            }
        }
    }
    
    Cat cat = new Cat();

    @Override
    protected void onStart() {
        backgroundTexture = assets.get("avoider/backgroundTexture.png", Texture.class);
        background = new Sprite(backgroundTexture);
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        
        limitsWidth = 20;
        limitsHeight = 20;
        limits = assets.get("avoider/grey.png", Texture.class);
        up = new Obstacle(batch, new Vector2(0, WORLD_HEIGHT - limitsHeight), WORLD_WIDTH, limitsHeight);
        down = new Obstacle(batch, new Vector2(0, 0), WORLD_WIDTH, limitsHeight);
        left = new Obstacle(batch, new Vector2(0, limitsWidth), limitsWidth, WORLD_HEIGHT);
        right = new Obstacle(batch, new Vector2(WORLD_WIDTH - limitsWidth, limitsWidth), limitsWidth, WORLD_HEIGHT);
        
        cat.texture = assets.get("avoider/cat.png", Texture.class);
        cat.direction = new Vector2(0, 0);
        cat.sprite = new Sprite(cat.texture);
        cat.sprite.setSize(100, 100);
        cat.sprite.setOrigin(cat.sprite.getWidth()/2, cat.sprite.getHeight()/2);
        cat.sprite.setPosition(WORLD_WIDTH / 2, limitsWidth);
        cat.moveType = 'D';
        cat.state = cat.randomState;
        
        cat.mouse.position = new Vector2(Gdx.input.getX(), Gdx.input.getY());
    }
    
    

    @Override
    protected void configureDifficultyParameters(float difficulty) {
    }

    @Override
    public void onHandlePlayingInput() {
    }

    

    @Override
    public void onUpdate(float dt) {
       int changeState = randomGenerator.nextInt(30);
       if(changeState==29)
           cat.state = cat.jumpState;
        cat.mouse.getPosition();
        cat.move();
    }

    @Override
    public void onDrawGame() {
        background.draw(batch);
        
        down.draw();
        up.draw();
        left.draw();
        right.draw();
        
        cat.sprite.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Não deixe o gato pegar o novelo!!!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
}