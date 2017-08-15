package br.cefetmg.games;

import br.cefetmg.games.screens.SplashScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * Classe de inicialização do jogo.
 * Um game, na LibGDX, é um ApplicationListener que possui várias telas
 * e delega para a tela corrente a responsabilidade pelo ciclo de vida da
 * aplicação (create, dispose, render, resize, pause, resume).
 * 
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class MeowAuGame extends Game {


    @Override
    public void create() {
        this.setScreen(new SplashScreen(this, null));
    }
    
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
    
    @Override
    public void render() {
        handleInput();
        super.render();
    }

    @Override
    public void dispose() {
        if (this.getScreen() != null) {
            this.getScreen().dispose();
        }
    }
}
