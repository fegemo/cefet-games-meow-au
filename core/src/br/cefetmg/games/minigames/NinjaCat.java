package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class NinjaCat extends MiniGame{
    
   
    private Sprite backGround;
    private Sprite arrow;
    private int spawnedZombies,totalZombies;
    private float spawnInterval;
    private float speed;
    private OrthographicCamera camera;
    private Cat cat;
    private int enemiesKilled;
    private Array<Zombie> zombies;
    private boolean rampage;
    private boolean right;
    private boolean flip;
    private boolean hit;
    
    
   
    private Texture playerTexture;
    private Texture arrowTexture;
    private Texture sakamoto,sakamoto2;
    private Texture bgTexture;
    private Texture catTexture;
    private Texture zombieTex;
    private Texture rampageTex;
    private Texture deadZombie;
    private Texture atk1,atk;
    
    private Sound intro;
    private Sound ken1,ken2;
    
  
    
    public NinjaCat(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 50f,/*Tempo maximo da fase*/
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }
    
    @Override
    protected void onStart(){
       
        rampage = false;
        right = true;
        flip = false;
        hit = true;
        
        bgTexture = assets.get("ninja-cat/bg1.jpg",Texture.class);
        arrowTexture = assets.get("ninja-cat/arrow.png",Texture.class);
        catTexture = assets.get("ninja-cat/cat.png",Texture.class);
        zombieTex = assets.get("ninja-cat/zombie1.png",Texture.class);
        rampageTex = assets.get("ninja-cat/cat1.png",Texture.class);
        atk1 = assets.get("ninja-cat/atk1.png",Texture.class);
        atk = assets.get("ninja-cat/atk.png",Texture.class);
        deadZombie = assets.get("ninja-cat/zombie2.png",Texture.class);
        intro = assets.get("ninja-cat/Intro.mp3",Sound.class);
        ken1 = assets.get("ninja-cat/ken1.mp3",Sound.class);
        ken2 = assets.get("ninja-cat/ken2.mp3",Sound.class);
        
        intro.play(.3f);
        zombies = new Array<Zombie>();
        cat = new Cat(catTexture);
        arrow = new Sprite(arrowTexture);
        arrow.setScale(0.08f);
        arrow.setOrigin(0,0);
        arrow.setPosition(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
        
        backGround = new Sprite(bgTexture);
        backGround.setOrigin(0,0);
        backGround.setScale(viewport.getWorldWidth()/backGround.getWidth(), viewport.getWorldHeight()/backGround.getHeight());
        backGround.setPosition(0,0);

        cat.setOrigin(0, 0);
        cat.setScale(1.5f);
        cat.setPosition(viewport.getWorldWidth()*0.4f,viewport.getWorldHeight()*.1f);
         
        scheduleZombiesSpawn();
        
    }
    
    private void scheduleZombiesSpawn(){
        Task t = new Task(){
            @Override
            public void run(){
                spawnZombies();
                if(++spawnedZombies < totalZombies){
                    scheduleZombiesSpawn();
                }
            }
        };
        float nextSpawnMillis = this.spawnInterval
                * (rand.nextFloat() / 3 + 0.15f);
        timer.scheduleTask(t, nextSpawnMillis);
               
    }
    private void spawnZombies(){
        Zombie zomb;
        zomb = new Zombie(zombieTex) ;

        zomb.setCenter(0, 0);
        zomb.setScale(3.5f);
        if(rand.nextInt()%2 == 0){
            zomb.flipFrames(true, false);
            zomb.setPosition(0,viewport.getWorldHeight()*.1f+70);
        }
        else
            zomb.setPosition(viewport.getWorldWidth(),viewport.getWorldHeight()*.1f+70);
        
        zombies.add(zomb);
    }
    
    
      @Override
    protected void configureDifficultyParameters(float difficulty) {
        
        this.speed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 2, 6);
        this.spawnInterval = DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 1.5f, 2.5f);
        this.totalZombies = (int) Math.ceil(maxDuration / spawnInterval) - 3;
    
    }
    
    @Override
    public void onHandlePlayingInput() {
        Vector2 click = new Vector2 (Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        arrow.setPosition(click.x-arrow.getWidth()/2*arrow.getScaleX(),click.y-arrow.getHeight()/2*arrow.getScaleY());

        if (Gdx.input.justTouched() && !rampage) {
            for (int i = 0; i < zombies.size; i++) {
                Zombie zomb = zombies.get(i);
                if (zomb.getBoundingRectangle().overlaps(
                        arrow.getBoundingRectangle())) {
                    rampage = true;
                    if(arrow.getX() > cat.getX() && !right){
                         flip = false;
                         right = true;
                    }
                     else if(arrow.getX() < cat.getX() && right){
                          flip = true;
                          right = false;
                    }
                    break;
                }
            }
        }  
    }
    
     class Cat extends AnimatedSprite {
        static final int FRAME_WIDTH = 95;
        static final int FRAME_HEIGHT = 76;

        Cat(final Texture catTexture) {
            super(new Animation(.14f, new Array<TextureRegion>() {
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
        }
    } 
    
     
     class Zombie extends AnimatedSprite {

        static final int FRAME_WIDTH = 32;
        static final int FRAME_HEIGHT = 52;

        Zombie(final Texture zombieTexture) {
            super(new Animation(.14f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            zombieTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],
                        frames[0][4],
                        frames[0][5],
                        frames[0][6],
                        frames[0][7]
                            
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
        }   
    } 
     
     void setCat(Texture tex){
         float x = cat.getX();
         float y = cat.getY();
         cat = new Cat(tex);
         cat.setOrigin(0, 0);
         cat.setScale(2f);
         cat.setPosition(x,y);
         if(flip)
             cat.flipFrames(true, false);  
     }
    
    @Override
    public void onUpdate(float dt) {
        if(rampage){
            
            setCat(rampageTex);
            if(right)
                cat.setX(cat.getX()+18f);
            else
                cat.setX(cat.getX()-18f);
            
            for(int i = 0; i < zombies.size; i++){
                Zombie zomb = zombies.get(i);
                if(zomb.getBoundingRectangle().overlaps(cat.getBoundingRectangle())){
                    rampage = false;
                    if(rand.nextInt()%2==0)
                        ken1.play(.1f);
                    else 
                        ken2.play(.1f);
                        
                    if(hit){
                        setCat(atk1);
                        hit = !hit;
                    }
                    else{
                        setCat(atk);
                        hit = !hit;
                    }
                        
                    this.zombies.removeValue(zomb, true);
                    this.enemiesKilled++;
                    if (this.enemiesKilled >= this.totalZombies) {
                        super.challengeSolved();
                    }
                }
            }
        }
       
        cat.update();
        
        for(int i = 0; i < zombies.size; i++){
            if(zombies.get(i).getX() < cat.getX())
                zombies.get(i).setPosition(zombies.get(i).getX() + speed ,zombies.get(i).getY());
            else
                zombies.get(i).setPosition(zombies.get(i).getX() - speed ,zombies.get(i).getY());
            zombies.get(i).update();
            if(zombies.get(i).getBoundingRectangle().overlaps(cat.getBoundingRectangle()) && !rampage)
                super.challengeFailed();
        }
        
       
    }
    
     
    @Override
    public String getInstructions(){
        return "Mate os Zumbis";
    }
    
    @Override
    public void onDrawGame() {
        backGround.draw(batch);
        cat.draw(batch);

        for(int i = 0; i < zombies.size;i++){
            Zombie sprite = zombies.get(i);
            sprite.draw(batch);
        }  
        arrow.draw(batch);
    }
    
    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
}
