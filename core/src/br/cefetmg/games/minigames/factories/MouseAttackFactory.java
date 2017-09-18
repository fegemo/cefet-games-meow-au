package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
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
                put("mouse-attack/sprite-cat.png",Texture.class);
                put("mouse-attack/sprite-monster.png",Texture.class);
                put("mouse-attack/projetil.png",Texture.class);
                put("mouse-attack/target.png", Texture.class);
                put("mouse-attack/shoot-sound.mp3", Sound.class);
                put("mouse-attack/monster-dying.mp3", Sound.class);
            }
        };
    }
}
