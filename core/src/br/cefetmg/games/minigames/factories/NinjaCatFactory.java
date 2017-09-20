package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.NinjaCat;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

public class NinjaCatFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new NinjaCat(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("ninja-cat/bg1.jpg", Texture.class);
                put("ninja-cat/arrow.png", Texture.class);
                put("ninja-cat/atk.png", Texture.class);
                put("ninja-cat/atk1.png", Texture.class);
                put("ninja-cat/zombie1.png", Texture.class);
                put("ninja-cat/zombie.png", Texture.class);
                put("ninja-cat/zombie2.png", Texture.class);
                put("ninja-cat/cat.png", Texture.class);
                put("ninja-cat/cat1.png", Texture.class);
                put("ninja-cat/bg1.jpg", Texture.class);
                put("ninja-cat/d1.png", Texture.class);
                put("ninja-cat/d2.png", Texture.class);
                put("ninja-cat/d3.png", Texture.class);
                put("ninja-cat/d4.png", Texture.class);
                put("ninja-cat/d5.png", Texture.class);
                put("ninja-cat/d6.png", Texture.class);
                put("ninja-cat/hit.png", Texture.class);
                put("ninja-cat/hit1.png", Texture.class);
                put("ninja-cat/intro.png", Texture.class);

                put("ninja-cat/Intro.mp3", Sound.class);
                put("ninja-cat/ken1.mp3", Sound.class);
                put("ninja-cat/ken2.mp3", Sound.class);
                put("ninja-cat/GameOver.mp3", Sound.class);
                put("ninja-cat/zombie.mp3", Sound.class);
                put("ninja-cat/zombie1.mp3", Sound.class);

            }
        };
    }

}
