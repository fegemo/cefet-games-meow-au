
package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

import java.util.Random;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class TheFridgeGame extends MiniGame {    
    private Random generator;
    private Texture[] foodTexture, buttonsTexture;      
    private Object[][] food;
    private Object[] shelfs;
    private Object fish, background, fridge;
    private Button buttons[];
    private Cat cat;
    private CHOICE currentChoice;
    
    private int shelfAmount, fridgeLimitsXMax, fridgeLimitsXMin, fridgeLimitsYMax, fridgeLimitsYMin;
    private final int initialFridgeHeight=550,  initialFridgeWidth=500; 
    private final Vector2 initialFridgePosition = new Vector2(750,100), finalFridgePosition = new Vector2(650,20);
    private boolean started, jumping , falling, mistake, ending, directionRight;
     
    private enum CHOICE {RIGHT,LEFT,JUMP};
    
    private class Object {
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
    
    private class Button extends Object{       
        public CHOICE choice;
        public boolean show;

        public Button(Vector2 position, float width, float height, Texture texture, CHOICE choice, boolean show) {
            super(position,width,height,texture);          
            this.choice = choice;
            this.show = show;
        }     
        
        @Override
        public void Draw(){   
            if(show){
                batch.draw(texture, position.x, position.y, width, height);
            }
        }        
         
        public void clicked(Vector2 click){
            if(show && currentChoice==null && jumping==false && falling==false){
                if( (click.x>=this.position.x && click.x<=(this.position.x+this.width)) && (click.y>=this.position.y && click.y<=(this.position.y+this.height)) ){
                    if(this.choice==CHOICE.JUMP){  
                        System.out.println("Button JUMP clicked!");
                        this.show=false;                    
                    }
                    if(this.choice==CHOICE.RIGHT){
                        System.out.println("Button RIGHT clicked!");
                    }
                    else if(this.choice==CHOICE.LEFT){
                        System.out.println("Button LEFT clicked!");
                    }
                    jumping = true;
                    currentChoice = this.choice;
                }
            }
        }        
    }
    
    private class Cat {
        final int width = 400;
        final int height = 199;
        int nextShelf, nextPosition;
        AnimatedSprite walking, jumping, siting, currentAnimation;
        
        Cat(Texture catTexture){ 
            TextureRegion[][] AnimationFrames = TextureRegion.split(catTexture, width, height);  
            TextureRegion[] walkingFrames = new TextureRegion[30];
            for(int i=0; i<24; i++){ //select the animation frames//               
                walkingFrames[i] = AnimationFrames[i%12][0];   
            }           
            for(int i=24; i<30; i++){
                walkingFrames[i] = AnimationFrames[i%24][1];
            }
            walking = new AnimatedSprite(new Animation(0.1f, walkingFrames)); //create animation//       
            walking.setPosition(viewport.getWorldWidth(), 0);
            TextureRegion[] jumpingFrames = new TextureRegion[18];
            for(int i=0; i<12; i++){  
               jumpingFrames[i]=AnimationFrames[(i+6)%13][3];                 
            }
            for(int i=12; i<18; i++){
               jumpingFrames[i] = AnimationFrames[i-12][1];
            }
            jumping = new AnimatedSprite(new Animation(0.1f, jumpingFrames));   
            currentAnimation = null;
        }
        public void draw(Batch batch){
            if(currentAnimation!=null){
                currentAnimation.draw(batch);
            }
        }          
    }
    
    private void endingAnimation(){
      //  if(true){//FIX ME//
            
      //  }
      //  else{
            challengeSolved();        
            ending=false;
     //   }
    }
    
    private void fallingAnimation(){
        if(cat.jumping.getY()>0){//FIX ME//
             cat.jumping.setY(cat.jumping.getY()-5);     
        }
        else {
            challengeFailed();
            falling=false;
        }        
    }
        
    private void getChoice(){
        if(currentChoice==CHOICE.RIGHT){
            System.out.println("Current Choice is RIGHT");
            cat.jumping.setTime(0);
            if(cat.jumping.isFlipX()==false){
                cat.jumping.flipFrames(true, false);                
            }
            cat.nextPosition++;
            cat.nextShelf++;            
            directionRight=true;
        }
        else{
            if(currentChoice==CHOICE.JUMP){
                System.out.println("Current Choice is JUMP");
                cat.currentAnimation=cat.jumping;
                cat.jumping.setPosition(cat.walking.getX(),cat.walking.getY());
                cat.jumping.setSize(food[0][0].width+50,food[0][0].height+50); 
                buttons[1].show=true;
                buttons[2].show=true;
                cat.nextPosition=1;
                cat.nextShelf = 0;
            }
            else {
                System.out.println("Current Choice is LEFT");   
                cat.nextPosition--;
                cat.nextShelf++;
            }
            cat.jumping.setTime(0);
            if(cat.jumping.isFlipX()==true){
                cat.jumping.flipFrames(true, false);               
            }
            directionRight=false;
        }        
        if(cat.nextPosition<0||cat.nextPosition>2||(cat.nextShelf<shelfAmount && food[cat.nextShelf][cat.nextPosition]!=null)){
            mistake=true;    
            System.out.println("Mistake!"); 
        } 
        currentChoice = null;
        System.out.println("NextPosition: " + cat.nextPosition + "\nNextShelf: " + cat.nextShelf);   
    }
    
    private void jumpAnimation(){         
        boolean done; //flag to finish animation//
        if(currentChoice!=null){
            getChoice();
            done = false;
        }
        else{
            done = true;
            float shelfY = (fridgeLimitsYMax-fridgeLimitsYMin)/shelfAmount;
            float foodX = (fridgeLimitsXMax-fridgeLimitsXMin)/3;
            //verify position y//
            if(cat.nextShelf<shelfAmount && cat.jumping.getY()<(shelfs[cat.nextShelf].position.y-shelfY+20)){
                cat.jumping.setY(cat.jumping.getY()+2); 
                done=false;
            }
            else if(cat.nextShelf==shelfAmount && cat.jumping.getY()<(shelfs[cat.nextShelf-1].position.y+20)){
                cat.jumping.setY(cat.jumping.getY()+2);    
                done=false;
            }
            //verify position x//
            if(cat.nextPosition<0 && (cat.jumping.getX()>(food[0][0].position.x-foodX))){//case jumping to the left, out of the fridge//
                cat.jumping.setX(cat.jumping.getX()-2); 
                done=false;
            }    
            else if(cat.nextPosition>2 && (cat.jumping.getX()<(food[0][2].position.x+foodX))){//case jumping to the right, out of the fridge//
                cat.jumping.setX(cat.jumping.getX()+2);  
                done=false;
            }
            else{
                float aux = fridge.position.x + fridgeLimitsXMin + (cat.nextPosition*foodX) - 10;
                if(directionRight && (cat.jumping.getX()<aux) ){
                    cat.jumping.setX(cat.jumping.getX()+2);
                    done=false;
                }
                else if(directionRight==false && cat.jumping.getX()>aux){
                    cat.jumping.setX(cat.jumping.getX()-2);
                    done=false;
                }
            }
        }
        if(done){ //finish animation//
            jumping=false; 
            if(mistake){
                falling=true;
            }
            if(cat.nextShelf==shelfAmount){              
                ending=true;
            }
        }
    }
    
    private void initialAnimation(){
        boolean done = true;//flag to finish zooming//        
        if(fridge.position.x>finalFridgePosition.x){//zooming//
            fridge.position.x-=1.25; //ratio between x and y final//
            fridge.width+=1.25;  
            fridgeLimitsXMin = Math.round((120*fridge.width)/initialFridgeWidth); //update the limits, using the ratios//
            fridgeLimitsXMax = Math.round((360*fridge.width)/initialFridgeWidth);           
            background.position.x-=2;
            background.width+=2;
            done = false;
        } 
        if(fridge.position.y>finalFridgePosition.y){
            fridge.position.y--;
            fridge.height++; 
            fridgeLimitsYMin = Math.round((50*fridge.height)/initialFridgeHeight);
            fridgeLimitsYMax =  Math.round((400*fridge.height)/initialFridgeHeight);
            background.position.y--;
            background.height++;           
            done = false;
        }           
        if(done){ //cat starts to walk//
            cat.currentAnimation = cat.walking;            
            cat.walking.setSize(food[0][0].width+50,food[0][0].height+50);            
            if(cat.currentAnimation.getX()>(fridge.position.x+400)){
                cat.currentAnimation.setX(cat.currentAnimation.getX()-1.7f);  
            }
            else{//animation ends//
                started=true; 
                buttons[0].show=true;
            }
        } 
        setPositionsFoodMatrix();//update food's positions//
    }
    
    private void fillFoodMatrix(){
        int emptySpace = 1; 
        int textureNumber = 0;
        System.out.println("Filling Food Matrix\nMapping:");
        for(int i=0;i<shelfAmount;i++){
            for(int j=0; j<3;j++){
                if(j==emptySpace){
                    System.out.print("- ");
                }
                else{
                    food[i][j] = new Object(new Vector2(0,0), (fridgeLimitsXMax-fridgeLimitsXMin)/3+10, (fridgeLimitsYMax-fridgeLimitsYMin)/shelfAmount+10, foodTexture[textureNumber]);
                    System.out.print("X ");
                    textureNumber++;
                }        
            }  
            switch(emptySpace){
                case 0: emptySpace++; 
                        break; 
                case 1: int aux = generator.nextInt(2); //number between 0 and 1//
                        if(aux==0) emptySpace++; //if it's in the midle column, we need to use a random number to choose//
                        else emptySpace--;
                        break;
                case 2: emptySpace--; 
                        break;
            }
            System.out.println();
        }
    }
    
    private void setPositionsFoodMatrix(){
        int x=fridgeLimitsXMin, y=fridgeLimitsYMin;    
        for(int i=0;i<shelfAmount;i++){
            for(int j=0;j<3;j++){
                if(food[i][j]!=null){
                    food[i][j].position.x=fridge.position.x + x; 
                    food[i][j].position.y=fridge.position.y + y;                       
                }
                x+=(fridgeLimitsXMax-fridgeLimitsXMin)/3;//set the food's position according to the fridge limits//
            }                                            //and the amount of food and shelfs//
            x=fridgeLimitsXMin; y+=(fridgeLimitsYMax-fridgeLimitsYMin)/shelfAmount;
            shelfs[i] = new Object(new Vector2((fridge.position.x + x)*0.99f,(fridge.position.y + y)-30),(fridgeLimitsXMax-fridgeLimitsXMin),(fridgeLimitsYMax-fridgeLimitsYMin)/shelfAmount+80,
                        screen.assets.get("the-fridge-game/shelf.png",Texture.class));
        }
    }
    
    public TheFridgeGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 100f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
        //FIX ME//set up time//
    }

    @Override
    protected void onStart() {
        started=false; jumping=false; directionRight=false;//set the flags//
        fridgeLimitsXMax = 360; fridgeLimitsXMin = 120; fridgeLimitsYMax = 400; fridgeLimitsYMin = 50;
        //textures//
        this.foodTexture = new Texture[19];    
        for(int i=1;i<19;i++){
            String aux = Integer.toString(i);
            if(aux.length()<2) aux = "0" + aux;//it's 01-18//
            this.foodTexture[i-1] = screen.assets.get("the-fridge-game/food" + aux + ".png",Texture.class);               
        }
        this.buttonsTexture = new Texture[3]; 
        for(int i=1;i<4;i++){
            String aux = Integer.toString(i); 
            aux = "0" + aux; //it's 01-03//
            this.buttonsTexture[i-1] =  screen.assets.get("the-fridge-game/button" + aux + ".png",Texture.class); 
        }
        System.out.println("\n--------------DEBUG--------------\nTextures succesfully loaded!");        
        generator = new Random();
        background = new Object(new Vector2(0,0), viewport.getWorldWidth(), viewport.getWorldHeight(), screen.assets.get("the-fridge-game/fridge-game-background.png", Texture.class));
        fridge = new Object(initialFridgePosition, initialFridgeWidth, initialFridgeHeight, screen.assets.get("the-fridge-game/open-fridge.png", Texture.class));
        cat = new Cat(screen.assets.get("the-fridge-game/cat.png",Texture.class));       
        buttons = new Button[3];
        buttons[0] = new Button(new Vector2 (830,0), 200, 60, screen.assets.get("the-fridge-game/button01.png",Texture.class), CHOICE.JUMP, false);
        buttons[1] = new Button(new Vector2 (920,0), 100, 100, screen.assets.get("the-fridge-game/button02.png",Texture.class), CHOICE.RIGHT, false);
        buttons[2] = new Button(new Vector2 (770,0), 100, 100, screen.assets.get("the-fridge-game/button03.png",Texture.class), CHOICE.LEFT, false);
        shelfs = new Object[shelfAmount];
        food = new Object[shelfAmount][3];
        System.out.println("Objects succesfully instantiated!");
        initialize();
    }
    
    private void initialize() {               
        fillFoodMatrix();    
        setPositionsFoodMatrix();          
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        System.out.println("\n\nDifficulty: " + difficulty + "\n\n");//FIX ME// SET UP TIME//
        shelfAmount = Math.round(DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 4, 7)); //between 4 and 7//
        System.out.println("shelfAmount: " + shelfAmount);
    }

    @Override
    public void onHandlePlayingInput(){
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);   
        if(Gdx.input.isTouched()){//verify if the button was clicked//
            for(int i=0; i<3; i++){
                buttons[i].clicked(click);
            } 
        }
    }

    @Override
    public void onUpdate(float dt) {
        if(started==false){
            initialAnimation();
        }
        else if(jumping){
            jumpAnimation();
        }
        else if(falling){
            fallingAnimation();
        }
        else if(ending){
            endingAnimation();
        }
    }

    @Override
    public void onDrawGame() {
        background.Draw();
        fridge.Draw();
        for(int i=0;i<shelfAmount;i++){
            for(int j=0;j<3;j++){
                if(food[i][j]!=null){
                    food[i][j].Draw();
                }
            }
            shelfs[i].Draw();
        }
        cat.draw(batch);
        for(int i=0; i<3; i++){
            buttons[i].Draw();
        }//FIX ME// DRAQ FISH
    }

    @Override
    public String getInstructions() {
        return "Alcance os peixes na última prateleira antes que o tempo acabe.";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
       
} 