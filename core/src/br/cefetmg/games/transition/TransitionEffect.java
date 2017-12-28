package br.cefetmg.games.transition;

import br.cefetmg.games.screens.BaseScreen;

public abstract class TransitionEffect {
    
    protected float duration;
    protected float timeElapsed;
    protected boolean isFinished;
    protected float delta;

    protected TransitionEffect(float duration) {
        this.duration = duration;
        timeElapsed = 0;
        delta = 0;
    }

    protected void update(float delta) {
        this.delta = delta;
        timeElapsed += delta;
    } 

    abstract void render(BaseScreen current);

    protected boolean isFinished() {
        return isFinished;
    }
}
