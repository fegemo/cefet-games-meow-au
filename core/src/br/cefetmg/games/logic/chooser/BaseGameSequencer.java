package br.cefetmg.games.logic.chooser;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.factories.MiniGameFactory;
import br.cefetmg.games.screens.BaseScreen;
import java.util.Set;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

/**
 * Uma classe base que define um algoritmo para determinar a sequência de jogos
 * a ser escolhida.
 *
 * @author lindley
 */
public abstract class BaseGameSequencer {

    protected final Set<MiniGameFactory> availableGames;
    protected final BaseScreen screen;
    protected final MiniGameStateObserver observer;

    /**
     * Cria um algoritmo sequenciador que pega um conjunto de minigames 
     * (factories deles, na verdade), a tela do jogo que vai executá-los e 
     * potenciais observadores do estado do minigame.
     * 
     * @param availableGames conjunto de fábricas dos minigames.
     * @param screen a tela que vai executar os minigames.
     * @param observer um observador do estado do minigame.
     */
    public BaseGameSequencer(Set<MiniGameFactory> availableGames,
            BaseScreen screen, MiniGameStateObserver observer) {
        this.availableGames = availableGames;
        this.screen = screen;
        this.observer = observer;
    }

    /**
     * Retorna se ainda há um próximo jogo, ou se todos da sequência já foram
     * jogados.
     *
     * @return true se ainda há um jogo, false do contrário.
     */
    public abstract boolean hasNextGame();

    /**
     * Retorna uma instância do próximo jogo.
     *
     * @return a instância do próximo jogo, devidamente construída por sua
     * factory.
     */
    public abstract MiniGame nextGame();

    /**
     * Retorna o índice do jogo na sequência, começando de 0.
     *
     * @return o índice do jogo na sequência.
     */
    public abstract int getGameNumber();
}
