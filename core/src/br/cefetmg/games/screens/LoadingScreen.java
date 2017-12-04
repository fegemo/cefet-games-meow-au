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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen {

    private final Stage stage;
    private final CooldownTimer cooldownTimer;
    private final TextureRegion background;

    public LoadingScreen(Viewport viewport) {
        stage = new Stage(viewport);

        cooldownTimer = new CooldownTimer(true, viewport.getWorldWidth() * 0.035f);
        cooldownTimer.setColor(0.75f, 0.64f, 0.8f, 1);

        stage.addActor(cooldownTimer);

        // instancia a textura e a região de textura
        Texture backgroundTexture = new Texture("loading-page.png");
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
        batch.draw(background, 0, 0,
                viewport.getScreenWidth(),
                viewport.getScreenHeight());
        batch.end();

        cooldownTimer.update((float) MathUtils.clamp(assets.getProgress() + 0.05, 0, 1));

        stage.act();
        stage.draw();

        return assets.getProgress() >= 1;
    }

    public class CooldownTimer extends Actor {

        private static final int TOTAL_SEGMENTS = 300;
        private static final float START_ANGLE = 90;
        private static final float RADIUS_VARIATION = 0.75f;
        private static final float MARGIN = 30f;
        private float totalAngle = 360;
        private final float initialRadius;
        private float radius;
        private final ShapeRenderer shapes;
        private float percentageComplete;
        private float ellapsedTime = 0;

        /**
         * @param clockwise determines the rotation side of the cooldown timer.
         * @param radius the radius of the circle.
         */
        public CooldownTimer(boolean clockwise, float radius) {
            this.shapes = new ShapeRenderer();
            this.percentageComplete = 0;
            this.radius = radius;
            this.initialRadius = radius;
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
            shapes.setColor(Color.WHITE);
            shapes.circle(
                    MARGIN + initialRadius + RADIUS_VARIATION,
                    MARGIN + initialRadius + RADIUS_VARIATION,
                    radius, TOTAL_SEGMENTS);
            shapes.setColor(super.getColor());
            shapes.arc(
                    MARGIN + initialRadius + RADIUS_VARIATION,
                    MARGIN + initialRadius + RADIUS_VARIATION,
                    radius,
                    START_ANGLE,
                    totalAngle * percentageComplete,
                    TOTAL_SEGMENTS);
            shapes.end();
            shapes.begin(ShapeRenderer.ShapeType.Line);
            shapes.setColor(Color.GRAY);
            shapes.circle(
                    MARGIN + initialRadius + RADIUS_VARIATION,
                    MARGIN + initialRadius + RADIUS_VARIATION,
                    radius, TOTAL_SEGMENTS);
            shapes.end();
            batch.begin();
        }

        /**
         * @param percentageComplete to be rendered by cooldown timer.
         */
        public void update(float percentageComplete) {
            this.percentageComplete = percentageComplete;
        }

        @Override
        public void act(float delta) {
            ellapsedTime += delta;
            this.radius = (float) (Math.sin(ellapsedTime) * RADIUS_VARIATION)
                    + initialRadius;
        }
    }
}
