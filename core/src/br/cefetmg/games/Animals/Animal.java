/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.Animals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author Pedro
 */
public class Animal extends Sprite {
    private Vector2 Pos;
    static final int FRAME_WIDTH = 131;
    static final int FRAME_HEIGHT = 32;
   
    public Animal(Vector2 Pos, TextureRegion AnimalSpriteSheet) {
        super(AnimalSpriteSheet);
        this.Pos = Pos;
    }

    public Vector2 getPos() {
        return Pos;
    }
    
    public void moviment (Vector2 NovaPosicao) {
        this.Pos = NovaPosicao;
    }
}
