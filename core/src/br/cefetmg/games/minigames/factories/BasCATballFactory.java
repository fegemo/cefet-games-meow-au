package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.BasCATball;
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
public class BasCATballFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new BasCATball(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class<?>> getAssetsToPreload() {
        return new HashMap<String, Class<?>>() {

        	private static final long serialVersionUID = 2905843381463040656L;

			{
                put("bascatball/ball.png", Texture.class);
                put("bascatball/player.png", Texture.class);
                put("bascatball/bar.png", Texture.class);
                put("bascatball/target.png", Texture.class);
                put("bascatball/selector.png", Texture.class);
                put("bascatball/court.png", Texture.class);
                put("bascatball/dorami.png", Texture.class);
                put("bascatball/pointer.png", Texture.class);
                put("bascatball/darkarrowL.png", Texture.class);
                put("bascatball/darkarrowR.png", Texture.class);
                put("bascatball/lightarrowL.png", Texture.class);
                put("bascatball/lightarrowR.png", Texture.class);
                put("bascatball/doraemon.png", Texture.class);
                put("bascatball/beats.mp3", Sound.class);
                put("bascatball/fail.mp3", Sound.class);
                put("bascatball/flyingdown.mp3", Sound.class);
                put("bascatball/doh.mp3", Sound.class);

            }
        };
    }

}
