/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.Animals;

import com.badlogic.gdx.math.Vector2;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author Pedro e Arthur
 */
public class Dog extends Animal{
    private int barkCounter;
    private boolean latindo;
    private int lives;
    private AnimatedSprite animacao;
    public Dog(Vector2 Pos, int lives) {
        super(Pos);
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
        if (/*CLICK*/){
            barkCounter += 1;
            latindo = true;
        }else
            latindo = false;
    }
    
    public void wasHurt () {
        
    }
}