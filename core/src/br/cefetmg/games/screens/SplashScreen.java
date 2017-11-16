package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.transition.TransitionScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * A tela de <em>splash</em> (inicial, com a logomarca) do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class SplashScreen extends BaseScreen {
    private final int MAX_FRAMES = 51;
    /**
     * Momento em que a tela foi mostrada (em milissegundos).
     */
    private long timeWhenScreenShowedUp;

    /**
     * Uma {@link Sprite} que contém a logo da empresa CEFET-GAMES.
     */
    
    private Sprite[] logo = new Sprite[MAX_FRAMES];
    Music backgroundMusic;
    
    /**
     * Cria uma nova tela de <em>splash</em>.
     *
     * @param game O jogo dono desta tela.
     * @param previous A tela de onde o usuário veio.
     */
    public SplashScreen(Game game, BaseScreen previous) {
        super(game, previous);
    }

    private int i;
    private int timer = 0;
    
    /**
     * Configura parâmetros iniciais da tela.
     */
    @Override
    public void appear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        timeWhenScreenShowedUp = TimeUtils.millis();
        for(int i = 0; i<MAX_FRAMES; i++) {
            logo[i] = new Sprite(new Texture("splash/frames/"+Integer.toString(i+1)+".png"));
            logo[i].getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            logo[i].setCenter(super.viewport.getWorldWidth()/2, super.viewport.getWorldHeight()/2);
        }
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("splash/splash.mp3"));
        backgroundMusic.play();
    }


    @Override
    protected void assetsLoaded() {
    }
   
    @Override
    public void cleanUp() {
        backgroundMusic.stop();
    }

    /**
     * Navega para a tela de Menu.
     */
    private void navigateToMenuScreen() {
        transitionScreen(new MenuScreen(game, this), 
                        TransitionScreen.Effect.FADE_IN_OUT, 0.5f);
        
        //game.setScreen(new MenuScreen(game, this));
    }

    /**
     * Verifica se houve <em>input</em> do jogador.
     */
    @Override
    public void handleInput() {
        // se o jogador apertar alguma tecla, clicar com o mouse ou 
        // tocar a tela, pula direto para a próxima tela.
        if (Gdx.input.justTouched()) {
            navigateToMenuScreen();
        }
    }

    /**
     * Atualiza a lógica da tela de acordo com o tempo.
     *
     * @param dt Tempo desde a última chamada.
     */
    @Override
    public void update(float dt) {
        // verifica se o tempo em que se passou na tela é maior do que o máximo
        // para que possamos navegar para a próxima tela.
        if (TimeUtils.timeSinceMillis(timeWhenScreenShowedUp)
                >= Config.TIME_ON_SPLASH_SCREEN) {
            navigateToMenuScreen();
        }
        if(i!=MAX_FRAMES-1 && timer%4==0) {
            i++;
        }
        timer++;
    }

    /**
     * Desenha a {@link Sprite} com a logomarca.
     */
    @Override
    public void draw() {
        batch.begin();
        logo[i].draw(batch);
        batch.end();
    }
}
