/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.TheFridgeGame;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sarit
 */
public class TheFridgeGameFactory implements MiniGameFactory{
    
    @Override
    public MiniGame createMiniGame(BaseScreen screen, 
            MiniGameStateObserver observer, float difficulty){
        
        return new TheFridgeGame(screen, observer, difficulty);
    }
    
    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("the-fridge-game/fridge-game-background.png", Texture.class);
                put("the-fridge-game/open-fridge.png", Texture.class);
              //  put("super-micro-jogo/tiro.wav", Sound.class);                  
            }
        };
    }
}
