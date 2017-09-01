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
    public AnimatedSprite moving;
    public Vector2 position, positionMin, positionMax;
    private Texture playerTexture;
    private SpriteBatch batch;
    
    public Player(String spritesheet, Vector2 positionInicial, Vector2 positionMin, Vector2 positionMax, Texture playerTexture, SpriteBatch batch, float playerStep) {
        
        this.walking = false;
        this.positionMin = positionMin;
        this.positionMax = positionMax;
        this.batch = batch;
        this.playerStep = playerStep;
        this.playerTexture = playerTexture;
        
        this.playerWidth = playerTexture.getWidth();
        this.playerHeight = playerTexture.getHeight();
        this.sprite_Player = new Sprite(playerTexture);
        
        this.position = positionInicial;
        this.moving = new AnimatedSprite(spritesheet,Integer.parseInt(Float.toString(positionInicial.x)),Integer.parseInt(Float.toString(positionInicial.y)));
        
        sprite_Player.setPosition(Integer.parseInt(Float.toString(positionInicial.x)),Integer.parseInt(Float.toString(positionInicial.y)));
        
        moving.createMoviment(NORTH, HORIZONTAL, 5, 0.1f, 2, 0);
        moving.createMoviment(SOUTH, HORIZONTAL, 5, 0.1f, 0, 0);
        moving.createMoviment(EAST, HORIZONTAL, 5, 0.1f, 1, 0);
        moving.createMoviment(WEST, HORIZONTAL, 5, 0.1f, 3, 0);
    }
    
    public void updateMoviment(){
        float x = sprite_Player.getX(), y = sprite_Player.getY();
        
        walking  = false;
        up= false;
        down= false; 
        left= false;
        right= false;
        
        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            if(x - playerStep > 0){
                x-=1;
            }    
            walking = true;
            left = true;
        }
        else if (Gdx.input.isKeyPressed(Keys.RIGHT ) || Gdx.input.isKeyPressed(Keys.D)) {
            if(x + playerStep < positionMax.x - playerWidth){
                x+=1;
            }
            walking = true;
            right = true;
        }
        else if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
            if(y + playerStep < positionMax.y - playerHeight){  
                y+=1;
            }
            walking = true;
            up = true;
        }
        else if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
            if(y - playerStep > 0){
                y-=1;
            }
            walking = true;
            down = true;
        }
        
        sprite_Player.setPosition(x, y);
    }
    
    public void updateTimeAnimation(){
        moving.timeAnimation +=  Gdx.graphics.getDeltaTime();
    }
     
    public void draw(){
        if(walking){
            if(up)
                batch.draw((TextureRegion)moving.moviments[NORTH].getKeyFrame(moving.timeAnimation), sprite_Player.getX(), sprite_Player.getY());
            else if(down)
                batch.draw((TextureRegion)moving.moviments[SOUTH].getKeyFrame(moving.timeAnimation), sprite_Player.getX(), sprite_Player.getY());
            else if(left)
                batch.draw((TextureRegion)moving.moviments[WEST].getKeyFrame(moving.timeAnimation), sprite_Player.getX(), sprite_Player.getY());
            else if(right)
                batch.draw((TextureRegion)moving.moviments[EAST].getKeyFrame(moving.timeAnimation), sprite_Player.getX(), sprite_Player.getY());

        }
        else{
            sprite_Player.draw(batch);
        }
    }
}