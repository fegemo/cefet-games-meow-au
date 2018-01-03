package br.cefetmg.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import br.cefetmg.games.Config;
import br.cefetmg.games.transition.TransitionScreen;

/**
 * A tela de <em>splash</em> (inicial, com a logomarca) do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class SplashScreen extends BaseScreen {

    private static final int NUMBER_OF_VIDEO_FRAMES = 47;
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
    private Array<Sprite> videoSprites;
    private Music backgroundMusic;
    private int currentFrame;
    private float timeShowingCurrentFrame;
    private TextureAtlas videoFrames;

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
        // carrega os quadro do vídeo e o áudio
        assets.load("splash/video.atlas", TextureAtlas.class);
        assets.load("splash/splash.mp3", Music.class);
    }

    @Override
    protected void assetsLoaded() {
        videoFrames = assets.get("splash/video.atlas", TextureAtlas.class);
        videoSprites = videoFrames.createSprites();
        for (Sprite sprite : videoSprites) {
            sprite.setCenter(super.viewport.getWorldWidth() / 2, super.viewport.getWorldHeight() / 2);
        }
        backgroundMusic = assets.get("splash/splash.mp3", Music.class);
        backgroundMusic.play();
        timeWhenScreenShowedUp = TimeUtils.millis();
    }

    @Override
    public void cleanUp() {
        backgroundMusic.dispose();
        videoSprites.clear();
        videoFrames.dispose();
    }

    /**
     * Navega para a tela de Menu.
     */
    private void navigateToMenuScreen() {
        backgroundMusic.stop();
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
        if (currentFrame < videoSprites.size - 1
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
        videoSprites.get(currentFrame).draw(batch);
        batch.end();
    }
}
