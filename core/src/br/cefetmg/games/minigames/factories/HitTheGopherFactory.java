package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.HitTheGopher;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

public class HitTheGopherFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new HitTheGopher(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("hit-the-gopher/hammer.png", Texture.class);
                put("hit-the-gopher/gopher.png", Texture.class);
                put("hit-the-gopher/gopher-hit.png", Texture.class);
                put("hit-the-gopher/background.png", Texture.class);
                put("hit-the-gopher/smw_kick.wav", Sound.class);

            }
        };
    }
}
