package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.graphics.hud.Hud;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author
 */
public class MouseAttack extends MiniGame {

    private Cat2 cat;
    private Monster monster;
    
    //private Array<Sprite> enemies;
    private Array<Monster> enemies;
    private Array<Projetil> projectiles;
    private Sprite target;
    
    private Texture monsterTexture;
    private Texture catTexture;
    private Texture targetTexture;
    private Texture projectileTexture;
    
    private int enemiesKilled;
    private int spawnedEnemies;
    
    private Sound shootSound;
    private Sound monsterDieSound;

    private float initialEnemyScale;
    private float minimumEnemyScale;
    private int totalEnemies;
    private float spawnInterval;
    private int numberOfEnemy;
    private int difficulty = 1; //dificuldades: 0-fácil, 1-médio, 2-difícil
    
    private boolean drawProj=false;

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
        catTexture = assets.get(
                "mouse-attack/sprite-cat.png", Texture.class);
        targetTexture = assets.get(
                "mouse-attack/target.png", Texture.class);
        projectileTexture = assets.get(
                "mouse-attack/projetil.png", Texture.class);
        shootSound = assets.get(
                "mouse-attack/shoot-sound.mp3", Sound.class);
        monsterDieSound = assets.get(
                "mouse-attack/monster-dying.mp3", Sound.class);
        
        target = new Sprite(targetTexture);
        target.setOriginCenter();
        
        cat = new Cat2(catTexture);
        cat.setScale(2);
        //monster = new Monster(monsterTexture);
        
        enemiesKilled = 0;
        spawnedEnemies = 0;
        //scheduleEnemySpawn();
        if(difficulty==0){
            numberOfEnemy=5;
        }
        else if(difficulty==1){
            numberOfEnemy=10;
        }
        else if(difficulty==2){
            numberOfEnemy=20;
        }
        
        for(int i=0; i<numberOfEnemy;i++){
            spawnEnemy();
        }
    }

    private void scheduleEnemySpawn() {
        Task t = new Task() {
            @Override
            public void run() {
                spawnEnemy();
                if (++spawnedEnemies < totalEnemies) {
                    scheduleEnemySpawn();
                }
            }
        };
        // spawnInterval * 15% para mais ou para menos
        float nextSpawnMillis = this.spawnInterval
                * (rand.nextFloat() / 3 + 0.15f);
        timer.scheduleTask(t, nextSpawnMillis);
    }

    int mul = 1;
    private void spawnEnemy() {
        // pega x e y entre 0 e 1
        
        Vector2 position = new Vector2(rand.nextFloat()-mul*(float)0.2, rand.nextFloat());
        mul=mul*(-1);
        // multiplica x e y pela largura e altura da tela
        monster = new Monster(monsterTexture);
        position.scl(
                viewport.getWorldWidth() - monster.getWidth()
                * initialEnemyScale,
                viewport.getWorldHeight()
                - monster.getHeight() * initialEnemyScale);
        

        //Sprite enemy = new Sprite(mouseTexture);
        //monster = new Monster(monsterTexture);
        //enemy.setPosition(position.x, position.y);
        //enemy.setScale(initialEnemyScale);
        monster.setScale(2);
        monster.setPosition(position.x, position.y);
        //monster.setScale(initialEnemyScale);
        enemies.add(monster);

    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.initialEnemyScale = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 1.15f, 0.8f);
        this.minimumEnemyScale = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.15f, 0.4f);
        this.spawnInterval = DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.totalEnemies = (int) Math.ceil(maxDuration / spawnInterval) - 3;
    }

    Vector2 direction;
    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        cat.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight()/ 2f);
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        this.target.setPosition(click.x - this.target.getWidth() / 2,
                click.y - this.target.getHeight() / 2);

        // verifica se matou um inimigo
        if (Gdx.input.justTouched()) {
            direction = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            
            Projetil projetil = new Projetil(projectileTexture);
            projetil.setPosition(viewport.getWorldWidth() / 2f, viewport.getWorldHeight()/ 2f);
          //  if(Gdx.input.getX()<viewport.getWorldWidth() / 2f)
                projetil.shoot(Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY());
          //  else
           //     projetil.shoot2(Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY());
            projectiles.add(projetil);
            
            drawProj=true;
            
            shootSound.play();
        }
        
    }

    @Override
    public void onUpdate(float dt) {
        cat.update(dt);
        cat.update();
        
        for(int i =0; i<enemies.size;i++){
            enemies.get(i).update(dt);
            
            if(enemies.get(i).getMorto()){
                monsterDieSound.play();
                this.enemiesKilled++;
                // remove o inimigo do array
                this.enemies.removeValue(enemies.get(i), true);
                // se tiver matado todos os inimigos, o desafio
                // está resolvido
                if (this.enemiesKilled >= this.numberOfEnemy) {
                    super.challengeSolved();
                }
            }
                
        }
        
        if(drawProj){
            for(int i=0;i<projectiles.size;i++){                
                //projectiles.get(i).setCenter(direction.x-x+5, y);
                projectiles.get(i).update(Gdx.graphics.getDeltaTime());
                
            }
            for (int i = 0; i < enemies.size; i++) {
                //Sprite sprite = enemies.get(i);
                Monster m = enemies.get(i);
                // se há interseção entre o retângulo da sprite e do alvo,
                // o tiro acertou
                for(int j=0; j<projectiles.size;j++){
                    if (m.getBoundingRectangle().overlaps(
                        projectiles.get(j).projeSprite.getBoundingRectangle())) {
                        
                        m.changeAnimation();
                        m.setMorto(true);
                        // contabiliza um inimigo morto
                        /*this.enemiesKilled++;
                        // remove o inimigo do array
                        this.enemies.removeValue(m, true);
                        // se tiver matado todos os inimigos, o desafio
                        // está resolvido
                        if (this.enemiesKilled >= this.numberOfEnemy) {
                            super.challengeSolved();
                        }
                        */
                        // pára de iterar, porque senão o tiro pode pegar em mais
                        // de um inimigo
                        break;
                    }
                }
                
            }
        }
        // vai diminuindo o tamanho das cáries existentes
        /*for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            // diminui só até x% do tamanho da imagem
            if (sprite.getScaleX() > minimumEnemyScale) {
                sprite.setScale(sprite.getScaleX() - 0.3f * dt);
            }
        }*/
    }

    @Override
    public String getInstructions() {
        return "Mate os monstros!";
    }

    @Override
    public void onDrawGame() {

        for (int i = 0; i < enemies.size; i++) {
            //Sprite sprite = enemies.get(i);
            Monster m = enemies.get(i);
            m.draw(batch);
        }
        if (drawProj){
            for(int i=0; i<projectiles.size;i++)
                projectiles.get(i).projeSprite.draw(batch);
        }
        cat.draw(batch);
        target.draw(batch);
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    
    
    /*draw:
    cat.draw(batch);
        if(desenhapro){
            for(int i=0; i<aproj.size;i++){
                aproj.get(i).draw(batch);
                aproj.get(i).setCenter(aproj.get(i).px, aproj.get(i).py);
            }
        }
    
    update:
    cat.update(dt);
        cat.update();
        if(desenhapro)
            projetil.update();
        if(Gdx.input.isButtonPressed(Buttons.LEFT)){
                 Gdx.input.setInputProcessor(new InputAdapter() {
                 public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                     if (button == Buttons.LEFT) {
                        projetil = new Projetil(projetiltex);
                        aproj.add(projetil);
                        desenhapro=true; 
                        posproj = new Vector2(50, 50);
                        return true;
                     }
                     return false;
                 }
             });
        }
        if(desenhapro)
            for(int i=0;i<aproj.size;i++)
                aproj.get(i).update();
    */
    class Cat2 extends AnimatedSprite{
        
        static final int FRAME_WIDTH = 50;
        static final int FRAME_HEIGHT = 50;
        TextureRegion[][] quadrosDaAnimacao;
        Texture spriteSheet;
        float sx;
        float sy;
        
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
        
        public void changeAnimation(){
            
            x++;
            
            if(x%2==0)
                super.setAnimation(power);
            else
                super.setAnimation(parado);
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            
        }
        public void update(){
            
           tempoDaAnimacao += Gdx.graphics.getDeltaTime();
           if(Gdx.input.isButtonPressed(Buttons.LEFT)){
                 Gdx.input.setInputProcessor(new InputAdapter() {
                 public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                     if (button == Buttons.LEFT) {
                        changeAnimation();             
                        return true;
                     }
                     return false;
                 }
             });
           }
        }
    }
    
    public class Projetil {
    
        public float maxVelocity = 450;
        public Vector2 position = new Vector2();
        public Vector2 velocity = new Vector2();
        float targetX;
        float targetY;
        Texture texture;
        public Sprite projeSprite;

        public Projetil(Texture texture){
            this.texture=texture;
            projeSprite = new Sprite(texture);
        }

        public void shoot(float targetX, float targetY){
            //velocity.set(targetX - position.x, targetY - position.y).nor().scl(maxVelocity);

            velocity.set(targetX - position.x, targetY - position.y).nor().scl(maxVelocity);
        }


        public void update(float deltaTime){
            position.add(velocity.x*deltaTime, velocity.y*deltaTime);
            //velocity.scl(1 - (0.98f * deltaTime));
            projeSprite.setPosition(position.x,position.y);
        }

        public void setPosition(float x, float y){
            projeSprite.setPosition(x, y);
        }
    
    }
    
    public class Monster extends AnimatedSprite{

        static final int FRAME_WIDTH = 64;
        static final int FRAME_HEIGHT = 64;
        TextureRegion[][] quadrosDaAnimacao;
        Texture spriteSheet;
        float sx;
        float sy;
        
        float tempoDaAnimacao;
        
        Animation morrendo;        
        Animation parado;
        boolean morto=false;
        
        int x = 0;
        
        public Monster(final Texture monster) {
            
             super(new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            monster, 64, 64);
                    super.addAll(new TextureRegion[]{
                        frames[1][0],
                        frames[1][1],
                        frames[1][2],
                        frames[1][3],
                        frames[1][4]
                        
                    });
                }
            }));
            
            quadrosDaAnimacao = TextureRegion.split(monster, 64, 64);
            
            parado = new Animation(0.1f,
            quadrosDaAnimacao[1][0], 
            quadrosDaAnimacao[1][1], 
            quadrosDaAnimacao[1][2],
            quadrosDaAnimacao[1][3],
            quadrosDaAnimacao[1][4]);
            
            morrendo = new Animation(0.1f,
            quadrosDaAnimacao[3][0], 
            quadrosDaAnimacao[3][1], 
            quadrosDaAnimacao[3][2],
            quadrosDaAnimacao[3][3],
            quadrosDaAnimacao[3][4],
            quadrosDaAnimacao[3][5],
            quadrosDaAnimacao[3][6]);
            
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
        
        public void changeAnimation(){
            super.setAnimation(morrendo);
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP_PINGPONG);            
        }
        
        public void setMorto(boolean morto){
            this.morto=morto;
        }
        
        public boolean getMorto(){
            return this.morto;
        }
        
        public void update(){
            tempoDaAnimacao += Gdx.graphics.getDeltaTime();
        
        }
    }    

}
