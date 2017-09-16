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

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class FlappySita extends MiniGame {

    private Calopsita calopsita;
    private Tube cattube;
    private Texture calopsitaTextura;
    private Texture tubeTexture;
    private Texture cattubeTexture;
    private Texture bg1;
    private Array<Tube> enemies;

    // variáveis do desafio - variam com a dificuldade do minigame
    private int spawnedEnemies;
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private float spawnInterval;
    private float ScreenWidth;
    private float ScreenHeight;
    private float posX,posY;
    private int totalEnemies;
    int  srcX,troca;
    float interval;
    public FlappySita(BaseScreen screen,
                      MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        troca=0;
        calopsitaTextura = assets.get("FlappySita/crow.png",Texture.class);
        cattubeTexture = assets.get("FlappySita/tubecat.png",Texture.class);
        bg1= assets.get("FlappySita/background.png",Texture.class);
        bg1.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        tubeTexture = assets.get("FlappySita/tube.png",Texture.class);
        calopsita= new Calopsita(calopsitaTextura);
      //  calopsita.setPosition(viewport.getScreenWidth()*0.4f,viewport.getScreenHeight()*0.5f);
        ScreenHeight = Gdx.graphics.getHeight();
        ScreenWidth = Gdx.graphics.getWidth();


        enemies = new Array<Tube>();

        spawnedEnemies = 0;
        posX=viewport.getScreenWidth()*0.4f;posY= viewport.getScreenHeight()*0.5f;
      /*  timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnEnemy();
            }

        }, 0,  (float)Math.random()+1f);//this.spawnInterval);*/
        spawnEnemy();
         srcX=0; 
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
       // enemy.setScale(0.4f);
        enemy.setsize(new Random().nextInt(4));
        enemy.setsize(0);
        enemy.setPosition(Gdx.graphics.getWidth()+viewport.getScreenWidth()*0.41f,60*enemy.getsize());//, Ddwown);
        enemy.setSpeed(tartarusSpeed);
        enemies.add(enemy);

        // toca um efeito sonoro
       /* Sound sound = tartarusAppearingSound.random();
        long id = sound.play(0.5f);
        sound.setPan(id, tartarusPosition.x < viewport.getWorldWidth()
                ? -1 : 1, 1);*/
    }

  

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 120, 220);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 240, 340);
        this.spawnInterval = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.25f, 1.5f);

        this.totalEnemies = (int)  Math.ceil(DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 0, 2)) + 1;

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
            float distance = calopsita.getHeadDistanceTo(
                    tubes.getX(), tubes.getY());
            System.out.printf("D%.2f\n", distance);
            if (distance <= 30) {
               // tubes.startFleeing(calopsita.getHeadPosition());
                super.challengeFailed();
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        calopsita.update(dt);
     //   System.out.printf("x=%.2f,Y=%.2f,Speed=%.2f\n",posX,posY,0.0);
        srcX+=5;
        if(posY<ScreenHeight+2)
            posY+=1;//2.5; 1;
        if(posX>ScreenWidth/2-16)
            posX-=1;
        if(Gdx.input.justTouched()) {
          //  System.out.printf("Heeee\n");
            posY-=70;
            posX+=2;
        }
        // atualiza a escova (quadro da animação)
        //toothBrush.update(dt);

        // atualizbea os inimigos (quadro de animação + colisão com dentes)
        for (int i = 0; i < this.enemies.size; i++){
            Tube tube = this.enemies.get(i);
            tube.setPosition(tube.getX()-5,tube.getY());
        }
        //tube.update(dt);
            // verifica se este inimigo está colidindo com algum dente
             /*for (Tooth tooth : this.teeth) {
                if (tart.getBoundingRectangle()
                        .overlaps(tooth.getBoundingRectangle())) {
                    toothWasHurt(tooth, tart);
                }
            }*/
       // }
    }

    @Override
    public void onDrawGame() {
      /* // for (Tooth tooth : this.teeth) {
            tooth.draw(batch);
        */
        batch.draw(bg1,0, 0, srcX, 0, (int)(viewport.getScreenWidth()+viewport.getScreenWidth()*0.41f),
                (int)(viewport.getScreenHeight()*viewport.getScreenHeight()*0.35));

        for (Tube tubes : this.enemies) {
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
        static final int FRAME_WIDTH =97;
        static final int FRAME_HEIGHT = 120;
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

      public Tube(final Texture tubesSpritesheet) {
          super(new HashMap<String, Animation>() {
              {
                  TextureRegion[][] frames = TextureRegion
                          .split(tubesSpritesheet,
                                  FRAME_WIDTH, FRAME_HEIGHT);
                  Animation walking = new Animation(0.2f,
                          frames[0][0]);
                  walking.setPlayMode(Animation.PlayMode.LOOP);
                  put("walking", walking);
              }
          }, "walking");
      }

      @Override
      public void update(float dt) {
          System.out.printf ("X%.2f Sx %.2f Y%.2f SY%.2f dy%.2f",super.getX() + this.speed.x,super.getY() , this.speed.y,dt );

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

      public void startFleeing(Vector2 from) {
          if (this.isFleeing) {
              return;
          }
          this.isFleeing = true;
          Vector2 position = new Vector2(super.getX(), super.getY());
          this.speed = position.sub(from).nor().scl(maximumEnemySpeed);
          this.setColor(Color.YELLOW);
      }
  }



}
