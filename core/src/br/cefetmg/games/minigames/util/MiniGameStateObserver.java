package br.cefetmg.games.minigames.util;

import br.cefetmg.games.minigames.MiniGame;

/**
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public interface MiniGameStateObserver {
    void onStateChanged(MiniGameState state);//, MiniGame game);
    void onTimeEnding();
    void onGamePaused();
    void onGameResumed();
}
