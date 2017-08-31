package br.cefetmg.games;

// classe do spritesheet que ficara descendo na tela

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import java.util.Random;

public class SpriteSheetCoin {
	
	private static float posicaoY = 720.0f;
	private static float posicaoX;
	private static float posicaoX_t; // variavel para auxiliar a evitar gerar objetos na mesma coordenada x

	private static TextureRegion[][] sheet;
	private static Sprite sprite;

	private static Animation queda;
	private static float tempo_animacao = 5.0f;
	
	
	public SpriteSheetCoin(Texture texture){
		sheet = TextureRegion.split(texture,30,30);
		queda = new Animation(0.1f, sheet[0][0], sheet[0][1], sheet[0][2], sheet[0][3], 
				sheet[1][0], sheet[1][1], sheet[1][2], sheet[1][3],
				sheet[2][0], sheet[2][1], sheet[2][2], sheet[2][3], 
				sheet[3][0], sheet[3][1], sheet[3][2], sheet[3][3]);
		
		queda.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		// gera um numero aleatorio entre 0 a 1280
		posicaoX = new Random().nextInt(1281);
	}
	
	public void update(){
		posicaoY -= 10;	
	}
	
	public void render(SpriteBatch sb){
		update();
		sb.draw((TextureRegion) queda.getKeyFrame(tempo_animacao), posicaoX, posicaoY);
	}
	
	

}
