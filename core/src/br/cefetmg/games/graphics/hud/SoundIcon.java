package br.cefetmg.games.graphics.hud;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 *
 * @author Luiza
 */
public class SoundIcon {

    private static final float MARGIN_LEFT = 8f;

    private final Stage stage;
    private SoundButton soundButton;

    public SoundIcon(Stage stage) {
        this.stage = stage;
    }

    public void create(Texture noSound, Texture sound) {
        soundButton = new SoundButton(noSound, sound);
        soundButton.getButton().setY(stage.getViewport().getWorldHeight() * 0.15f);
        soundButton.getButton().setX(MARGIN_LEFT);
        stage.addActor(soundButton.getButton());
    }

    public void show() {
        soundButton.show();
    }

    public void hide() {
        soundButton.hide();
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void draw() {
        stage.draw();
    }
}
