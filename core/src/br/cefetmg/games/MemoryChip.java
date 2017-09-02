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
    public static float posicaoX;

    private Sprite sprite;

    private static float tempo_animacao;

    public MemoryChip(Texture texture,float posx) {
        this.sprite = new Sprite(texture);
        // gera um numero aleatorio entre 0 a 1280
        posicaoX = posx;
        sprite.setPosition(posicaoX, posicaoY);
    }

    public void update(float dt) {
        posicaoY -= dt;
        sprite.setPosition(posicaoX , posicaoY);
        sprite.rotate((float) 3);
    }

    public void render(SpriteBatch sb,float dt) {

        if (posicaoY >= -35) {
            // se dentro da tela
            sprite.draw(sb); 
            update(dt*100);
        }

    }

}
