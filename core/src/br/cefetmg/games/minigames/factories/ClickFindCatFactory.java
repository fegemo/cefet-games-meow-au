package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.ClickFindCat;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pedro
 */
public class ClickFindCatFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new ClickFindCat(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("click-find-cat/gatinho-grande.png", Texture.class);
                put("click-find-cat/target.png", Texture.class);
                //Imagem Disponível em
                //http://i.imgur.com/8McJ3ce.png
                put("click-find-cat/crav_rat.png", Texture.class);
                //Audio Disponivel em:
                //https://freesound.org/people/tuberatanka/sounds/110011/
                put("click-find-cat/cat-meow.wav", Sound.class);
            }
        };
    }

}
