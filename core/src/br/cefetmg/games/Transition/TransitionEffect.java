
package br.cefetmg.games.Transition;

import br.cefetmg.games.screens.BaseScreen;

public abstract class TransitionEffect {
    
    protected float alpha;
    protected float duration;
    protected float timeElapsed;
    protected float delta;
    protected boolean isFinished;
    
    protected TransitionEffect(float duration) {
        this.duration = duration;
        alpha = 0;
        timeElapsed = 0;
    }
    
    protected float getAlpha() {
        return alpha;
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
