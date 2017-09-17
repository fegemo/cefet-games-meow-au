/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.sun.webkit.Timer;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kael
 */
public class CannonCat extends MiniGame {

    private Texture background;
    private Texture cat,cookie;
    private Texture cannon_downleft,cannon_down,cannon_downright,cannon_right,cannon_left,cannon_upright,cannon_up,cannon_upleft;
    public int i = 0; 
    public int k = 0;
    public int c = 0;
    public int tiro = 0;
    public int[] cat_x = new int[8];
    public int[] cat_y = new int[8];
    public int[] cookie_x = new int[10];
    public int[] cookie_y = new int[10];
    public int center_x = 1200/2;
    public int center_y = 700/2;
    public double velocidade = 1;
    public double dificuldade = 1;
    public double tempo1 =   System.currentTimeMillis();
    public double tempo2 =   System.currentTimeMillis(); 

    public CannonCat(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        background = assets.get("cannon-cat/background.png", Texture.class);

        cat = assets.get("cannon-cat/cat.png", Texture.class);
        cookie = assets.get("cannon-cat/biscoito.png", Texture.class);
        cannon_down = assets.get("cannon-cat/cannon_down.png", Texture.class);
        cannon_downleft = assets.get("cannon-cat/cannon_down+left.png", Texture.class);
        cannon_downright = assets.get("cannon-cat/cannon_down+right.png", Texture.class);
        cannon_right = assets.get("cannon-cat/cannon_right.png", Texture.class);
        cannon_left = assets.get("cannon-cat/cannon_left.png", Texture.class);
        cannon_upright = assets.get("cannon-cat/cannon_up+right.png", Texture.class);
        cannon_up = assets.get("cannon-cat/cannon_up.png", Texture.class);
        cannon_upleft = assets.get("cannon-cat/cannon_up+left.png", Texture.class);
        
        for(i=0;i<8;i++){
            cat_x[i] = (int) (200*Math.cos((i*Math.PI)/4)) + 1280/2;
            cat_y[i] = (int) ((-1)*200*Math.sin((i*Math.PI)/4)) + 720/2;
        }
        for(i=0;i<10;i++){
            cookie_x[i] = 300;
            cookie_y[i] = 200 + (-1)*i*20;
        }
        tiro = 10;
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        //velocidade = 1+3*difficulty;
        this.velocidade = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty,2,3);
    }

    @Override
    public void onHandlePlayingInput() {

    }

    @Override
    public void onUpdate(float dt) {
        //TECLADO
        tempo2 =   System.currentTimeMillis();
        if(tempo2-tempo1>100){
            tempo1 =  System.currentTimeMillis();
            k++;
            k = k%8;
        }
        
        for(i=0;i<8;i++){
            if(cat_x[i] == 0){
                c++;
            }
        }
        if(c==8){
                super.challengeSolved();
        }
        else{
            c=0;
        }
        if(tiro<=0){
            super.challengeFailed();
        }


        if (Gdx.input.justTouched()) {
  
            
            if(tiro>0){
                cat_x[k] = 0;
                tiro--;
            }
            else{
                super.challengeFailed();
            }

            
            cookie_x[tiro] = -500;
        }
    }
    
      
    

    @Override
    public void onDrawGame() {
        batch.draw(background, 0, 0);
        for(i=0;i<8;i++){
           batch.draw(cat, cat_x[i], cat_y[i]);
        }
        for(i=0;i<10;i++){
           batch.draw(cookie, cookie_x[i], cookie_y[i]);
        }
        
        switch(k){
            case 0:
                batch.draw(cannon_right, center_x, center_y);
                break;
    
            case 1:
                batch.draw(cannon_downright, center_x, center_y);
                break;
    
            case 2:
                batch.draw(cannon_down, center_x, center_y);
                break;
                
            case 3:
                batch.draw(cannon_downleft, center_x, center_y);
                break;
                
            case 4:
                batch.draw(cannon_left, center_x, center_y);
                break;
                
            case 5:
                batch.draw(cannon_upleft, center_x, center_y);
                break;
                
            case 6:
                batch.draw(cannon_up, center_x, center_y);
                break;
            
            case 7:
                batch.draw(cannon_upright, center_x, center_y);
                break;
            
                
    
            default:

        }   
        
        
    }
    
    @Override
    public String getInstructions() {
        return null;

    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

}
