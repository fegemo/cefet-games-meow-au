package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.JetRat;
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
public class JetRatFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new JetRat(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class getAssetsToPreload() {
        return new HashMap<String, Class>() {
			{
                put("jet-rat/tubecat.png", Texture.class);
                put("jet-rat/background.png", Texture.class);
                put("jet-rat/tube.png", Texture.class);
                put("jet-rat/jatmouse.png", Texture.class);
                put("jet-rat/meon.mp3", Sound.class);
            }
        };
    }
}
