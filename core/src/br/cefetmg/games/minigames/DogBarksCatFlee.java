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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;

/**
 *
 * @author Pedro
 */
public class DogBarksCatFlee extends MiniGame {
    private final int TILES_COUNT = 5;
    private Dog player;
    private Texture DogTexture;
    private Texture CatTexture;
    private Array<Cat> enemies;
    private Array<Vector2> tilesVector;
    private Vector2 PosicaoInicial;
    private Texture tileTexture[] = new Texture[5];
    private float spawnInterval;
    
    public DogBarksCatFlee(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }
    private void TilesDraw(){
        for (Vector2 tile : tilesVector) {
            batch.draw(tileTexture[MathUtils.random(4)], tile.x, tile.y);
        }
    }
    private void UpdateDraw(){
        for (Vector2 tile : tilesVector) {
            tile.x += -1; 
        }
    }
    @Override
    protected void onStart() {
        PosicaoInicial.x = 0;
        PosicaoInicial.y = 0;
        DogTexture = assets.get("DogBarksCatFlee/dog1.png", Texture.class);
        player = new Dog (3, PosicaoInicial, DogTexture);
        CatTexture = assets.get("DogBarksCatFlee/Kitten1.png",Texture.class);
        
        //DogAnimation = assets.get(null);
        //<editor-fold defaultstate="collapsed" desc="texturas tile">
        tileTexture[0] = assets.get("DogBarksCatFlee/tile0.png", Texture.class);
        tileTexture[1] = assets.get("DogBarksCatFlee/tile1.png", Texture.class);
        tileTexture[2] = assets.get("DogBarksCatFlee/tile2.png", Texture.class);
        tileTexture[3] = assets.get("DogBarksCatFlee/tile3.png", Texture.class);
        tileTexture[4] = assets.get("DogBarksCatFlee/tile4.png", Texture.class);
        //</editor-fold>
        enemies = new Array<Cat>();
        for (int i =0 ; i< TILES_COUNT ;i++ ) {
            tilesVector.add(new Vector2(PosicaoInicial.x + i*5, PosicaoInicial.y));
        }
        
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnEnemy();
            }

        }, 0, this.spawnInterval);
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void spawnEnemy () {
        Vector2 CatPosition = new Vector2();
        CatPosition.x = viewport.getWorldWidth();
        Cat enemy = new Cat(ScareThereshold(), CatPosition, CatTexture);
    }
    
    private int ScareThereshold (){
        return MathUtils.random(10, 30);
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
