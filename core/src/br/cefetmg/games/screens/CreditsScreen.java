package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;
import br.cefetmg.games.sound.SoundManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

/**
 * Tela de créditos
 *
 * @author Natália Natsumy
 * @author André Brait <andrebrait@gmail.com>
 */
public class CreditsScreen extends BaseScreen {

    private static final float SCROLL_SPEED = 150.0f;
    private static final float TIME_TO_SCROLL = 1.5f;
    private static final float TIME_TO_BACK = 2.0f;
    private static final float TIME_FADE = 0.25f;

    private MySound backSound;
    private Button menuBtn;
    private Stage stage;
    private Stack stack;
    private ScrollPane scrollPane;
    private Label textLabel;
    private float timePassedOnShow;
    private float timePassedAfterEnd;
    private boolean inputDetected;
    private boolean reachedEnd;
    private boolean isGoingBack;

    CreditsScreen(Game game, BaseScreen previous) {
        super(game, previous);
    }

    @Override
    public void appear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        // init font
        TextureLoader.TextureParameter linearFilter = new TextureLoader.TextureParameter();
        linearFilter.minFilter = Texture.TextureFilter.Linear;
        linearFilter.magFilter = Texture.TextureFilter.Linear;
        assets.load("menu/menu-background.png", Texture.class, linearFilter);
        assets.load("world/menu.png", Texture.class, linearFilter);
        assets.load("menu/click1.mp3", Sound.class);

        FreeTypeFontLoaderParameter snackerComicParams = new FreeTypeFontLoaderParameter();
        snackerComicParams.fontFileName = "fonts/orangejuice.ttf";
        snackerComicParams.fontParameters.size = 65;
        snackerComicParams.fontParameters.minFilter = Texture.TextureFilter.Linear;
        snackerComicParams.fontParameters.magFilter = Texture.TextureFilter.Linear;
        assets.load("orangejuice.ttf", BitmapFont.class, snackerComicParams);

        stage = new Stage(viewport, batch);
        stack = new Stack();

        timePassedOnShow = 0.0f;
        timePassedAfterEnd = 0.0f;
        inputDetected = false;
        reachedEnd = false;
        isGoingBack = false;

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void cleanUp() {
        stage.dispose();
    }

    @Override
    public void handleInput() {
        // Como estamos usando scene2d, o input é gerenciado nele.
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
        if (!isGoingBack) {
            if (timePassedOnShow >= TIME_TO_SCROLL) {
                if (!inputDetected && !reachedEnd) {
                    scrollPane.setScrollY(scrollPane.getScrollY() + SCROLL_SPEED * dt);
                    reachedEnd = scrollPane.isBottomEdge();
                }
            } else {
                timePassedOnShow += dt;
            }
            if (!inputDetected && reachedEnd) {
                if (timePassedAfterEnd >= TIME_TO_BACK) {
                    goBackActionSequence();
                } else {
                    timePassedAfterEnd += dt;
                }
            }
        }
    }

    @Override
    public void draw() {
        stage.draw();
    }

    @Override
    protected void assetsLoaded() {
        BitmapFont font = assets.get("orangejuice.ttf");
        Image background = new Image(assets.get("menu/menu-background.png", Texture.class));
        Image menu = new Image(assets.get("world/menu.png", Texture.class));
        menuBtn = new Button(menu.getDrawable());
        backSound = new MySound(assets.get("menu/click1.mp3", Sound.class));
        menuBtn.setY(viewport.getWorldHeight() * 0.02f);
        menuBtn.setX(menuBtn.getY());

        stage.addActor(menuBtn);
        stage.addActor(stack);

        background.setFillParent(true);
        background.setScaling(Scaling.stretch);
        stack.setFillParent(true);
        stack.add(background);

        LabelStyle fontStyle = new LabelStyle(font, Color.DARK_GRAY);
        String file = Gdx.files.internal(Config.CREDITS_FILE).readString();
        textLabel = new Label(file, fontStyle);
        textLabel.setAlignment(Align.center, Align.center);
        scrollPane = new ScrollPane(textLabel);
        stack.add(scrollPane);

        scrollPane.setFillParent(true);
        scrollPane.setFadeScrollBars(true);

        scrollPane.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                inputDetected = true;
                enableButton();
                return true;
            }
        });
        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backSound.play();
                inputDetected = true;
                goBackActionSequence();
            }
        });

        menuBtn.setDisabled(true);
        menuBtn.addAction(Actions.alpha(0.0f));
        textLabel.addAction(Actions.sequence(Actions.alpha(0.0f), Actions.delay(0.1f), Actions.fadeIn(TIME_FADE)));

        MyMusic musicaTema = SoundManager.getInstance().playBackgroundMusic("menu/meowautheme.mp3");
        musicaTema.setLooping(true);
        musicaTema.setVolume(0.4f);
    }

    private void enableButton() {
        if (!isGoingBack && menuBtn.isDisabled()) {
            menuBtn.toFront();
            menuBtn.clearActions();
            menuBtn.addAction(Actions.sequence(Actions.fadeIn(TIME_FADE), Actions.run(new Runnable() {
                @Override
                public void run() {
                    menuBtn.setDisabled(false);
                }
            })));
        }
    }

    private void goBackActionSequence() {
        scrollPane.setScrollingDisabled(true, true);
        menuBtn.setDisabled(true);
        if (!isGoingBack) {
            isGoingBack = true;
            menuBtn.clearActions();
            textLabel.clearActions();
            menuBtn.addAction(Actions.sequence(Actions.alpha(1.0f), Actions.fadeOut(TIME_FADE)));
            textLabel.addAction(
                    Actions.sequence(Actions.alpha(1.0f), Actions.fadeOut(TIME_FADE), Actions.delay(0.05f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    textLabel.setVisible(false);
                                    goBack();
                                }
                            })));
        }
    }

    private void goBack() {
        game.setScreen(new MenuScreen(game, this));
    }

}
