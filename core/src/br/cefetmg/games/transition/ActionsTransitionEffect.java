
package br.cefetmg.games.transition;

import br.cefetmg.games.MeowAuGame;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.screens.LoadingScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;

public class ActionsTransitionEffect extends TransitionEffect {
    
    private final Timer.Task task;
    private final LoadingScreen loadingScreen;
    private boolean alreadyExecutedOnce;
    private ShapeRenderer shapeRenderer;

    public ActionsTransitionEffect(Timer.Task task) {
        this(task, null);
    }
    
    public ActionsTransitionEffect(Timer.Task task, LoadingScreen loadingScreen) {
        super(0);
        shapeRenderer = new ShapeRenderer(20);
        this.task = task;
        this.loadingScreen = loadingScreen;
        alreadyExecutedOnce = false;
    }
    
    @Override
    public void render(BaseScreen current) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, current.viewport.getScreenWidth(), current.viewport.getScreenHeight());
        shapeRenderer.end();

        if (!alreadyExecutedOnce) {
            Timer.instance().postTask(task);
            alreadyExecutedOnce = true;
        } else {
            boolean isLoadingOver;
            if (loadingScreen != null) {
                loadingScreen.draw(current.assets);
            }
            isLoadingOver = current.assets.update();

            if (isLoadingOver) { // aguarda a conclusão do carregamento dos assets
                if (current.game instanceof MeowAuGame) {
                    MeowAuGame game = (MeowAuGame) current.game;
                    game.setLoadedScreen(current); // informa que a tela já foi carregada
                }
                isFinished = true;
            }
        }
    }
}
