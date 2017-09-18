package br.cefetmg.games;

import static br.cefetmg.games.Config.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class AnimatedSprite {
    public enum direction{ NORTH, SOUTH, WEST , EAST, SOUTHEAST, NORTHEAST, SOUTHWEST, NORTHWEST }
    public enum sense{ VERTICAL, HORIZONTAL }
    public Animation[] moviments;
    public float timeAnimation;
    private Texture spriteSheet;
    private TextureRegion[][] animationPictures;
    

    // na "create()"
    //spriteSheet = new Texture("goomba-spritesheet.png");
    //quadrosDaAnimacao = TextureRegion.split(spriteSheet, 20, 30);
    public  AnimatedSprite(String sprite, int width, int height){
        spriteSheet = new Texture(sprite);
        animationPictures = TextureRegion.split(spriteSheet, width, height);
        moviments = new Animation[8]; 
        timeAnimation = 0;
    }
    
    public void configurePlayMode(int direction, PlayMode p){
        moviments[direction].setPlayMode(p);
    }
    
    public void createMoviment(int direction, int sense, int n,  float time,  int line, int column){
        TextureRegion[] t = new TextureRegion[n];
        int i,k;
        k = 0;
        if(sense == VERTICAL){
            for(i = line; i < n; i++){
                t[k] = animationPictures[i][column];
                k++;
            }
            for(i = 0; i < line; i++){
                t[k] = animationPictures[i][column];
                k++;
            }
        }
        else{
            for(i = column; i < n; i++){
                t[k] = animationPictures[line][i];
                k++;
            }
            for(i = 0; i < column; i++){
                t[k] = animationPictures[line][i];
                k++;
            }
        }
        moviments[direction] = new Animation(time, t);
        moviments[direction].setPlayMode(PlayMode.LOOP_PINGPONG);
    }
    
    public void createMoviment(direction d, sense s, int n,  float time,  int line, int column){
        TextureRegion[] t = new TextureRegion[n];
        int i,k;
        k = 0;
        if(s.equals(sense.VERTICAL)){
            for(i = line; i < n; i++){
                t[k] = animationPictures[i][column];
                k++;
            }
            for(i = 0; i < line; i++){
                t[k] = animationPictures[i][column];
                k++;
            }
        }
        else{
            for(i = column; i < n; i++){
                t[k] = animationPictures[line][i];
                k++;
            }
            for(i = 0; i < column; i++){
                t[k] = animationPictures[line][i];
                k++;
            }
        }
        moviments[d.ordinal()] = new Animation(time, t);
        moviments[d.ordinal()].setPlayMode(PlayMode.LOOP_PINGPONG);
    }
}