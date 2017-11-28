package br.cefetmg.games.minigames.util;

/**
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public interface MiniGameStateObserver {
    void onStateChanged(MiniGameState state);//, MiniGame game);
    void onTimeEnding();
    void onGamePaused();
    void onGameResumed();
    void showMessage(String strMessage);
}
