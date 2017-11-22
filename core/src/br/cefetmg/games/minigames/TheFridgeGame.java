package br.cefetmg.games.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;

import java.util.Random;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class TheFridgeGame extends MiniGame {

    private Random generator;
    private MyMusic backgroundSound;
    private MySound whistleUp, whistleDown, crash, clap;
    private Texture[] foodTexture, buttonsTexture;
    private Object[][] food;
    private Object[] shelfs;
    private Object fish, penguin, background, fridge;
    private Button buttons[];
    private Cat cat;
    private CHOICE currentChoice;

    private int shakingCounter = 0, shelfAmount;
    private float fridgeLimitsXMax, fridgeLimitsXMin, fridgeLimitsYMax, fridgeLimitsYMin;
    private final int initialFridgeHeight = 550, initialFridgeWidth = 500;
    private final float gravity = 9.8f, scale = 305;
    private final Vector2 initialFridgePosition = new Vector2(750, 100), finalFridgePosition = new Vector2(650, 20);
    private boolean started, jumping, falling, mistake, ending, directionRight, crashPlaying = false;

    private enum CHOICE {
        RIGHT, LEFT, JUMP
    };

    private class Object {

        public Sprite texture;
        public float initialFallingPosition, fallingTime;

        public Object(Vector2 position, float width, float height, Texture texture) {
            this.texture = new Sprite(texture);
            this.texture.setPosition(position.x, position.y);
            this.texture.setSize(width, height);
        }

        public void Draw() {
            texture.draw(batch);
        }
    }

    private class Button extends Object {

        public CHOICE choice;
        public boolean show;

        public Button(Vector2 position, float width, float height, Texture texture, CHOICE choice, boolean show) {
            super(position, width, height, texture);
            this.choice = choice;
            this.show = show;
        }

        @Override
        public void Draw() {
            if (show) {
                this.texture.draw(batch);
            }
        }

        public void clicked(Vector2 click) {
            if (show && currentChoice == null && jumping == false && falling == false && cat.currentAnimation.isAnimationFinished()) {
                if ((click.x >= this.texture.getX() && click.x <= (this.texture.getX() + this.texture.getWidth())) && (click.y >= this.texture.getY() && click.y <= (this.texture.getY() + this.texture.getHeight()))) {
                    if (this.choice == CHOICE.JUMP) {
                        this.show = false;
                    }
                    if (this.choice == CHOICE.RIGHT) {
                    } else if (this.choice == CHOICE.LEFT) {
                    }
                    jumping = true;
                    currentChoice = this.choice;
                }
            }
        }
    }

    private class Cat {

        public final int width = 400, height = 199;
        public boolean jump;
        public int nextShelf, nextPosition;
        public float initialFallingPosition, initialJumpingPosition, jumpingTime, fallingTime;
        AnimatedSprite walking, jumping, falling, currentAnimation;

        Cat(Texture catTexture) {
            TextureRegion[][] AnimationFrames = TextureRegion.split(catTexture, width, height);
            TextureRegion[] walkingFrames = new TextureRegion[30];
            for (int i = 0; i < 24; i++) { //select the animation frames//               
                walkingFrames[i] = AnimationFrames[i % 12][0];
            }
            for (int i = 24; i < 30; i++) {
                walkingFrames[i] = AnimationFrames[i % 24][1];
            }
            walking = new AnimatedSprite(new Animation<TextureRegion>(0.1f, walkingFrames)); //create animation//       
            walking.setPosition(viewport.getWorldWidth(), -15);
            TextureRegion[] jumpingFrames = new TextureRegion[10];
            jumpingFrames[0] = AnimationFrames[7][3];
            jumpingFrames[1] = AnimationFrames[9][3];
            jumpingFrames[2] = AnimationFrames[11][3];
            jumpingFrames[3] = AnimationFrames[12][3];
            jumpingFrames[4] = AnimationFrames[0][1];
            jumpingFrames[5] = AnimationFrames[1][1];
            jumpingFrames[6] = AnimationFrames[2][1];
            jumpingFrames[7] = AnimationFrames[3][1];
            jumpingFrames[8] = AnimationFrames[4][1];
            jumpingFrames[9] = AnimationFrames[5][1];
            jumping = new AnimatedSprite(new Animation<TextureRegion>(0.1f, jumpingFrames));
            TextureRegion[] fallingFrames = new TextureRegion[5];
            for (int i = 0; i < 5; i++) {
                fallingFrames[i] = AnimationFrames[i][3];
            }
            falling = new AnimatedSprite(new Animation<TextureRegion>(0.1f, fallingFrames));
            currentAnimation = null;
        }

        public void draw(Batch batch) {
            if (currentAnimation != null) {
                currentAnimation.draw(batch);
            }
        }
    }

    private boolean shakingAnimation() {//shakes the penguin on the top of fridge//
        boolean done = true;
        if (shakingCounter == 2) {
            if (penguin.texture.getRotation() < 70) {
                penguin.texture.setRotation(penguin.texture.getRotation() + 5);
                penguin.texture.setY(penguin.texture.getY() + 1.25f);
                done = false;
            }
        } else if (shakingCounter % 2 == 0 && penguin.texture.getRotation() < 15) {
            penguin.texture.setRotation(penguin.texture.getRotation() + 3);
            done = false;
        } else if (shakingCounter % 2 == 0 && penguin.texture.getRotation() >= 15) {
            shakingCounter++;
            done = false;
        } else if (shakingCounter % 2 != 0 && penguin.texture.getRotation() > -15) {
            penguin.texture.setRotation(penguin.texture.getRotation() - 3);
            done = false;
        } else if (shakingCounter % 2 != 0 && penguin.texture.getRotation() <= -15) {
            shakingCounter++;
            done = false;
        }
        return done;
    }

    private float MyPhysics_UpdateFallingPosition(float initialPosition, float fallingTime) {//made my own physics//
        float newPosition = (float) (initialPosition - (gravity * scale * Math.pow(fallingTime, 2) / 2));
        return newPosition > 0 ? newPosition : 0;
    }

    private float MyPhysics_UpdateJumpingPosition(float initialVelocity, float initialPosition, float fallingTime) {//made my own physics//
        float newPosition = (float) (initialPosition + (initialVelocity * scale * fallingTime - gravity * scale * Math.pow(fallingTime, 2) / 2));
        return newPosition;
    }

    private void fallingAnimation() {
        boolean done = shakingAnimation();
        ////////////////////////////////if collided with a food////////////////////////////////
        if (cat.nextPosition <= 2 && cat.nextPosition >= 0 && food[cat.nextShelf][cat.nextPosition] != null) {
            float shelfY = (fridgeLimitsYMax - fridgeLimitsYMin) / shelfAmount;
            ////////////////////////////////food falls to the previous shelf////////////////////////////////
            if (food[cat.nextShelf][cat.nextPosition].texture.getY() >= (shelfs[cat.nextShelf - 1].texture.getY())) {
                float newPosition = MyPhysics_UpdateFallingPosition(food[cat.nextShelf][cat.nextPosition].initialFallingPosition, food[cat.nextShelf][cat.nextPosition].fallingTime);
                food[cat.nextShelf][cat.nextPosition].texture.setY(newPosition);//set new position for the food// 
                food[cat.nextShelf][cat.nextPosition].fallingTime += Gdx.graphics.getDeltaTime();
                done = false;
            }
            ////////////////////////////////shelf falls to the previous shelf////////////////////////////////
            if (shelfs[cat.nextShelf - 1].texture.getY() > fridgeLimitsYMin + ((cat.nextShelf - 1) * shelfY) + (shelfY / 2)) {
                float newShelfPosition = MyPhysics_UpdateFallingPosition(shelfs[cat.nextShelf - 1].initialFallingPosition, shelfs[cat.nextShelf - 1].fallingTime);
                shelfs[cat.nextShelf - 1].texture.setY(newShelfPosition);//set new position for the food// 
                shelfs[cat.nextShelf - 1].fallingTime += Gdx.graphics.getDeltaTime();
                done = false;
            }
            if (cat.nextPosition == 2 && shelfs[cat.nextShelf - 1].texture.getRotation() > -5) {//rotating the shelf//
                shelfs[cat.nextShelf - 1].texture.rotate(shelfs[cat.nextShelf - 1].texture.getRotation() - 0.1f);
                done = false;
            } else if (cat.nextPosition != 2 && shelfs[cat.nextShelf - 1].texture.getRotation() < 5) {//rotating the shelf//
                shelfs[cat.nextShelf - 1].texture.rotate(shelfs[cat.nextShelf - 1].texture.getRotation() + 0.1f);
                done = false;
            }
            ////////////////////////////////moving the other food on the shelf////////////////////////////////            
            int i;
            for (i = 0; i < 2; i++) {
                if (i != cat.nextPosition && food[cat.nextShelf][i] != null) {
                    break;
                }
            }//verify position y//
            if (food[cat.nextShelf][i].texture.getY() > food[cat.nextShelf][cat.nextPosition].texture.getY()) {
                food[cat.nextShelf][i].texture.setY(food[cat.nextShelf][i].texture.getY() - 3);
                done = false;
            } else if (crashPlaying == false) {
                crash.play();
                crashPlaying = true;
            }
            //verify position x//             
            if (cat.nextPosition == 2 && food[cat.nextShelf][i].texture.getX() < (fridge.texture.getX() + fridgeLimitsXMax - food[cat.nextShelf][i].texture.getWidth())) {
                food[cat.nextShelf][i].texture.setX(food[cat.nextShelf][i].texture.getX() + 3);
                done = false;
            } else if (cat.nextPosition != 2 && food[cat.nextShelf][i].texture.getX() > fridge.texture.getX() + fridgeLimitsXMin) {
                food[cat.nextShelf][i].texture.setX(food[cat.nextShelf][i].texture.getX() - 3);
                done = false;
            }
        }
        ////////////////////////cat falls////////////////////////
        if (cat.currentAnimation.getY() > 0) {
            float newPositionCat = MyPhysics_UpdateFallingPosition(cat.initialFallingPosition, cat.fallingTime);
            cat.currentAnimation.setY(newPositionCat);//set new position//    
            cat.fallingTime += Gdx.graphics.getDeltaTime();
            done = false;
        }
        ///////////stop music and call fail///////////
        if (done) {
            falling = false;
            backgroundSound.stop();
            challengeFailed();
        }
    }

    private void getChoice() {
        if (currentChoice == CHOICE.RIGHT) {
            cat.currentAnimation.setTime(0);
            if (cat.currentAnimation.isFlipX() == false) {
                cat.currentAnimation.flipFrames(true, false);
            }
            cat.nextPosition++;
            cat.nextShelf++;
            directionRight = true;
        } else {
            if (currentChoice == CHOICE.JUMP) {
                cat.currentAnimation = cat.jumping;
                cat.currentAnimation.setPosition(cat.walking.getX(), cat.walking.getY());
                cat.currentAnimation.setSize(cat.walking.getWidth(), cat.walking.getHeight());
                buttons[1].show = true;
                buttons[2].show = true;
                cat.nextPosition = 1;
                cat.nextShelf = 0;
            } else if (currentChoice == CHOICE.LEFT) {
                cat.nextPosition--;
                cat.nextShelf++;
            }
            cat.currentAnimation.setTime(0);
            if (cat.currentAnimation.isFlipX() == true) {
                cat.currentAnimation.flipFrames(true, false);
            }
            directionRight = false;
        }
        if (cat.nextPosition < 0 || cat.nextPosition > 2 || (cat.nextShelf < shelfAmount && food[cat.nextShelf][cat.nextPosition] != null)) {
            mistake = true;
        }
        currentChoice = null;
        cat.jump = false;
        cat.initialJumpingPosition = cat.currentAnimation.getY();
        cat.jumpingTime = 0;
        whistleUp.play(0.15f);
    }

    private void jumpAnimation() {
        boolean done; //flag to finish animation//
        if (currentChoice != null) {
            getChoice();
            done = false;
        } else {
            done = true;
            float shelfY = (fridgeLimitsYMax - fridgeLimitsYMin) / shelfAmount;
            float foodX = (fridgeLimitsXMax - fridgeLimitsXMin) / 3;
            float initialVelocity = 3;
            //verify position y// 
            if (cat.jump == false && cat.nextShelf < shelfAmount && cat.currentAnimation.getY() < (shelfs[cat.nextShelf].texture.getY() - shelfY + 60)) {
                cat.currentAnimation.setY(MyPhysics_UpdateJumpingPosition(initialVelocity, cat.initialJumpingPosition, cat.jumpingTime));
                cat.jumpingTime += Gdx.graphics.getDeltaTime();
                done = false; //jumping//
            } else if (cat.jump == false && cat.nextShelf < shelfAmount && cat.currentAnimation.getY() >= (shelfs[cat.nextShelf].texture.getY() - shelfY + 60)) {
                cat.jump = true;//starts to fall//
                cat.initialFallingPosition = cat.currentAnimation.getY();
                cat.fallingTime = 0;
                done = false;
            } else if (cat.jump == false && cat.nextShelf == shelfAmount && cat.currentAnimation.getY() < (shelfs[cat.nextShelf - 1].texture.getY() + 60)) {
                cat.currentAnimation.setY(MyPhysics_UpdateJumpingPosition(initialVelocity, cat.initialJumpingPosition, cat.jumpingTime));
                cat.jumpingTime += Gdx.graphics.getDeltaTime();
                done = false;    //jumping//
            } else if (cat.jump == false && cat.nextShelf == shelfAmount && cat.currentAnimation.getY() >= (shelfs[cat.nextShelf - 1].texture.getY() + 60)) {
                cat.jump = true;//starts to fall//
                cat.initialFallingPosition = cat.currentAnimation.getY();
                cat.fallingTime = 0;
                done = false;
            } else if (cat.jump == true && cat.nextShelf < shelfAmount && cat.currentAnimation.getY() > (shelfs[cat.nextShelf].texture.getY() - shelfY + 30)) {
                cat.currentAnimation.setY(MyPhysics_UpdateFallingPosition(cat.initialFallingPosition, cat.fallingTime));
                cat.fallingTime += Gdx.graphics.getDeltaTime();
                done = false; //falling//
            } else if (cat.jump == true && cat.nextShelf == shelfAmount && cat.currentAnimation.getY() > (shelfs[cat.nextShelf - 1].texture.getY() + 30)) {
                cat.currentAnimation.setY(MyPhysics_UpdateFallingPosition(cat.initialFallingPosition, cat.fallingTime));
                cat.fallingTime += Gdx.graphics.getDeltaTime();
                done = false; //falling//
            }
            //verify position x//
            int deltaX = 5;
            if (cat.nextPosition < 0 && (cat.currentAnimation.getX() > (food[0][0].texture.getX() - foodX))) {//case jumping to the left, out of the fridge//
                cat.currentAnimation.setX(cat.currentAnimation.getX() - deltaX);
                done = false;
            } else if (cat.nextPosition > 2 && (cat.currentAnimation.getX() < (food[0][2].texture.getX() + foodX))) {//case jumping to the right, out of the fridge//
                cat.currentAnimation.setX(cat.currentAnimation.getX() + deltaX);
                done = false;
            } else {
                float aux = fridge.texture.getX() + fridgeLimitsXMin + (cat.nextPosition * foodX);
                if (directionRight && (cat.currentAnimation.getX() < aux - 15)) {
                    cat.currentAnimation.setX(cat.currentAnimation.getX() + deltaX);
                    done = false;
                } else if (directionRight == false && (cat.jumping.getX() > aux)) {
                    cat.currentAnimation.setX(cat.currentAnimation.getX() - deltaX);
                    done = false;
                }
            }
        }
        if (done && cat.currentAnimation.isAnimationFinished()) { //finish animation//
            jumping = false;//update flags//
            whistleUp.stop();
            if (cat.nextShelf == shelfAmount) {
                ending = true;
            }
        } else if (done) {
            if (mistake) {
                jumping = false;//update flags//
                whistleUp.stop();
                falling = true;
                whistleDown.play(0.3f);
                cat.currentAnimation = cat.falling;
                if (cat.jumping.isFlipX() == true) {
                    cat.currentAnimation.flipFrames(true, false);
                }
                cat.currentAnimation.setPosition(cat.jumping.getX(), cat.jumping.getY());
                cat.currentAnimation.setSize(cat.jumping.getWidth(), cat.jumping.getHeight());
                cat.initialFallingPosition = cat.currentAnimation.getY();
                cat.fallingTime = 0;
                if (cat.nextPosition <= 2 && cat.nextPosition >= 0 && food[cat.nextShelf][cat.nextPosition] != null) {
                    food[cat.nextShelf][cat.nextPosition].initialFallingPosition = food[cat.nextShelf][cat.nextPosition].texture.getY();
                    food[cat.nextShelf][cat.nextPosition].fallingTime = 0;
                    shelfs[cat.nextShelf - 1].initialFallingPosition = shelfs[cat.nextShelf - 1].texture.getY();
                    shelfs[cat.nextShelf - 1].fallingTime = 0;
                }
            }
        }
    }

    private void initialAnimation() {
        boolean done = true;//flag to finish zooming//        
        if (fridge.texture.getX() > finalFridgePosition.x) {//zooming//
            fridge.texture.setX(fridge.texture.getX() - 1.25f); //ratio between x and y final//
            fridge.texture.setSize(fridge.texture.getWidth() + 1.25f, fridge.texture.getHeight());
            fridgeLimitsXMin = ((120 * fridge.texture.getWidth()) / initialFridgeWidth); //update the limits, using the ratios//
            fridgeLimitsXMax = ((360 * fridge.texture.getWidth()) / initialFridgeWidth);
            background.texture.setX(background.texture.getX() - 2);
            background.texture.setSize(background.texture.getWidth() + 2, background.texture.getHeight());
            done = false;
        }
        if (fridge.texture.getY() > finalFridgePosition.y) {
            fridge.texture.setY(fridge.texture.getY() - 1f);
            fridge.texture.setSize(fridge.texture.getWidth(), fridge.texture.getHeight() + 1f);
            fridgeLimitsYMin = ((50 * fridge.texture.getHeight()) / initialFridgeHeight);
            fridgeLimitsYMax = ((400 * fridge.texture.getHeight()) / initialFridgeHeight);
            background.texture.setY(background.texture.getY() - 1f);
            background.texture.setSize(background.texture.getWidth(), background.texture.getHeight() + 1f);
            done = false;
        }
        if (done) { //cat starts to walk//
            cat.currentAnimation = cat.walking;
            cat.currentAnimation.setSize(food[0][0].texture.getWidth() + 30, food[0][0].texture.getHeight() + 30);
            if (cat.currentAnimation.getX() > (fridge.texture.getX() + 350)) {
                cat.currentAnimation.setX(cat.currentAnimation.getX() - 1.85f);
            } else {//animation ends//
                started = true;
                buttons[0].show = true;
            }
        }
        setPositionsFoodMatrix();//update food's positions//
    }

    private void fillFoodMatrix() {
        int emptySpace = 1;
        int textureNumber = 0;
        for (int i = 0; i < shelfAmount; i++) {
            for (int j = 0; j < 3; j++) {
                if (j != emptySpace) {
                    food[i][j] = new Object(new Vector2(0, 0), (fridgeLimitsXMax - fridgeLimitsXMin) / 3 + 10, (fridgeLimitsYMax - fridgeLimitsYMin) / shelfAmount + 10, foodTexture[textureNumber]);
                    textureNumber++;
                }
            }
            switch (emptySpace) {
                case 0:
                    emptySpace++;
                    break;
                case 1:
                    int aux = generator.nextInt(2); //number between 0 and 1//
                    if (aux == 0) {
                        emptySpace++; //if it's in the midle column, we need to use a random number to choose//
                    } else {
                        emptySpace--;
                    }
                    break;
                case 2:
                    emptySpace--;
                    break;
            }
        }
        fish.texture.setSize((fridgeLimitsXMax - fridgeLimitsXMin) / 3, (fridgeLimitsYMax - fridgeLimitsYMin) / shelfAmount - 20);
    }

    private void setPositionsFoodMatrix() {
        float x = fridgeLimitsXMin, y = fridgeLimitsYMin;
        for (int i = 0; i < shelfAmount; i++) {
            for (int j = 0; j < 3; j++) {
                if (food[i][j] != null) {
                    food[i][j].texture.setX(fridge.texture.getX() + x);
                    food[i][j].texture.setY(fridge.texture.getY() + y);
                }
                x += (fridgeLimitsXMax - fridgeLimitsXMin) / 3;//set the food's position according to the fridge limits//
            }                                            //and the amount of food and shelfs//
            x = fridgeLimitsXMin;
            y += (fridgeLimitsYMax - fridgeLimitsYMin) / shelfAmount;
            shelfs[i] = new Object(new Vector2((fridge.texture.getX() + x) * 0.99f, fridge.texture.getY() + y - 30), (fridgeLimitsXMax - fridgeLimitsXMin), (fridgeLimitsYMax - fridgeLimitsYMin) / shelfAmount + 80,
                    assets.get("the-fridge-game/shelf.png", Texture.class));
        }
        fish.texture.setPosition((fridge.texture.getX() + fridgeLimitsXMin + (fridgeLimitsXMax - fridgeLimitsXMin) / 3), shelfs[shelfAmount - 1].texture.getY() + 30);
        penguin.texture.setPosition((fridge.texture.getX() + fridgeLimitsXMin + (fridgeLimitsXMax - fridgeLimitsXMin) / 3) + 20, fridge.texture.getY() + fridge.texture.getHeight() - 25);
    }

    public TheFridgeGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 23, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        started = false;
        jumping = false;
        directionRight = false;//set the flags//
        fridgeLimitsXMax = 360;
        fridgeLimitsXMin = 120;
        fridgeLimitsYMax = 400;
        fridgeLimitsYMin = 50;//set initial limits//
        //textures//
        this.foodTexture = new Texture[19];
        for (int i = 1; i < 19; i++) {
            String aux = Integer.toString(i);
            if (aux.length() < 2) {
                aux = "0" + aux;//it's 01-18//
            }
            this.foodTexture[i - 1] = assets.get("the-fridge-game/food" + aux + ".png", Texture.class);
        }
        this.buttonsTexture = new Texture[3];
        for (int i = 1; i < 4; i++) {
            String aux = Integer.toString(i);
            aux = "0" + aux; //it's 01-03//
            this.buttonsTexture[i - 1] = assets.get("the-fridge-game/button" + aux + ".png", Texture.class);
        }
        //sounds//
        backgroundSound =  new MyMusic(assets.get("the-fridge-game/city-shopping.mp3", Music.class));
        whistleUp =new MySound( assets.get("the-fridge-game/whistle-up.mp3", Sound.class));
        whistleDown = new MySound(assets.get("the-fridge-game/whistle-down.mp3", Sound.class));
        crash = new MySound(assets.get("the-fridge-game/crash.mp3", Sound.class));
        clap =new MySound( assets.get("the-fridge-game/clap.mp3", Sound.class));
        //objects//
        generator = new Random();
        background = new Object(new Vector2(0, 0), viewport.getWorldWidth(), viewport.getWorldHeight(),
                assets.get("the-fridge-game/fridge-game-background.png", Texture.class));
        fridge = new Object(initialFridgePosition, initialFridgeWidth, initialFridgeHeight,
                assets.get("the-fridge-game/open-fridge.png", Texture.class));
        fish = new Object(new Vector2(0, 0), 0, 0, assets.get("the-fridge-game/fish.png", Texture.class));
        penguin = new Object(new Vector2(0, initialFridgeHeight), 70, 100, assets.get("the-fridge-game/penguin.png", Texture.class));
        penguin.texture.setOrigin(penguin.texture.getWidth() / 2, 0);
        cat = new Cat(assets.get("the-fridge-game/cat.png", Texture.class));
        buttons = new Button[3];
        buttons[0] = new Button(new Vector2(820, 0), 200, 60,
                assets.get("the-fridge-game/button01.png", Texture.class), CHOICE.JUMP, false);
        buttons[1] = new Button(new Vector2(1000, 0), 100, 100,
                assets.get("the-fridge-game/button02.png", Texture.class), CHOICE.RIGHT, false);
        buttons[2] = new Button(new Vector2(770, 0), 100, 100,
                assets.get("the-fridge-game/button03.png", Texture.class), CHOICE.LEFT, false);
        shelfs = new Object[shelfAmount];
        food = new Object[shelfAmount][3];
        initialize();
    }

    private void initialize() {
        fillFoodMatrix();
        setPositionsFoodMatrix();
        backgroundSound.play();
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        shelfAmount = Math.round(DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 4, 7)); //between 4 and 7//
    }

    @Override
    public void onHandlePlayingInput() {
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        if (Gdx.input.isTouched()) {//verify if the button was clicked//
            for (int i = 0; i < 3; i++) {
                buttons[i].clicked(click);
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        if (started == false) {
            initialAnimation();
        } else if (jumping) {
            jumpAnimation();
        } else if (falling) {
            fallingAnimation();
        } else if (ending) {
            backgroundSound.stop();
            challengeSolved();
        }
    }

    @Override
    public void onDrawGame() {
        background.Draw();
        fridge.Draw();
        for (int i = 0; i < shelfAmount; i++) {
            for (int j = 0; j < 3; j++) {
                if (food[i][j] != null) {
                    food[i][j].Draw();
                }
            }
            shelfs[i].Draw();
        }
        fish.Draw();
        penguin.Draw();
        cat.draw(batch);
        for (int i = 0; i < 3; i++) {
            buttons[i].Draw();
        }
    }

    @Override
    public String getInstructions() {
        return "Alcance os peixes na última prateleira";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }

}
