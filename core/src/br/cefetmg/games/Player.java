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
    private float  playerStep, playerWidth, playerHeight; 
    public Sprite sprite_Player;
    public Vector2 position, positionMin, positionMax, speed;
    private Texture playerTexture;
    private SpriteBatch batch;
    private float maxSpeed;
    
    public Player(Vector2 positionInicial, Vector2 positionMin, Vector2 positionMax, Texture playerTexture, SpriteBatch batch, float playerStep, float maxSpeed, float sizeX, float sizeY) {
        this.speed = new Vector2(0, 0);
        this.walking = false;
        this.positionMin = positionMin;
        this.positionMax = positionMax;
        this.batch = batch;
        this.playerStep = playerStep;
        this.playerTexture = playerTexture;
        this.maxSpeed = maxSpeed;
        

        this.sprite_Player = new Sprite(playerTexture);
        this.sprite_Player.setSize(sizeX, sizeY);
        this.playerWidth = sprite_Player.getWidth();
        this.playerHeight = sprite_Player.getHeight();
        
        this.position = positionInicial;
        
        sprite_Player.setPosition(positionInicial.x, positionInicial.y);
    }
    
    public void updateMoviment(){
         float x = sprite_Player.getX(), y = sprite_Player.getY();
        
        walking  = false;
        up= false;
        down= false; 
        left= false;
        right= false;
        
        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            if(speed.x > -1*maxSpeed){
                speed.x-=1;
            }    
        }
        else if (Gdx.input.isKeyPressed(Keys.RIGHT ) || Gdx.input.isKeyPressed(Keys.D)) {
            if(speed.x < maxSpeed){
                speed.x+=1;
            }
        }
        else{
            speed.x = 0;
        }  
        
        if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
            if(speed.y < maxSpeed){  
                speed.y+=1;
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
            if(speed.y > -1*maxSpeed){
                speed.y-=1;
            }
        }
        else {
            speed.y = 0;
        }
        
        float playerStepx = playerStep*(speed.x/maxSpeed);
        float playerStepy = playerStep*(speed.y/maxSpeed);
        
        if (speed.x < 0) {
            if(x + playerStepx > positionMin.x){
                x+= playerStepx;
            }    
            walking = true;
            left = true;
        }
        else if (speed.x > 0) {
            if(x + playerStepx < positionMax.x - playerWidth){
                x+=playerStepx;
            }
            walking = true;
            right = true;
        }
        else if (speed.y > 0) {
            if(y + playerStepy < positionMax.y - playerHeight){  
                y+=playerStepy;
            }
            walking = true;
            up = true;
        }
        else if (speed.y < 0) {
            if(y + playerStepy > positionMin.y){
                y+=playerStepy;
            }
            walking = true;
            down = true;
        }
        sprite_Player.setPosition(x, y);
        System.out.println("x: "+sprite_Player.getX()+"y: "+sprite_Player.getY());
    }
    
     
    public void draw(){
        sprite_Player.draw(batch);
    }
}