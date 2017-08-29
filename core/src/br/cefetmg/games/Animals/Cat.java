/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.Animals;

import br.cefetmg.games.minigames.util.TimeoutBehavior;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Pedro
 */
public class Cat extends Animal {
    
    // Precisa de armazenar a sprite para animacao
    
    private final int BeScaredThereshold;

    public Cat(int BeScaredThereshold, Vector2 Pos, Animation<TextureRegion> animation) {
        super(Pos, animation);
        this.BeScaredThereshold = BeScaredThereshold;
    }
    
    
    
    public boolean FleeAction (int BarkCounter) {
        // Caso positivo ativa a funcao de sair da dela
        // Presente na classe do jogo
        return BarkCounter >= BeScaredThereshold;
    }
    
    public abstract void Attack ();
    
    
}
