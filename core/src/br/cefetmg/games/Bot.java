package br.cefetmg.games;

import static br.cefetmg.games.Config.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bot {

    public boolean walking, up, down, left, right, orientation, footUp, footDown, movingFoot, jump;
    private float botStep, playerWidth, playerHeight;
    public Sprite sprite_Bot, sprite_Shoes;
    public Vector2 position, positionMin, positionMax, speed, footPosition;
    private Texture botTexture;
    private SpriteBatch batch;
    public float maxSpeed;
    private float mass, aTime;
    private float rotation_angle,initial_angle, final_angle, correctionX, correctionY, aCorrectionX, aCorrectionY;
    public float kick_power;
    private int moveAlone;
    private int oldOrientation;
    private float oldTime, decisionPeriod;

    public Bot(Vector2 positionInicial, Vector2 positionMin, Vector2 positionMax, Texture botTexture, SpriteBatch batch, float botStep, float maxSpeed, float sizeX, float sizeY, float mass) {
        this.speed = new Vector2(0, 0);
        this.walking = false;
        this.positionMin = positionMin;
        this.positionMax = positionMax;
        this.batch = batch;
        this.botStep = botStep;
        this.botTexture = botTexture;
        this.maxSpeed = maxSpeed;
        this.mass = mass;
        aTime = 0.3f;
        footUp = false;
        footDown = false;
        movingFoot = false;
        jump = false;
        kick_power = 20;
        oldTime = 0;
        decisionPeriod = 0.2f;
        this.sprite_Bot = new Sprite(botTexture);
        this.sprite_Bot.setSize(sizeX, sizeY);
        this.sprite_Bot.setFlip(false, false);
        this.sprite_Shoes = new Sprite(new Texture("head-soccer/shoes.png"));
        this.sprite_Shoes.setSize(0.7f * sizeX, 0.3f * sizeY);
        this.sprite_Shoes.setOriginCenter();
        this.sprite_Shoes.setRotation(27);
        this.sprite_Shoes.setFlip(true, false);
        this.initial_angle = -27;
        final_angle = 45;

        correctionY = -sprite_Shoes.getHeight()/2;
        
        aCorrectionX = 0;
        aCorrectionY = 0;
        
        oldOrientation = 0;
        this.playerWidth = sprite_Bot.getWidth();
        this.playerHeight = sprite_Bot.getHeight();

        this.position = positionInicial;
        footPosition = new Vector2(positionInicial.x, positionInicial.y - sprite_Shoes.getHeight() / 2) ;
        
        orientation = false;//true= Direita false = left
        correctionX = sprite_Bot.getWidth()/3;
        
        sprite_Bot.setPosition(positionInicial.x, positionInicial.y);
        sprite_Shoes.setPosition(footPosition.x,footPosition.y);
    }

    public Sprite getSprite_Bot() {
        return sprite_Bot;
    }

    public float getRotation_angle() {
        return rotation_angle;
    }
    public void changeDificulty(float difficulty){
        maxSpeed = maxSpeed * difficulty/10;
        botStep = botStep * difficulty/10;
        kick_power = kick_power * difficulty/10;
    }
    public void moveFoot(float dt) {
        if (oldOrientation == 0 || (oldOrientation == 1 && orientation)
                || (oldOrientation == 2 && orientation == false)) {
            if (movingFoot == true) {
               
                if (oldOrientation == 0) {
                    if (orientation == true) {
                        oldOrientation = 1;
                    } else {
                        oldOrientation = 2;
                    }
                }
                
                float prop = dt / aTime;
                float limitX = playerWidth/1.5f;
                float limitY = 20;
                float speedUp = 2.5f;
                rotation_angle = sprite_Shoes.getRotation();
                if (orientation == true) {
                    if (footDown == false) {
                        if (rotation_angle - prop * speedUp * initial_angle < final_angle) {
                            sprite_Shoes.setRotation(rotation_angle - prop * speedUp * initial_angle );
                             if(aCorrectionX + prop * limitX < limitX)
                                aCorrectionX += prop * limitX;
                             if(aCorrectionY + prop * limitY < limitY)
                                  aCorrectionY += prop * limitY;    
                        } else {
                            aCorrectionX = limitX;
                            aCorrectionY = limitY;
                            sprite_Shoes.setRotation(final_angle);
                            footDown = true;
                        }
                    } else {
                        if (rotation_angle + prop * speedUp * initial_angle > initial_angle) {
                            sprite_Shoes.setRotation(rotation_angle + prop * speedUp * initial_angle);
                            if(aCorrectionX - prop * limitX > 0)
                                aCorrectionX -= prop * limitX;
                             if(aCorrectionY - prop * limitY > 0)
                                  aCorrectionY -= prop * limitY;  
                        } else {
                            aCorrectionX = 0;
                            aCorrectionY = 0;
                            sprite_Shoes.setRotation(initial_angle);
                            footUp = true;
                        }
                    }
                } 
         
                else {
                    if (footDown == false) {
                        if (rotation_angle +  prop * speedUp * initial_angle > -final_angle) {
                            sprite_Shoes.setRotation(rotation_angle + prop * speedUp * initial_angle);
                            if(aCorrectionX - prop * limitX > -limitX)
                                aCorrectionX -= prop * limitX;
                             if(aCorrectionY + prop * limitY < limitY)
                                  aCorrectionY += prop * limitY;  
                        } else {
                            aCorrectionX = -limitX;
                            aCorrectionY = limitY;
                            sprite_Shoes.setRotation(-final_angle);
                            footDown = true;
                        }
                    } else {
                        if (rotation_angle - prop * speedUp * initial_angle < -initial_angle) {
                            sprite_Shoes.setRotation(rotation_angle - prop * speedUp * initial_angle);  
                            if(aCorrectionX + prop * limitX < 0)
                                aCorrectionX += prop * limitX;
                             if(aCorrectionY - prop * limitY > 0)
                                  aCorrectionY -= prop * limitY;  
                        } else {
                            aCorrectionX = 0;
                            aCorrectionY = 0;
                            sprite_Shoes.setRotation(-initial_angle);
                            footUp = true;
                        }
                    }

                }
                if (footUp) {
                    footUp = false;
                    footDown = false;
                    movingFoot = false;
                    oldOrientation = 0;
                }

            }
        } else {
            footUp = false;
            footDown = false;
            movingFoot = false;
            oldOrientation = 0;
            aCorrectionX = 0;
            aCorrectionY = 0;
            if (orientation) {
                sprite_Shoes.setRotation(initial_angle);
            } else {
                sprite_Shoes.setRotation(-initial_angle);
            }
        }
    }

    public Sprite getSprite_Shoes() {
        return sprite_Shoes;
    }
    
    public void updateState(float dt, Vector2 ball, Vector2 player){
        if(oldTime > 0)
            oldTime += dt;
        if(oldTime>decisionPeriod){
            oldTime = 0;
        }
        if(oldTime == 0){
            float x = sprite_Bot.getX();
            float y = sprite_Bot.getY() + playerHeight;
            
            if(ball.x > x + playerWidth && ball.y >y){
                moveAlone = 1;
            }
            else if(ball.x > x  && ball.y <= y){
                moveAlone = 2;
            }
            else if(ball.x < x && ball.y > y){
                moveAlone = 3;
            }
            else{
                moveAlone = 4;
            }
            if(Math.abs(ball.x-x) < playerWidth && movingFoot == false && ball.x < x ){
                movingFoot = true;
            }
            if(Math.abs(ball.x-x) < playerWidth/4 && ball.x > x)
                jump = true;
        }
    }  
    
    public void updateMoviment(float dt, Vector2 ball, Vector2 player) {
        updateState(dt,ball,player);
        float x = sprite_Bot.getX(), y = sprite_Bot.getY();
        rotation_angle = sprite_Shoes.getRotation();
        walking = false;
        up = false;
        down = false;
        left = false;
        right = false;
        switch (moveAlone) {
            case 3:
            case 4:
                if (speed.x > -1 * maxSpeed) {
                    speed.x -= 1;
                }   if (orientation == true) {
                    this.sprite_Bot.setFlip(false, false);
                    this.sprite_Shoes.setFlip(true, false);
                    this.sprite_Shoes.setRotation(27);
                    correctionX = sprite_Bot.getWidth() / 3;
                    
                    orientation = false;
                }   break;
            case 1:
            case 2:
                if (speed.x < maxSpeed) {
                    speed.x += 1;
                }   if (orientation == false) {
                    correctionX = 0;
                    this.sprite_Bot.setFlip(true, false);
                    this.sprite_Shoes.setFlip(false, false);
                    this.sprite_Shoes.setRotation(-27);
                    orientation = true;
                }   break;
            default:
                speed.x = 0;
                break;
        }

        if (moveAlone == 1 || moveAlone == 3 || jump == true) {
            if(sprite_Bot.getY() == FLOOR)
                speed.y = JUMP;
        }

        float botStepx = botStep * (speed.x / maxSpeed);
        float botStepy = botStep * (speed.y / maxSpeed);

        if (speed.x < 0) {
            if (x + botStepx > positionMin.x) {
                x += botStepx;
            }
            walking = true;
            left = true;
        } else if (speed.x > 0) {
            if (x + botStepx < positionMax.x - playerWidth) {
                x += botStepx;
            }
            walking = true;
            right = true;
        }

        if (y > FLOOR) {
            if (speed.y > 0) {
                y += botStepy;
                walking = true;
                up = true;
            } else if (speed.y < 0) {
                y += botStepy;
                walking = true;
                down = true;

            }
        } else if (y == FLOOR) {
            if (speed.y > 0) {
                y += botStepy;
                walking = true;
                up = true;
            }
        }

        if (y < FLOOR) {
            y = FLOOR;
        }
        
        sprite_Bot.setPosition(x, y);
        sprite_Shoes.setPosition(x + correctionX + aCorrectionX, y - sprite_Shoes.getHeight() / 2 + correctionY + aCorrectionY);
    }

    public void actionGravity(float value) {
        speed.set(speed.x, speed.y - value);
    }

    public void draw() {
        sprite_Bot.draw(batch);
        sprite_Shoes.draw(batch);
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public float getMass() {
        return mass;
    }

}
