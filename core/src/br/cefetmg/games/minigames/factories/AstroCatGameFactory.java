package br.cefetmg.games.minigames.factories;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import br.cefetmg.games.minigames.AstroCatGame;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;

/**
 * Factory do jogo AstroCat
 * 
 * @author andrebrait
 *
 */
public class AstroCatGameFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        return new AstroCatGame(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("astrocat/asteroid1.png", Texture.class);
                put("astrocat/asteroid2.png", Texture.class);
                put("astrocat/asteroid3.png", Texture.class);
                put("astrocat/asteroid4.png", Texture.class);
                put("astrocat/asteroid5.png", Texture.class);
                put("astrocat/asteroid6.png", Texture.class);
                put("astrocat/astrocat.png", Texture.class);
                put("astrocat/background.png", Texture.class);
                put("astrocat/background.mp3", Music.class);
                put("astrocat/gasnoise.mp3", Sound.class);
                put("astrocat/impact.ogg", Sound.class);
                put("astrocat/planet.png", Texture.class);
                put("astrocat/crosshair.png", Texture.class);
            }
        };
    }

}
