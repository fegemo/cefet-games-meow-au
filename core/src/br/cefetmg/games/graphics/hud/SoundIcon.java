/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.graphics.hud;

import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.sound.SoundManeger;
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
    private Button soundButton;
    
    private BaseScreen screen;
    private Stage stage;
    private Skin skin;
    
    public SoundIcon(BaseScreen screen) {
        this.screen = screen;
        stage = new Stage(screen.viewport, screen.batch);
    }
    
    public void upgrade(BaseScreen screen) {
        this.screen = screen;
        stage = new Stage(screen.viewport, screen.batch);
    }
    
    public void create() {
        skin = new Skin(Gdx.files.internal("hud/uiskin.json"));
        
        skin.add("noSound", screen.assets.get("hud/no-sound-button.png",
                Texture.class));
        skin.add("sound", screen.assets.get("hud/sound-button.png",
                Texture.class));
   
        
        soundButton = new ImageButton(
                skin.getDrawable("sound"),
                skin.getDrawable("sound"),
                skin.getDrawable("noSound")
        );
        soundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                boolean sound = SoundManeger.getSound();
                if( sound){
                    SoundManeger.disableSounds();
                }else{
                    SoundManeger.enableSounds();
                }
            }
        });
        soundButton.setY(stage.getViewport().getWorldHeight() * 0.15f);
        stage.addActor(soundButton);
        
    }
    
    public void showSoundButton() {
        soundButton.setVisible(true);
    }

    public void hideSoundButton() {
        soundButton.setVisible(false);
    }
    
    public void update(float dt) {
        stage.act(dt);
    }

    public void draw() {
        stage.draw();
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }


}
