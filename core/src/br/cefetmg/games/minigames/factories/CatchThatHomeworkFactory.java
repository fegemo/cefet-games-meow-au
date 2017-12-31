package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.CatchThatHomework;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.screens.BaseScreen;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

public class CatchThatHomeworkFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new CatchThatHomework(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("catch-that-homework/cat-spritesheet.png",
                        Texture.class);
                put("catch-that-homework/homework.png",
                        Texture.class);
                put("catch-that-homework/cat-sprite.png",
                        Texture.class);
                put("catch-that-homework/bensound-sexy.mp3", Music.class);
                put("catch-that-homework/valley.png", Texture.class);
            }
        };
    }
}
