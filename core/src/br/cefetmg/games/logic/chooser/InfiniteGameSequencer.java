package br.cefetmg.games.logic.chooser;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.factories.MiniGameFactory;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author GHMarques <gustavo.marques2011@gmail.com>
 */
public class InfiniteGameSequencer extends BaseGameSequencer {
    private static final int STAGNANT_LEVEL = 30;
    private int level;
    private float finalDifficulty;
    private float initialDifficulty;

    /**
     *
     * @param availableGames os tipos de minigames disponíveis para o
     * sequenciador.
     * @param initialDifficulty dificuldade usada para o primeiro MiniGame. Deve
     * estar entre 0 e 1.
     * @param finalDifficulty dificuldade usada para o último MiniGames. Deve
     * estar entre 0 e 1.
     * @param screen a tela dona destes jogos.
     * @param observer um observador da mudança de estado dos jogos.
     */
    public InfiniteGameSequencer(Set<MiniGameFactory> availableGames,
            float initialDifficulty, float finalDifficulty,
            BaseScreen screen, MiniGameStateObserver observer) {
        super(availableGames, screen, observer);
        if (availableGames.size() <= 0) {
            throw new IllegalArgumentException("Tentou-se criar um "
                    + "GameSequencer com 0 jogos. Deve haver ao menos 1.");
        }
        this.initialDifficulty = initialDifficulty;
        this.finalDifficulty = finalDifficulty;
        level = 0;
        preloadAssets();
    }

    @Override
    public boolean hasNextGame() {
        return true;
    }

    private float getSequenceProgress() {
        return Math.min(1, ((float) this.level) / (STAGNANT_LEVEL - 1));
    }

    /**
     * Pré-carrega os <em>assets</em> de todos os minigames.
     */
    private void preloadAssets() {
        HashMap<String, Class> allAssets = new HashMap<String, Class>();
        for(int i = 0; i < availableGames.size(); i++)
            allAssets.putAll(((MiniGameFactory) availableGames.toArray()[i])
                    .getAssetsToPreload());
        for (Map.Entry<String, Class> asset : allAssets.entrySet())
            screen.assets.load(asset.getKey(), asset.getValue());
    }

    /**
     * Retorna uma instância do próximo jogo.
     *
     * @return uma instância do próximo jogo.
     */
    @Override
    public MiniGame nextGame() {
        MiniGameFactory factory = (MiniGameFactory) availableGames
                .toArray()[randomGameNumber()];
        float difficulty = DifficultyCurve.S.getCurveValueBetween(
                getSequenceProgress(), initialDifficulty, finalDifficulty);
        this.level++;

        return factory.createMiniGame(screen, observer, difficulty);
    }
    
    public int randomGameNumber(){
        return MathUtils.random(availableGames.size() - 1);
    }

    @Override
    public int getGameNumber() {
        return this.level;
    }

}
