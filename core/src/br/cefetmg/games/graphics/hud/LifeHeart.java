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
    private static final int FRAME_WIDTH = 100;
    private static final int FRAME_HEIGHT = 112;

    LifeHeart(Texture lifeTexture) {
        TextureRegion[][] frames = TextureRegion
                .split(lifeTexture, FRAME_WIDTH, FRAME_HEIGHT);
        Animation alive = new Animation(1f, frames[3][4]);
        Animation dying = new Animation(0.025f,
                frames[3][4], frames[3][3], frames[3][2], frames[3][1],
                frames[3][0], frames[2][7], frames[2][6], frames[2][5],
                frames[2][4], frames[2][3], frames[2][2], frames[2][1],
                frames[2][0], frames[1][7], frames[1][6], frames[1][5],
                frames[1][4], frames[1][3], frames[1][2], frames[1][1],
                frames[1][0], frames[0][7], frames[0][6], frames[0][5],
                frames[0][4], frames[0][3], frames[0][2], frames[0][1],
                frames[0][0]
        );
        HashMap<String, Animation> animations
                = new HashMap<String, Animation>();
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
}
