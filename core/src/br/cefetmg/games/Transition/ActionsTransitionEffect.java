
package br.cefetmg.games.Transition;

import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.utils.Timer;

public class ActionsTransitionEffect extends TransitionEffect {
    
    private Timer.Task task;
    private boolean once;
    
    public ActionsTransitionEffect(Timer.Task task) {
        super(0);
        this.task = task;
        once = true;
    }
    
    public ActionsTransitionEffect(Timer.Task task, float duration) {
        super(duration);
        this.task = task;
        once = true;
    }
    
    @Override
    public void render(BaseScreen current) {
        if (once) {
            Timer.instance().scheduleTask(task, duration);
            once = false;
        } else {
            current.show();
            if (current.assets.update())
                isFinished = true;
        }
    }
}
