package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.Running;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author gustavo
 */
public class RunningFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new Running(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class<?>> getAssetsToPreload() {
        return new HashMap<String, Class<?>>() {

        	private static final long serialVersionUID = -7323404034696729084L;

			{
                put("running/dog-run-spritesheet.png", Texture.class);
                put("running/cat-run-spritesheet.png", Texture.class);
                put("running/fundo.png", Texture.class);
                put("running/ball.png", Texture.class);
                put("running/wool.png", Texture.class);
                put("running/bone.png", Texture.class);
                put("running/kit.png", Texture.class);
                put("running/pickup_wool.wav", Sound.class);
                put("running/pickup_kit.wav", Sound.class);
                put("running/final.wav", Sound.class);
                put("running/lose.wav", Sound.class);
            }
        };
    }
}
