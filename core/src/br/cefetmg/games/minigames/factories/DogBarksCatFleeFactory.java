<<<<<<< HEAD
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.DogBarksCatFlee;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pedro
 */
public class DogBarksCatFleeFactory implements MiniGameFactory{

    @Override
    public MiniGame createMiniGame(BaseScreen screen, 
            MiniGameStateObserver observer, float difficulty) {
        return new DogBarksCatFlee (screen, observer,difficulty);
        
    }

    @Override
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.DogBarksCatFlee;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShooTheTartarus;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
public class DogBarksCatFleeFactory implements MiniGameFactory {
    
    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new DogBarksCatFlee(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {put("DogBarksCatFlee/dog1", Texture.class);            
             put("DogBarksCatFlee/tile0.png", Texture.class);
             put("DogBarksCatFlee/tile1.png", Texture.class);
             put("DogBarksCatFlee/tile2.png", Texture.class);
             put("DogBarksCatFlee/tile3.png", Texture.class);
             put("DogBarksCatFlee/tile4.png", Texture.class);
            }
        };
    }
}
