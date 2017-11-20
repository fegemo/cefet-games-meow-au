package br.cefetmg.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen {

    private final Stage stage;
    private final CooldownTimer cooldownTimer;

    private float remainingPercentage = 1.0f;

    private final float STEP = 0.05f;
    private final int TIMER_SIZE = 100;

    private final TextureRegion background;

    public LoadingScreen() {
        stage = new Stage();

        cooldownTimer = new CooldownTimer(true);
        cooldownTimer.setSize(TIMER_SIZE, TIMER_SIZE);
        cooldownTimer.setPosition(0, 0);
        cooldownTimer.setColor(0.75f, 0.64f, 0.8f, 1);

        stage.addActor(cooldownTimer);

        // instancia a textura e a região de textura
        Texture backgroundTexture = new Texture("loading-page.jpg");
        backgroundTexture.setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        background = new TextureRegion(backgroundTexture);
    }

    public boolean draw(AssetManager assets, SpriteBatch batch,
            Viewport viewport) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, TIMER_SIZE,
                viewport.getScreenWidth(),
                viewport.getScreenHeight() - TIMER_SIZE);
        batch.end();

        cooldownTimer.update(remainingPercentage);

        //A medida que os assets são carregados, atualiza-se suavemente o 
        // círculo de carregamento
        if (remainingPercentage - STEP >= 1 - assets.getProgress()) {
            remainingPercentage -= STEP;
        }

        stage.act();
        stage.draw();

        if (remainingPercentage <= STEP) {
            remainingPercentage = 1.0f;
            return true;
        }
        return false;
    }

    public class CooldownTimer extends Actor {

        private static final int TOTAL_SEGMENTS = 300;
        private static final float START_ANGLE = 90;
        private static final float INITIAL_RADIUS = 15;
        private static final float RADIUS_VARIATION = 0.75f;
        private static final float MARGIN = 4f;
        private float totalAngle = 360;
        private float radius = INITIAL_RADIUS;
        private final ShapeRenderer shapes;
        private float alpha = 0.5f;
        private float percentageComplete;
        private float ellapsedTime = 0;

        /**
         * @param clockwise determines the rotation side of the cooldown timer.
         */
        public CooldownTimer(boolean clockwise) {
            this.shapes = new ShapeRenderer();
            this.percentageComplete = 0;
            if (clockwise) {
                totalAngle *= -1;
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.end();
            shapes.setProjectionMatrix(batch.getProjectionMatrix());
            shapes.setTransformMatrix(batch.getTransformMatrix());
            shapes.begin(ShapeRenderer.ShapeType.Filled);
            shapes.setColor(super.getColor());
            shapes.arc(
                    MARGIN + INITIAL_RADIUS + RADIUS_VARIATION,
                    MARGIN + INITIAL_RADIUS + RADIUS_VARIATION,
                    radius,
                    START_ANGLE,
                    totalAngle * percentageComplete,
                    TOTAL_SEGMENTS);
            shapes.end();
            shapes.begin(ShapeRenderer.ShapeType.Line);
            shapes.setColor(Color.GRAY);
            shapes.circle(
                    MARGIN + INITIAL_RADIUS + RADIUS_VARIATION,
                    MARGIN + INITIAL_RADIUS + RADIUS_VARIATION,
                    radius, TOTAL_SEGMENTS);
            shapes.end();
            batch.begin();
        }

        /**
         * @param remainingPercentage to be rendered by cooldown timer.
         */
        public void update(float remainingPercentage) {
            this.percentageComplete = 1 - remainingPercentage;
        }

        @Override
        public void act(float delta) {
            ellapsedTime += delta;
            this.radius = (float) (Math.sin(ellapsedTime) * RADIUS_VARIATION)
                    + INITIAL_RADIUS;
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
    }
}
