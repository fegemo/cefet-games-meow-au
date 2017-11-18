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
 * @author Alberto
 */
public class SoundIcon {

    private static Button soundButton;
    private boolean inicializado;
    private Stage stage;
    private Skin skin;

    public SoundIcon() {
        this.inicializado = false;
    }

    public void create(Texture noSound, Texture sound, Stage stage) {

        skin = new Skin(Gdx.files.internal("hud/uiskin.json"));

        skin.add("noSound", noSound);
        skin.add("sound", sound);

        soundButton = new ImageButton(
                skin.getDrawable("sound"),
                skin.getDrawable("sound"),
                skin.getDrawable("noSound")
        );

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
        this.inicializado = true;
        soundButton.setY(stage.getViewport().getWorldHeight() * 0.15f);
        stage.addActor(soundButton);
        this.stage = stage;
    }

    public void create(Stage stage) {
        soundButton.setY(stage.getViewport().getWorldHeight() * 0.15f);
        stage.addActor(soundButton);
        this.stage = stage;
    }

    public static void showSoundButton() {
        soundButton.setVisible(true);
    }

    public static void hideSoundButton() {
        soundButton.setVisible(false);
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    public boolean isInicializado() {
        return inicializado;
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void draw() {
        stage.draw();
    }
}
