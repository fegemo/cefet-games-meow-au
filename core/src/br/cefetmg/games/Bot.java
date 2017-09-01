package br.cefetmg.games;

import static br.cefetmg.games.Config.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Bot {
    public boolean walking, up, down, left, right;  
    public Sprite sprite_Bot;
    public AnimatedSprite moving;
    public Vector2 position, positionMin, positionMax;
    
    private float interval, BotStep, PlayerWidth, BotHeight, count, timebase; 
    private int way = 5;
    
    private long mSeconds;
    private Texture botTexture;
    private Timer timer;
    private Random r ;
    private SpriteBatch batch;
    
    
    class BotTask extends TimerTask {
        @Override
        public void run() {
            updateMoviment();
        }
    }
    
    public Bot(String spritesheet, Vector2 positionInicial, Vector2 positionMin, Vector2 positionMax, long mSeconds, Texture botTexture, SpriteBatch batch, boolean walking) {
        timer = new Timer();
        TimerTask task = new BotTask();
        timer.schedule(task, 0, mSeconds);
        
        this.count = 0;
        this.timebase = 0;
        
        this.walking = walking;
        this.mSeconds = mSeconds;
        this.positionMin = positionMin;
        this.positionMax = positionMax;
        this.batch = batch;
        this.botTexture = botTexture;
        
        this.PlayerWidth = botTexture.getWidth();
        this.BotHeight = botTexture.getHeight();
        this.sprite_Bot = new Sprite(botTexture);
        this.BotStep = (float)r.nextInt(21)/10 + 0.5f;
        this.interval = (float)r.nextInt(21)/10;
        
        this.r = new Random();
        this.position = positionInicial;
        this.moving = new AnimatedSprite(spritesheet,Integer.parseInt(Float.toString(positionInicial.x)),Integer.parseInt(Float.toString(positionInicial.y)));
        
        sprite_Bot.setPosition(Integer.parseInt(Float.toString(positionInicial.x)),Integer.parseInt(Float.toString(positionInicial.y)));
        
        moving.createMoviment(NORTH, HORIZONTAL, 5, 0.1f, 2, 0);
        moving.createMoviment(SOUTH, HORIZONTAL, 5, 0.1f, 0, 0);
        moving.createMoviment(EAST, HORIZONTAL, 5, 0.1f, 1, 0);
        moving.createMoviment(WEST, HORIZONTAL, 5, 0.1f, 3, 0);
    }
    
    public void updateMoviment(){
        float x = sprite_Bot.getX();
        float y = sprite_Bot.getY();
        
        up= false;
        down= false; 
        left= false;
        right= false;
        
        if(way == 5){
            way = r.nextInt(4);
        }
        else if(count - timebase > 2){
            timebase = count;
            if(r.nextInt(10) == 9){
                walking = false;
            }
            else{
                walking = true;
                way = r.nextInt(4);
            }
        }
        if(walking){
            switch(way){
                case WEST:
                    if(x - BotStep > positionMin.x){
                        x-=1;
                    }
                    else{
                        if(r.nextInt(10) == 9){
                            walking = false;
                        }
                        else{
                            walking = true;
                            way = r.nextInt(4);
                        }
                    }
                    left = true;
                    break;
                case EAST:
                    if(x + BotStep < positionMax.x - PlayerWidth){
                        x+=1;
                    }
                    else{
                        if(r.nextInt(10) == 9){
                            walking = false;
                        }
                        else{
                            walking = true;
                            way = r.nextInt(4);
                        }
                    }
                    right = true;
                    break;
                case NORTH:
                    if(y + BotStep < positionMax.y - BotHeight){  
                        y+=1;
                    }
                    else{
                        if(r.nextInt(10) == 9){
                            walking = false;
                        }
                        else{
                            walking = true;
                            way = r.nextInt(4);
                        }
                    }
                    up = true;
                    break;
                case SOUTH:
                    if(y - BotStep > positionMin.y){
                        y-=1;
                    }
                    else{
                        if(r.nextInt(10) == 9){
                            walking = false;
                        }
                        else{
                            walking = true;
                            way = r.nextInt(4);
                        }
                    }
                    down = true;
                    break;
            }
        }
        count += (float)mSeconds/1000;
        sprite_Bot.setPosition(x, y);
    }
    
    public void updateTimeAnimation(){
        moving.timeAnimation +=  Gdx.graphics.getDeltaTime();
    }
     
    public void draw(){
        if(walking){
            if(up)
                batch.draw((TextureRegion)moving.moviments[NORTH].getKeyFrame(moving.timeAnimation), sprite_Bot.getX(), sprite_Bot.getY());
            else if(down)
                batch.draw((TextureRegion)moving.moviments[SOUTH].getKeyFrame(moving.timeAnimation), sprite_Bot.getX(), sprite_Bot.getY());
            else if(left)
                batch.draw((TextureRegion)moving.moviments[WEST].getKeyFrame(moving.timeAnimation), sprite_Bot.getX(), sprite_Bot.getY());
            else if(right)
                batch.draw((TextureRegion)moving.moviments[EAST].getKeyFrame(moving.timeAnimation), sprite_Bot.getX(), sprite_Bot.getY());

        }
        else{
            sprite_Bot.draw(batch);
        }
    }
}