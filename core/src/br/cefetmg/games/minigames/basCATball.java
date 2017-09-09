package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
/**
 *
 * @author Rógenes
 */
public class basCATball extends MiniGame{
    
    private Sprite ball;
    private Sprite player;
    private Sprite bar;
    private Sprite target;
    private Sprite selector;
    private Sprite ball2;
    private Sprite court;
    private Sprite dorami;
    private Sprite pointer;
    
    
    private Texture ballTexture;
    private Texture playerTexture;
    private Texture barTexture;
    private Texture targetTexture;
    private Texture selectorTexture;
    private Texture ball2Texture;
    private Texture courtTexture;
    private Texture doramiTexture;
    private Texture pointerTexture;
    private Texture tras;
    private Texture frente;
    
    private TextureRegion[][] quad0;
    private TextureRegion[][] quad1;
    
    private Animation ahead;
    private Animation back;
    private Animation actual;
    
    private boolean withBall;
    private boolean shooting;
    private boolean failing;
    private boolean withoutBall;
    private boolean forward;
    
    private float barPositionX;
    private float targetPositionX;
    private float selectorPositionX;
    private float selectorPositionY;
    private float selectorSpeed;
    private float barLimit;
    private float targetLimit;
    private float targetScaleX;
    private float a,fall=3;
    private final float basketPositionX = viewport.getWorldWidth()*0.795f;
    
    private int cont=0;

    public basCATball(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 50f,/*Tempo maximo da fase*/
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }
    
    @Override
    protected void onStart(){
        
        withoutBall=true;
        withBall = false;
        shooting = false;
        
        courtTexture = assets.get("bascatball/court.png",Texture.class);      
        ballTexture = assets.get("bascatball/ball.png",Texture.class);
        playerTexture = assets.get("bascatball/player.png",Texture.class);
        barTexture = assets.get("bascatball/bar.png",Texture.class);
        targetTexture = assets.get("bascatball/target.png",Texture.class);
        selectorTexture = assets.get("bascatball/selector.png",Texture.class);
        ball2Texture = assets.get("bascatball/ball2.png",Texture.class);
        doramiTexture = assets.get("bascatball/dorami.png",Texture.class);
        pointerTexture = assets.get("bascatball/pointer.png",Texture.class);
        tras = assets.get("bascatball/back.png",Texture.class);
        frente = assets.get("bascatball/ahead.png",Texture.class);
        
        quad0 = TextureRegion.split(frente,frente.getWidth()/4,frente.getHeight()/2);
        ahead = new Animation(0.1f,
                quad0[0][0],
                quad0[0][1],
                quad0[0][2],
                quad0[0][3],
                quad0[1][0],
                quad0[1][1]
        );
        ahead.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        quad1 = TextureRegion.split(tras,tras.getWidth()/4,tras.getHeight()/2);
        back = new Animation(0.1f,
                quad1[0][0],
                quad1[0][1],
                quad1[0][2],
                quad1[0][3],
                quad1[1][2],
                quad1[1][3]
        );
        back.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        pointer = new Sprite(pointerTexture);
        pointer.setScale(0.08f);
        pointer.setOrigin(0,0);
        pointer.setPosition(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
        
        court = new Sprite(courtTexture);
        court.setOrigin(0,0);
        court.setScale(viewport.getWorldWidth()/court.getWidth(), viewport.getWorldHeight()/court.getHeight());
        court.setPosition(0,0);
        
        dorami = new Sprite(doramiTexture);
        dorami.setOrigin(0,0);
        dorami.setScale(0.3f);
        dorami.setPosition(basketPositionX, viewport.getWorldHeight()*1.3f);
        
        player = new Sprite(playerTexture);
        player.setScale(0.2f);
        player.setOrigin(0,0); //origem se torna canto inferior esquerdo
        player.setPosition(viewport.getWorldWidth()*0.2f,viewport.getWorldHeight()*0.25f);
        
        ball = new Sprite(ballTexture);
        ball.setScale(0.08f);
        ball.setOrigin(0,0);
        ball.setPosition((viewport.getWorldWidth()*(0.05f+rand.nextFloat()*0.4f)),
            viewport.getWorldHeight()-viewport.getWorldHeight()*0.1f);
        
        bar = new Sprite(barTexture);
        bar.setScale(0.8f,0.3f);
        bar.setOrigin(0,0);
        barPositionX = viewport.getWorldWidth()/2-(bar.getWidth()/2)*bar.getScaleX();
        bar.setPosition(barPositionX, viewport.getWorldHeight()*0.75f);
        
        target = new Sprite(targetTexture);
        target.setScale(targetScaleX,0.35f);
        target.setOrigin(0, 0);
        targetPositionX = viewport.getWorldWidth()/2-(target.getWidth()/2)*target.getScaleX();
        target.setPosition(targetPositionX, viewport.getWorldHeight()*0.75f);
        
        selector = new Sprite(selectorTexture);
        selector.setScale(0.8f,0.8f);
        selector.setOrigin(0,0);
        selectorPositionX = barPositionX-viewport.getWorldWidth()/150;
        selectorPositionY = bar.getY()+bar.getHeight()/2*bar.getScaleY()-selector.getHeight()/2*selector.getScaleY();
        selector.setPosition(selectorPositionX, selectorPositionY);
        
        ball2 = new Sprite(ball2Texture);
        ball2.setScale(0.053f);
        ball2.setOrigin(0,0);
        
        barLimit = bar.getX()+bar.getWidth()*bar.getScaleX();
        targetLimit = target.getX()+target.getWidth()*target.getScaleX();
        
    }
    
    float eq(float posicaox){
        return player.getY()+player.getHeight()*player.getScaleY()+a*posicaox; 
    }
    
      @Override
    protected void configureDifficultyParameters(float difficulty) {
        
        selectorSpeed=DifficultyCurve. LINEAR.getCurveValueBetween(difficulty, viewport.getWorldWidth()/120f, viewport.getWorldWidth()/50f);
        targetScaleX=DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty,0.02f , 0.3f);
        
    }
    
    @Override
    public void onHandlePlayingInput() {
        Vector2 click = new Vector2 (Gdx.input.getX(), Gdx.input.getY());   
        viewport.unproject(click);
        pointer.setPosition(click.x-pointer.getWidth()/2*pointer.getScaleX(),click.y-pointer.getHeight()/2*pointer.getScaleY());
        
        if(Gdx.input.isTouched() && withoutBall){
            if(Gdx.input.getX()>=viewport.getWorldWidth()/2){
                    player.setX(player.getX()+6);
                }
                else{
                    player.setX(player.getX()-6);
                }
        }
        if(Gdx.input.justTouched()){
            if(withBall){
                if(selector.getBoundingRectangle().overlaps(target.getBoundingRectangle())){
                    shooting=true;
                    withBall=false;
                    withoutBall=false;
                }
                else{
                    withBall=false;
                    withoutBall=false;
                    failing=true;
                }
            }
        }
    }
    
    @Override
    public void onUpdate(float dt) {
        

        if(ball.getBoundingRectangle().overlaps(player.getBoundingRectangle()) && !shooting){
            withBall = true;
            withoutBall = false;
           
            a=((viewport.getWorldHeight()*6/10)-viewport.getWorldHeight()*0.25f)/(viewport.getWorldWidth()-player.getX()+player.getWidth()/2*player.getScaleX());
        }
        
        if((ball.getY()+ball.getHeight()*ball.getScaleY())<0) //Se não conseguiu nem pegar a bola perde o desafio
            super.challengeFailed();
            
        if(withoutBall){
            ball.setPosition(ball.getX(), ball.getY()-3);
        }
        if(withBall){
            ball2.setPosition(player.getX()+player.getWidth()/2*player.getScaleX(), player.getY()+player.getHeight()*player.getScaleY());
            
            if(selector.getX()<=bar.getX()){
                forward=true;
            }
            else if(selector.getX()>=barLimit){
                forward=false;
            }
            if(forward){
                selector.setX(selector.getX()+selectorSpeed);
            }
            else{
                selector.setX(selector.getX()-selectorSpeed);
            }
        }
        
        if(shooting){
            
            if(ball2.getX()+ball2.getWidth()/2*ball2.getScaleX()<=basketPositionX){
                ball2.setX(ball2.getX()+viewport.getWorldWidth()/150);
                ball2.setY(eq(ball2.getX()));
            }
            else{
                cont++;
                if(cont>30)
                    dorami.setY(dorami.getY()-fall);
                if(dorami.getBoundingRectangle().overlaps(ball2.getBoundingRectangle()))
                    ball2.setY(ball2.getY()-fall);           
                if(cont>80)
                    super.challengeSolved();
                fall+=0.2;
            }
        }
        
    }
    
     
    @Override
    public String getInstructions(){
        return "Pegue a bola e faça a cesta";
    }
    
    @Override
    public void onDrawGame() {
        
        court.draw(batch);
        player.draw(batch);
        pointer.draw(batch);
        if(withoutBall){
            ball.draw(batch);
        }
        else if(withBall){
            bar.draw(batch);
            target.draw(batch);
            selector.draw(batch);
            ball2.draw(batch);
        }
        else if(shooting){
            ball2.draw(batch);
            dorami.draw(batch);
        }
    }
    
    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
}
