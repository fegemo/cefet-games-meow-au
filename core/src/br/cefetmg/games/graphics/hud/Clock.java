package br.cefetmg.games.graphics.hud;

import com.badlogic.gdx.audio.Sound;
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
    private static final int FRAME_WIDTH = 50;
    private static final int FRAME_HEIGHT = 50;
    private static final int TOTAL_TICKS = 3;
    private float timeTickingThisSecond;
    private boolean isTicking;
    private int ticksDone;
    private final Sound timerSound;
    private boolean isPaused;

    Clock(Texture clockTexture, Sound timerSound) {
        this.timerSound = timerSound;
        TextureRegion[][] frames = TextureRegion
                .split(clockTexture, FRAME_WIDTH, FRAME_HEIGHT);
        Animation animation = new Animation(1f,
                frames[0][3],
                frames[0][2],
                frames[0][1],
                frames[0][0]);

        sprite = new AnimatedSprite(animation);
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
}
