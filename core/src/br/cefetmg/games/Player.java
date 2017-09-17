package br.cefetmg.games;

import static br.cefetmg.games.Config.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player {

    public boolean walking, up, down, left, right, orientation, footUp, footDown, movingFoot;
    private float playerStep, playerWidth, playerHeight;
    public Sprite sprite_Player, sprite_Shoes;
    public Vector2 position, positionMin, positionMax, speed, footPosition;
    private Texture playerTexture;
    private SpriteBatch batch;
    public float maxSpeed;
    private float mass, aTime;
    private float rotation_angle,initial_angle, final_angle, correctionX, correctionY, aCorrectionX, aCorrectionY;
    public float kick_power;
    private int oldOrientation;

    public Player(Vector2 positionInicial, Vector2 positionMin, Vector2 positionMax, Texture playerTexture, SpriteBatch batch, float playerStep, float maxSpeed, float sizeX, float sizeY, float mass) {
        this.speed = new Vector2(0, 0);
        this.walking = false;
        this.positionMin = positionMin;
        this.positionMax = positionMax;
        this.batch = batch;
        this.playerStep = playerStep;
        this.playerTexture = playerTexture;
        this.maxSpeed = maxSpeed;
        this.mass = mass;
        aTime = 0.3f;
        footUp = false;
        footDown = false;
        movingFoot = false;
        kick_power = 20;
        this.sprite_Player = new Sprite(playerTexture);
        this.sprite_Player.setSize(sizeX, sizeY);
        this.sprite_Player.setFlip(true, false);
        this.sprite_Shoes = new Sprite(new Texture("head-soccer/shoes.png"));
        this.sprite_Shoes.setSize(0.7f * sizeX, 0.3f * sizeY);
        this.sprite_Shoes.setOriginCenter();
        this.sprite_Shoes.setRotation(-27);
        this.initial_angle = -27;
        final_angle = 45;

        correctionY = -sprite_Shoes.getHeight()/2;
        
        aCorrectionX = 0;
        aCorrectionY = 0;
        
        oldOrientation = 0;
        this.playerWidth = sprite_Player.getWidth();
        this.playerHeight = sprite_Player.getHeight();

        this.position = positionInicial;
        footPosition = new Vector2(positionInicial.x, positionInicial.y - sprite_Shoes.getHeight() / 2) ;
        
        orientation = true;//true= Direita false = left
        correctionX = 0;
        
        sprite_Player.setPosition(positionInicial.x, positionInicial.y);
        sprite_Shoes.setPosition(footPosition.x,footPosition.y);
    }

    public Sprite getSprite_Player() {
        return sprite_Player;
    }

    public float getRotation_angle() {
        return rotation_angle;
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

    public void updateMoviment() {
        float x = sprite_Player.getX(), y = sprite_Player.getY();
        rotation_angle = sprite_Shoes.getRotation();
        walking = false;
        up = false;
        down = false;
        left = false;
        right = false;
        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            if (speed.x > -1 * maxSpeed) {
                speed.x -= 1;
            }
            if (orientation == true) {
                this.sprite_Player.setFlip(false, false);
                this.sprite_Shoes.setFlip(true, false);
                this.sprite_Shoes.setRotation(27);
                correctionX = sprite_Player.getWidth() / 3;
            
                orientation = false;
            }
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            if (speed.x < maxSpeed) {
                speed.x += 1;
            }
            if (orientation == false) {
                correctionX = 0;
                this.sprite_Player.setFlip(true, false);
                this.sprite_Shoes.setFlip(false, false);
                this.sprite_Shoes.setRotation(-27);
                orientation = true;
            }
        } else {
            speed.x = 0;
        }

        if ((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) && y == FLOOR) {
            if(sprite_Player.getY() == FLOOR)
                speed.y = JUMP;
        }

        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            if (movingFoot == false) {
                movingFoot = true;
            }
        }

        float playerStepx = playerStep * (speed.x / maxSpeed);
        float playerStepy = playerStep * (speed.y / maxSpeed);

        if (speed.x < 0) {
            if (x + playerStepx > positionMin.x) {
                x += playerStepx;
            }
            walking = true;
            left = true;
        } else if (speed.x > 0) {
            if (x + playerStepx < positionMax.x - playerWidth) {
                x += playerStepx;
            }
            walking = true;
            right = true;
        }

        if (y > FLOOR) {
            if (speed.y > 0) {
                y += playerStepy;
                walking = true;
                up = true;
            } else if (speed.y < 0) {
                y += playerStepy;
                walking = true;
                down = true;

            }
        } else if (y == FLOOR) {
            if (speed.y > 0) {
                y += playerStepy;
                walking = true;
                up = true;
            }
        }

        if (y < FLOOR) {
            y = FLOOR;
        }
        
        sprite_Player.setPosition(x, y);
        sprite_Shoes.setPosition(x + correctionX + aCorrectionX, y - sprite_Shoes.getHeight() / 2 + correctionY + aCorrectionY);
    }

    public void actionGravity(float value) {
        speed.set(speed.x, speed.y - value);
    }

    public void draw() {
        sprite_Player.draw(batch);
        sprite_Shoes.draw(batch);
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public float getMass() {
        return mass;
    }

}
