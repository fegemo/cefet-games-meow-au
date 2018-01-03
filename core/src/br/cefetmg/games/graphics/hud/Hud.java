package br.cefetmg.games.graphics.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.Locale;

import br.cefetmg.games.Config;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.sound.MySound;

/**
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class Hud {

    private final BaseScreen screen;
    private final MiniGameStateObserver stateObserver;
    private final Stage stage;
    private SoundIcon soundIcon;
    private Label centeredLabel;
    private Label sequenceIndexLabel;
    private HorizontalGroup livesGroup;

    private Countdown countdown;
    private Image mask;
    private Button pauseButton;
    private Button backMenuButton;
    private Button backGameButton;
    private Button confirmButton;
    private Button unnconfirmedButton;
    private Clock clock;

    private int currentLives;
    private boolean isPaused;

    private MySound clickConfirmSound;
    private MySound clickCancelSound;

    public Hud(BaseScreen screen, MiniGameStateObserver stateObserver) {
        this.screen = screen;
        this.stateObserver = stateObserver;
        stage = new Stage(screen.viewport, screen.batch);
    }

    public void create() {
        clickConfirmSound = new MySound(screen.assets.get("menu/click1.mp3", Sound.class));
        clickCancelSound = new MySound(screen.assets.get("menu/click2.mp3", Sound.class));

        Skin skin = new Skin(Gdx.files.internal("hud/uiskin.json"));
        skin.add("unpause", screen.assets.get("hud/unpause-button.png",
                Texture.class));
        skin.add("pause", screen.assets.get("hud/pause-button.png",
                Texture.class));

        skin.add("confirm", screen.assets.get("hud/confirm-button.png",
                Texture.class));
        skin.add("unnconfirmed", screen.assets.get("hud/unnconfirmed-button.png",
                Texture.class));
        skin.add("back-menu", screen.assets.get("hud/back-menu-button.png",
                Texture.class));
        skin.add("back-game", screen.assets.get("hud/back-game-button.png",
                Texture.class));

        Texture lifeTexture = screen.assets.get("hud/lifeTexture.png");
        Texture explodeLifeTexture = screen.assets.get("hud/explodeLifeTexture.png");

        Texture clockTexture = screen.assets.get("hud/clock.png");


        countdown = new Countdown(screen.assets.get("hud/countdown.png", Texture.class));
        countdown.setAlign(Align.center);
        countdown.setOrigin(Align.center);
        countdown.setPosition(stage.getViewport().getWorldWidth() / 2, stage.getViewport().getWorldHeight() / 2, Align.center);
        stage.addActor(countdown);


        mask = new Image(screen.assets.get("hud/gray-mask.png", Texture.class));
        mask.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        mask.setVisible(false);
        mask.setTouchable(Touchable.disabled);
        stage.addActor(mask);

        centeredLabel = new Label("", new LabelStyle(screen.assets.get("snaphand-v1-free.ttf", BitmapFont.class), Color.WHITE));
        centeredLabel.setWrap(true);
        centeredLabel.setAlignment(Align.center);
        centeredLabel.setWidth(stage.getViewport().getWorldWidth());
        centeredLabel.setY(stage.getViewport().getWorldHeight() * 0.75f);
        stage.addActor(centeredLabel);


        soundIcon = new SoundIcon(stage);
        soundIcon.create(
                screen.assets.get("hud/no-sound-button.png", Texture.class),
                screen.assets.get("hud/sound-button.png", Texture.class));
        soundIcon.getButton().setY(screen.viewport.getWorldHeight() * 0.20f);
        soundIcon.getButton().setX(screen.viewport.getWorldHeight() * 0.06f);

        pauseButton = new ImageButton(
                skin.getDrawable("unpause"),
                skin.getDrawable("unpause"),
                skin.getDrawable("pause")
        );
        pauseButton.setProgrammaticChangeEvents(false);

        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleHudPauseState();
                clickConfirmSound.play();
            }
        });

        backGameButton = new ImageButton(
                skin.getDrawable("back-game")
        );
        backGameButton.setVisible(false);
        backGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                triggerResumeActions();
                clickCancelSound.play();
            }
        });

        backMenuButton = new ImageButton(
                skin.getDrawable("back-menu")
        );
        backMenuButton.setVisible(false);
        backMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backMenuButton.setVisible(false);
                backGameButton.setVisible(false);
                hidePauseButton();
                showMessage("Ao voltar para o menu inicial seu progresso sera perdido\nDeseja continuar?");
                confirmButton.setVisible(true);
                unnconfirmedButton.setVisible(true);
                clickConfirmSound.play();
            }
        });

        confirmButton = new ImageButton(
                skin.getDrawable("confirm")
        );
        confirmButton.setVisible(false);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stateObserver.onStateChanged(MiniGameState.GOING_BACK_TO_MENU);
                clickConfirmSound.play();
            }
        });
        unnconfirmedButton = new ImageButton(
                skin.getDrawable("unnconfirmed")
        );
        unnconfirmedButton.setVisible(false);
        unnconfirmedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideMessage();
                unnconfirmedButton.setVisible(false);
                confirmButton.setVisible(false);
                backMenuButton.setVisible(true);
                backGameButton.setVisible(true);
                showPauseButton();
                clickCancelSound.play();
            }
        });
        backMenuButton.setX(stage.getViewport().getWorldWidth() * 0.50f - backMenuButton.getWidth() / 2);
        backMenuButton.setY(stage.getViewport().getWorldHeight() * 0.55f);
        backGameButton.setX(stage.getViewport().getWorldWidth() * 0.50f - backMenuButton.getWidth() / 2);
        backGameButton.setY(stage.getViewport().getWorldHeight() * 0.35f);
        confirmButton.setY(stage.getViewport().getWorldHeight() * 0.50f);
        unnconfirmedButton.setY(stage.getViewport().getWorldHeight() * 0.50f);
        confirmButton.setX(stage.getViewport().getWorldWidth() * 0.75f);
        unnconfirmedButton.setX(stage.getViewport().getWorldWidth() * 0.25f);
        stage.addActor(backMenuButton);
        stage.addActor(backGameButton);
        stage.addActor(confirmButton);
        stage.addActor(unnconfirmedButton);

        currentLives = Config.MAX_LIVES;

        // faz a parte de baixo da HUD com um "layout de tabela":
        // https://github.com/libgdx/libgdx/wiki/Table
        Table table = new Table();
        table.bottom();
        table.setFillParent(true);

        sequenceIndexLabel = new Label(
                String.format(Locale.getDefault(), "%d", 1), new LabelStyle(
                screen.assets.get("snaphand-v1-free.ttf", BitmapFont.class), Color.WHITE));

        livesGroup = new HorizontalGroup();
        for (int i = 0; i < Config.MAX_LIVES; i++) {
            livesGroup.addActor(new LifeHeart(lifeTexture, explodeLifeTexture));
        }

        MySound timerSound = new MySound(screen.assets.get("hud/tick-tock.mp3", Sound.class));
        clock = new Clock(clockTexture, timerSound);

        table.padBottom(10).row().expandX();

        table.add(pauseButton).uniformX().left().padLeft(screen.viewport.getWorldHeight() * 0.055f);
        table.add(clock).uniformX();
        table.add(livesGroup).uniformX();
        table.add().uniformX();
        table.add(sequenceIndexLabel).uniformX();

        // DESCOMENTE a linha abaixo para ver as bordas da tabela
        //table.debug();
        stage.addActor(table);
    }

    /**
     * Dispara as ações envolvidas em pausar a tela atual.
     */
    public void triggerPauseActions() {
        if (!isPaused) {
            toggleHudPauseState();
        }
    }

    /**
     * Dispara as ações envolvidas em resumir a tela atual.
     */
    private void triggerResumeActions() {
        if (isPaused) {
            toggleHudPauseState();
        }
    }

    private void toggleHudPauseState() {
        isPaused = !isPaused;
        mask.setVisible(isPaused);
        backMenuButton.setVisible(isPaused);
        backGameButton.setVisible(isPaused);
        pauseButton.setChecked(isPaused);
        if (isPaused) {
            stateObserver.onGamePaused();
            clock.pauseTicking();
            soundIcon.show();
        } else {
            stateObserver.onGameResumed();
            clock.resumeTicking();
            soundIcon.hide();
        }
    }

    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            toggleHudPauseState();
        }
        if (!isPaused) {
            stage.act(dt);
        }
    }

    public void draw() {
        stage.draw();
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    public void setGameIndex(int index) {
        sequenceIndexLabel.setText(String.format(Locale.getDefault(), "%d", index));
    }

    public void setLives(int lives) {
        if (lives < 0 || lives > Config.MAX_LIVES) {
            throw new IllegalArgumentException("A HUD está tentando mostrar "
                    + "um número de vidas menor que 0 ou maior que "
                    + Config.MAX_LIVES + ".");
        }
        // contabiliza os dentes de acordo com as vidas (currentLives) e 
        // executa a animação de perda de uma vida
        if (lives < currentLives && currentLives != 0) {
            LifeHeart heart = ((LifeHeart) livesGroup.getChildren()
                    .get(currentLives - 1));
            heart.die();
            currentLives--;
        }

    }

    public void startEndingTimer() {
        clock.startTicking();
    }

    public void cancelEndingTimer() {
        clock.stopTicking();
    }

    public void showGameInstructions(String instructions) {
        centeredLabel.setText(instructions);
    }

    public void hideGameInstructions() {
        centeredLabel.setText("");
    }

    public void startInitialCountdown() {
        countdown.start();
    }

    public void showPauseButton() {
        pauseButton.setVisible(true);
    }

    public void hidePauseButton() {
        pauseButton.setVisible(false);
    }

    public void showMessage(String message) {
        centeredLabel.setText(message);
    }

    private void hideMessage() {
        centeredLabel.setText("");
    }

    public void hideSoundsButton() {
        soundIcon.hide();
    }

}