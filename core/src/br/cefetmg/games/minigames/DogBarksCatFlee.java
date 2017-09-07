/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.Animals.*;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import java.time.Clock;

/**
 *
 * @author Pedro e Arthur
 * ouçam https://www.youtube.com/watch?v=Gfw4yxn_kPQ
 */
public class DogBarksCatFlee extends MiniGame {
    private final int TILES_COUNT = 9;
    private Dog player;
    private Texture DogTextureStandBy;
    private Texture DogTexture;
    private Animation<TextureRegion> DogBarking;
    private Texture CatTexture;
    //private Array<Cat> enemies;
    private Cat enemy;
    private Array<Tiles> tilesVector; // Tem que criar 1 tipo tile que tem Vector2 e um int para representar qual é o 
    private Vector2 PosicaoInicial;
    private Texture tileTexture[] = new Texture[5];
    private float spawnInterval;
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private float TempoDeAnimacao;
    
    public DogBarksCatFlee(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }
    
    private void TilesDraw(){
        for (Tiles tile : tilesVector) {
            batch.draw(tileTexture[tile.tile_Type ], tile.tileVector.x, tile.tileVector.y);
           // batch.draw(tileTexture[MathUtils.random(4)], tile.x, tile.y);
        }
    }
    
    private void UpdateDraw(){
        for (Tiles tile : tilesVector) {
            tile.tileVector.x += -0.5; 
            if(tile.tileVector.x <= 0- tileTexture[0].getWidth()){
                tile.tileChange();
                tile.tileVector.x = 500; // ainda não definido o num;
            } // ainda não definido o num;   
        }
    }
    
    private void PlayerDraw(float dt) {
        if (!player.isLatindo()) {
            batch.draw (player.getTexture(),player.getPos().x,player.getPos().y);
        } else {
            batch.draw (player.Anima(dt), player.getPos().x, player.getPos().y);
            player.InvertLatindo();
        }
        
    }
    
    private void CatDraw(){
        if( ! enemy.vivoMorto() )
            batch.draw(enemy.getTexture(),enemy.getPos().x,enemy.getPos().x);
    }
    
    @Override
    protected void onStart() {
        TempoDeAnimacao = 0;
        DogTextureStandBy = assets.get("DogBarksCatFlee/dog_separado_1.png", Texture.class);
        DogTexture = assets.get("DogBarksCatFlee/dog_spritesheet.png", Texture.class);
        TextureRegion[][] quadrosDeAnimacao = TextureRegion.split(DogTexture, 128,128);
        System.out.println(+ quadrosDeAnimacao.length);
        DogBarking = new Animation <TextureRegion>(1f,
                    quadrosDeAnimacao[0][0],
                    quadrosDeAnimacao[0][1],
                    quadrosDeAnimacao[0][2],
                    quadrosDeAnimacao[0][3]);
        DogBarking.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        CatTexture = assets.get("DogBarksCatFlee/kitten1-alt.png", Texture.class);
        //enemies = new Array<Cat>();
        //DogAnimation = assets.get(null);
//        //<editor-fold defaultstate="collapsed" desc="texturas tile">
        tileTexture[0] = assets.get("DogBarksCatFlee/tile0.png", Texture.class);
        tileTexture[1] = assets.get("DogBarksCatFlee/tile1.png", Texture.class);
        tileTexture[2] = assets.get("DogBarksCatFlee/tile2.png", Texture.class);
        tileTexture[3] = assets.get("DogBarksCatFlee/tile3.png", Texture.class);
        tileTexture[4] = assets.get("DogBarksCatFlee/tile4.png", Texture.class);
//        //</editor-fold>
        tilesVector = new Array<Tiles>();
        
        
        for (int i =0 ; i< TILES_COUNT ;i++ ) {
            tilesVector.add(new Tiles(new Vector2(1 + i * tileTexture[0].getWidth(), 41)));
        }
        inicializeDog();
        inicializeCat();
//        timer.scheduleTask(new Task() {
//            @Override
//            public void run() {
//                spawnEnemy();
//            }
//        }, 0, this.spawnInterval);    
    }
    
    private void UpdateEnemy(){
        if( enemy.FleeAction( player.getBarkCounter())){
            enemy.morreu();
            player.BarkZero();
        }    
        if( enemy.vivoMorto() && (enemy.get_quantidade_vidas() > 0))
            enemy.spawn();
        //spawnEnemy();
        
    }

//    private void spawnEnemy () {
//          enemy.spawn();
//          Vector2 CatPosition = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
//          Cat enemy = new Cat(ScareThereshold(), CatPosition, CatTexture);
//          enemies.add(enemy);
//    }
    
    private void inicializeDog(){
        TextureRegion[][] TextureDog = TextureRegion.split(DogTextureStandBy, DogTextureStandBy.getWidth(), DogTextureStandBy.getHeight());
        PosicaoInicial = new Vector2 (1,41);
        player = new Dog (3, PosicaoInicial, TextureDog[0][0], DogBarking);
    }
    
    private void inicializeCat(){
        TextureRegion[][] Cat = TextureRegion.split(CatTexture, CatTexture.getWidth(), CatTexture.getHeight());
        enemy = new Cat( (int) ( (float) ScareThereshold() * DifficultyCurve.S.getCurveValueBetween(spawnInterval,3f,1f)),PosicaoInicial,Cat[0][0] );
        enemy.settarQuantidade_vidas(spawnInterval);
        enemy.moviment(enemy.PosIniCat());
        enemy.spawn();
    }
    
    private int ScareThereshold (){
        return MathUtils.random(1,5);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
//        this.minimumEnemySpeed = DifficultyCurve.LINEAR;
//                .getCurveValueBetween(difficulty, 120, 220);
//        this.maximumEnemySpeed = DifficultyCurve.LINEAR
//                .getCurveValueBetween(difficulty, 240, 340);
//        this.spawnInterval = DifficultyCurve.LINEAR_NEGATIVE
//                .getCurveValueBetween(difficulty, 0.25f, 1.5f);
        spawnInterval = DifficultyCurve.S.getCurveValue(difficulty);
        System.out.println("spawnInterval" + spawnInterval);
        //enemy.settarQuantidade_vidas( DifficultyCurve.S.getCurveValue(difficulty) );
  }

    @Override
    public void onHandlePlayingInput() {
        if (Gdx.input.justTouched()){
            player.Bark();
            System.out.println( player.getBarkCounter() + " " + enemy.GetScareTheresold());
            player.InvertLatindo();
            System.out.println("Click");
        }
            
    }

    public void onUpdate(float dt) {
        TempoDeAnimacao += Gdx.graphics.getDeltaTime();
        UpdateDraw();
        UpdateEnemy();
    }

    @Override
    public void onDrawGame() {
        TilesDraw();
        PlayerDraw(TempoDeAnimacao);
        CatDraw();
    }

    @Override
    public String getInstructions() {
        return "RAWR RAWR";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
        
}
