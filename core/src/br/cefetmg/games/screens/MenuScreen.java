package br.cefetmg.games.screens;

import br.cefetmg.games.graphics.hud.SoundButton;
import br.cefetmg.games.transition.TransitionScreen;
import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;
import br.cefetmg.games.sound.SoundManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Uma tela de Menu Principal do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class MenuScreen extends BaseScreen {

    private static final float FADE_TIME = 0.7f;
    private static final float BUTTONS_PADDING = 5.0f;
    private static final int BUTTON_COLSPAN = 3;

    private SoundButton soundButton;

    private Stage stage;
    private Table table;

    private Stack playNormalBtnStack;
    private Stack rankSurvivalBtnStack;
    private Stack creditsBackBtnStack;

    private Image background;
    private Button btnPlay;
    private Button btnExit;
    private Button btnRanking;
    private Button btnCredits;
    private Button btnNormal;
    private Button btnSurvival;
    private Button btnBack;
    private Image logo;

    private TextureRegion backgroundTexture;
    private Texture btnPlayTexture;
    private Texture btnExitTexture;
    private Texture btnRankingTexture;
    private Texture btnCreditsTexture;
    private Texture btnNormalTexture;
    private Texture btnSurvivalTexture;
    private Texture btnBackTexture;
    private Texture logoTexture;
    private MySound backClickSound;
    private MySound enterClickSound;

    private boolean shouldContinueBackgroundMusic;

    /**
     * Cria uma nova tela de menu.
     *
     * @param game
     *            o jogo dono desta tela.
     * @param previous
     *            a tela de onde o usuário veio.
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

        // instancia música tema
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

        stage = new Stage(viewport, batch);
        table = new Table();
        playNormalBtnStack = new Stack();
        rankSurvivalBtnStack = new Stack();
        creditsBackBtnStack = new Stack();
    }

    @Override
    protected void assetsLoaded() {
        createTexturesAndSounds();
        createWidgets();
        setButtonListeners();
        assembleScreen();
    }

    private void assembleScreen() {
        stage.addActor(table);

        background.setFillParent(true);
        table.setFillParent(true);

        btnNormal.setVisible(false);
        playNormalBtnStack.add(btnNormal);
        playNormalBtnStack.add(btnPlay);
        btnSurvival.setVisible(false);
        rankSurvivalBtnStack.add(btnSurvival);
        rankSurvivalBtnStack.add(btnRanking);
        btnBack.setVisible(false);
        creditsBackBtnStack.add(btnBack);
        creditsBackBtnStack.add(btnCredits);

        Skin skin = soundButton.getSkin();
        table.center();
        table.setSkin(skin);
        table.setBackground(background.getDrawable());
        table.add(logo).colspan(BUTTON_COLSPAN);
        table.row();
        table.add(playNormalBtnStack).pad(BUTTONS_PADDING).colspan(BUTTON_COLSPAN).right();
        table.row();
        table.add(soundButton.getButton()).pad(BUTTONS_PADDING).left();
        table.add(rankSurvivalBtnStack).pad(BUTTONS_PADDING).colspan(2).right();
        table.row();
        table.add(creditsBackBtnStack).pad(BUTTONS_PADDING).colspan(BUTTON_COLSPAN).right();
        table.row();
        table.add(btnExit).pad(BUTTONS_PADDING).colspan(BUTTON_COLSPAN).right();

        Gdx.input.setInputProcessor(stage);
    }

    private void createTexturesAndSounds() {
        backgroundTexture = new TextureRegion(assets.get("menu/menu-background.png", Texture.class));
        btnPlayTexture = assets.get("menu/button_jogar.png", Texture.class);
        btnExitTexture = assets.get("menu/button_sair.png", Texture.class);
        btnRankingTexture = assets.get("menu/button_ranking.png", Texture.class);
        btnCreditsTexture = assets.get("menu/button_creditos.png", Texture.class);
        btnNormalTexture = assets.get("menu/button_normal.png", Texture.class);
        btnSurvivalTexture = assets.get("menu/button_survival.png", Texture.class);
        btnBackTexture = assets.get("menu/button_voltar.png", Texture.class);
        logoTexture = assets.get("menu/logo.png", Texture.class);

        MyMusic musicaTema = SoundManager.getInstance().playBackgroundMusic("menu/meowautheme.mp3");
        musicaTema.setLooping(true);
        musicaTema.setVolume(0.4f);

        backClickSound = new MySound(assets.get("menu/click1.mp3", Sound.class));
        enterClickSound = new MySound(assets.get("menu/click2.mp3", Sound.class));
    }

    private void createWidgets() {
        soundButton = new SoundButton(assets.get("hud/no-sound-button.png", Texture.class),
                assets.get("hud/sound-button.png", Texture.class));
        background = new Image(backgroundTexture);
        btnPlay = new Button(new Image(btnPlayTexture).getDrawable());
        btnExit = new Button(new Image(btnExitTexture).getDrawable());
        btnRanking = new Button(new Image(btnRankingTexture).getDrawable());
        btnCredits = new Button(new Image(btnCreditsTexture).getDrawable());
        btnNormal = new Button(new Image(btnNormalTexture).getDrawable());
        btnSurvival = new Button(new Image(btnSurvivalTexture).getDrawable());
        btnBack = new Button(new Image(btnBackTexture).getDrawable());
        logo = new Image(logoTexture);
    }

    private void setButtonListeners() {
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                enterClickSound.play();
                btnNormal.toFront();
                btnSurvival.toFront();
                btnBack.toFront();
                btnNormal.setVisible(true);
                btnSurvival.setVisible(true);
                btnBack.setVisible(true);
                btnPlay.setVisible(false);
                btnRanking.setVisible(false);
                btnCredits.setVisible(false);
                btnExit.setVisible(false);
            }
        });
        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backClickSound.play();
                exitGame();
            }
        });
        btnRanking.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                enterClickSound.play();
                navigateToRanking();
            }
        });
        btnCredits.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                enterClickSound.play();
                navigateToCredits();
            }
        });
        btnNormal.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                enterClickSound.play();
                navigateToOverworld();
            }
        });
        btnSurvival.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                enterClickSound.play();
                navigateToSurvivalGame();
            }
        });
        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backClickSound.play();
                btnNormal.toBack();
                btnSurvival.toBack();
                btnBack.toBack();
                btnNormal.setVisible(false);
                btnSurvival.setVisible(false);
                btnBack.setVisible(false);
                btnPlay.setVisible(true);
                btnRanking.setVisible(true);
                btnCredits.setVisible(true);
                btnExit.setVisible(true);
            }
        });
    }

    /**
     * Recebe <em>input</em> do jogador.
     */
    @Override
    public void handleInput() {
        // Como estamos usando scene2d, o input é gerenciado nele.
    }

    /**
     * Atualiza a lógica da tela.
     *
     * @param dt
     *            Tempo desde a última atualização.
     */
    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    /**
     * Desenha o conteúdo da tela de Menu.
     */
    @Override
    public void draw() {
        stage.draw();
    }

    private void navigateToOverworld() {
        shouldContinueBackgroundMusic = false;
        transitionScreen(new OverworldScreen(super.game, this), TransitionScreen.Effect.FADE_IN_OUT, FADE_TIME);
    }

    private void navigateToSurvivalGame() {
        shouldContinueBackgroundMusic = false;
        transitionScreen(new PlayingGamesScreen(super.game, this), TransitionScreen.Effect.FADE_IN_OUT, FADE_TIME);
    }

    private void navigateToCredits() {
        shouldContinueBackgroundMusic = true;
        game.setScreen(new CreditsScreen(game, this));
    }

    private void navigateToRanking() {
        shouldContinueBackgroundMusic = true;
        transitionScreen(new RankingScreen(game, this), TransitionScreen.Effect.FADE_IN_OUT, FADE_TIME);
    }

    private void exitGame() {
        Gdx.app.exit();
    }

    /**
     * Libera os recursos necessários para esta tela.
     */
    @Override
    public void cleanUp() {
        backgroundTexture.getTexture().dispose();
        if (!shouldContinueBackgroundMusic) {
            SoundManager.getInstance().stopBackgroundMusic("menu/meowautheme.mp3");
        }
    }

}
