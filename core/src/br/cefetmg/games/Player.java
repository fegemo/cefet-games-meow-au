package br.cefetmg.games;

import static br.cefetmg.games.Config.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player {

    public boolean walking, up, down, left, right, orientation, footUp, footDown, movingFoot;
    private float playerStep, playerWidth, playerHeight;
    public Sprite sprite_Player, sprite_Shoes;
    public Vector2 position, positionMin, positionMax, speed;
    private Texture playerTexture;
    private SpriteBatch batch;
    private float maxSpeed;
    private float mass, aTime;
    private float rotation_angle, initial_angle, final_angle, correctionX, correctionY, fcorrectionX, fcorrectionY;
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

        this.sprite_Player = new Sprite(playerTexture);
        this.sprite_Player.setSize(sizeX, sizeY);
        this.sprite_Player.setFlip(true, false);
        this.sprite_Shoes = new Sprite(new Texture("head-soccer/shoes.png"));
        this.sprite_Shoes.setSize(0.8f * sizeX, 0.5f * sizeY);
        this.sprite_Shoes.setOrigin(sprite_Shoes.getWidth() / 2, sprite_Shoes.getHeight() / 2);
        this.sprite_Shoes.rotate(-27);
        this.initial_angle = -27;
        final_angle = 9;

        this.correctionX = 0;
        this.correctionY = 0;
        this.fcorrectionX = 0;
        this.fcorrectionY = 0;

        oldOrientation = 0;
        this.playerWidth = sprite_Player.getWidth();
        this.playerHeight = sprite_Player.getHeight();

        this.position = positionInicial;
        orientation = true;//true= Diireita false = left

        sprite_Player.setPosition(positionInicial.x, positionInicial.y);
        sprite_Shoes.setPosition(positionInicial.x, positionInicial.y - sprite_Shoes.getHeight() / 2);
    }

    public Sprite getSprite_Player() {
        return sprite_Player;
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
                float rotation = sprite_Shoes.getRotation() ;
                if (orientation == true) {
                    if (footDown == false) {
                        if (rotation + prop * initial_angle > final_angle) {
                            sprite_Shoes.rotate(-prop * initial_angle);
                            if(fcorrectionX + prop * 60 < 60)
                                fcorrectionX += prop * 60;
                            if(fcorrectionY + prop * 5 < 5)
                                  fcorrectionY += prop * 5;
                        } else {
                            sprite_Shoes.rotate(final_angle - rotation);
                            footDown = true;
                            fcorrectionX = 60;
                            fcorrectionY = 5;
                        }
                    } else {
                        if (rotation - (dt / 1) * initial_angle > initial_angle) {
                            sprite_Shoes.rotate(prop * initial_angle);
                            if(fcorrectionX + prop * 60 > 0)
                                fcorrectionX -=  prop * 60;
                            if(fcorrectionY + prop * 5 > 0)
                                fcorrectionY -= prop * 5;
                        } else {
                            sprite_Shoes.rotate(initial_angle - rotation);
                            footUp = true;
                            fcorrectionX = 0;
                            fcorrectionY = 0;
                        }
                    }
                } else {
                    if (footDown == false) {
                        if (rotation + prop * initial_angle > -final_angle) {
                            sprite_Shoes.rotate(prop * initial_angle);
                           
                            if(fcorrectionX + prop * -72 > -72)
                                fcorrectionX += prop * -72;
                            if(fcorrectionY + prop * 40 < 40)
                                fcorrectionY += prop * 40;
                            
                        } else {
                            sprite_Shoes.rotate(-final_angle - rotation);
                            footDown = true;
                            fcorrectionX = -72;
                            fcorrectionY = 40;
                        }
                    } else {
                        if (rotation - prop * initial_angle > -initial_angle) {
                            sprite_Shoes.rotate(-prop * initial_angle);
                            if(fcorrectionX - prop * -72 < 0)
                                fcorrectionX -= prop * -72;
                            if(fcorrectionY - prop * 40 > 0)
                                fcorrectionY -= prop * 40;
    
                        } else {
                            sprite_Shoes.rotate(-initial_angle - rotation);
                            footUp = true;
                            fcorrectionX = 0;
                            fcorrectionY = 0;
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
            fcorrectionX = 0;
            fcorrectionY = 0;

            if (orientation) {
                sprite_Shoes.rotate(initial_angle - sprite_Shoes.getRotation());
            } else {
                sprite_Shoes.rotate(-initial_angle - sprite_Shoes.getRotation());
            }
        }
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
                this.sprite_Shoes.rotate(rotation_angle * -2);
                correctionX = sprite_Player.getWidth() / 3;
                correctionY = -sprite_Shoes.getHeight() / 2 - 10;
                orientation = false;
            }
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            if (speed.x < maxSpeed) {
                speed.x += 1;
            }
            if (orientation == false) {
                correctionX = 0;
                correctionY = 0;
                this.sprite_Player.setFlip(true, false);
                this.sprite_Shoes.setFlip(false, false);
                this.sprite_Shoes.rotate(rotation_angle * -2);
                orientation = true;
            }
        } else {
            speed.x = 0;
        }

        if ((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) && y == FLOOR) {
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
        this.sprite_Shoes.setOrigin(0, 0);
        sprite_Player.setPosition(x, y);
        sprite_Shoes.setPosition(x + correctionX + fcorrectionX, y - sprite_Shoes.getHeight() / 2 + correctionY + fcorrectionY);
        //System.out.println("speed x: "+ speed.x + "speed y: "+ speed.y);
        //System.out.println("x: " + sprite_Player.getX() + "y: " + sprite_Player.getY());
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
