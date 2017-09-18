/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.util;

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

        static final int FRAME_WIDTH = 64;
        static final int FRAME_HEIGHT = 64;
        TextureRegion[][] quadrosDaAnimacao;
        Texture spriteSheet;
        float sx;
        float sy;
        
        float tempoDaAnimacao;
        
        Animation morrendo;        
        Animation parado;
        boolean morto=false;
        
        int x = 0;
        
        public Monster(final Texture monster) {
            
             super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            monster, 64, 64);
                    super.addAll(new TextureRegion[]{
                        frames[1][0],
                        frames[1][1],
                        frames[1][2],
                        frames[1][3],
                        frames[1][4]
                        
                    });
                }
            }));
            
            quadrosDaAnimacao = TextureRegion.split(monster, 64, 64);
            
            parado = new Animation(0.1f,
            quadrosDaAnimacao[1][0], 
            quadrosDaAnimacao[1][1], 
            quadrosDaAnimacao[1][2],
            quadrosDaAnimacao[1][3],
            quadrosDaAnimacao[1][4]);
            
            morrendo = new Animation(0.1f,
            quadrosDaAnimacao[3][0], 
            quadrosDaAnimacao[3][1], 
            quadrosDaAnimacao[3][2],
            quadrosDaAnimacao[3][3],
            quadrosDaAnimacao[3][4],
            quadrosDaAnimacao[3][5],
            quadrosDaAnimacao[3][6]);
            
            super.setAnimation(parado);
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
            super.setAnimation(morrendo);
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);            
        }
        
        public void setMorto(boolean morto){
            this.morto=morto;
        }
        
        public boolean getMorto(){
            return this.morto;
        }
        
        public void update(){
            tempoDaAnimacao += Gdx.graphics.getDeltaTime();
        
        }
    }    
