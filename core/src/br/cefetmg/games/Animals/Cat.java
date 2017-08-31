/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.Animals;

import br.cefetmg.games.minigames.util.TimeoutBehavior;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Pedro
 */
public class Cat extends Animal {
    
    // Precisa de armazenar a sprite para animacao
    
    private final int BeScaredThereshold;


    public Cat(int BeScaredThereshold, Vector2 Pos, TextureRegion CatTexture) {
        super(Pos, CatTexture);
        this.BeScaredThereshold = BeScaredThereshold;
    }
    
    public int GetWidth () {
        return FRAME_WIDTH;
    }
    
    public int GetHeight () {
        return FRAME_HEIGHT;
    }
    
//    public void update(float dt) {
//        super.update(dt);
//    }
//    
    public boolean FleeAction (int BarkCounter) {
        // Caso positivo ativa a funcao de sair da dela
        // Presente na classe do jogo
        return BarkCounter >= BeScaredThereshold;
    }
    

    
    
}
