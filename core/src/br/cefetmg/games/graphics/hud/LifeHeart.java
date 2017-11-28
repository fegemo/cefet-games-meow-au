package br.cefetmg.games.graphics.hud;

import br.cefetmg.games.graphics.MultiAnimatedSprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.HashMap;

/**
 * Uma das vidas do jogador.
 *
 * Como é parte da HUD, precisa herda de Actor.
 * 
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
class LifeHeart extends Actor {

    private final MultiAnimatedSprite sprite;

    LifeHeart(Texture lifeTexture, Texture explodeTexture) {
        Animation<TextureRegion> alive = new Animation<TextureRegion>(0.025f, workFrames(lifeTexture, 7, 7));
        alive.setPlayMode(Animation.PlayMode.LOOP);
        Animation<TextureRegion> dying = new Animation<TextureRegion>(0.025f, workFrames(explodeTexture, 3, 6));
                
        HashMap<String, Animation<TextureRegion>> animations = new HashMap<String, Animation<TextureRegion>>();
        animations.put("alive", alive);
        animations.put("dying", dying);
        
        sprite = new MultiAnimatedSprite(animations, "alive");
        sprite.setCenterFrames(true);
        sprite.setUseFrameRegionSize(true);

        setWidth(sprite.getWidth());
        setHeight(sprite.getHeight());
    }

    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(), getY());
    }

    @Override
    public void draw(Batch batch, float alpha) {
        sprite.draw(batch, alpha);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        sprite.setPosition(getX(), getY());
        sprite.update(dt);
    }

    public void die() {
        sprite.startAnimation("dying");
    }

    public void alive() {
        sprite.startAnimation("alive");
    }
    
    private TextureRegion[] workFrames(final Texture texture, int rows, int columns) {
        TextureRegion[][] tmp = TextureRegion.split(texture,
                texture.getWidth() / columns,
                texture.getHeight() / rows);

        TextureRegion[] frames = new TextureRegion[columns * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return frames;
    }
}

