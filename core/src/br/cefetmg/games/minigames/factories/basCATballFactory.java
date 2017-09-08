package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShootTheCaries;
import br.cefetmg.games.minigames.basCATball;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
/**
 *
 * @author Rógenes
 */
public class basCATballFactory implements MiniGameFactory {
    
    
    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new basCATball(screen, observer, difficulty);
    }
    
    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("bascatball/ball.png", Texture.class);
                put("bascatball/ball2.png", Texture.class);
                put("bascatball/basket.png", Texture.class);
                put("bascatball/player.png", Texture.class);
                put("bascatball/bar.png", Texture.class);
                put("bascatball/target.png", Texture.class);
                put("bascatball/selector.png", Texture.class);
                
                
              //  put("shoot-the-caries/basket.png", Texture.class);
                
              //  put("shoot-the-caries/caries1.mp3", Sound.class);
               // put("shoot-the-caries/caries2.mp3", Sound.class);
            }
        };
    }
    
    
}
