package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.RatoaJato;
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
public class RatoaJatoFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
                                   MiniGameStateObserver observer, float difficulty) {
        return new RatoaJato(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {


                put("RatoaJato/tubecat.png", Texture.class);
                put("RatoaJato/background.png",
                        Texture.class);
                put("RatoaJato/tube.png",
                        Texture.class);
                put("RatoaJato/jatmouse.png",
                        Texture.class);
                put("RatoaJato/meon.mp3", Sound.class);

            }
        };
    }
}
