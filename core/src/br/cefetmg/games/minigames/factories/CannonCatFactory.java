package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.CannonCat;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

public class CannonCatFactory implements MiniGameFactory {
    // ...

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("cannon-cat/background.png", Texture.class);
                put("cannon-cat/biscoito.png", Texture.class);
                put("cannon-cat/cat.png", Texture.class);
                put("cannon-cat/cannon_down.png", Texture.class);
                put("cannon-cat/cannon_down+left.png", Texture.class);
                put("cannon-cat/cannon_down+right.png", Texture.class);
                put("cannon-cat/cannon_right.png", Texture.class);
                put("cannon-cat/cannon_left.png", Texture.class);
                put("cannon-cat/cannon_up+right.png", Texture.class);
                put("cannon-cat/cannon_up.png", Texture.class);
                put("cannon-cat/cannon_up+left.png", Texture.class);
                put("cannon-cat/background-music.mp3", Sound.class);
            }
        };
    }

    @Override
    public MiniGame createMiniGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        return new CannonCat(screen, observer, difficulty);
    }
}
