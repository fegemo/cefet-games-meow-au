package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.DodgeTheVeggies;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

public class DodgeTheVeggiesFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new DodgeTheVeggies(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("cucumber-madness/potato.png", Texture.class);
                put("cucumber-madness/tomato.png", Texture.class);
                put("cucumber-madness/onion.png", Texture.class);
                put("cucumber-madness/carrot.png", Texture.class);
                put("cucumber-madness/cat-sprite.png", Texture.class);
                put("cucumber-madness/background.png", Texture.class);
                put("cucumber-madness/bensound-jazzcomedy.mp3", Sound.class);
            }
        };
    }
}
