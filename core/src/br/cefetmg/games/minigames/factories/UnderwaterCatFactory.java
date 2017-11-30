package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.UnderwaterCat;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Adriel
 */
public class UnderwaterCatFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new UnderwaterCat(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
			{
                put("underwater-cat/swimcatspritesheet.png", Texture.class);
                put("underwater-cat/fish1.png", Texture.class);
                put("underwater-cat/fish2.png", Texture.class);
                put("underwater-cat/fish3.png", Texture.class);
                put("underwater-cat/fish5.png", Texture.class);
                put("underwater-cat/background.bmp", Texture.class);
                put("underwater-cat/water.mp3", Music.class);
                put("underwater-cat/swim.wav", Music.class);
                put("underwater-cat/eat.wav", Sound.class);

            }
        };
    }

}
