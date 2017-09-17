package br.cefetmg.games.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import br.cefetmg.games.MeowAuGame;
import br.cefetmg.games.Config;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config
                = new LwjglApplicationConfiguration();

        config.setFromDisplayMode(
                LwjglApplicationConfiguration.getDesktopDisplayMode());

        // defina "fullscreen" para true/false para testar em tela cheia ou 
        // modo janela
        boolean fullscreen = false;
        if (!fullscreen) {
            config.fullscreen = false;
            config.width /= 1.5f;
            config.height = (int) (config.width / Config.DESIRED_ASPECT_RATIO);
        }
        config.samples = 4;
        config.vSyncEnabled = true;
        config.title = "Meow Au";

        new LwjglApplication(new MeowAuGame(), config);
    }
}
