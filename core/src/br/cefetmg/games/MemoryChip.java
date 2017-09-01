package br.cefetmg.games;

// classe do spritesheet que ficara descendo na tela

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Random;

public class MemoryChip {
	
	private static float posicaoY = 720.0f;
	private static float posicaoX;

	private Sprite sprite;

	private static float tempo_animacao;
	
	public MemoryChip(Texture texture){
		this.sprite = new Sprite(texture);
		// gera um numero aleatorio entre 0 a 1280
		posicaoX = new Random().nextInt(1281);
                sprite.setPosition(posicaoX,posicaoY);
	}
	
	public void update(){
		
	}
	
	public void render(SpriteBatch sb){
		
                if( posicaoY >= -25){
			// se dentro da tela
			posicaoY -= 1;	
                        sprite.draw(sb);
                        sprite.setPosition(posicaoX,posicaoY);
                        sprite.rotate((float) 3);
		}
               
	}
	
	

}
