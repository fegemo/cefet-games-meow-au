/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.GatoFantasma;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import java.util.Map;

/**
 *
 * @author Alberto
 */
public class GatoFantasmaFactory implements MiniGameFactory{

    @Override
    public MiniGame createMiniGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        return new GatoFantasma(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
