package br.cefetmg.games.graphics.hud;

import br.cefetmg.games.sound.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 *
 * @author Luiza
 */
public class SoundIcon {

    private Button soundButton;
    private final Stage stage;
    private Skin skin;
    private static final float MARGIN_LEFT = 8f;

    public SoundIcon(Stage stage) {
        this.stage = stage;
    }

    public void create(Texture noSound, Texture sound) {

        skin = new Skin(Gdx.files.internal("hud/uiskin.json"));

        skin.add("noSound", noSound);
        skin.add("sound", sound);

        soundButton = new ImageButton(
                skin.getDrawable("sound"),
                skin.getDrawable("sound"),
                skin.getDrawable("noSound")
        );
        soundButton.setChecked(!SoundManager.getInstance().isAudioEnabled());

        soundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                boolean sound = SoundManager.getInstance().isAudioEnabled();
                if (sound) {
                    SoundManager.getInstance().disableSounds();
                } else {
                    SoundManager.getInstance().enableSounds();
                }
            }
        });
        soundButton.setY(stage.getViewport().getWorldHeight() * 0.15f);
        soundButton.setX(MARGIN_LEFT);
        stage.addActor(soundButton);
    }

    public void show() {
        soundButton.setVisible(true);
    }

    public void hide() {
        soundButton.setVisible(false);
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
