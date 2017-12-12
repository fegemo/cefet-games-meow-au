package br.cefetmg.games.transition;

import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.screens.LoadingScreen;
import br.cefetmg.games.screens.PlayingGamesScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Timer;
import java.util.ArrayList;

public class TransitionScreen extends ScreenAdapter {

    public enum Effect {
        NONE,
        FADE_IN,
        FADE_OUT,
        FADE_IN_OUT
    }

    private static TransitionScreen lastInstance = null;
    private final Game game;
    private BaseScreen current;
    private final BaseScreen next;
    private final ArrayList<TransitionEffect> transitionEffects;
    private int currentTransitionEffect;

    private LoadingScreen loadingScreen; //Tela de carregamento utilizada para transição para PlayingGamesScreen
    private boolean isLoadingOver = true; //Variável de controle utilizada para garantir a conclusão da tela de carregamento

    private TransitionScreen(BaseScreen current, BaseScreen next, ArrayList<TransitionEffect> transitionEffects) {
        this.current = current;
        this.next = next;
        this.transitionEffects = transitionEffects;
        this.currentTransitionEffect = 0;
        this.game = current.game;
        if (next instanceof PlayingGamesScreen) {
            this.loadingScreen = new LoadingScreen(next.viewport, next.batch);
        }
    }

    // ======== Sobrecarga para getInstance ======== //
    public static TransitionScreen getInstance(BaseScreen current, BaseScreen next, ArrayList<TransitionEffect> transitionEffects) {
        if (lastInstance == null) {
            return new TransitionScreen(current, next, transitionEffects);
        } else {
            return lastInstance;
        }
    }

    public static TransitionScreen getInstance(BaseScreen current, BaseScreen next) {
        return getInstance(current, next, new ArrayList<TransitionEffect>());
    }

    public static TransitionScreen getInstance(BaseScreen current) {
        return getInstance(current, current, new ArrayList<TransitionEffect>());
    }

    public static TransitionScreen getInstance() {
        return lastInstance;
    }

    // ================ //
    @Override
    public void render(float delta) {
        //Verifica se os assets já foram carregados e se a tela de loading já foi concluída
        if (current.assets.update() && isLoadingOver) {
            if (currentTransitionEffect >= transitionEffects.size()) {
                game.setScreen(next);
                lastInstance = null;
                return;
            }

            transitionEffects.get(currentTransitionEffect).update(delta);
            transitionEffects.get(currentTransitionEffect).render(current);

            if (transitionEffects.get(currentTransitionEffect).isFinished()) {
                currentTransitionEffect++;
            }
        } else if (next instanceof PlayingGamesScreen) {
            // se a próxima tela for a de PlayingGamesScreen
            // a tela de carregamento será chamada até sua conclusão 
            // (isLoadingOver receber true)
            isLoadingOver = loadingScreen.draw(next.assets);
        }
    }

    // ================ //
    private void setTask(Timer.Task task) {
        if (task != null) {
            transitionEffects.add(new ActionsTransitionEffect(task));
        } else if (current != next) {
            transitionEffects.add(new ActionsTransitionEffect(new Timer.Task() {
                @Override
                public void run() {
                    current = next;
                }
            }));
        }
    }

    public void execute(Effect effect, float duration, Timer.Task task) {
        if (lastInstance != null) {
            return;
        }

        lastInstance = this;

        switch (effect) {
            case FADE_IN:
                transitionEffects.add(new FadeInTransitionEffect(duration));
                setTask(task);
                break;
            case FADE_IN_OUT:
                transitionEffects.add(new FadeInTransitionEffect(duration));
            case FADE_OUT:
                setTask(task);
                transitionEffects.add(new FadeOutTransitionEffect(duration));
                break;
            default:
                setTask(task);
                break;
        }

        game.setScreen(this);
    }

    public void execute(Effect effect, float duration) {
        execute(effect, duration, null);
    }

    public void execute() {
        if (lastInstance != null) {
            return;
        } else {
            lastInstance = this;
        }

        if (transitionEffects != null && !transitionEffects.isEmpty()) {
            game.setScreen(this);
        }
    }
}
