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
 * @author Pedro
 */
public class Animal extends AnimatedSprite {
    private Vector2 Pos;
    
    
    public Animal(Vector2 Pos) {
        this.Pos = Pos;
    }
    
    private void moviment (Vector2 NovaPosicao) {
        this.Pos = NovaPosicao;
    }
}
