package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.RainingCats;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import com.badlogic.gdx.audio.Music;

public class RainingCatsFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new RainingCats(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class<?>> getAssetsToPreload() {
        return new HashMap<String, Class<?>>() {

        	private static final long serialVersionUID = 3503356991871998393L;

			{

                put("raining-cats/player.png", Texture.class);
                put("raining-cats/sakamoto.png", Texture.class);
                put("raining-cats/sakamoto1.png", Texture.class);
                put("raining-cats/sakamoto2.jpg", Texture.class);
                put("raining-cats/arrow.png", Texture.class);

                put("raining-cats/music.mp3", Music.class);
                put("raining-cats/Pop.mp3", Sound.class);

            }
        };
    }

}
