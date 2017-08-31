/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.Animals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Pedro e Arthur
 */
public class Dog extends Animal{
    private int barkCounter;
    private boolean latindo;
    private int lives;
    

    public Dog(int lives, Vector2 Pos, TextureRegion DogTexture, TextureRegion d1, TextureRegion d2) {
        super(Pos, DogTexture);
        this.lives = lives;
        barkCounter = 0;
        this.lives = lives;
        latindo = false;
    }
    
//    @Override
//    public void update(float dt) {
//        super.update(dt);
//    }
    

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