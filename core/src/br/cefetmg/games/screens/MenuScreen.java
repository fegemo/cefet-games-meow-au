package br.cefetmg.games.screens;

import br.cefetmg.games.graphics.hud.SoundIcon;
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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

/**
 * Uma tela de Menu Principal do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class MenuScreen extends BaseScreen {

    private static final float SCREENS_FADE_TIME = 0.7f;
    private static final float BUTTONS_FADE_TIME = 0.25f;
    private static final float BUTTONS_FADE_DELAY = 0.1f;
    private static final float AMOUNT_MOVE_BUTTON = 30.0f;
    private static final float INITIAL_LOGO_SCALE = 0.9f;
    private static final float FINAL_LOGO_SCALE = 1.0f;
    private static final float BUTTONS_PADDING = 5.0f;
    private static final int BUTTON_COLSPAN = 3;

    private static enum Direction {
        LEFT, RIGHT;
    }

    private SoundIcon soundButton;

    private Stage stage;
    private Table table;

    private Stack playNormalSurvivalBtnStack;
    private Table normalSurvivalTable;
    private Table playTable;
    private Stack rankBackBtnStack;

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
        normalSurvivalTable = new Table();
        playTable = new Table();
        playNormalSurvivalBtnStack = new Stack();
        rankBackBtnStack = new Stack();
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
        soundButton.create(assets.get("hud/no-sound-button.png", Texture.class),
                assets.get("hud/sound-button.png", Texture.class));

        btnPlay.setDisabled(true);
        btnExit.setDisabled(true);
        btnRanking.setDisabled(true);
        btnCredits.setDisabled(true);
        btnNormal.setDisabled(true);
        btnSurvival.setDisabled(true);
        btnBack.setDisabled(true);
        
        btnPlay.getColor().a = 0.0f;
        btnExit.getColor().a = 0.0f;
        btnRanking.getColor().a = 0.0f;
        btnCredits.getColor().a = 0.0f;
        btnNormal.getColor().a = 0.0f;
        btnSurvival.getColor().a = 0.0f;
        btnBack.getColor().a = 0.0f;

        logo.setOrigin(Align.center);
        background.setFillParent(true);

        normalSurvivalTable.center();
        normalSurvivalTable.add(btnNormal).padLeft(BUTTONS_PADDING).padRight(BUTTONS_PADDING).center();
        normalSurvivalTable.add(btnSurvival).padLeft(BUTTONS_PADDING).padRight(BUTTONS_PADDING).center();
        
        playTable.center();
        playTable.add(btnPlay);
        
        playNormalSurvivalBtnStack.add(normalSurvivalTable);
        playNormalSurvivalBtnStack.add(playTable);
        rankBackBtnStack.add(btnBack);
        rankBackBtnStack.add(btnRanking);

        Skin skin = soundButton.getSkin();
        table.setFillParent(true);
        table.center();
        table.setSkin(skin);
        table.setBackground(background.getDrawable());
        table.add(logo).colspan(BUTTON_COLSPAN).pad(-40.0f);
        table.row();
        table.add(playNormalSurvivalBtnStack).pad(BUTTONS_PADDING).colspan(BUTTON_COLSPAN).center();
        table.row();
        table.add(rankBackBtnStack).pad(BUTTONS_PADDING).colspan(BUTTON_COLSPAN).center();
        table.row();
        table.add(btnCredits).pad(BUTTONS_PADDING).colspan(BUTTON_COLSPAN).center();
        table.row();
        table.add(btnExit).pad(BUTTONS_PADDING).colspan(BUTTON_COLSPAN).center();

        float delay = SCREENS_FADE_TIME / 2.0f;
        int pos = 0;
        fadeInButton(btnPlay, null, delay + (++pos * BUTTONS_FADE_DELAY));
        fadeInButton(btnRanking, null, delay + (++pos * BUTTONS_FADE_DELAY));
        fadeInButton(btnCredits, null, delay + (++pos * BUTTONS_FADE_DELAY));
        fadeInButton(btnExit, null, delay + (++pos * BUTTONS_FADE_DELAY));

        fadeInElements(delay);

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
        soundButton = new SoundIcon(stage);
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

    private void fadeInButton(Button button, int position, float additionalDelay) {
        fadeInButton(button, Direction.LEFT, position, additionalDelay);
    }

    private void fadeInButton(Button button, Direction direction, int position, float additionalDelay) {
        fadeInButton(button, direction, (position + 1) * BUTTONS_FADE_DELAY + additionalDelay);
    }

    private void fadeInButton(final Button button, Direction direction, float delay) {
        Action actionEnableButton = Actions.run(new Runnable() {
            @Override
            public void run() {
                button.setDisabled(false);
            }
        });
        button.clearActions();
        button.toFront();
        float moveAmount = getMoveAmount(direction);
        Action noAlphaAction = Actions.alpha(0.0f);
        Action delayAction = Actions.delay(delay);
        Action fadeInAction = Actions.fadeIn(BUTTONS_FADE_TIME);
        Action setPositionAction = Actions.moveBy(-moveAmount, 0.0f);
        Action moveAction = Actions.moveBy(moveAmount, 0.0f, BUTTONS_FADE_TIME);
        Action moveAndFade = Actions.parallel(fadeInAction, moveAction);
        button.addAction(
                Actions.sequence(noAlphaAction, setPositionAction, delayAction, moveAndFade, actionEnableButton));
    }

    private void fadeOutButton(Button button, int position) {
        fadeOutButton(button, position, null);
    }

    private void fadeOutButton(Button button, int position, Runnable runAfter) {
        fadeOutButton(button, Direction.LEFT, position, runAfter);
    }

    private void fadeOutButton(Button button, Direction direction, int position) {
        fadeOutButton(button, direction, position, null);
    }

    private void fadeOutButton(Button button, Direction direction, int position, Runnable runAfter) {
        fadeOutButton(button, direction, (position + 1) * BUTTONS_FADE_DELAY, runAfter);
    }

    private void fadeOutButton(Button button, Direction direction, float delay, final Runnable runAfter) {
        button.clearActions();
        button.setDisabled(true);
        button.toBack();
        float moveAmount = getMoveAmount(direction);
        Action fullAlphaAction = Actions.alpha(1.0f);
        Action delayAction = Actions.delay(delay);
        Action fadeOutAction = Actions.fadeOut(BUTTONS_FADE_TIME);
        Action setPositionAction = Actions.moveBy(-moveAmount, 0.0f);
        Action moveAction = Actions.moveBy(moveAmount, 0.0f, BUTTONS_FADE_TIME);
        Action moveAndFade = Actions.parallel(fadeOutAction, moveAction);
        if (runAfter != null) {
            button.addAction(Actions.sequence(fullAlphaAction, delayAction, moveAndFade, setPositionAction,
                    Actions.run(runAfter)));
        } else {
            button.addAction(Actions.sequence(fullAlphaAction, delayAction, moveAndFade, setPositionAction));
        }
    }

    private static float getMoveAmount(Direction direction) {
        return direction == Direction.LEFT ? -AMOUNT_MOVE_BUTTON
                : direction == Direction.RIGHT ? AMOUNT_MOVE_BUTTON : 0.0f;
    }

    private void setButtonListeners() {
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                enterClickSound.play();
                int posSecondary = 0;
                normalSurvivalTable.toFront();
                fadeInButton(btnNormal, posSecondary, BUTTONS_FADE_TIME);
                fadeInButton(btnSurvival, Direction.RIGHT, posSecondary++, BUTTONS_FADE_TIME);
                fadeInButton(btnBack, posSecondary++, BUTTONS_FADE_TIME);
                int posPrimary = 0;
                fadeOutButton(btnPlay, posPrimary++);
                fadeOutButton(btnRanking, posPrimary++);
                fadeOutButton(btnCredits, posPrimary++);
                fadeOutButton(btnExit, posPrimary++);
            }
        });
        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backClickSound.play();
                int pos = 0;
                fadeOutButton(btnPlay, null, pos++);
                fadeOutButton(btnRanking, null, pos++);
                fadeOutButton(btnCredits, null, pos++);
                fadeOutButton(btnExit, null, pos++, new Runnable() {
                    @Override
                    public void run() {
                        exitGame();
                    }
                });
                fadeOutElements(pos * BUTTONS_FADE_DELAY);
            }
        });
        btnRanking.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                enterClickSound.play();
                int pos = 0;
                fadeOutButton(btnPlay, null, pos++);
                fadeOutButton(btnRanking, null, pos++, new Runnable() {
                    @Override
                    public void run() {
                        navigateToRanking();
                    }
                });
                fadeOutButton(btnCredits, null, pos++);
                fadeOutButton(btnExit, null, pos++);
                fadeOutElements(pos * BUTTONS_FADE_DELAY);
            }
        });
        btnCredits.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                enterClickSound.play();
                int pos = 0;
                fadeOutButton(btnPlay, null, pos++);
                fadeOutButton(btnRanking, null, pos++);
                fadeOutButton(btnCredits, null, pos++, new Runnable() {
                    @Override
                    public void run() {
                        navigateToCredits();
                    }
                });
                fadeOutButton(btnExit, null, pos++);
                fadeOutElements(pos * BUTTONS_FADE_DELAY);
            }
        });
        btnNormal.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                enterClickSound.play();
                int pos = 0;
                fadeOutButton(btnNormal, null, pos, new Runnable() {
                    @Override
                    public void run() {
                        navigateToOverworld();
                    }
                });
                fadeOutButton(btnSurvival, null, pos++);
                fadeOutButton(btnBack, null, pos++);
                fadeOutElements(pos * BUTTONS_FADE_DELAY);
            }
        });
        btnSurvival.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                enterClickSound.play();
                int pos = 0;
                fadeOutButton(btnNormal, null, pos);
                fadeOutButton(btnSurvival, null, pos++, new Runnable() {
                    @Override
                    public void run() {
                        navigateToSurvivalGame();
                    }
                });
                fadeOutButton(btnBack, null, pos++);
                fadeOutElements(pos * BUTTONS_FADE_DELAY);
            }
        });
        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backClickSound.play();
                int posPrimary = 0;
                playTable.toFront();
                fadeInButton(btnPlay, Direction.RIGHT, posPrimary++, BUTTONS_FADE_TIME);
                fadeInButton(btnRanking, Direction.RIGHT, posPrimary++, BUTTONS_FADE_TIME);
                fadeInButton(btnCredits, Direction.RIGHT, posPrimary++, BUTTONS_FADE_TIME);
                fadeInButton(btnExit, Direction.RIGHT, posPrimary++, BUTTONS_FADE_TIME);
                int posSecondary = 0;
                fadeOutButton(btnNormal, Direction.RIGHT, posSecondary);
                fadeOutButton(btnSurvival, posSecondary++);
                fadeOutButton(btnBack, Direction.RIGHT, posSecondary++);
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
        transitionScreen(new OverworldScreen(super.game, this), TransitionScreen.Effect.FADE_IN_OUT, SCREENS_FADE_TIME);
    }

    private void navigateToSurvivalGame() {
        shouldContinueBackgroundMusic = false;
        transitionScreen(new PlayingGamesScreen(super.game, this), TransitionScreen.Effect.FADE_IN_OUT,
                SCREENS_FADE_TIME);
    }

    private void navigateToCredits() {
        shouldContinueBackgroundMusic = true;
        game.setScreen(new CreditsScreen(game, this));
    }

    private void navigateToRanking() {
        shouldContinueBackgroundMusic = true;
        transitionScreen(new RankingScreen(game, this), TransitionScreen.Effect.FADE_IN_OUT, SCREENS_FADE_TIME);
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

    private void fadeInElements(float delay) {
        logo.clearActions();
        soundButton.getButton().clearActions();
        logo.addAction(Actions.sequence(Actions.alpha(0.0f), Actions.scaleTo(INITIAL_LOGO_SCALE, INITIAL_LOGO_SCALE),
                Actions.delay(delay), Actions.parallel(Actions.fadeIn(BUTTONS_FADE_TIME),
                        Actions.scaleTo(FINAL_LOGO_SCALE, FINAL_LOGO_SCALE, BUTTONS_FADE_TIME))));
        soundButton.getButton().addAction(
                Actions.sequence(Actions.alpha(0.0f), Actions.delay(2.0f * delay), Actions.fadeIn(BUTTONS_FADE_TIME)));
    }

    private void fadeOutElements(float delay) {
        logo.clearActions();
        soundButton.getButton().clearActions();
        logo.addAction(Actions.sequence(Actions.alpha(1.0f), Actions.scaleTo(FINAL_LOGO_SCALE, FINAL_LOGO_SCALE),
                Actions.delay(delay), Actions.parallel(Actions.fadeOut(BUTTONS_FADE_TIME),
                        Actions.scaleTo(INITIAL_LOGO_SCALE, INITIAL_LOGO_SCALE, BUTTONS_FADE_TIME))));
        soundButton.getButton().addAction(
                Actions.sequence(Actions.alpha(1.0f), Actions.delay(delay), Actions.fadeOut(BUTTONS_FADE_TIME)));
    }

}
