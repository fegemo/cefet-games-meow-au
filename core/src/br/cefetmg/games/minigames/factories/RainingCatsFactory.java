package br.cefetmg.games.minigames.factories;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.RainingCats;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

public class RainingCatsFactory implements MiniGameFactory {
    
    
    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new RainingCats(screen, observer, difficulty);
    }
    
    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
              
                put("raining-cats/player.png", Texture.class);
                put("raining-cats/sakamoto.png",Texture.class);
                put("raining-cats/sakamoto1.png",Texture.class);
                put("raining-cats/sakamoto2.jpg",Texture.class);
                put("raining-cats/arrow.png",Texture.class);
                
                
             
            }
        };
    }
    
    
}
