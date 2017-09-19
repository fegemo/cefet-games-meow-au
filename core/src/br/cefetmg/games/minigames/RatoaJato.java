package br.cefetmg.games.minigames;

import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSorter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import java.util.HashMap;
import java.util.Random;

import static br.cefetmg.games.Config.WORLD_HEIGHT;
import static br.cefetmg.games.Config.WORLD_WIDTH;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class RatoaJato extends MiniGame {

    private Calopsita calopsita;
    private Tube cattube;
    private Texture calopsitaTextura;
    private Texture tubeTexture;
    private Texture cattubeTexture;
    private Texture bg1;
    private Array<Tube> enemies;

    // variáveis do desafio - variam com a dificuldade do minigame
    private float speed;
    private float minimumEnemySpeed;
    private float ScreenWidth;
    private float ScreenHeight;
    private float posX,posY;
    private int totalEnemies;
    int  srcX,troca;
    float aceleracao,velocidade;
    private Sound meon;

    public RatoaJato(BaseScreen screen,
                     MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        troca=0;
        calopsitaTextura = assets.get("RatoaJato/jatmouse.png",Texture.class);
        cattubeTexture = assets.get("RatoaJato/tubecat.png",Texture.class);
        bg1= assets.get("RatoaJato/background.png",Texture.class);
        bg1.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        tubeTexture = assets.get("RatoaJato/tube.png",Texture.class);
        meon=assets.get("RatoaJato/meon.mp3", Sound.class);
        calopsita= new Calopsita(calopsitaTextura);
        calopsita.setScale(0.5f);
        ScreenHeight = Gdx.graphics.getHeight();
        ScreenWidth = Gdx.graphics.getWidth();


        enemies = new Array<Tube>();

        posX=viewport.getScreenWidth()*0.4f;posY= viewport.getScreenHeight()*0.5f;
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnEnemy();
            }

        }, 0,  (float)Math.random()+0.7f);//this.spawnInterval);*/
         srcX=0;
         velocidade=-1*WORLD_HEIGHT*0.0005f;;
         long id = meon.play(0.2f);
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                meon.stop();
            }

        },9.5f,10f);

    }
    private void spawnEnemy() {

        float Ddwown =(float) Math.random()*viewport.getScreenHeight()*0.3f;
        float Dup =(float) Math.random()*viewport.getScreenHeight()*0.3f+viewport.getScreenHeight()*0.70f;

        Vector2 goalCenter = new Vector2();
        Vector2 tartarusGoal = new Vector2(-ScreenWidth,Ddwown);
        Vector2 tartarusPosition = new Vector2();
        boolean appearFromSides = MathUtils.randomBoolean();

         Vector2 tartarusSpeed = tartarusGoal
                .sub(tartarusPosition)
                .nor()

                .scl(this.minimumEnemySpeed);
        Tube enemy = new Tube(cattubeTexture);
        enemy.setsize(new Random().nextInt(4));

        enemy.setPosition(WORLD_WIDTH,60*enemy.getsize());//, Ddwown);
        enemy.setSpeed(tartarusSpeed);
        enemies.add(enemy);




    }

  

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.speed= DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 120, 220);

        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty,120, 220);
    }

    @Override
    public void onHandlePlayingInput() {

        // atualiza a posição do alvo de acordo com o mouse

        Vector3 Posi;
        Posi = new Vector3(posX , posY, 0);
        viewport.unproject(Posi);
        //  toothBrush.setCenter(click.x, click.y);
        calopsita.setCenter(Posi.x, Posi.y);
        for (Tube tubes : this.enemies) {
            if(calopsita.getY()+70<=tubes.getHeight()+tubes.getsize()*60&&
                    (calopsita.getX()>tubes.getX()-80&&calopsita.getX()<tubes.getX()+80)){

                super.challengeFailed();
                meon.stop();
                tubes.changePicture();
            }
            //System.out.printf("c %.2f %.2f Tube %.2f %.2f\n",calopsita.getX(),
              //      calopsita.getY(),tubes.getX(),tubes.getHeight());
        }

    }

    @Override
    public void onUpdate(float dt) {

        calopsita.update(dt);
     //   System.out.printf("x=%.2f,Y=%.2f,Speed=%.2f\n",posX,posY,0.0);
        srcX+=5;
        if(aceleracao>-1*WORLD_HEIGHT*0.0001f);
        aceleracao-=WORLD_HEIGHT*0.00005f; //gravidade

        if(posY<ScreenHeight+2)
            posY-=velocidade+aceleracao;//2.5; 1;
        if(posX>ScreenWidth/2-16)
            posX-=0.5;
        if(Gdx.input.justTouched()) {
            //  System.out.printf("Heeee\n");
            aceleracao+=WORLD_HEIGHT*0.002f;
            posY-=WORLD_HEIGHT*0.09;
            posX+=2;
        }
        // atualiza a escova (quadro da animação)
        //toothBrush.update(dt);

        // atualizbea os inimigos (quadro de animação + colisão com dentes)
        for (int i = 0; i < this.enemies.size; i++){
            Tube tube = this.enemies.get(i);
            tube.setPosition(tube.getX()-5,tube.getY());
            tube.changePicture();
        }
        if(calopsita.getY()+calopsita.getHeight()/2>WORLD_HEIGHT) {
            //tubes.changePicture();
            super.challengeFailed();
            meon.stop();


        }
    }

    @Override
    public void onDrawGame() {
      /* // for (Tooth tooth : this.teeth) {
            tooth.draw(batch);
        */
        batch.draw(bg1,0, 0, srcX, 0,WORLD_WIDTH,WORLD_HEIGHT);/* (int)(viewport.getScreenWidth()+viewport.getScreenWidth()*0.41f),
                (int)(viewport.getScreenHeight()*viewport.getScreenHeight()*0.35));*/

        for (Tube tubes : this.enemies){
          tubes.draw(batch);
            for (int i=0;i<tubes.getsize();i++){
                batch.draw(tubeTexture,tubes.getX(),60*i);
            }

        }


        //toothBrush.draw(batch);
        calopsita.draw(batch);
    }

    @Override
    public String getInstructions() {
        return "Voe!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
    class Calopsita extends AnimatedSprite{
        static final int FRAME_WIDTH =131;//131
        static final int FRAME_HEIGHT = 156;//156
        public Calopsita(final Texture toothbrushTexture) {
             super(new Animation(0.22f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            toothbrushTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],

                       
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
        }

       
         Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() ,
                    this.getY() + this.getHeight() );
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }
    }
    


  class Tube extends MultiAnimatedSprite {

      private Vector2 speed;
      private boolean isFleeing = false;

      static final int FRAME_WIDTH = 220;
      static final int FRAME_HEIGHT = 390;
      int size;
      Texture temp;
      Animation a;
      public Tube(final Texture tubesSpritesheet) {

          super(new HashMap<String, Animation>() {
              {
                  TextureRegion[][] frames = TextureRegion
                          .split(tubesSpritesheet,
                                  FRAME_WIDTH, FRAME_HEIGHT);
                  Animation sleep= new Animation(0.1f,
                          frames[0][0]);
                  Animation acordado= new Animation(0.1f,
                          frames[0][1]);
                  sleep.setPlayMode(Animation.PlayMode.NORMAL);
                  put("walking", sleep);
                  put("acordado", acordado);

              }
          }, "walking");

      }
        public void changePicture(){
            this.startAnimation("acordado");
        }
      @Override
      public void update(float dt) {

          super.update(dt);
          super.setPosition(super.getX() + this.speed.x * dt,
                  super.getY() + this.speed.y * dt);
      }
      public void setsize(int siz){
          this.size=siz;
      }
      public int getsize(){return size;}


      public Vector2 getSpeed() {
          return speed;
      }

      public void setSpeed(Vector2 speed) {
          this.speed = speed;
      }


  }



}
