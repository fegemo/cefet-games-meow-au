package br.cefetmg.games.minigames;

import static br.cefetmg.games.Config.*;
import static br.cefetmg.games.minigames.CatAvoider.*;
import static br.cefetmg.games.minigames.HeadSoccer.Player.convertToRad;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;


public class CatAvoider extends MiniGame {
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int WEST = 2;
    public static final int EAST = 3;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public CatAvoider(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    private Texture backgroundTexture; //backgroud texture of the world
    private Sprite backgroundSprite;//sprite created for the background

    private Texture limits;//texture created for the screen limits
    private float limitsWidth;//width of the screen limits
    private float limitsHeight;//height of the screen limits
    private Obstacle down;//scren limit down
    private Obstacle up;//screen limit up
    private Obstacle left;//screen limit left
    private Obstacle right;//screen limit right

    protected Random randomGenerator = new Random();//objetct to generate random numbers
    protected int signal = randomGenerator.nextInt(2);//set the direction of cat moviment in random mode

    protected AnimatedSprite catMovingUpR, catMovingUpL, catMovingDownR, catMovingDownL;
    protected AnimatedSprite catMovingLeft, catMovingRight;
    protected static Music backgroundMusic, impact;
    protected enum catMoveType{upR, upL, downL, downR, left, right}

    protected Wool wool = new Wool();

    Cat cat = new Cat();

    public void verifyCollision() {
        cat.rect = cat.sprite.getBoundingRectangle();

        wool.circle = new Circle();
        wool.circle.set(wool.sprite.getX(), wool.sprite.getY(), 0.25f);

        /*
        /**collision cat wool*/
        if (Colision.rectCircleOverlap(cat.rect, wool.circle) != null) {
            wool.life = 0;
            impact.play();
            super.challengeFailed();
            backgroundMusic.stop();
        }

        /**
         * collision cat floor
         */
        if (Colision.rectsOverlap(down.getBounds(), cat.rect)) {
            cat.sprite.setPosition(cat.sprite.getX(), down.getBounds().y+down.getBounds().height);
            cat.reflect();
            if(wool.position.x > cat.sprite.getX())
                cat.moveType = catMoveType.downR;
            else if(wool.position.x < cat.sprite.getX())
                cat.moveType = catMoveType.downL;
            cat.state = cat.randomState;
        } /**
         * collision cat roof
         */
        else if (Colision.rectsOverlap(up.getBounds(), cat.rect)) {
            cat.sprite.setPosition(cat.sprite.getX(), up.getBounds().y - up.getBounds().height - cat.sprite.getHeight());
            cat.reflect();
            if(wool.position.x > cat.sprite.getX())
                cat.moveType = catMoveType.upR;
            else if(wool.position.x < cat.sprite.getX())
                cat.moveType = catMoveType.upL;
            cat.state = cat.randomState;
        }

        /**
         * collision cat left wall
         */
        if (Colision.rectsOverlap(left.getBounds(), cat.rect)) {
            cat.sprite.setPosition(left.getBounds().x + left.getBounds().width , cat.sprite.getY());
            cat.reflect();
            cat.moveType = catMoveType.left;
            cat.state = cat.randomState;
        } /**
         * collision cat right wall
         */
        else if (Colision.rectsOverlap(right.getBounds(), cat.rect)) {
            cat.sprite.setPosition(right.getBounds().x - right.getBounds().width - cat.sprite.getWidth(), cat.sprite.getY());
            cat.reflect();
            cat.moveType = catMoveType.right;
            cat.state = cat.randomState;
        }
    }

    @Override
    protected void onStart() {
        backgroundTexture = assets.get("avoider/backgroundTexture.png", Texture.class);
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);

        limitsWidth = 20;
        limitsHeight = 20;
        limits = assets.get("avoider/grey.png", Texture.class);
        up = new Obstacle(new Vector2(0, WORLD_HEIGHT - limitsHeight), WORLD_WIDTH, limitsHeight);
        down = new Obstacle(new Vector2(0, 0), WORLD_WIDTH, limitsHeight);
        left = new Obstacle(new Vector2(0, limitsWidth), limitsWidth, WORLD_HEIGHT);
        right = new Obstacle(new Vector2(WORLD_WIDTH - limitsWidth, limitsWidth), limitsWidth, WORLD_HEIGHT);

        cat.texture = assets.get("avoider/catNinja.png", Texture.class);
        cat.sprite = new Sprite(cat.texture);
        cat.sprite.setSize(100, 150);
        cat.sprite.setOrigin(cat.sprite.getWidth() / 2, cat.sprite.getHeight() / 2);
        cat.sprite.setPosition(WORLD_WIDTH / 2, limitsWidth);
        int random = randomGenerator.nextInt();
        cat.moveType = catMoveType.downL;
        cat.state = cat.randomState;

        catMovingUpL = new AnimatedSprite("avoider/cat-moving-upL.png", 0.1f, 144, 150, 12 , RIGHT, 0);
        catMovingUpR = new AnimatedSprite("avoider/cat-moving-upR.png", 0.1f, 144, 150, 12 , RIGHT, 0);
        
        catMovingDownL = new AnimatedSprite("avoider/cat-moving-downL.png", 0.1f, 144, 150, 12 , RIGHT, 0);
        catMovingDownR = new AnimatedSprite("avoider/cat-moving-downR.png", 0.1f, 144, 150, 12 , RIGHT, 0);
        
        catMovingLeft =  new AnimatedSprite("avoider/cat-moving-left.png", 0.1f, 142, 150, 12 , RIGHT, 0);
        catMovingRight = new AnimatedSprite("avoider/cat-moving-right.png", 0.1f, 142, 150, 12 , RIGHT, 0);
        
        wool.direction = new Vector2(0, 0);
        wool.texture = assets.get("avoider/wool.png", Texture.class);
        wool.position = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        wool.sprite = new Sprite(wool.texture);
        wool.sprite.setSize(50, 50);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("avoider/ninja_theme.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        impact = Gdx.audio.newMusic(Gdx.files.internal("avoider/impact.mp3"));
    }

    public float getCurveValue(float value) {
        return (float) (1f / (1f + Math.pow(Math.E, -6 * (value - 0.5f))));
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

        cat.speed = cat.speed + getCurveValue(cat.speed);
    }

    @Override
    public void onHandlePlayingInput() {
    }

    @Override
    public void onUpdate(float dt) {
        int changeState = randomGenerator.nextInt(30);
        if (changeState == 29) {
            cat.state = cat.jumpState;
        }
        cat.move();
        wool.getPosition();
        wool.sprite.setPosition(wool.position.x, wool.position.y);

        catMovingUpL.animationTime += Gdx.graphics.getDeltaTime();
        catMovingUpR.animationTime += Gdx.graphics.getDeltaTime();
        catMovingDownL.animationTime += Gdx.graphics.getDeltaTime();
        catMovingDownR.animationTime += Gdx.graphics.getDeltaTime();
        catMovingLeft.animationTime += Gdx.graphics.getDeltaTime();
        catMovingRight.animationTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void onDrawGame() {
        backgroundSprite.draw(batch);

        down.draw(batch);
        up.draw(batch);
        left.draw(batch);
        right.draw(batch);
        
        if(cat.moveType.equals(catMoveType.upL))
            batch.draw((TextureRegion) catMovingUpL.movement.getKeyFrame(catMovingUpL.animationTime), cat.sprite.getX(), cat.sprite.getY());
        else if(cat.moveType.equals(catMoveType.upR))
            batch.draw((TextureRegion) catMovingUpR.movement.getKeyFrame(catMovingUpR.animationTime), cat.sprite.getX(), cat.sprite.getY());
        else if(cat.moveType.equals(catMoveType.downL))
            batch.draw((TextureRegion) catMovingDownL.movement.getKeyFrame(catMovingDownL.animationTime), cat.sprite.getX(), cat.sprite.getY());
        else if(cat.moveType.equals(catMoveType.downR))
            batch.draw((TextureRegion) catMovingDownR.movement.getKeyFrame(catMovingDownR.animationTime), cat.sprite.getX(), cat.sprite.getY());
        else if(cat.moveType.equals(catMoveType.left))
            batch.draw((TextureRegion) catMovingLeft.movement.getKeyFrame(catMovingLeft.animationTime), cat.sprite.getX(), cat.sprite.getY());
        else if(cat.moveType.equals(catMoveType.right))
            batch.draw((TextureRegion) catMovingRight.movement.getKeyFrame(catMovingRight.animationTime), cat.sprite.getX(), cat.sprite.getY());
        
        if (wool.life == 1) {
            wool.sprite.draw(batch);
        }
    }

    @Override
    public String getInstructions() {
        return "Não deixe o gato pegar o novelo!!!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    // <editor-fold desc="Classes internas de CatAvoider" defaultstate="collapsed">
    
    class Obstacle {
        private final Texture colorTexture;
        private final Sprite sprite;
        private final Vector2 position;
        private final Rectangle bounds;

        public Obstacle(Vector2 position, float width, float height) {
            colorTexture = assets.get("avoider/grey.png", Texture.class);
            this.position = position;
            bounds = new Rectangle(this.position.x, this.position.y, width, height);
            sprite = new Sprite(colorTexture);
            sprite.setPosition(this.position.x, this.position.y);
            sprite.setSize(width, height);
        }

        public void draw(SpriteBatch batch) {
            sprite.draw(batch);
        }

        public Rectangle getBounds() {
            return bounds;
        }
    }
    
    class AnimatedSprite {
        private Animation movement;
        private float animationTime;
        private final Texture spriteSheet;
        private final TextureRegion[][] animationPictures;

        AnimatedSprite(String textureName, float time, int frameWidth, int frameHeight, int frames, int sense, int startPosiiton) {
            spriteSheet = assets.get(textureName, Texture.class);
            animationPictures = TextureRegion.split(spriteSheet, frameWidth, frameHeight);

            animationTime = 0;
            createMovement(time, sense, frames, startPosiiton);
        }

        void configurePlayMode(Animation.PlayMode p) {
            movement.setPlayMode(p);
        }

        private void createMovement(float time, int sense, int frames, int startPositon) {
            TextureRegion[] t = new TextureRegion[frames];
            int i, k;
            k = 0;
            if (sense == LEFT) {
                for (i = startPositon; i >= 0; i--) {
                    t[k] = animationPictures[0][i];
                    k++;
                }
                for (i = frames-1; i > startPositon; i--) {
                    t[k] = animationPictures[0][i];
                    k++;
                }
            } else {
                for (i = startPositon; i < frames; i++) {
                    t[k] = animationPictures[0][i];
                    k++;
                }
                for (i = 0; i < startPositon; i++) {
                    t[k] = animationPictures[0][i];
                    k++;
                }
            }

            movement = new Animation<TextureRegion>(time, t);
            movement.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        }
    }
    
    static class Colision {
        /**
         * Verifica se dois círculos em 2D estão colidindo.
         *
         * @param c1 círculo 1
         * @param c2 círculo 2
         * @return true se há colisão ou false, do contrário.
         */
        public static final boolean circlesOverlap(Circle c1, Circle c2) {
            Vector2 v1, v2;
            v1 = new Vector2(c1.x, c1.y);
            v2 = new Vector2(c2.x, c2.y);
            double distance2 = v1.dst2(v2);
            if (distance2 > ((c1.radius + c2.radius) * (c1.radius + c2.radius))) {
                return false;
            } else {
                return true;
            }
        }

        /**
         * Verifica se dois retângulos em 2D estão colidindo. Esta função pode
         * verificar se o eixo X dos dois objetos está colidindo e então se o mesmo
         * ocorre com o eixo Y.
         *
         * @param r1 retângulo 1
         * @param r2 retângulo 2
         * @return true se há colisão ou false, do contrário.
         */
        public static final boolean rectsOverlap(Rectangle r1, Rectangle r2) {
            float r1XMin = r1.x, r1XMax = r1.x + r1.width, r1YMin = r1.y, r1YMax = r1.y + r1.height;
            float r2XMin = r2.x, r2XMax = r2.x + r2.width, r2YMin = r2.y, r2YMax = r2.y + r2.height;

            if ((r1XMax >= r2XMin && r1XMin <= r2XMax) && (r1YMax >= r2YMin && r1YMin <= r2YMax)) {
                return true;
            } else {
                return false;
            }
        }

        public static final Vector2 rectCircleOverlap(Rectangle r1, Circle c1) {
            float r1XCenter = r1.x + (r1.width / 2), r1YCenter = r1.y + (r1.height / 2);
            float horizontalDistance = Math.abs(c1.x - r1XCenter);
            float verticalDistance = Math.abs(c1.y - r1YCenter);
            float pXCoordinate;
            float pYCoordinate;

            //Clamp operation
            if (horizontalDistance > (r1.width / 2)) {
                horizontalDistance = r1.width / 2;
            }
            if (verticalDistance > (r1.height / 2)) {
                verticalDistance = r1.height / 2;
            }

            //Finding p Point Coordinates
            if (c1.x > r1XCenter) {
                pXCoordinate = r1XCenter + horizontalDistance;
            } else {
                pXCoordinate = r1XCenter - horizontalDistance;
            }
            if (c1.y > r1YCenter) {
                pYCoordinate = r1YCenter + verticalDistance;
            } else {
                pYCoordinate = r1YCenter - verticalDistance;
            }

            Vector2 c1Center = new Vector2(c1.x, c1.y);
            Vector2 pPoint = new Vector2(pXCoordinate, pYCoordinate);

            float distance2 = c1Center.dst2(pPoint);

            if (distance2 <= (c1.radius * c1.radius)) {
                return pPoint;
            } else {
                return null;
            }
        }

        public static boolean collideCircleWithRotatedRectangle(Circle c, Rectangle r, float rotation) {

            Vector2 rectCenter = new Vector2();
            r.getCenter(rectCenter);

            Vector2 unrotatedCircle = new Vector2();

            unrotatedCircle.x = (float) (Math.cos(rotation * convertToRad) * (c.x - rectCenter.x) - Math.sin(rotation * convertToRad) * (c.y - rectCenter.y) + rectCenter.x);
            unrotatedCircle.y = (float) (Math.sin(rotation * convertToRad) * (c.x - rectCenter.x) + Math.cos(rotation * convertToRad) * (c.y - rectCenter.y) + rectCenter.y);

            Vector2 closestPoint = new Vector2();

            if (unrotatedCircle.x < r.x) {
                closestPoint.x = r.x;
            } else if (unrotatedCircle.x > r.x + r.width) {
                closestPoint.x = r.x + r.width;
            } else {
                closestPoint.x = unrotatedCircle.x;
            }

            if (unrotatedCircle.y < r.y) {
                closestPoint.y = r.y;
            } else if (unrotatedCircle.y > r.y + r.height) {
                closestPoint.y = r.y + r.height;
            } else {
                closestPoint.y = unrotatedCircle.y;
            }

            return unrotatedCircle.dst(closestPoint) < c.radius;
        }
    }
    
    class Cat {
        protected Rectangle rect;//rectangle to enclose the cat and treat the collision
        protected Texture texture;//texture for the non playable character ninja cat
        protected Sprite sprite;//sprite of the non non playable character ninja cat
        protected float speed = 40;//variable used to set the cat ninja speed
        protected float delta = 5;//variable used to set the delta of displacement of the cat ninja in the screen per period of time
        protected catMoveType moveType; //variable to set the type of moviment of the cat (jump or random)
        protected int state;//variable to indicate the type of moviment of the cat (jump or random)
        protected final int randomState = 0;//constant to indicate that the cat is moving randomly
        protected final int jumpState = 1;//constante to indicate that the cat will jump towars the mouse
        
        public void lookAhead() {
            double angle = Math.atan(wool.direction.y / wool.direction.x);

            angle += (wool.direction.x > 0) ? -Math.PI / 2 : Math.PI / 2;
            angle *= 180 / Math.PI;
            sprite.setRotation((float) angle);
        }

        public void setDirection() {
            wool.direction.x = wool.position.x - (sprite.getX() + sprite.getWidth() / 2);
            wool.direction.y = wool.position.y - (sprite.getY() + sprite.getHeight() / 2);
            lookAhead();
        }

        public void incrementX(float delta) {
            if ((sprite.getX() + sprite.getWidth() + delta) < WORLD_WIDTH && (moveType.equals(catMoveType.upR) || moveType.equals(catMoveType.upL) || moveType.equals(catMoveType.downR) || moveType.equals(catMoveType.downL))) {
                sprite.setX(sprite.getX() + delta);
            }
        }

        public void decrementX(float delta) {
            if ((sprite.getX() - delta) > limitsWidth && (moveType.equals(catMoveType.upR) || moveType.equals(catMoveType.upL) || moveType.equals(catMoveType.downR) || moveType.equals(catMoveType.downL))) {
                sprite.setX(sprite.getX() - delta);
            }
        }

        public void incrementY(float delta) {
            if ((sprite.getY() + sprite.getHeight() + delta) < WORLD_HEIGHT - limitsHeight && (moveType.equals(catMoveType.left) || moveType.equals(catMoveType.right))) {
                sprite.setY(sprite.getY() + delta);
            }
        }

        public void decrementY(float delta) {
            if ((sprite.getY() - delta) > limitsHeight && (moveType.equals(catMoveType.left) || moveType.equals(catMoveType.right))) {
                sprite.setY(sprite.getY() - delta);
            }
        }

        public void randomMovementX(int signal) {
            if (signal == 0) {
                incrementX(delta);
            } else if (signal == 1) {
                decrementX(delta);
            }
        }

        public void randomMovementY(int signal) {
            if (signal == 0) {
                incrementY(delta);
            } else if (signal == 1) {
                decrementY(delta);
            }
        }

        public void moveRandom(int signal) {
            final int move = 1;
            int movement = move;

            if (movement == move) {
                if (moveType.equals(catMoveType.downL) || moveType.equals(catMoveType.downR) || moveType.equals(catMoveType.upL) || moveType.equals(catMoveType.upR)) {
                    randomMovementX(signal);
                }
                if (moveType.equals(catMoveType.left) || moveType.equals(catMoveType.right)) {
                    randomMovementY(signal);
                }
            }
        }

        public void move() {
            if (state == jumpState) {
                jump();
                verifyCollision();
                signal = randomGenerator.nextInt(2);
            } else {
                moveRandom(signal);
            }
        }

        public void jump() {
            Vector2 normalized = new Vector2(wool.direction);
            normalized.nor();
            normalized.scl(speed);

            normalized.x += sprite.getX();
            normalized.y += sprite.getY();

            sprite.setPosition(normalized.x, normalized.y);
            System.out.println("Wool: " + wool.position.x + " " + wool.position.y);
            System.out.println("Cat: " + sprite.getX() + " " + sprite.getY());
        }

        public void reflect() {
            wool.direction.x = wool.position.x - (sprite.getX() + sprite.getWidth() / 2);
            wool.direction.y = wool.position.y - (sprite.getY() + sprite.getHeight() / 2);
            lookAhead();
        }
    }
    
    class Wool {

        protected Circle circle;//circle to enclose the cat and treat the colision
        protected Sprite mouse;//sprite for the mouse(playable character)
        protected Vector2 position;//vector to get the mouse positon on the screen
        protected Vector2 direction;//vetor to get the cat direction on the screen
        protected Texture texture;
        protected Sprite sprite;
        protected int life = 1;

        public void getPosition() {
            position.x = Gdx.input.getX() * WORLD_WIDTH / viewport.getScreenWidth();
            position.y = WORLD_HEIGHT - (Gdx.input.getY() * WORLD_HEIGHT / viewport.getScreenHeight());
        }
    }

    // </editor-fold>
}
