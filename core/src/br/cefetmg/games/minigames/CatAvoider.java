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

public class CatAvoider extends MiniGame {

    private boolean jumped;
    private Texture backgroundTexture;
    private Sprite background;
    private Texture limits;
    private float width;
    private float height;
    private Obstacle down;
    private Obstacle up;
    private Obstacle left;
    private Obstacle right;
    private Texture catTexture;
    private Rectangle catRect;
    private Sprite cat;
    private Sprite mouse;
    private Vector2 mousePosition, direction;
    private float speed = 10, timeAnimation = 1;

    public CatAvoider(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        width = 20;
        height = 20;
        jumped = false;
        direction = new Vector2(0, 0);
        mousePosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        backgroundTexture = assets.get("avoider/backgroundTexture.png", Texture.class);
        limits = assets.get("avoider/grey.png", Texture.class);
        catTexture = assets.get("avoider/cat.png", Texture.class);

        background = new Sprite(backgroundTexture);

        cat = new Sprite(catTexture);
        cat.setSize(100, 100);
        cat.setOrigin(cat.getWidth()/2, cat.getHeight()/2);
        cat.setPosition(WORLD_WIDTH / 2, width);

        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);

        up = new Obstacle(batch, new Vector2(0, WORLD_HEIGHT - height), WORLD_WIDTH, height);
        down = new Obstacle(batch, new Vector2(0, 0), WORLD_WIDTH, height);
        left = new Obstacle(batch, new Vector2(0, width), width, WORLD_HEIGHT);
        right = new Obstacle(batch, new Vector2(WORLD_WIDTH - width, width), width, WORLD_HEIGHT);
    }
    
    public void verifyCollision(float dt) {
        catRect = cat.getBoundingRectangle();
        /*
        //Colisão cat mouse
        if (Colision.rectCircleOverlap(catRect, ball.circle) != null) {
            
        }
         */
        //Colisão gato chao
        if (Colision.rectsOverlap(down.getRec(), catRect)) {
            reflect();
        } //Colisão gato teto
        else if (Colision.rectsOverlap(up.getRec(), catRect)) {
            reflect();
        }

        //Colisão lateral esquerda e gato
        if (Colision.rectsOverlap(left.getRec(), catRect)) {
            reflect();
        }//Colisão lateral direita e gato
        else if (Colision.rectsOverlap(right.getRec(), catRect)) {
            reflect();
        }

    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
    }

    @Override
    public void onHandlePlayingInput() {
    }

    public void lookAhead() {
        double angle = Math.atan(direction.y / direction.x);

        angle += (direction.x > 0) ? -Math.PI / 2 : Math.PI / 2;
        /*else
			angle += (direction.x > 0) ? Math.PI : 0;
         */
        angle *= 180 / Math.PI;
        cat.setRotation((float) angle);
    }

    public void updateCatPosition() {
        if (jumped) {
            //setDirection();//atualiza ou não a direção
            Vector2 normalized = new Vector2(direction);
            normalized.nor(); // normaliza o vetor
            normalized.scl(speed);

            normalized.x += cat.getX();
            normalized.y += cat.getY();

            cat.setPosition(normalized.x, normalized.y);
            System.out.println("Mouse: "+mousePosition.x +" "+ mousePosition.y);
            System.out.println("Cat: "+cat.getX() +" "+ cat.getY());
        }
    }

    public void setDirection() {
        direction.x = mousePosition.x - (cat.getX() + cat.getWidth()/2);
        direction.y = mousePosition.y - (cat.getY() + cat.getHeight()/2);
        lookAhead();
    }
    
    public void reflect() {
        direction.x = mousePosition.x - (cat.getX() + cat.getWidth()/2);
        direction.y = mousePosition.y - (cat.getY() + cat.getHeight()/2);
        lookAhead();
    }
  
    public void jump(){
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (jumped == false) {
                setDirection();
                jumped = true;
            }
        }
    }

    public void follow() {
        Vector2 normalized = new Vector2(direction);
        normalized.nor(); // normaliza o vetor
        normalized.scl(speed);

        normalized.x += cat.getX();
        normalized.y += cat.getY();

        cat.setPosition(normalized.x, normalized.y);
    }

    public void getMousePosition() {
        mousePosition.x = Gdx.input.getX()*WORLD_WIDTH/viewport.getScreenWidth();
        mousePosition.y = WORLD_HEIGHT - (Gdx.input.getY()*WORLD_HEIGHT/viewport.getScreenHeight());
    }

    @Override
    public void onUpdate(float dt) {
        getMousePosition();
        jump();
        updateCatPosition();
        verifyCollision(1);
    }

    @Override
    public void onDrawGame() {
        background.draw(batch);
        down.draw();
        up.draw();
        left.draw();
        right.draw();
        cat.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Não deixe o gato pegar o mouse";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
}