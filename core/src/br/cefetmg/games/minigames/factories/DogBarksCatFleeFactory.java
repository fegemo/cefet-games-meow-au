
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.DogBarksCatFlee;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShooTheTartarus;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class DogBarksCatFleeFactory implements MiniGameFactory {
    
    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new DogBarksCatFlee(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {put("DogBarksCatFlee/dog_separado_1.png", Texture.class);
             put("DogBarksCatFlee/dog1.png", Texture.class);
             put("DogBarksCatFlee/kitten1-alt.png", Texture.class);  
             put("DogBarksCatFlee/tile0.png", Texture.class);
             put("DogBarksCatFlee/tile1.png", Texture.class);
             put("DogBarksCatFlee/tile2.png", Texture.class);
             put("DogBarksCatFlee/tile3.png", Texture.class);
             put("DogBarksCatFlee/tile4.png", Texture.class);
            }
        };
    }
}


