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
    
    private Texture ballTexture;
    private Texture playerTexture;
    private Texture basketTexture;
    private Texture barTexture;
    private Texture targetTexture;
    private Texture selectorTexture;
    
    private World planet;
    private BodyDef bdef;
    private Body body;
    
    private boolean withBall;
    private boolean shooting;
    
    private float barPosition;
    private float targetPosition;

    public basCATball(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 50f,/*Tempo maximo da fase*/
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }
   
    @Override
    protected void onStart(){
        
        withBall = false;
        shooting = false;
        
       // planet = new World(new Vector2(0,-98),true);
        
        ballTexture = assets.get("bascatball/ball.png",Texture.class);
        playerTexture = assets.get("bascatball/player.png",Texture.class);
        basketTexture = assets.get("bascatball/basket.png",Texture.class);
        barTexture = assets.get("bascatball/bar.png",Texture.class);
        targetTexture = assets.get("bascatball/target.png",Texture.class);
        selectorTexture = assets.get("bascatball/selector.png",Texture.class);
        
    
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
        ball.setPosition((viewport.getWorldWidth()*(0.1f+rand.nextFloat()*0.5f)),
            viewport.getWorldHeight()-viewport.getWorldHeight()*0.1f);
        
        bar = new Sprite(barTexture);
        bar.setScale(0.8f,0.3f);
        bar.setOrigin(0,0);
        barPosition = viewport.getWorldWidth()/2-(bar.getWidth()/2)*bar.getScaleX();
        bar.setPosition(viewport.getWorldWidth()*0.2f, viewport.getWorldHeight()*0.75f);
        
        target = new Sprite(targetTexture);
        target.setScale(0.5f,0.35f);
        target.setOrigin(0, 0);
        targetPosition = viewport.getWorldWidth()/2-(target.getWidth()/2)*target.getScaleX();
        target.setPosition(targetPosition, viewport.getWorldHeight()*0.75f);
        
        
        /*
            PARTE IMPOSSIVEL DE FISICA DA LIB QUE N FUNCIONA DIREITO
        
            Cria uma definição de corpo dinâmico
            e o coloca nas mesmas posições que a sprite da bola
        
        
        bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(ball.getX(), ball.getY());
        
        /*
            Cria um corpo com as definições acima 
            e o cria no mundo
        
        
        body = planet.createBody(bdef);
        
        /*
            Cria e adiciona uma forma ao corpo
        
        PolygonShape shape = new PolygonShape();
        shape.setRadius(ball.getWidth()/2);
        
        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.density = 3000f;
        
        shape.dispose();
        */
    }
    
      @Override
    protected void configureDifficultyParameters(float difficulty) {
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
    
    }
    
    @Override
    public void onUpdate(float dt) {
        
        /*ATUALIZA TODO O TEMPO, DESENVOLVER A LÓGICA AQUI*/

        /*planet.step(dt, 6, 2);
        if(body.getPosition().y > viewport.getWorldHeight()*0.05f)
            ball.setPosition(body.getPosition().x, body.getPosition().y);
        */
        if(!withBall)
            ball.setPosition(ball.getX(), ball.getY()-4);
        
        if((ball.getY()+ball.getHeight()*ball.getScaleY())<0) //Se não conseguiu nem pegar a bola perde o desafio
            super.challengeFailed();
            
        if(!withBall){
            /*
                Jogador so se move se estiver sem a bola
            */
            if((Gdx.input.isKeyPressed(Input.Keys.A)||Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    &&player.getX()>viewport.getWorldWidth()*0.1){
                    player.setX(player.getX()-viewport.getWorldWidth()/60);
            }
            if((Gdx.input.isKeyPressed(Input.Keys.D)||Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    &&player.getX()<viewport.getWorldWidth()*0.6){
                    player.setX(player.getX()+viewport.getWorldWidth()/60);
            }
        }
        
        if(ball.getBoundingRectangle().overlaps(player.getBoundingRectangle())){
            withBall = true;
            shooting = true;
        }
      
        
    }
    
     
    @Override
    public String getInstructions(){
        return "Pegue a bola e faça a cesta";
    }
    
    @Override
    public void onDrawGame() {

        if(!withBall)
            ball.draw(batch);
        player.draw(batch);
        basket.draw(batch);
        if(shooting){
            bar.draw(batch);
            target.draw(batch);
           // selector.draw(batch);
        }
            
    }
    
    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
}
