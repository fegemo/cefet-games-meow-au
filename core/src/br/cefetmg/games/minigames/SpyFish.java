/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.HashMap;

/**
 *
 * @author Alberto
 */
public class SpyFish extends MiniGame{
    //texturas
    private Texture texturaFish;
    private Texture texturaCard;
    private Texture texturaFundo;
    private Texture texturaFcontrol;
    private Texture texturaMcontrol;
    
    //elementos de logica
//    private Fish fish;
//    private Control control;
//    private MemoryCard memoryCard;
    
    //elementos de dificuldade
    
    
     public SpyFish(BaseScreen screen,
          MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty,10000,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
//        fish=new Fish(texturaFish);
//        control = new Control();
    }
    @Override
    protected void challengeSolved() {
        super.challengeSolved(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onStart() {
        this.texturaFish = assets.get("spy-fish/fish.png",Texture.class);
        this.texturaCard = assets.get("spy-fish/memory-card.png",Texture.class);
        this.texturaFundo = assets.get("spy-fish/fundo.png",Texture.class);
        this.texturaFcontrol = assets.get("spy-fish/fundo-controle.png",Texture.class);
        this.texturaMcontrol = assets.get("spy-fish/controle-principal.png",Texture.class);
        
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onHandlePlayingInput() {
       /*Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        if(Gdx.input.isTouched())
            control.update(click);
        else
            control.update();
        */// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onUpdate(float dt) {
        /*fish.update(dt);
        memoryCard.update();*/
    }

    @Override
    public void onDrawGame() {
        batch.draw(texturaFish, 0, 0);
        /*fish.draw(batch);
        control.draw(batch);
        memoryCard.draw(batch);*/
    }

    @Override
    public String getInstructions() {
        return "Pegue o cartão de memória";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
/*    class Fish extends MultiAnimatedSprite{

        private Vector2 dposi;
        private int lado;

        static final int FRAME_WIDTH = 28;
        static final int FRAME_HEIGHT = 36;

        public Fish(final Texture fishSprite) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(fishSprite,
                                    FRAME_WIDTH, FRAME_HEIGHT);
                    Animation walking = new Animation(0.2f,
                            frames[0][0],
                            frames[0][1],
                            frames[0][2],
                            frames[0][1]);
                    walking.setPlayMode(Animation.PlayMode.LOOP);
                    put("walking", walking);
                }
            }, "walking");
        }
        @Override
        public void update(float dt){
            super.update(dt);
            float auxX = dposi.x>0?min(Config.WORLD_WIDTH,super.getX()+dposi.x):max(0,super.getX()+dposi.x);
            float auxY = dposi.y>0?min(Config.WORLD_HEIGHT,super.getY()+dposi.y):max(0,super.getY()+dposi.y);
            super.setPosition(auxX, auxY);
            setDposi(0, 0);
        }
        public void setDposi(float x, float y){
            this.dposi.x=x;
            this.dposi.y=y;
        }
    }
    
    class MemoryCard extends Sprite{

        public MemoryCard() {
        }
        public void update(){}
    }
    
    class Control extends Sprite{
        private Texture fundo;
        private Texture meio;
        private Vector2 centro;
        private Vector2 centroMeio;
        
        
        public void update(Vector2 click){
            //verifica se esta pressionando a bolinha de dentro se esta é possivel arrasta-la ate a borda da bola grande
        }
        public void update(){
            centroMeio = centro;
        }
    }*/
}
