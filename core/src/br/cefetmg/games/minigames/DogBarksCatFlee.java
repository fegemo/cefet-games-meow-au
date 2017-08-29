/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.Animals.Cat;
import br.cefetmg.games.Animals.Dog;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Pedro
 */
public class DogBarksCatFlee extends MiniGame {
    
    private Dog player;
    private Texture DogTexture;
    private Array<Cat> enemies;
    private Vector2 PosicaoInicial;
    
    public DogBarksCatFlee(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);

    }

    @Override
    protected void onStart() {
        PosicaoInicial.x = 0;
        PosicaoInicial.y = 0;
        DogTexture = assets.get("DogBarksCatFlee/dog1", Texture.class);
        player = new Dog (3, PosicaoInicial, DogTexture);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean shouldHideMousePointer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
