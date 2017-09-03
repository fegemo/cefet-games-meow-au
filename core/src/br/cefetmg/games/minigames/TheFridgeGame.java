
package br.cefetmg.games.minigames;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
    private Object[] shelfs;
    private Object fish;
    private Object background, fridge;
    private Cat cat;
    private float TimeCounter;
    
    private int fridgeLimitsXMax, fridgeLimitsXMin, fridgeLimitsYMax, fridgeLimitsYMin;
    private final Vector2 initialFridgePosition = new Vector2(750,100), finalFridgePosition = new Vector2(600,00);
    private final int initialFridgeHeight=550,  initialFridgeWidth=500;       
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
    
     class Cat {
        final int width = 400;
        final int height = 199;
        AnimatedSprite walking, stoped, jumping, siting, currentAnimation;
        
        Cat(Texture catTexture){  
            TextureRegion[][] AnimationFrames = TextureRegion.split(catTexture, width, height);  
            TextureRegion[] walkingFrames = new TextureRegion[30];
            for(int i=0; i<24; i++){                
                walkingFrames[i] = AnimationFrames[i%12][0];   
            }           
            for(int i=24; i<30; i++){
                walkingFrames[i] = AnimationFrames[i%24][1];
            }
            walking = new AnimatedSprite(new Animation(0.1f, walkingFrames));        
            walking.setPosition( viewport.getWorldWidth(), 0);
            stoped =  new AnimatedSprite(new Animation(0.1f, AnimationFrames[5][1]));           
            
           // jumping = new Animation(0.1f, AnimationFrames[2][0], AnimationFrames[2][1], AnimationFrames[2][2], AnimationFrames[2][3], AnimationFrames[2][4]);
           // jumping.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);*/ 
           currentAnimation = null;
        }
        public void draw(Batch batch){
            if(currentAnimation==walking){
               walking.draw(batch);
            }
            else if(currentAnimation==jumping){
             //  jumping.draw(batch);
            }
            else if(currentAnimation==stoped){
               stoped.draw(batch);
            }
    }    
       
    }
    public void initialAnimation(){
        boolean flag;         
        if(fridge.position.x>finalFridgePosition.x){
            fridge.position.x-=3; 
            fridge.width+=3;  
            fridgeLimitsXMin = Math.round((120*fridge.width)/initialFridgeWidth); 
            fridgeLimitsXMax = Math.round((360*fridge.width)/initialFridgeWidth);           
            background.position.x-=6;
            background.width += 6;
            flag = true;
        }
        else flag = false;
        if(fridge.position.y>finalFridgePosition.y){//update the limits, using the ratio//
            fridge.position.y-=3;
            fridge.height+=3; 
            fridgeLimitsYMin = Math.round((50*fridge.height)/initialFridgeHeight);
            fridgeLimitsYMax =  Math.round((400*fridge.height)/initialFridgeHeight);
            background.position.y-=3;
            background.height += 3;           
            flag=true;
        }       
        else flag=false;        
        if(flag==false){ 
            cat.currentAnimation = cat.walking;            
            cat.walking.setSize(food[0][0].width+50,food[0][0].height+50);            
            if(cat.currentAnimation.getX()>(fridge.position.x+400)){
                cat.currentAnimation.setX(cat.currentAnimation.getX()-2);   
              //  if(cat.walking.)
            }
            else{ 
                cat.stoped.setPosition(cat.walking.getX(),0);
                cat.stoped.setSize(food[0][0].width+50,food[0][0].height+50);
                started=true; 
            }
        }        
        setPositionsFoodMatrix();
    }
    
    public void fillFoodMatrix(){
        int emptySpace = 1; 
        int textureNumber = 0;
        System.out.println("Filling Food Matrix\nMapping:");
        for(int i=0;i<shelfAmount;i++){
            for(int j=0; j<3;j++){
                if(j==emptySpace) System.out.print("- ");
                else{
                    food[i][j] = new Object(new Vector2(0,0), (fridgeLimitsXMax-fridgeLimitsXMin)/3+10, (fridgeLimitsYMax-fridgeLimitsYMin)/shelfAmount+10, foodTexture[textureNumber]);
                    System.out.print("X ");
                    textureNumber++;
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
            shelfs[i] = new Object(new Vector2((fridge.position.x + x)*0.99f,(fridge.position.y + y)-30),(fridgeLimitsXMax-fridgeLimitsXMin),(fridgeLimitsYMax-fridgeLimitsYMin)/shelfAmount+80,
                        screen.assets.get("the-fridge-game/shelf.png",Texture.class));
        }
    }
    
    public TheFridgeGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 100f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        //variables//
        TimeCounter = 0;
        started=false;
        fridgeLimitsXMax = 360; fridgeLimitsXMin = 120; fridgeLimitsYMax = 400; fridgeLimitsYMin = 50;
        // objetos de textura
        this.foodTexture = new Texture[19];    
        for(int i=1;i<19;i++){
            String aux = Integer.toString(i);
            if(aux.length()<2) aux = "0" + aux; //to ensure it's 01-18//
            this.foodTexture[i-1] = screen.assets.get("the-fridge-game/food" + aux + ".png",Texture.class);               
        }
        System.out.println("\n\n--------------DEBUG--------------\n\nTextures succesfully loaded!");
        // instancias das subclasses da fase 
        generator = new Random();
        background = new Object(new Vector2(0,0), viewport.getWorldWidth(), viewport.getWorldHeight(), screen.assets.get("the-fridge-game/fridge-game-background.png", Texture.class));
        fridge = new Object(initialFridgePosition, initialFridgeWidth, initialFridgeHeight, screen.assets.get("the-fridge-game/open-fridge.png", Texture.class));
        cat = new Cat(screen.assets.get("the-fridge-game/cat.png",Texture.class)); //FIX ME//
        food = new Object[shelfAmount][3];
        shelfs = new Object[shelfAmount];
        System.out.println("Objects succesfully instantiated!");
        initialize();
    }
    
    private void initialize() {               
        fillFoodMatrix();    
        setPositionsFoodMatrix();        
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        System.out.println("\n\nDifficulty: " + difficulty + "\n\n");
        shelfAmount = 6; //FIX ME//between 4 and 6//
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
            initialAnimation();
        }
        background.Draw();
        fridge.Draw();
        //percorrer matrix aqui//
        for(int i=0;i<shelfAmount;i++){
            for(int j=0;j<3;j++){
                if(food[i][j]!=null){
                    food[i][j].Draw();
                }
            }
            shelfs[i].Draw();
        }
        cat.draw(batch);
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
