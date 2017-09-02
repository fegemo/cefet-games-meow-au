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
    private Random generator;
    private Texture[] foodTexture;   
    private Object[][] food;
    private Object background, fridge;
    private Cat cat;
    
    private int fridgeLimitsXMax, fridgeLimitsXMin, fridgeLimitsYMax, fridgeLimitsYMin;
            
    private boolean started, goingUp, end;
    private int shelfAmount;  
   
    public class Object {
        public Vector2 position;
        public Texture texture;
        public float width, height;
       
        public Object(Vector2 position, float width, float height, Texture texture) {
            this.position = position;
            this.texture = texture;
            this.width = width;
            this.height = height;
        }
        
        public void Draw(){            
            batch.draw(texture, position.x, position.y, width, height);
        }
    }    
    
     class Cat extends AnimatedSprite {
        static final int frameWidth = 30;
        static final int frameHeiht = 30;

        Cat(final Texture catTexture){
            super(new Animation(1.0f, new Array<TextureRegion>(){
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            catTexture, frameWidth, frameHeiht);
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
    public void initialAnimation(){
        if(fridge.position.x>550){
            fridge.position.x--;
        }
    }
    
    public void fillFoodMatrix(){
        int emptySpace = 1; 
        System.out.println("Filling Food Matrix\nMapping:");
        for(int i=0;i<shelfAmount;i++){
            for(int j=0; j<3;j++){
                if(j==emptySpace) System.out.print("- ");
                else{
                    int textureNumber = (i*3+j)%13;
                    food[i][j] = new Object(new Vector2(0,0), (fridgeLimitsXMax-fridgeLimitsXMin)/3 - 10, (fridgeLimitsYMax-fridgeLimitsYMin)/shelfAmount - 10, foodTexture[textureNumber]);
                    System.out.print("X ");
                }        
            }  
            int aux = generator.nextInt(2); //number between 0 and 1//
            switch(emptySpace){
                case 0: emptySpace++; break;
                case 1: if(aux==0) emptySpace++; 
                        else emptySpace--;
                        break;
                case 2: emptySpace--; break;
            }
            System.out.println();
        }
    }
    
    public void setPositionsFoodMatrix(){
        int x=fridgeLimitsXMin, y=fridgeLimitsYMin;    
        for(int i=0;i<shelfAmount;i++){
            for(int j=0;j<3;j++){
                if(food[i][j]!=null){
                    food[i][j].position.x=fridge.position.x + x;
                    food[i][j].position.y=fridge.position.y + y;                       
                }
                x+=(fridgeLimitsXMax-fridgeLimitsXMin)/3;
            }
            x=fridgeLimitsXMin; y+=(fridgeLimitsYMax-fridgeLimitsYMin)/shelfAmount;
        }
    }
    
    public TheFridgeGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 100f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        //variables//
        started=false;
        fridgeLimitsXMax = 360; fridgeLimitsXMin = 120; fridgeLimitsYMax = 400; fridgeLimitsYMin = 50;
        // objetos de textura
        this.foodTexture = new Texture[13];    
        for(int i=0;i<13;i++){
             String aux = Integer.toString(i+1);
             if(aux.length()<2) aux = "0" + aux; //to ensure it's 01-13//
             this.foodTexture[i]= screen.assets.get("the-fridge-game/food" + aux + ".png",Texture.class);   
        }
        System.out.println("\n\n--------------DEBUG--------------\n\nTextures succesfully loaded!");
        // instancias das subclasses da fase 
        generator = new Random();
        background = new Object(new Vector2(0,0), viewport.getWorldWidth(), viewport.getWorldHeight(), screen.assets.get("the-fridge-game/fridge-game-background.png", Texture.class));
        fridge = new Object(new Vector2(750,100), 500, 550, screen.assets.get("the-fridge-game/open-fridge.png", Texture.class));
        cat = new Cat(screen.assets.get("the-fridge-game/food13.png",Texture.class)); 
        food = new Object[shelfAmount][3];
        System.out.println("Objects succesfully instantiated!");
        initialize();
    }
    
    private void initialize() {        
        cat.setCenter( viewport.getWorldWidth() * 0.8f, viewport.getWorldHeight() / 2f);
        fillFoodMatrix();    
        setPositionsFoodMatrix();        
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        System.out.println("\n\nDifficulty: " + difficulty + "\n\n");
        shelfAmount = 6; //FIX ME//
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
        fridge.Draw();
        //cat.draw(batch);
        //percorrer matrix aqui//
        for(int i=0;i<shelfAmount;i++){
            for(int j=0;j<3;j++){
                if(food[i][j]!=null){
                    food[i][j].Draw();
                }
            }
        }
    }

    public void AnimatedStart(){
        
    }
    @Override
    public String getInstructions() {
        return "Alcance os peixes antes que o tempo acabe.";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
       
}
