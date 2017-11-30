package br.cefetmg.games.graphics.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import br.cefetmg.games.sound.SoundManager;

/**
 * Classe que representa o botão de ligar/desligar Som
 * 
 * Esta classe foi criada para desacoplar a criação do botão da classe SoundIcon
 * enquanto mantém uma API semelhante.
 * 
 * @author andre
 *
 */
public class SoundButton {

    private final Button soundButton;
    private final Skin skin;

    public SoundButton(Texture noSound, Texture sound) {
        skin = new Skin(Gdx.files.internal("hud/uiskin.json"));

        skin.add("noSound", noSound);
        skin.add("sound", sound);

        soundButton = new ImageButton(skin.getDrawable("sound"), skin.getDrawable("sound"),
                skin.getDrawable("noSound"));
        soundButton.setChecked(!SoundManager.getInstance().isAudioEnabled());

        soundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean sound = SoundManager.getInstance().isAudioEnabled();
                if (sound) {
                    SoundManager.getInstance().disableSounds();
                } else {
                    SoundManager.getInstance().enableSounds();
                }
            }
        });
    }

    public Button getButton() {
        return this.soundButton;
    }

    public Skin getSkin() {
        return this.skin;
    }
    
    public void show() {
        soundButton.setVisible(true);
    }

    public void hide() {
        soundButton.setVisible(false);
    }


}
