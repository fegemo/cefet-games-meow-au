/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;
import br.cefetmg.games.Config;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import java.util.Random;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author sarit
 */
public class TheFridgeGame extends MiniGame {
    
    private  Random generator = new Random();
    private Object background, fridge;
    private Object[][] food;
    private int shelfAmount;
    private boolean started, goingUp, end;
    private Texture[] foodTexture;
    private Cat cat;
   
    public class Object {
        public Vector2 position;
        public Texture texture;

        public Object(Vector2 position, Texture texture) {
            this.position = position;
            this.texture = texture;
        }        
        
        public void Draw(){            
            batch.draw(texture, position.x, position.y, viewport.getWorldWidth(), viewport.getWorldHeight());
        }
    }    
    
     class Cat extends AnimatedSprite {
        static final int FRAME_WIDTH = 467;
        static final int FRAME_HEIGHT = 547;

        Cat(final Texture catTexture){
            super(new Animation(1.0f, new Array<TextureRegion>(){
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            catTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0]
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
            init();
        }

        public void init() {
            //setScale(0.5f);
        }
    }
    
    public void fillFoodMatrix(){
        int emptySpace = 1;       
        System.out.println("Filling Food Matrix");
        for(int i=0;i<shelfAmount;i++){
            for(int j=0;j<3;j++){
                if(j==emptySpace) j++;
                else food[i][j] = this.RandomObject();             
            }
            while(emptySpace<0 || emptySpace>2){
                int aux = generator.nextInt(3); //number between 0 and 2//
                aux--; //we need a number between -1 and 1//
                emptySpace += aux;  
            }
        }
    }
    
    public Object RandomObject(){ //pick up a random food texture//        
        int rand = generator.nextInt(13); //number between 0 and 12//
        switch(rand){ //they gonna have different sizes//
            case 0: return new Object(new Vector2(10, 10),foodTexture[0]);
            case 1: return new Object(new Vector2(10, 10),foodTexture[1]);
            case 2: return new Object(new Vector2(10, 10),foodTexture[2]);
            case 3: return new Object(new Vector2(10, 10),foodTexture[3]);
            case 4: return new Object(new Vector2(10, 10),foodTexture[4]);
            case 5: return new Object(new Vector2(10, 10),foodTexture[5]);
            case 6: return new Object(new Vector2(10, 10),foodTexture[6]);
            case 7: return new Object(new Vector2(10, 10),foodTexture[7]);
            case 8: return new Object(new Vector2(10, 10),foodTexture[8]);
            case 9: return new Object(new Vector2(10, 10),foodTexture[9]);
            case 10: return new Object(new Vector2(10, 10),foodTexture[10]);
            case 11: return new Object(new Vector2(10, 10),foodTexture[11]);              
            case 12: return new Object(new Vector2(10, 10),foodTexture[12]);                
        }
        return null;
    }
    
    public TheFridgeGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 100f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        // objetos de textura
        background = new Object(new Vector2(0,0),screen.assets.get("the-fridge-game/fridge-game-background.png", Texture.class));
        fridge = new Object(new Vector2(0,0),screen.assets.get("the-fridge-game/open-fridge.png", Texture.class));
        this.foodTexture = new Texture[13];    
        for(int i=0;i<13;i++){
             String aux = Integer.toString(i+1);
             if(aux.length()<2) aux = "0" + aux; //to ensure it's 01-13//
             this.foodTexture[i]= screen.assets.get("the-fridge-game/food" + aux + ".png",Texture.class);   
        }
        System.out.println("Textures succesfully loaded!");
        // instancias das subclasses da fase        
        cat = new Cat(screen.assets.get("the-fridge-game/food13.png",Texture.class));
        fillFoodMatrix();            
        System.out.println("Objects succesfully instantiated!");
        initialize();
    }
    
    private void initialize() {
        cat.setCenter( viewport.getWorldWidth() * 0.8f, viewport.getWorldHeight() / 2f);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        /*
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 120, 220);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 240, 340);
        */
    }

    @Override
    public void onHandlePlayingInput() {
        // obtem a posição do mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        
    }

    @Override
    public void onUpdate(float dt) {
        
    }

    @Override
    public void onDrawGame() {
        if(started==false){
            this.AnimatedStart();
        }
        background.Draw();
        cat.draw(batch);
        //percorrer matrix aqui//
    }

    public void AnimatedStart(){
        
    }
    @Override
    public String getInstructions() {
        return "Alcance a útima prateleira antes que o tempo acabe.";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
       
}
