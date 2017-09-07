/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.Animals;

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
    private final Animation<TextureRegion> animacao;
    private final Animation<TextureRegion> animacao2;
    private int lives;
    

    public Dog(int lives, Vector2 Pos, TextureRegion DogTexture, Animation<TextureRegion> animacao , Animation<TextureRegion> animacao2 ) {
        
        super(Pos, DogTexture);
        this.animacao = animacao;
        this.animacao2 = animacao2;
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
    
    public TextureRegion Anima2 (float dt) {
        return ((TextureRegion)animacao2.getKeyFrame(dt));
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
    
    
    
    public void Bark (boolean gatoOuve){
        if(!gatoOuve)
            this.barkCounter ++;
        else
            this.barkCounter = 0;
        latindo = true;
    }
    public void BarkZero(){
        barkCounter = 0;
    }
    public void wasHurt () {
        lives --;
    }
}