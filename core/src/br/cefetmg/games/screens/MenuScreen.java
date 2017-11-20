package br.cefetmg.games.screens;

import br.cefetmg.games.graphics.hud.SoundIcon;
import br.cefetmg.games.transition.TransitionScreen;
import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Uma tela de Menu Principal do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class MenuScreen extends BaseScreen {

    public static final int BUTTONS_X = (int) (Gdx.graphics.getWidth() * 0.6);
    public static final int BUTTONS_WIDTH = 204;
    public static final int BUTTONS_HEIGHT = 54;
    public static final int PLAY_Y = 360;
    public static final int RANKING_Y = 276;
    public static final int CREDITS_Y = 192;
    public static final int EXIT_Y = 108;

    public static final int LOGO_X = 160;
    public static final int LOGO_Y = 360;
    public static final int LOGO_WIDTH = 960;
    public static final int LOGO_HEIGHT = 386;

    private SoundIcon soundIcon;

    private TextureRegion background;
    private Texture btnPlay;
    private Texture btnExit;
    private Texture btnRanking;
    private Texture btnCredits;
    private Texture btnNormal;
    private Texture btnSurvival;
    private Texture btnBack;
    private Texture logo;
    private MySound click1;
    private MySound click2;
    private int selecionaModo = 0;

    private MyMusic musicaTema;

    /**
     * Cria uma nova tela de menu.
     *
     * @param game o jogo dono desta tela.
     * @param previous a tela de onde o usuário veio.
     */
    public MenuScreen(Game game, BaseScreen previous) {
        super(game, previous);
    }

    /**
     * Configura parâmetros da tela e instancia objetos.
     */
    @Override
    public void appear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.input.setCursorCatched(false);

        //instancia música tema
        assets.load("menu/meowautheme.mp3", Music.class);

        // instancia a textura e a região de textura (usada para repetir)
        TextureLoader.TextureParameter linearFilter = new TextureLoader.TextureParameter();
        linearFilter.minFilter = Texture.TextureFilter.Linear;
        linearFilter.magFilter = Texture.TextureFilter.Linear;
        assets.load("menu/menu-background.png", Texture.class, linearFilter);
        assets.load("menu/button_jogar.png", Texture.class, linearFilter);
        assets.load("menu/button_sair.png", Texture.class, linearFilter);
        assets.load("menu/button_ranking.png", Texture.class, linearFilter);
        assets.load("menu/button_creditos.png", Texture.class, linearFilter);
        assets.load("menu/button_normal.png", Texture.class, linearFilter);
        assets.load("menu/button_survival.png", Texture.class, linearFilter);
        assets.load("menu/button_voltar.png", Texture.class, linearFilter);
        assets.load("menu/logo.png", Texture.class, linearFilter);

        assets.load("menu/click1.mp3", Sound.class);
        assets.load("menu/click2.mp3", Sound.class);

        assets.load("hud/no-sound-button.png", Texture.class, linearFilter);
        assets.load("hud/sound-button.png", Texture.class, linearFilter);
        soundIcon = new SoundIcon(new Stage(viewport, batch));
    }

    @Override
    protected void assetsLoaded() {
        background = new TextureRegion(assets.get("menu/menu-background.png", Texture.class));
        btnPlay = assets.get("menu/button_jogar.png", Texture.class);
        btnExit = assets.get("menu/button_sair.png", Texture.class);
        btnRanking = assets.get("menu/button_ranking.png", Texture.class);
        btnCredits = assets.get("menu/button_creditos.png", Texture.class);
        btnNormal = assets.get("menu/button_normal.png", Texture.class);
        btnSurvival = assets.get("menu/button_survival.png", Texture.class);
        btnBack = assets.get("menu/button_voltar.png", Texture.class);
        logo = assets.get("menu/logo.png", Texture.class);

        musicaTema = new MyMusic(assets.get("menu/meowautheme.mp3", Music.class));
        musicaTema.setLooping(true);
        musicaTema.setVolume(0.4f);
        musicaTema.play();

        click1 = new MySound(assets.get("menu/click1.mp3", Sound.class));
        click2 = new MySound(assets.get("menu/click2.mp3", Sound.class));
        soundIcon.create(
                    assets.get("hud/no-sound-button.png", Texture.class),
                    assets.get("hud/sound-button.png", Texture.class));
        Gdx.input.setInputProcessor(soundIcon.getInputProcessor());
    }

    /**
     * Recebe <em>input</em> do jogador.
     */
    @Override
    public void handleInput() {

        //verifica se clique foi em algum botão
        if (Gdx.input.justTouched()) {
            Vector3 clickPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(clickPosition);
            Rectangle playBounds = new Rectangle(BUTTONS_X, PLAY_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
            Rectangle rankingBounds = new Rectangle(BUTTONS_X, RANKING_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
            Rectangle creditsBounds = new Rectangle(BUTTONS_X, CREDITS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
            Rectangle exitBounds = new Rectangle(BUTTONS_X, EXIT_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);

            if (selecionaModo == 0) {
                if (playBounds.contains(clickPosition.x, clickPosition.y)) {
                    selecionaModo = 1;
                    click2.play();
                }
                if (rankingBounds.contains(clickPosition.x, clickPosition.y)) {
                    transitionScreen(new RankingScreen(super.game, this),
                            TransitionScreen.Effect.FADE_IN_OUT, 0.3f);

                    click2.play();
                }
                if (creditsBounds.contains(clickPosition.x, clickPosition.y)) {
                    /*
                    
                    CHAMADA DA TELA DE CRÉDITOS
                    
                     */
                    click2.play();
                }
                if (exitBounds.contains(clickPosition.x, clickPosition.y)) {
                    click1.play();
                    Gdx.app.exit();
                }
            } else {
                if (playBounds.contains(clickPosition.x, clickPosition.y)) {
                    click2.play();
                    navigateToMicroGameScreen(false);
                }
                if (rankingBounds.contains(clickPosition.x, clickPosition.y)) {
                    click2.play();
                    navigateToMicroGameScreen(true);
                }
                if (creditsBounds.contains(clickPosition.x, clickPosition.y)) {
                    //Volta para os botões
                    selecionaModo = 0;
                    click1.play();
                }
            }

        }
    }

    /**
     * Atualiza a lógica da tela.
     *
     * @param dt Tempo desde a última atualização.
     */
    @Override
    public void update(float dt) {
        soundIcon.update(dt);
    }

    /**
     * Desenha o conteúdo da tela de Menu.
     */
    @Override
    public void draw() {
        batch.begin();
        batch.draw(background, 0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());
        batch.draw(logo, LOGO_X, LOGO_Y,
                LOGO_WIDTH, LOGO_HEIGHT);

        if (selecionaModo == 0) {
            batch.draw(btnPlay, BUTTONS_X, PLAY_Y,
                    BUTTONS_WIDTH, BUTTONS_HEIGHT);
            batch.draw(btnRanking, BUTTONS_X, RANKING_Y,
                    BUTTONS_WIDTH, BUTTONS_HEIGHT);
            batch.draw(btnCredits, BUTTONS_X, CREDITS_Y,
                    BUTTONS_WIDTH, BUTTONS_HEIGHT);
            batch.draw(btnExit, BUTTONS_X, EXIT_Y,
                    BUTTONS_WIDTH, BUTTONS_HEIGHT);
        } else if (selecionaModo == 1) {
            batch.draw(btnNormal, BUTTONS_X, PLAY_Y,
                    BUTTONS_WIDTH, BUTTONS_HEIGHT);
            batch.draw(btnSurvival, BUTTONS_X, RANKING_Y,
                    BUTTONS_WIDTH, BUTTONS_HEIGHT);
            batch.draw(btnBack, BUTTONS_X, CREDITS_Y,
                    BUTTONS_WIDTH, BUTTONS_HEIGHT);
        }

        batch.end();

        soundIcon.draw();
    }

    /**
     * Navega para a tela de jogo.
     */
    private void navigateToMicroGameScreen(boolean isSurvival) {
        if (isSurvival) {
            transitionScreen(new PlayingGamesScreen(super.game, this),
                    TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
        } else {
            transitionScreen(new OverworldScreen(super.game, this),
                    TransitionScreen.Effect.FADE_IN_OUT, 0.4f);
        }
    }

    /**
     * Libera os recursos necessários para esta tela.
     */
    @Override
    public void cleanUp() {
        background.getTexture().dispose();
        musicaTema.stop();
    }

}
