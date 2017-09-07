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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Pedro
 */
public class Cat extends Animal {
    private final int POSICAO_INICIAL_GATO_X = 500;
    private final int POSICAO_INICIAL_GATO_Y = 500;
    // Precisa de armazenar a sprite para animacao
    
    private final int BeScaredThereshold;
    private int quantidade_vidas;
    private boolean morto;

    
    public Cat(int BeScaredThereshold,Vector2 Pos, TextureRegion CatTexture) {
        super(Pos, CatTexture);
        this.morto = true;
        this.BeScaredThereshold = BeScaredThereshold;
        this.quantidade_vidas = 1;
    }
    
    public Vector2 PosIniCat(){
        return new Vector2(POSICAO_INICIAL_GATO_X,POSICAO_INICIAL_GATO_Y);
    }
    
    public int GetWidth () {
        return FRAME_WIDTH;
    }
    public int GetScareTheresold(){
        return this.BeScaredThereshold;
    }
    public int GetHeight () {
        return FRAME_HEIGHT;
    }
    
    public int get_quantidade_vidas(){
        return this.quantidade_vidas;
    }
    
    public void morreu(){
        this.quantidade_vidas--;
        this.morto=true;
        System.out.println("MORREUU");
        moviment(new Vector2(POSICAO_INICIAL_GATO_X,POSICAO_INICIAL_GATO_Y));
    }
    
    public boolean vivoMorto(){
        return this.morto;
    }
    
    public void spawn(){
        System.out.println("spawn" + this.quantidade_vidas );
        this.morto=false;
    }
    
    public void settarQuantidade_vidas(float variavelControleDificuldade){
        this.quantidade_vidas = (int)( MathUtils.ceil(variavelControleDificuldade*10/8));
    }
//    public void update(float dt) {
//        super.update(dt);
//    }
    
    
    public boolean FleeAction (int BarkCounter) {
        // Caso positivo ativa a funcao de sair da dela
        // Presente na classe do jogo
        return BarkCounter >= BeScaredThereshold;
    }
    

    
    
}
