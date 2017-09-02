package br.cefetmg.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Random;

public class MemoryChip {

    public float posicaoY = 720.0f;
    public float posicaoX;

    private Sprite sprite;

    public MemoryChip(Texture texture) {
        this.sprite = new Sprite(texture);
        this.posicaoX = new Random().nextInt(1271);
        this.sprite.setPosition(this.posicaoX, this.posicaoY);
    }

    public void update(float dt) {
        this.posicaoY -= dt;
        sprite.rotate((float) 10);
        this.sprite.setPosition(this.posicaoX, this.posicaoY);
    }

    public void render(SpriteBatch sb) {

        if (this.posicaoY >= -35) {
            // se dentro da tela
            this.sprite.draw(sb);
            update(new Random().nextInt(5));
        }

    }

}
