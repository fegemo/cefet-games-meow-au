package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.Meowsic;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

public class MeowsicFactory implements MiniGameFactory {
    // ...

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("meowsic/background.png", Texture.class);
                put("meowsic/cat.png", Texture.class);
                put("meowsic/sheet.png", Texture.class);
                put("meowsic/note.png", Texture.class);
                put("meowsic/Pressed1.png", Texture.class);
                put("meowsic/Pressed2.png", Texture.class);
                put("meowsic/Pressed3.png", Texture.class);
                put("meowsic/Pressed4.png", Texture.class);
                put("meowsic/Pressed5.png", Texture.class);
                put("meowsic/note1.png", Texture.class);
                put("meowsic/note2.png", Texture.class);
                put("meowsic/note3.png", Texture.class);
                put("meowsic/note4.png", Texture.class);
                put("meowsic/note5.png", Texture.class);

                put("meowsic/song1.mp3", Sound.class);
                put("meowsic/song2.mp3", Sound.class);
                put("meowsic/song3.mp3", Sound.class);
                put("meowsic/song4.mp3", Sound.class);
                put("meowsic/song5.mp3", Sound.class);
                put("meowsic/music1.mp3", Music.class);
                put("meowsic/music2.mp3", Music.class);
                put("meowsic/music3.mp3", Music.class);
                put("meowsic/music4.mp3", Music.class);
                put("meowsic/music5.mp3", Music.class);
                put("meowsic/fail.mp3", Sound.class);
            }
        };
    }

    @Override
    public MiniGame createMiniGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        return new Meowsic(screen, observer, difficulty);
    }
}
