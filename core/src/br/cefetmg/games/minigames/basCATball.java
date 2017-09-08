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
import com.badlogic.gdx.physics.box2d.*;
/**
 *
 * @author Rógenes
 */
public class basCATball extends MiniGame{
    
    private Sprite ball;
    private Sprite player;
    private Sprite basket;
    private Sprite bar;
    private Sprite target;
    private Sprite selector;
    private Sprite ball2;
    
    private Texture ballTexture;
    private Texture playerTexture;
    private Texture basketTexture;
    private Texture barTexture;
    private Texture targetTexture;
    private Texture selectorTexture;
    private Texture ball2Texture;
    
    private boolean withBall;
    private boolean shooting;
    private boolean failing;
    private boolean withoutBall;
    
    private float barPositionX;
    private float targetPositionX;
    private float selectorPositionX;
    private float selectorPositionY;
    
    private float selectorSpeed;
    
    private float barLimit;
    private float targetLimit;
    
    private boolean forward;
    
    private float parameterV;
    
    private float targetScaleX;
    
    private float a,dy,dx,fall=3;
    private int cont=0;

    public basCATball(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 50f,/*Tempo maximo da fase*/
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }
    
    @Override
    protected void onStart(){
        
        targetScaleX = 0.5f;
        parameterV=150;
        withoutBall=true;
        withBall = false;
        shooting = false;
        
        
        ballTexture = assets.get("bascatball/ball.png",Texture.class);
        playerTexture = assets.get("bascatball/player.png",Texture.class);
        basketTexture = assets.get("bascatball/basket.png",Texture.class);
        barTexture = assets.get("bascatball/bar.png",Texture.class);
        targetTexture = assets.get("bascatball/target.png",Texture.class);
        selectorTexture = assets.get("bascatball/selector.png",Texture.class);
        ball2Texture = assets.get("bascatball/ball2.png",Texture.class);
        
        
    
        player = new Sprite(playerTexture);
        player.setScale(0.2f);
        player.setOrigin(0,0); //origem se torna canto inferior esquerdo
        player.setPosition(viewport.getWorldWidth()*0.2f,viewport.getWorldHeight()*0.1f);
        
        basket = new Sprite(basketTexture);
        basket.setScale(0.4f);
        basket.setOrigin(0,0);
        basket.setPosition(viewport.getWorldWidth()*0.9f,viewport.getWorldHeight()*0.1f);
        
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
        selectorPositionX = barPositionX-viewport.getWorldWidth()/parameterV;
        selectorPositionY = bar.getY()+bar.getHeight()/2*bar.getScaleY()-selector.getHeight()/2*selector.getScaleY();
        selector.setPosition(selectorPositionX, selectorPositionY);
        
        ball2 = new Sprite(ball2Texture);
        ball2.setScale(0.053f);
        ball2.setOrigin(0,0);
        
        selectorSpeed = viewport.getWorldWidth()/parameterV;
        
        barLimit = bar.getX()+bar.getWidth()*bar.getScaleX();
        targetLimit = target.getX()+target.getWidth()*target.getScaleX();
        
    }
    
    float eq(float posicaox){
        return player.getY()+player.getHeight()*player.getScaleY()+a*posicaox; 
    }
    
      @Override
    protected void configureDifficultyParameters(float difficulty) {
        /*
        selectorSpeed=DifficultyCurve. LINEAR.getCurveValueBetween(difficulty, 0.2f, 5.8f);
        targetScaleX=DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty,0.2f , 5.8f);
        */
        
        
        /* Exemplo do shoot the caries
        this.initialEnemyScale = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 1.15f, 0.8f);
        this.minimumEnemyScale = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.15f, 0.4f);
        this.spawnInterval = DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.totalEnemies = (int) Math.ceil(maxDuration / spawnInterval) - 3;
        */
    }
    
    @Override
    public void onHandlePlayingInput() {
        if(withoutBall)
        player.setX(Gdx.input.getX());
        
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
        /*
        if(shooting)
            super.challengeSolved();
        else if(failing)
            super.challengeFailed();*/
    }
    
    @Override
    public void onUpdate(float dt) {
        
        /*ATUALIZA TODO O TEMPO, DESENVOLVER A LÓGICA AQUI*/

        if(ball.getBoundingRectangle().overlaps(player.getBoundingRectangle()) && !shooting){
            withBall = true;
            withoutBall = false;
           // dy=(viewport.getWorldHeight()*3/4-player.getY());
           // dx=(viewport.getWorldWidth()-player.getX());
            //a=dy/dx;
            a=(viewport.getWorldHeight()*5/8)/(viewport.getWorldWidth()-player.getX()+player.getWidth()/2*player.getScaleX());
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
            if(ball2.getX()+ball2.getWidth()/2*ball2.getScaleX()<=basket.getX()+basket.getWidth()/2*basket.getScaleX()-3){
                ball2.setX(ball2.getX()+viewport.getWorldWidth()/150);
                ball2.setY(eq(ball2.getX()));
            }
            else{
                cont++;
                if(cont>50)
                    ball2.setY(ball2.getY()-fall);
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
        
        player.draw(batch);
        basket.draw(batch);
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
        }
            
    }
    
    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
}
