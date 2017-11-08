package br.cefetmg.games.transition;

public class FadeOutTransitionEffect extends FadeTransitionEffect {

    public FadeOutTransitionEffect(float duration) {
        super(duration, 1);
    }

    @Override
    protected void updateAlphaValue() {
        super.alpha = 1 - timeElapsed / duration;
    }
}
