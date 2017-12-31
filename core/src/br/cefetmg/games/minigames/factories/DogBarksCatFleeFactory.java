package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.DogBarksCatFlee;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

public class DogBarksCatFleeFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new DogBarksCatFlee(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("dog-barks-cat-flee/dog_separado_1.png", Texture.class);
                put("dog-barks-cat-flee/kitten1-alt_3.png", Texture.class);
                put("dog-barks-cat-flee/kitten1-alt_4.png", Texture.class);
                put("dog-barks-cat-flee/dog_separado_2.png", Texture.class);
                put("dog-barks-cat-flee/dog_separado_3.png", Texture.class);
                put("dog-barks-cat-flee/dog_separado_4.png", Texture.class);
                put("dog-barks-cat-flee/dog_spritesheet.png", Texture.class);
                put("dog-barks-cat-flee/spritesheet2.png", Texture.class);
                put("dog-barks-cat-flee/kitten1-alt.png", Texture.class);
                put("dog-barks-cat-flee/tile0.png", Texture.class);
                put("dog-barks-cat-flee/tile1.png", Texture.class);
                put("dog-barks-cat-flee/tile2.png", Texture.class);
                put("dog-barks-cat-flee/tile3.png", Texture.class);
                put("dog-barks-cat-flee/tile4.png", Texture.class);
                //Audio Disponivel em:
                //https://freesound.org/people/Jace/sounds/155309/
                put("dog-barks-cat-flee/bark-sound.wav", Sound.class);
                //Audio Disponivel em:
                //https://freesound.org/people/tuberatanka/sounds/110011/
                put("dog-barks-cat-flee/cat-meow.wav", Sound.class);
                //Audio Disponivel em:
                //https://freesound.org/people/Piink_Aces/sounds/257824/
                put("dog-barks-cat-flee/dog-whining-sound.mp3", Sound.class);
            }
        };
    }
}
