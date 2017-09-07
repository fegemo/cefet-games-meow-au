/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.Animals.Cat;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Pedro
 */
public class ClickFindCat extends MiniGame {
    
    private Texture CatTexture;
    private Cat gato;
    private Sound MeawSound;
    

    public ClickFindCat(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        CatTexture = assets.get("DogBarksCatFlee/kitten1-alt_3.png", Texture.class);
        MeawSound = assets.get("DogBarksCatFlee/cat-meow.wav", Sound.class);
        Vector2 PosicaoInicial = new Vector2 (Math.random() * screen)
        gato = new Cat(Vector2, CatTexture)
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onHandlePlayingInput() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onUpdate(float dt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDrawGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getInstructions() {
        return "Ache o gato invisivel";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
    
}
