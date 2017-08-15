package br.cefetmg.games.graphics.hud;

import br.cefetmg.games.Config;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class Hud {

    private final BaseScreen screen;
    private final MiniGameStateObserver stateObserver;
    private final Stage stage;
    private Skin skin;
    private Table table;
    private Label centeredLabel;
    private Label sequenceIndexLabel;
    private HorizontalGroup livesGroup;

    private Countdown countdown;
    private Texture lifeTexture;
    private Texture clockTexture;
    private Image mask;
    private Button pauseButton;
    private Sound timerSound;
    private Clock clock;

    private int currentLives;
    private boolean isPaused;

    public Hud(BaseScreen screen, MiniGameStateObserver stateObserver) {
        this.screen = screen;
        this.stateObserver = stateObserver;
        stage = new Stage(screen.viewport, screen.batch);
    }

    public void create() {
        skin = new Skin(Gdx.files.internal("hud/uiskin.json"));
        skin.add("unpause", screen.assets.get("hud/unpause-button.png",
                Texture.class));
        skin.add("pause", screen.assets.get("hud/pause-button.png",
                Texture.class));
        lifeTexture = screen.assets.get("hud/lives.png");
        clockTexture = screen.assets.get("hud/clock.png");

        pauseButton = new ImageButton(
                skin.getDrawable("unpause"),
                skin.getDrawable("unpause"),
                skin.getDrawable("pause")
        );
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isPaused = !isPaused;
                mask.setVisible(isPaused);
                if (isPaused) {
                    stateObserver.onGamePaused();
                    clock.pauseTicking();
                } else {
                    stateObserver.onGameResumed();
                    clock.resumeTicking();
                }
                
            }

        });

        currentLives = Config.MAX_LIVES;
        
        mask = new Image(screen.assets.get("hud/gray-mask.png", Texture.class));
        mask.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        mask.setVisible(false);
        mask.setTouchable(Touchable.disabled);
        stage.addActor(mask);

        centeredLabel = new Label("", new LabelStyle(screen.assets.get("snacker-comic-50.ttf", BitmapFont.class), Color.BLACK));
        centeredLabel.setWrap(true);
        centeredLabel.setAlignment(Align.center);
        centeredLabel.setWidth(stage.getViewport().getWorldWidth());
        centeredLabel.setY(stage.getViewport().getWorldHeight() * 0.75f);
        stage.addActor(centeredLabel);
        
        countdown = new Countdown(screen.assets.get("hud/countdown.png", Texture.class));
        countdown.setAlign(Align.center);
        countdown.setOrigin(Align.center);
        countdown.setPosition(stage.getViewport().getWorldWidth()/2, stage.getViewport().getWorldHeight()/2, Align.center);
        stage.addActor(countdown);
        

        // faz a parte de baixo da HUD com um "layout de tabela":
        // https://github.com/libgdx/libgdx/wiki/Table
        table = new Table();
        table.bottom();
        table.setFillParent(true);

        sequenceIndexLabel = new Label(
                String.format("%03d", 1), new LabelStyle(
                        screen.assets.get("fonts/sawasdee-50.fnt", BitmapFont.class)
                        , Color.ORANGE));

        livesGroup = new HorizontalGroup();
        for (int i = 0; i < Config.MAX_LIVES; i++) {
            livesGroup.addActor(new LifeHeart(lifeTexture));
        }

        timerSound = screen.assets.get("hud/tick-tock.mp3", Sound.class);
        clock = new Clock(clockTexture, timerSound);

        table.padBottom(10).row().expandX();

        table.add(pauseButton).uniformX().left();
        table.add(clock).uniformX();
        table.add(livesGroup).uniformX();
        table.add().uniformX();
        table.add(sequenceIndexLabel).uniformX();
        
        
        // DESCOMENTE a linha abaixo para ver as bordas da tabela
        //table.debug();

        stage.addActor(table);

    }

    public void update(float dt) {
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
        sequenceIndexLabel.setText(String.format("%03d", index));
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

    public void pauseEndingTimer() {
        clock.pauseTicking();
    }

    public void resumeEndingTimer() {
        clock.resumeTicking();
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

}
