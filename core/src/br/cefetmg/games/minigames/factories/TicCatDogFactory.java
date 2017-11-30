package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.TicCatDog;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

public class TicCatDogFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new TicCatDog(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
			{
                put("tic-cat-dog/main-background.jpg", Texture.class);
                put("tic-cat-dog/white-square.png", Texture.class);
                put("tic-cat-dog/cat-square.png", Texture.class);
                put("tic-cat-dog/dog-square.png", Texture.class);
                put("tic-cat-dog/mouse-arrow.png", Texture.class);
                put("tic-cat-dog/cat-meowing.wav", Sound.class);
                put("tic-cat-dog/dog-barking.wav", Sound.class);
            }
        };
    }
}
