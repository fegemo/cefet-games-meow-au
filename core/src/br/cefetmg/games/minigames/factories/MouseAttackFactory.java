package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.FlappySita;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.Mouse;
import br.cefetmg.games.minigames.MouseAttack;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author
 */
public class MouseAttackFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new MouseAttack(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("MouseAttack/sprite-cat.png",Texture.class);
                put("MouseAttack/sprite-monster1.png",Texture.class);
                put("shoot-the-caries/caries.png", Texture.class);
                put("shoot-the-caries/target.png", Texture.class);
                put("shoot-the-caries/caries1.mp3", Sound.class);
                put("shoot-the-caries/caries2.mp3", Sound.class);
            }
        };
    }
}
