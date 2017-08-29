/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.Animals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author Pedro e Arthur
 */
public class Dog extends Animal{
    private int barkCounter;
    private boolean latindo;
    private int lives;
    static final int FRAME_WIDTH = 131;
    static final int FRAME_HEIGHT = 32;

    public Dog(int lives, Vector2 Pos, final Texture DogTexture) {
        super(Pos, new Animation(0.1f, new Array<TextureRegion>(){
            {
            TextureRegion[][] frames = TextureRegion.split(
                    DogTexture, FRAME_WIDTH, FRAME_HEIGHT);
            super.addAll(new TextureRegion[]{
                frames[0][0],
                frames[0][1],
                frames[0][2],
                frames[0][3]
            });
            }
        }));
        this.lives = lives;
        barkCounter = 0;
        this.lives = lives;
        latindo = false;
    }
    
    

    public int getBarkCounter() {
        return barkCounter;
    }

    public boolean isLatindo() {
        return latindo;
    }
    
    
    
    public void Bark (){
        // Se click late
        if (/*CLICK*/true){
            barkCounter += 1;
            latindo = true;
        }else
            latindo = false;
    }
    
    public void wasHurt () {
        lives --;
    }
}