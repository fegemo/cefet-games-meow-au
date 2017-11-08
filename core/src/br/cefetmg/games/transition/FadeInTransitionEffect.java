package br.cefetmg.games.transition;

public class FadeInTransitionEffect extends FadeTransitionEffect {

    public FadeInTransitionEffect(float duration) {
        super(duration, 0);
    }

    @Override
    protected void updateAlphaValue() {
        alpha = timeElapsed / duration;
    }
}
