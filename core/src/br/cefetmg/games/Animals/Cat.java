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

/**
 *
 * @author Pedro
 */
public class Cat extends Animal {
    
    // Precisa de armazenar a sprite para animacao
    
    private final int BeScaredThereshold;
    static final int FRAME_WIDTH = 131;
    static final int FRAME_HEIGHT = 32;

    public Cat(int BeScaredThereshold, Vector2 Pos, final Texture CatTexture) {
        super(Pos, new Animation(0.1f, new Array<TextureRegion>(){
            {
            TextureRegion[][] frames = TextureRegion.split(
                    CatTexture, FRAME_WIDTH, FRAME_HEIGHT);
            super.addAll(new TextureRegion[]{
                frames[0][0],
                frames[0][1],
                frames[0][2],
                frames[0][3]
            });
            }
        }));
        this.BeScaredThereshold = BeScaredThereshold;
    }
    
    
    
    public boolean FleeAction (int BarkCounter) {
        // Caso positivo ativa a funcao de sair da dela
        // Presente na classe do jogo
        return BarkCounter >= BeScaredThereshold;
    }
    

    
    
}
