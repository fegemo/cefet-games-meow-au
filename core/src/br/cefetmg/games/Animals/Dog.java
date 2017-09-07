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

/**
 *
 * @author Pedro e Arthur
 */
public class Dog extends Animal{
    private int barkCounter;
    private boolean latindo;
    private Animation<TextureRegion> animacao;
    private int lives;
    

    public Dog(int lives, Vector2 Pos, TextureRegion DogTexture, Animation<TextureRegion> animacao) {
        
        super(Pos, DogTexture);
        this.animacao = animacao;
        this.lives = lives;
        barkCounter = 0;
        this.lives = lives;
        latindo = false;
    }
    
//    @Override
//    public void update(float dt) {
//        super.update(dt);
//    }
    
    public TextureRegion Anima (float dt) {
        return ((TextureRegion)animacao.getKeyFrame(dt));
    }

    public int getFrameWidth () {
        return FRAME_WIDTH;
    }
    
    public int getFrameHeight () {
        return FRAME_HEIGHT;
    }
    
    public int getBarkCounter() {
        return barkCounter;
    }

    public boolean isLatindo() {
        return latindo;
    }
    
    public void InvertLatindo () {
        latindo = !latindo;
    }
    
    
    
    public void Bark (){
        barkCounter ++;
        latindo = true;
    }
    public void BarkZero(){
        barkCounter = 0;
    }
    public void wasHurt () {
        lives --;
    }
}