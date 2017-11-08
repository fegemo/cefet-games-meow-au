/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 *
 * @author acces
 */
public abstract class GameCustom extends Game {
    
    private static Screen loadedScreen = null;
    
    @Override
    public void setScreen (Screen screen) {
        if (loadedScreen == screen) {
            this.screen = screen;
            loadedScreen = null;
            return;
        }
        
        super.setScreen(screen);
    }
    
    public void setLoadedScreen(Screen screen) {
        loadedScreen = screen;
    }
}
