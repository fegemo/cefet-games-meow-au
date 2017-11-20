package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.transition.TransitionScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * A tela de <em>splash</em> (inicial, com a logomarca) do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class SplashScreen extends BaseScreen {

    private final int NUMBER_OF_VIDEO_FRAMES = 47;
    private final float VIDEO_DURATION_IN_SECONDS = 2.9f;
    private final float TIME_ON_EACH_FRAME_IN_SECONDS
            = VIDEO_DURATION_IN_SECONDS / NUMBER_OF_VIDEO_FRAMES;
    /**
     * Momento em que a tela foi mostrada (em milissegundos).
     */
    private long timeWhenScreenShowedUp;

    /**
     * Uma {@link Sprite} que contém a logo da empresa CEFET-GAMES.
     */
    private final Sprite[] logo = new Sprite[NUMBER_OF_VIDEO_FRAMES];
    private Music backgroundMusic;
    private int currentFrame;
    private float timeShowingCurrentFrame;

    /**
     * Cria uma nova tela de <em>splash</em>.
     *
     * @param game O jogo dono desta tela.
     * @param previous A tela de onde o usuário veio.
     */
    public SplashScreen(Game game, BaseScreen previous) {
        super(game, previous);
    }

    /**
     * Configura parâmetros iniciais da tela.
     */
    @Override
    public void appear() {
        // usa uma cor de fundo igual à cor do fundo do vídeo
        Gdx.gl.glClearColor(0.914f, 0.914f, 0.914f, 1);
        TextureLoader.TextureParameter linearFilter = new TextureLoader.TextureParameter();
        linearFilter.magFilter = TextureFilter.Linear;
        linearFilter.minFilter = TextureFilter.Linear;
        // carrega cada quadro do vídeo separadamente, como uma textura
        for (int i = 0; i < NUMBER_OF_VIDEO_FRAMES; i++) {
            assets.load("splash/frames/" + Integer.toString(i + 1) + ".png", Texture.class, linearFilter);
        }
        assets.load("splash/splash.mp3", Music.class);
    }

    @Override
    protected void assetsLoaded() {
        for (int i = 0; i < NUMBER_OF_VIDEO_FRAMES; i++) {
            logo[i] = new Sprite(assets.get("splash/frames/" + Integer.toString(i + 1) + ".png", Texture.class));
            logo[i].setCenter(super.viewport.getWorldWidth() / 2, super.viewport.getWorldHeight() / 2);
        }
        backgroundMusic = assets.get("splash/splash.mp3", Music.class);
        backgroundMusic.play();
        timeWhenScreenShowedUp = TimeUtils.millis();
    }

    @Override
    public void cleanUp() {
        backgroundMusic.stop();
        for (int i = 0; i < NUMBER_OF_VIDEO_FRAMES; i++) {
            logo[i].getTexture().dispose();
        }
    }

    /**
     * Navega para a tela de Menu.
     */
    private void navigateToMenuScreen() {
        transitionScreen(new MenuScreen(game, this),
                TransitionScreen.Effect.FADE_IN_OUT, 0.4f);
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
        timeShowingCurrentFrame += dt;
        if (currentFrame < NUMBER_OF_VIDEO_FRAMES - 1
                && timeShowingCurrentFrame > TIME_ON_EACH_FRAME_IN_SECONDS) {
            currentFrame++;
            timeShowingCurrentFrame -= TIME_ON_EACH_FRAME_IN_SECONDS;
        }
    }

    /**
     * Desenha a {@link Sprite} com a logomarca.
     */
    @Override
    public void draw() {
        batch.begin();
        logo[currentFrame].draw(batch);
        batch.end();
    }
}
