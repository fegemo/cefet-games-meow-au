package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShootTheCaries;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class ShootTheCariesFactory implements MiniGameFactory {

    /**
     * Veja {@link MiniGameFactory}.
     *
     * @param screen
     * @param observer
     * @param difficulty
     * @return
     */
    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new ShootTheCaries(screen, observer, difficulty);
    }

    /**
     * Veja {@link MiniGameFactory}.
     *
     * @return
     */
    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("shoot-the-caries/caries.png", Texture.class);
                put("shoot-the-caries/target.png", Texture.class);
                put("shoot-the-caries/caries1.mp3", Sound.class);
                put("shoot-the-caries/caries2.mp3", Sound.class);
            }
        };
    }

}
