package br.cefetmg.games.graphics.hud;

import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 * O relógio que é mostrado na HUD quando um microgame está terminando.
 *
 * Como ele é uma parte da HUD, precisa herdar de Actor.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
class Clock extends Actor {

    private final AnimatedSprite sprite;
    private static final int TOTAL_TICKS = 3;
    private float timeTickingThisSecond;
    private boolean isTicking;
    private int ticksDone;
    private final MySound timerSound;
    private boolean isPaused;

    Clock(Texture clockTexture, MySound timerSound) {
        this.timerSound = timerSound;

        sprite = new AnimatedSprite(new Animation<TextureRegion>(0.2f, workFrames(clockTexture, 5, 7)));
        sprite.setAutoUpdate(false);
        sprite.setCenterFrames(true);
        sprite.setUseFrameRegionSize(true);

        setWidth(sprite.getWidth());
        setHeight(sprite.getHeight());
        resetTicking();
    }

    /**
     * Posiciona a sprite do relógio na posição que o ator deve ser posicionado.
     */
    @Override
    protected void positionChanged() {
        sprite.setPosition(super.getX(), super.getY());
    }

    /**
     * Desenha, mas apenas se o relógio estiver "ticando".
     *
     * @param batch
     * @param alpha
     */
    @Override
    public void draw(Batch batch, float alpha) {
        if (isTicking) {
            sprite.draw(batch, alpha);
        }
    }

    /**
     * Atualiza o estado do relógio.
     *
     * @param dt tempo passado desde a última atualização.
     */
    @Override
    public void act(float dt) {
        super.act(dt);
        if (isTicking && !isPaused) {
            sprite.update(dt);
            timeTickingThisSecond += dt;
            if (timeTickingThisSecond > 1f) {
                timeTickingThisSecond -= 1f;
                timerSound.play();

                if (++ticksDone > TOTAL_TICKS) {
                    stopTicking();
                }
            }
        }
    }

    private void resetTicking() {
        isTicking = false;
        ticksDone = 0;
        timeTickingThisSecond = 0;
    }

    void startTicking() {
        isTicking = true;
        timerSound.play();
        sprite.play();
    }

    void resumeTicking() {
        this.isPaused = false;
    }

    void pauseTicking() {
        this.isPaused = true;
    }

    void stopTicking() {
        resetTicking();
        sprite.stop();
        sprite.update(1);
        timerSound.stop();
    }
    
    
      private TextureRegion[] workFrames(final Texture texture, int rows, int columns) {
        TextureRegion[][] tmp = TextureRegion.split(texture,
                texture.getWidth() / columns,
                texture.getHeight() / rows);

        TextureRegion[] swimFrames = new TextureRegion[columns * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                swimFrames[index++] = tmp[i][j];
            }
        }
        return swimFrames;

    }
}
