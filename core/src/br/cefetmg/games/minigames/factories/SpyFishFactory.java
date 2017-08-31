/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.SpyFish;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alberto
 */
public class SpyFishFactory implements MiniGameFactory{

    @Override
    public MiniGame createMiniGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        return new SpyFish(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>(){
            {
                put("spy-fish/fish.png", Texture.class );
                put("spy-fish/memory-card.png", Texture.class);
                put("spy-fish/fundo.png", Texture.class);
                put("spy-fish/fundo-controle.png",Texture.class);
                put("spy-fish/controle-principal.png",Texture.class);
            }
        };
    //To change body of generated methods, choose Tools | Templates.
    }
    
}
