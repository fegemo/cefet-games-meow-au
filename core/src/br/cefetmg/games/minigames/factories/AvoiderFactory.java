package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.CatAvoider;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShooTheTartarus;
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
public class AvoiderFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new CatAvoider(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("avoider/backgroundTexture.png", Texture.class);
                put("avoider/grey.png", Texture.class);
                put("avoider/catNinja.png", Texture.class);
                put("avoider/wool.png", Texture.class);
                put("avoider/cat-moving-up-spritesheet.png", Texture.class);
                put("avoider/cat-moving-down-spritesheet.png", Texture.class);
                put("avoider/cat-moving-left-spritesheet.png", Texture.class);
                put("avoider/cat-moving-right-spritesheet.png", Texture.class);
                put("avoider/cat-moving-dLeft-spritesheet.png", Texture.class);
                put("avoider/cat-moving-dRight-spritesheet.png", Texture.class);
            }
        };
    }
}
