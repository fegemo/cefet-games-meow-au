package br.cefetmg.games.graphics.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 *
 * @author fegemo
 */
public class Countdown extends Image {

    private final TextureRegionDrawable[] steps;
    private static final int COUNTDOWN_STEP_HEIGHT = 200;
    private static final int COUNTDOWN_STEP_NUMBER_WIDTH = 180;
    private static final int COUNTDOWN_STEP_FINAL_WIDTH = 540;
    private static final int COUNTDOWN_NUMBER_STEPS = 3;

    public Countdown(Texture texture) {
        TextureRegion[] regions = new TextureRegion[COUNTDOWN_NUMBER_STEPS + 1];
        for (int i = 0; i < regions.length - 1; i++) {
            // 3, 2, 1
            regions[i] = new TextureRegion(texture,
                    COUNTDOWN_STEP_NUMBER_WIDTH * i,
                    0,
                    COUNTDOWN_STEP_NUMBER_WIDTH,
                    COUNTDOWN_STEP_HEIGHT
            );
        }

        // Começar!
        regions[COUNTDOWN_NUMBER_STEPS] = new TextureRegion(texture,
                0,
                COUNTDOWN_STEP_HEIGHT,
                COUNTDOWN_STEP_FINAL_WIDTH,
                COUNTDOWN_STEP_HEIGHT
        );

        steps = new TextureRegionDrawable[regions.length];
        for (int i = 0; i < regions.length; i++) {
            steps[i] = new TextureRegionDrawable(regions[i]);
        }

        setWidth(COUNTDOWN_STEP_FINAL_WIDTH);
        setHeight(COUNTDOWN_STEP_HEIGHT);
    }

    public void start() {
        Integer frameCounter = 0;

        ScaleToAction reduce = new ScaleToAction();
        reduce.setScale(0.5f / COUNTDOWN_NUMBER_STEPS, 0.5f);
        reduce.setDuration(0.5f);
        reduce.setInterpolation(Interpolation.sineOut);

        ScaleToAction reset = new ScaleToAction();
        reset.setScale(1f / COUNTDOWN_NUMBER_STEPS, 1f);
        reset.setDuration(0);

        DelayAction delay = new DelayAction(0.5f);

        NextFrameAction next = new NextFrameAction(frameCounter, steps);
        SetDrawableAction first = new SetDrawableAction(steps[0]);

        scaleBy(1f / COUNTDOWN_NUMBER_STEPS, 1f);

        addAction(Actions.sequence(
                first,
                Actions.show(),
                Actions.repeat(
                        COUNTDOWN_NUMBER_STEPS,
                        Actions.sequence(reset, reduce, delay, next)),
                Actions.scaleTo(1, 1),
                Actions.delay(0.5f),
                Actions.hide(),
                Actions.scaleTo(1f / COUNTDOWN_NUMBER_STEPS, 1)
        ));
    }

    class NextFrameAction extends SetDrawableAction {

        private final Drawable[] drawables;
        private Integer frame;

        public NextFrameAction(Integer frame, Drawable[] drawables) {
            super(drawables[0]);
            this.drawables = drawables;
            this.frame = frame;
        }

        @Override
        public boolean act(float delta) {
            frame = (frame + 1) % drawables.length;
            super.setDrawable(drawables[frame]);
            return super.act(delta);
        }

    }

    class SetDrawableAction extends Action {

        private Drawable drawable;

        public SetDrawableAction(Drawable drawable) {
            this.drawable = drawable;
        }

        @Override
        public boolean act(float delta) {
            if (target instanceof Image) {
                ((Image) target).setDrawable(drawable);
            }
            return true;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }
}
