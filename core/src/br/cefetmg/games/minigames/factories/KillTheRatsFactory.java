/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.KillTheRats;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author acces
 */
public class KillTheRatsFactory implements MiniGameFactory {
    @Override
    public MiniGame createMiniGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        return new KillTheRats(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("kill-the-rats/Background_Sewer.png", Texture.class);
                put("kill-the-rats/lakitu.png", Texture.class);
                put("kill-the-rats/ratframes.png", Texture.class);
                put("kill-the-rats/fireball_0.png", Texture.class);
                //put("kill-the-rats/sound.wav", Sound.class);
            }
        };
    }
}
