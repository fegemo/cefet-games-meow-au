package br.cefetmg.games.minigames;

import static br.cefetmg.games.Config.WORLD_HEIGHT;
import static br.cefetmg.games.Config.WORLD_WIDTH;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.sound.MySound;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Random;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author
 */

public class MouseAttack extends MiniGame {

    private static Cat2 cat;
    private Monster monster;

    private Array<Monster> enemies;
    private Array<Projetil> projectiles;
    private Sprite target;

    private Texture monsterTexture;
    private Texture catTexture;
    private Texture targetTexture;
    private Texture projectileTexture;
    private Texture background;

    private int enemiesKilled;

    private MySound shootSound;
    private MySound monsterDieSound;

    private int numberOfEnemies;

    private boolean drawProj = false;
    public boolean animateCat = false;
    
    private static final Vector2 CAT_POSITION = new Vector2(100, 100);
    private static final Vector2 BALL_ORIGIN_POSITION = new Vector2(CAT_POSITION)
            .add(15, 0);

    public MouseAttack(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {

        enemies = new Array<Monster>();
        projectiles = new Array<Projetil>();

        monsterTexture = assets.get(
                "mouse-attack/sprite-monster.png", Texture.class);
        background = assets.get(
                "mouse-attack/bg_grass.png", Texture.class);
        catTexture = assets.get(
                "mouse-attack/sprite-cat.png", Texture.class);
        targetTexture = assets.get(
                "mouse-attack/target.png", Texture.class);
        projectileTexture = assets.get(
                "mouse-attack/projetil.png", Texture.class);
        shootSound = new MySound(assets.get(
                "mouse-attack/shoot-sound.mp3", Sound.class));
        monsterDieSound = new MySound(assets.get(
                 "mouse-attack/monster-dying.mp3", Sound.class));

        target = new Sprite(targetTexture);
        target.setOriginCenter();

        cat = new Cat2(catTexture);
        cat.setScale(2);

        enemiesKilled = 0;

        for (int i = 0; i < numberOfEnemies; i++) {
            spawnEnemy();
        }
    }

    int mul = 1;

    private void spawnEnemy() {

        Vector2 position = new Vector2(rand.nextFloat() - mul * (float) 0.2, rand.nextFloat());
        mul = mul * (-1);
        monster = new Monster(monsterTexture);
        rand.nextInt((int)viewport.getWorldWidth());
        
        Random r = new Random();
        position.x  = (int) (r.nextInt((int) ((int)viewport.getWorldWidth()-110)) + 50);
        position.y  = (int) (r.nextInt((int) ((int)viewport.getWorldHeight()-110)) + 100);

        
        monster.setScale(2);
        monster.setPosition(Math.abs(position.x), position.y);
        enemies.add(monster);

    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.numberOfEnemies = (int) (10*difficulty + 5);
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        cat.setCenter(CAT_POSITION.x, CAT_POSITION.y);
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        this.target.setPosition(click.x - this.target.getWidth() / 2,
                click.y - this.target.getHeight() / 2);

        // verifica se matou um inimigo
        if (Gdx.input.justTouched()) {
            Projetil projetil = new Projetil(projectileTexture);
            projetil.setPosition(BALL_ORIGIN_POSITION.x, BALL_ORIGIN_POSITION.y);
            projetil.shoot(click.x, click.y);
            projectiles.add(projetil);

            drawProj = true;
            animateCat = true;

            shootSound.play();
        }

    }

    @Override
    public void onUpdate(float dt) {
        

        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).update(dt);

            if (enemies.get(i).getMorto()) {
                monsterDieSound.play();
                this.enemiesKilled++;
                this.enemies.removeValue(enemies.get(i), true);
                if (this.enemiesKilled >= this.numberOfEnemies) {
                    super.challengeSolved();
                }
            }

        }

        if (drawProj) {
            for (int i = 0; i < projectiles.size; i++) {
                projectiles.get(i).update(dt);
            }
            
            for (int i = 0; i < enemies.size; i++) {
                Monster m = enemies.get(i);
                for (int j = 0; j < projectiles.size; j++) {
                    if (m.getBoundingRectangle().overlaps(
                            projectiles.get(j).getBoundingRectangle())) {

                        m.changeAnimation();
                        m.setMorto(true);
                        break;
                    }
                }

            }
        }
        cat.update(dt);
        if(animateCat){            
            cat.animate= true;
            animateCat=false;
        }
    }

    @Override
    public String getInstructions() {
        return "Mate os monstros!";
    }

    @Override
    public void onDrawGame() {

        batch.draw(background, 0, 0, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        
        for (int i = 0; i < enemies.size; i++) {
            Monster m = enemies.get(i);
            m.draw(batch);
        }
        
        if (drawProj) {
            for (int i = 0; i < projectiles.size; i++) {
                projectiles.get(i).draw(batch);
            }
        }
        cat.draw(batch);
        target.draw(batch);
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
    // <editor-fold desc="Classes internas da MouseAttack" defaultstate="collapsed">

    static class Cat2 extends AnimatedSprite {

        static final int FRAME_WIDTH = 50;
        static final int FRAME_HEIGHT = 50;
        TextureRegion[][] quadrosDaAnimacao;
        Texture spriteSheet;
        float sx;
        float sy;
        public boolean animate = false;

        float tempoDaAnimacao;

        Animation power;
        Animation socar;
        Animation chutar;
        Animation morrer;
        Animation parado;

        int x = 0;

        public Cat2(final Texture cat) {

            super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            cat, 50, 50);
                    super.addAll(new TextureRegion[]{
                        frames[4][0]
                    });
                }
            }));

            quadrosDaAnimacao = TextureRegion.split(cat, 50, 50);

            chutar = new Animation(0.1f,
                    quadrosDaAnimacao[4][0],
                    quadrosDaAnimacao[4][1],
                    quadrosDaAnimacao[4][2],
                    quadrosDaAnimacao[4][3],
                    quadrosDaAnimacao[4][4],
                    quadrosDaAnimacao[4][5],
                    quadrosDaAnimacao[4][6],
                    quadrosDaAnimacao[4][7],
                    quadrosDaAnimacao[4][8],
                    quadrosDaAnimacao[4][9]);

            power = new Animation(0.1f,
                    quadrosDaAnimacao[1][0],
                    quadrosDaAnimacao[1][1],
                    quadrosDaAnimacao[1][2],
                    quadrosDaAnimacao[1][3],
                    quadrosDaAnimacao[1][4],
                    quadrosDaAnimacao[1][5]);

            morrer = new Animation(0.1f,
                    quadrosDaAnimacao[3][0],
                    quadrosDaAnimacao[3][1],
                    quadrosDaAnimacao[3][2],
                    quadrosDaAnimacao[3][3]);

            parado = new Animation(0.1f,
                    quadrosDaAnimacao[0][0]);

            socar = new Animation(0.1f,
                    quadrosDaAnimacao[5][5],
                    quadrosDaAnimacao[5][6],
                    quadrosDaAnimacao[5][7],
                    quadrosDaAnimacao[5][8],
                    quadrosDaAnimacao[5][9]);

        }

        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }

        public void changeAnimation() {
            this.setAnimation(power);
            this.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
            

        }

        @Override
        public void update() {

            tempoDaAnimacao += Gdx.graphics.getDeltaTime();
            if(animate)
                changeAnimation();
            
            if(isAnimationFinished()&& animate==true){
                animate=false;
                this.setAnimation(parado);
                this.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
                
            }
        }
    }

    static class Projetil extends AnimatedSprite{

        TextureRegion[][] quadrosDaAnimacao;
        public float maxVelocity = 300;
        public Vector2 velocity = new Vector2();
        float targetX;
        float targetY;
        Texture texture;
        public Sprite projeSprite;
        Animation shoot;

        public Projetil(final Texture texture) {
            
            super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            texture, 16, 19);
                    super.addAll(new TextureRegion[]{
                        frames[0][0]
                    });
                }
            }));

            quadrosDaAnimacao = TextureRegion.split(texture, 16, 19);

            shoot = new Animation(0.1f,
                    quadrosDaAnimacao[0][0],
                    quadrosDaAnimacao[0][1],
                    quadrosDaAnimacao[0][2]);
            
            shoot.setPlayMode(Animation.PlayMode.LOOP);
            this.setAnimation(shoot);
        }

        public void shoot(float targetX, float targetY) {
            velocity.set(targetX - getX(), targetY - getY())
                    .nor()
                    .scl(maxVelocity);
        }

        @Override
        public void update(float dt) {
            super.update(dt);
            super.setPosition(getX() + velocity.x * dt, getY() + velocity.y * dt);
        }
    }

    static class Monster extends AnimatedSprite {

        static final int FRAME_WIDTH = 35;
        static final int FRAME_HEIGHT = 35;
        TextureRegion[][] quadrosDaAnimacao;
        Texture spriteSheet;
        float sx;
        float sy;

        float tempoDaAnimacao;

        Animation morrendo;
        Animation parado;
        boolean morto = false;

        int x = 0;

        public Monster(final Texture monster) {

            super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            monster, 35, 35);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],
                        frames[0][4]

                    });
                }
            }));

            quadrosDaAnimacao = TextureRegion.split(monster, 35, 35);

            parado = new Animation(0.1f,
                    quadrosDaAnimacao[0][0],
                    quadrosDaAnimacao[0][1],
                    quadrosDaAnimacao[0][2],
                    quadrosDaAnimacao[0][3],
                    quadrosDaAnimacao[0][4]);


            super.setAnimation(parado);
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        }

        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }

        public void changeAnimation() {
            //super.setAnimation(morrendo);
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        }

        public void setMorto(boolean morto) {
            this.morto = morto;
        }

        public boolean getMorto() {
            return this.morto;
        }

        @Override
        public void update() {
            tempoDaAnimacao += Gdx.graphics.getDeltaTime();

        }
    }
    
    // </editor-fold>

}
