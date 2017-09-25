package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.ClickFindCat;
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
public class ClickFindCatFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new ClickFindCat(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("ClickFindCat/gatinho-grande.png", Texture.class);
                put("ClickFindCat/target.png", Texture.class);
                //Audio Disponivel em:
                //https://freesound.org/people/tuberatanka/sounds/110011/
                put("ClickFindCat/cat-meow.wav", Sound.class);
                //Audio Disponivel em:
                //https://freesound.org/people/NoiseCollector/sounds/4914/
                put("ClickFindCat/ScaredCat.wav", Sound.class);
                //Audio Disponivel em:
                //https://freesound.org/people/zut50/sounds/162395/
                put("ClickFindCat/YAY.mp3", Sound.class);

            }
        };
    }

}
