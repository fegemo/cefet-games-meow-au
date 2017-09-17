package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.FlappySita;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class FlappySitaFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
                                   MiniGameStateObserver observer, float difficulty) {
        return new FlappySita(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {

                put("FlappySita/bird2.png",
                        Texture.class);
                put("FlappySita/tubecat.png",
                        Texture.class);
                 put("FlappySita/bird.png",
                        Texture.class);
                put("FlappySita/background.png",
                        Texture.class);
                put("FlappySita/crow.png",
                        Texture.class);
                put("FlappySita/tube.png",
                        Texture.class);
                put("FlappySita/jatmouse.png",
                        Texture.class);
            }
        };
    }
}
