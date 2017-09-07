/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.ClickFindCat;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pedro
 */
public class ClickFindCatFactory implements MiniGameFactory{

    @Override
    public MiniGame createMiniGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        return new ClickFindCat(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("DogBarksCatFlee/gatinho-grande.png", Texture.class);
                put("DogBarksCatFlee/Dog_separado_1.png", Texture.class);
            }
        };
    }
    
}
