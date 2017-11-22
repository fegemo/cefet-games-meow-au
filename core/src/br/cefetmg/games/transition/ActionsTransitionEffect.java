
package br.cefetmg.games.transition;

import br.cefetmg.games.MeowAuGame;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;

public class ActionsTransitionEffect extends TransitionEffect {
    
    private final Timer.Task task;
    private boolean alreadyExecutedOnce;
    private boolean alreadySwitchedScreens;
    
    public ActionsTransitionEffect(Timer.Task task) {
        this(task, 0);
    }
    
    public ActionsTransitionEffect(Timer.Task task, float duration) {
        super(duration);
        this.task = task;
        alreadyExecutedOnce = false;
        alreadySwitchedScreens = false;
    }
    
    @Override
    public void render(BaseScreen current) {
        if (!alreadyExecutedOnce) {
            Timer.instance().scheduleTask(task, duration);
            alreadyExecutedOnce = true;
        } else {
            if (!alreadySwitchedScreens) {
                alreadySwitchedScreens = true;
                current.show();
                current.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
            if (current.assets.update()) { // aguarda a conclusão do carregamento dos assets
                if (current.game instanceof MeowAuGame) {
                    MeowAuGame game = (MeowAuGame) current.game;
                    game.setLoadedScreen(current); // informa que a tela já foi carregada
                }
                isFinished = true;
            }
        }
    }
}
