package br.cefetmg.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

//REFERÊNCIA DESTE CÓDIGO: https://github.com/serhiy/libgdx-circular-cooldown
public class LoadingScreen {

    private final Stage stage;
    private final CooldownTimer cooldownTimerBlue;

    private long lastUpdate = 0L;
    private float remainingPercentage = 1.0f;

    private final float STEP = 0.05f;
    private final int TIMER_SIZE = 100;

    private final TextureRegion background;

    public LoadingScreen() {
        stage = new Stage();

        cooldownTimerBlue = new CooldownTimer(true);
        cooldownTimerBlue.setSize(TIMER_SIZE, TIMER_SIZE);
        cooldownTimerBlue.setPosition(0, 0);
        cooldownTimerBlue.setColor(Color.BLUE);

        stage.addActor(cooldownTimerBlue);

        // instancia a textura e a região de textura
        Texture backgroundTexture = new Texture("loading-page.jpg");
        backgroundTexture.setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        background = new TextureRegion(backgroundTexture);
    }

    public boolean draw(AssetManager assets, SpriteBatch batch, Viewport viewport) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, TIMER_SIZE, viewport.getScreenWidth(), viewport.getScreenHeight() - TIMER_SIZE);
        batch.end();

        if (System.currentTimeMillis() - lastUpdate > 25L) {
            cooldownTimerBlue.update(remainingPercentage);

            //A medida que os assets são carregados, atualiza-se suavemente o círculo de carregamento
            if (remainingPercentage - STEP >= 1 - assets.getProgress()) {
                remainingPercentage -= STEP;
            }

            lastUpdate = System.currentTimeMillis();
        }

        stage.act();
        stage.draw();

        if (remainingPercentage <= STEP) {
            remainingPercentage = 1.0f;
            return true;
        }
        return false;
    }

    public class CooldownTimer extends Table {

        private static final float START_ANGLE = 90;

        private final boolean clockwise;

        private final Table cooldownDisplay;
        private TextureRegionDrawable cooldownTexture;

        private float alpha = 0.5f;

        /**
         * @param clockwise determines the rotation side of the cooldown timer.
         */
        public CooldownTimer(boolean clockwise) {
            this.clockwise = clockwise;

            cooldownDisplay = new Table();
            cooldownDisplay.setPosition(0, 0);
            addActor(cooldownDisplay);
        }

        /**
         * @param remainingPercentage to be rendered by cooldown timer.
         */
        public void update(float remainingPercentage) {
            cooldownDisplay.clear();

            Image cooldownTimer = new Image(cooldownTimer(remainingPercentage));
            cooldownTimer.setColor(1.0f, 1.0f, 1.0f, alpha);
            cooldownDisplay.addActor(cooldownTimer);
        }

        public float getAlpha() {
            return alpha;
        }

        /**
         * @param alpha to be applied to the final cooldown indicator.
         */
        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        private Drawable cooldownTimer(float remainingPercentage) {
            if (remainingPercentage > 1.0f || remainingPercentage < 0.0f) {
                return null;
            }

            float radius = Math.min(getWidth() / 2, getHeight() / 2);
            float angle = calculateAngle(remainingPercentage);
            int segments = calculateSegments(angle);

            Pixmap display = new Pixmap((int) getWidth(), (int) getHeight(), Format.RGBA8888);

            Blending blending = display.getBlending();

            try {
                float theta = (2 * MathUtils.PI * (angle / 360.0f)) / segments;
                float cos = MathUtils.cos(theta);
                float sin = MathUtils.sin(theta);
                float cx = radius * MathUtils.cos(START_ANGLE * MathUtils.degreesToRadians);
                float cy = radius * MathUtils.sin((-1 * START_ANGLE) * MathUtils.degreesToRadians);

                display.setColor(getColor());

                for (int count = 0; count < segments; count++) {
                    float pcx = cx;
                    float pcy = cy;
                    float temp = cx;
                    cx = cos * cx - sin * cy;
                    cy = sin * temp + cos * cy;
                    display.fillTriangle((int) getWidth() / 2, (int) getHeight() / 2, (int) (getWidth() / 2 + pcx), (int) (getHeight() / 2 + pcy), (int) (getWidth() / 2 + cx), (int) (getHeight() / 2
                            + cy));
                }

                display.setBlending(Blending.None);

                if (cooldownTexture == null) {
                    cooldownTexture = new TextureRegionDrawable(new TextureRegion(new Texture(display)));
                } else {
                    cooldownTexture.getRegion().getTexture().draw(display, 0, 0);
                }

                return cooldownTexture;
            } finally {
                display.setBlending(blending);

                display.dispose();
            }
        }

        private float calculateAngle(float remainingPercentage) {
            if (clockwise) {
                return 360 - 360 * remainingPercentage;
            } else {
                return 360 * remainingPercentage - 360;
            }
        }

        private int calculateSegments(float angle) {
            return Math.max(1, (int) (6 * (float) Math.cbrt(Math.abs(angle)) * (Math.abs(angle) / 360.0f)));
        }
    }
}
