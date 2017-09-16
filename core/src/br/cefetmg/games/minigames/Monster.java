/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author NataliaNatsumy
 */
public class Monster extends AnimatedSprite{
    
    static final int FRAME_WIDTH = 50;
        static final int FRAME_HEIGHT = 50;
        TextureRegion[][] quadrosDaAnimacao;
        Texture spriteSheet;
        
        float tempoDaAnimacao;
        
        Animation normal;
        Animation morrer;
        
        int x = 0;
        
        public Monster(final Texture monster) {
            
             super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            monster, 50, 50);
                    super.addAll(new TextureRegion[]{
                        frames[4][0]
                    });
                }
            }));
            
            quadrosDaAnimacao = TextureRegion.split(monster, 50, 50);
            
            normal = new Animation(0.1f,
            quadrosDaAnimacao[2][0], 
            quadrosDaAnimacao[2][1], 
            quadrosDaAnimacao[2][2],
            quadrosDaAnimacao[2][3],
            quadrosDaAnimacao[2][4],
            quadrosDaAnimacao[2][5]);
            
            morrer = new Animation(0.1f,
            quadrosDaAnimacao[3][0], 
            quadrosDaAnimacao[3][1], 
            quadrosDaAnimacao[3][2],
            quadrosDaAnimacao[3][3],
            quadrosDaAnimacao[3][4],
            quadrosDaAnimacao[3][5],
            quadrosDaAnimacao[3][6],
            quadrosDaAnimacao[3][7]);
            
            super.setAnimation(normal);
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        }

       
        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }
        
        public void changeAnimation(){
            
        }
        public void update(){
            
            tempoDaAnimacao += Gdx.graphics.getDeltaTime();
           if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                 Gdx.input.setInputProcessor(new InputAdapter() {
                 public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                     if (button == Input.Buttons.LEFT) {
                         changeAnimation();
                         return true;
                     }
                     return false;
                 }
             });
           }
            
        /*if (Gdx.input.) {
            super.setAnimation(power);
                  
        }
        else{
        }*/
        
    }
}
