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

    public boolean walking, up, down, left, right;
    private float playerStep, playerWidth, playerHeight;
    public Sprite sprite_Player;
    public Vector2 position, positionMin, positionMax, speed;
    private Texture playerTexture;
    private SpriteBatch batch;
    private float maxSpeed;
    private float mass;

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

        this.sprite_Player = new Sprite(playerTexture);
        this.sprite_Player.setSize(sizeX, sizeY);
        this.playerWidth = sprite_Player.getWidth();
        this.playerHeight = sprite_Player.getHeight();

        this.position = positionInicial;

        sprite_Player.setPosition(positionInicial.x, positionInicial.y);
    }

    public Sprite getSprite_Player() {
        return sprite_Player;
    }

    public void updateMoviment() {
        float x = sprite_Player.getX(), y = sprite_Player.getY();

        walking = false;
        up = false;
        down = false;
        left = false;
        right = false;
        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            if (speed.x > -1 * maxSpeed) {
                speed.x -= 1;
            }
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            if (speed.x < maxSpeed) {
                speed.x += 1;
            }
        } else {
            speed.x = 0;
        }

        if ((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) && y == FLOOR) {
            speed.y = JUMP;
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
        System.out.println("speed x: "+ speed.x + "speed y: "+ speed.y);
        //System.out.println("x: " + sprite_Player.getX() + "y: " + sprite_Player.getY());
    }

    public void actionGravity(float value) {
        speed.set(speed.x, speed.y - value);
    }

    public void draw() {
        sprite_Player.draw(batch);
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public float getMass() {
        return mass;
    }
    
}
