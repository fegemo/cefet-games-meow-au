package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.JumpTheObstacles;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.PacDog;
import br.cefetmg.games.minigames.TicCatDog;
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
public class JumpTheObstaclesFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new JumpTheObstacles(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("jump-the-obstacles/dog.png", Texture.class);
                put("jump-the-obstacles/obstacles.png", Texture.class);
                put("jump-the-obstacles/background.png", Texture.class);
                put("jump-the-obstacles/appearing1.wav", Sound.class);
                put("jump-the-obstacles/appearing2.wav", Sound.class);
                put("jump-the-obstacles/appearing3.wav", Sound.class);
                put("jump-the-obstacles/29-extra-life-balloon.mp3", Sound.class);
            }
        };
    }
}
