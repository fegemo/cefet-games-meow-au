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
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 *
 */
public class PacDog extends MiniGame
{
    private Texture enemiesTexture;
    private Texture dogTexture;
    private TextureRegion[][] animationFramesDogTexture, animationFramesEnemiesTexture;
    
    //Definição de constantes
    private final int frameWidthDogTexture = 32;
    private final int frameHeightDogTexture = 32;
    private final int frameWidthEnemieTexture = 16;
    private final int frameHeightEnemieTexture = 16;
    private final int animationSpeed = 2;
    private final int CURRENT_ANIMATION = 0, MOVE_UP = 1, MOVE_DOWN = 2, MOVE_RIGHT = 3, MOVE_LEFT = 4;
    private final int FIRST_ENEMIE = 0, SECOND_ENEMIE = 1, THIRD_ENEMIE = 2, FOURTH_ENEMIE = 3;
    
    private Vector2 positionMainCharacter;
    private int positionXMainCharacter;
    private int positionYMainCharacter;
    private Vector2[] positionEnemie;
    private int []positionXEnemie;
    private int []positionYEnemie;

    private Animation[] mainCharacterAnimation;
    private Animation[][] enemiesAnimation;
    private float animationTime = 0;

    public PacDog(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty)
    {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart()
    {
        /*********************************************/
        /*** CONFIGURAÇÕES DO PERSONAGEM PRINCIPAL ***/
        /*********************************************/
        dogTexture = assets.get(
                "pac-dog/dog-spritesheet.png", Texture.class);
        
        //Define-se a matriz de quadros da sprite do cachorro
        animationFramesDogTexture = TextureRegion.split(dogTexture, frameWidthDogTexture, frameHeightDogTexture);
        
        //Define-se as animações dos quatro tipo de movimentos possíveis do personagem
        mainCharacterAnimation = new Animation[5];
        mainCharacterAnimation[MOVE_UP] = new Animation(0.1f,
                animationFramesDogTexture[0][0],
                animationFramesDogTexture[0][1],
                animationFramesDogTexture[0][2]);
        mainCharacterAnimation[MOVE_LEFT] = new Animation(0.1f,
                animationFramesDogTexture[1][0],
                animationFramesDogTexture[1][1],
                animationFramesDogTexture[1][2]);
        mainCharacterAnimation[MOVE_RIGHT] = new Animation(0.1f,
                animationFramesDogTexture[2][0],
                animationFramesDogTexture[2][1],
                animationFramesDogTexture[2][2]);
        mainCharacterAnimation[MOVE_DOWN] = new Animation(0.1f,
                animationFramesDogTexture[3][0],
                animationFramesDogTexture[3][1],
                animationFramesDogTexture[3][2]);
        mainCharacterAnimation[MOVE_UP].setPlayMode(PlayMode.LOOP_PINGPONG);
        mainCharacterAnimation[MOVE_LEFT].setPlayMode(PlayMode.LOOP_PINGPONG);
        mainCharacterAnimation[MOVE_RIGHT].setPlayMode(PlayMode.LOOP_PINGPONG);
        mainCharacterAnimation[MOVE_DOWN].setPlayMode(PlayMode.LOOP_PINGPONG);
        mainCharacterAnimation[CURRENT_ANIMATION] = mainCharacterAnimation[MOVE_UP];

        //Posição inicial da animação do personagem
        positionXMainCharacter = Math.round(viewport.getWorldWidth()/2);
        positionYMainCharacter = Math.round(viewport.getWorldHeight()/2);
        positionMainCharacter = new Vector2(positionXMainCharacter, positionYMainCharacter);
        
        /**********************************/
        /*** CONFIGURAÇÕES DOS INIMIGOS ***/
        /**********************************/
        enemiesTexture = assets.get(
                "pac-dog/enemies-spritesheet.png", Texture.class);
        
        //Define-se a matriz de quadros da sprite dos inimigos
        animationFramesEnemiesTexture = TextureRegion.split(enemiesTexture, frameWidthEnemieTexture, frameHeightEnemieTexture);
        
        //Define-se as animações dos inimigos
        enemiesAnimation = new Animation[4][5];
        
        enemiesAnimation[FIRST_ENEMIE][MOVE_RIGHT] = new Animation(0.1f,
                animationFramesEnemiesTexture[0][0],
                animationFramesEnemiesTexture[0][1]);
        enemiesAnimation[FIRST_ENEMIE][MOVE_LEFT] = new Animation(0.1f,
                animationFramesEnemiesTexture[0][2],
                animationFramesEnemiesTexture[0][3]);
        enemiesAnimation[FIRST_ENEMIE][MOVE_UP] = new Animation(0.1f,
                animationFramesEnemiesTexture[0][4],
                animationFramesEnemiesTexture[0][5]);
        enemiesAnimation[FIRST_ENEMIE][MOVE_DOWN] = new Animation(0.1f,
                animationFramesEnemiesTexture[0][6],
                animationFramesEnemiesTexture[0][7]);
        
        enemiesAnimation[SECOND_ENEMIE][MOVE_RIGHT] = new Animation(0.1f,
                animationFramesEnemiesTexture[1][0],
                animationFramesEnemiesTexture[1][1]);
        enemiesAnimation[SECOND_ENEMIE][MOVE_LEFT] = new Animation(0.1f,
                animationFramesEnemiesTexture[1][2],
                animationFramesEnemiesTexture[1][3]);
        enemiesAnimation[SECOND_ENEMIE][MOVE_UP] = new Animation(0.1f,
                animationFramesEnemiesTexture[1][4],
                animationFramesEnemiesTexture[1][5]);
        enemiesAnimation[SECOND_ENEMIE][MOVE_DOWN] = new Animation(0.1f,
                animationFramesEnemiesTexture[1][6],
                animationFramesEnemiesTexture[1][7]);
        
        enemiesAnimation[THIRD_ENEMIE][MOVE_RIGHT] = new Animation(0.1f,
                animationFramesEnemiesTexture[2][0],
                animationFramesEnemiesTexture[2][1]);
        enemiesAnimation[THIRD_ENEMIE][MOVE_LEFT] = new Animation(0.1f,
                animationFramesEnemiesTexture[2][2],
                animationFramesEnemiesTexture[2][3]);
        enemiesAnimation[THIRD_ENEMIE][MOVE_UP] = new Animation(0.1f,
                animationFramesEnemiesTexture[2][4],
                animationFramesEnemiesTexture[2][5]);
        enemiesAnimation[THIRD_ENEMIE][MOVE_DOWN] = new Animation(0.1f,
                animationFramesEnemiesTexture[2][6],
                animationFramesEnemiesTexture[2][7]);
        
        enemiesAnimation[FOURTH_ENEMIE][MOVE_RIGHT] = new Animation(0.1f,
                animationFramesEnemiesTexture[3][0],
                animationFramesEnemiesTexture[3][1]);
        enemiesAnimation[FOURTH_ENEMIE][MOVE_LEFT] = new Animation(0.1f,
                animationFramesEnemiesTexture[3][2],
                animationFramesEnemiesTexture[3][3]);
        enemiesAnimation[FOURTH_ENEMIE][MOVE_UP] = new Animation(0.1f,
                animationFramesEnemiesTexture[3][4],
                animationFramesEnemiesTexture[3][5]);
        enemiesAnimation[FOURTH_ENEMIE][MOVE_DOWN] = new Animation(0.1f,
                animationFramesEnemiesTexture[3][6],
                animationFramesEnemiesTexture[3][7]);
        
        enemiesAnimation[FIRST_ENEMIE][MOVE_UP].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[FIRST_ENEMIE][MOVE_DOWN].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[FIRST_ENEMIE][MOVE_RIGHT].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[FIRST_ENEMIE][MOVE_LEFT].setPlayMode(PlayMode.LOOP_PINGPONG);
        
        enemiesAnimation[SECOND_ENEMIE][MOVE_UP].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[SECOND_ENEMIE][MOVE_DOWN].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[SECOND_ENEMIE][MOVE_RIGHT].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[SECOND_ENEMIE][MOVE_LEFT].setPlayMode(PlayMode.LOOP_PINGPONG);
        
        enemiesAnimation[THIRD_ENEMIE][MOVE_UP].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[THIRD_ENEMIE][MOVE_DOWN].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[THIRD_ENEMIE][MOVE_RIGHT].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[THIRD_ENEMIE][MOVE_LEFT].setPlayMode(PlayMode.LOOP_PINGPONG);
        
        enemiesAnimation[FOURTH_ENEMIE][MOVE_UP].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[FOURTH_ENEMIE][MOVE_DOWN].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[FOURTH_ENEMIE][MOVE_RIGHT].setPlayMode(PlayMode.LOOP_PINGPONG);
        enemiesAnimation[FOURTH_ENEMIE][MOVE_LEFT].setPlayMode(PlayMode.LOOP_PINGPONG);
        
        enemiesAnimation[FIRST_ENEMIE][CURRENT_ANIMATION] = enemiesAnimation[FIRST_ENEMIE][MOVE_DOWN];
        enemiesAnimation[SECOND_ENEMIE][CURRENT_ANIMATION] = enemiesAnimation[SECOND_ENEMIE][MOVE_DOWN];
        enemiesAnimation[THIRD_ENEMIE][CURRENT_ANIMATION] = enemiesAnimation[THIRD_ENEMIE][MOVE_UP];
        enemiesAnimation[FOURTH_ENEMIE][CURRENT_ANIMATION] = enemiesAnimation[FOURTH_ENEMIE][MOVE_UP];
        
        positionEnemie = new Vector2[4];
        positionXEnemie = new int[4];
        positionYEnemie = new int[4];
        
        positionXEnemie[FIRST_ENEMIE] = 0;
        positionYEnemie[FIRST_ENEMIE] = Math.round(viewport.getWorldHeight()) - frameHeightEnemieTexture;
        positionXEnemie[SECOND_ENEMIE] = Math.round(viewport.getWorldWidth()) - frameWidthEnemieTexture;
        positionYEnemie[SECOND_ENEMIE] = Math.round(viewport.getWorldHeight()) - frameHeightEnemieTexture;
        positionXEnemie[THIRD_ENEMIE] = Math.round(viewport.getWorldWidth()) - frameWidthEnemieTexture;
        positionYEnemie[THIRD_ENEMIE] = Math.round(viewport.getWorldHeight()/2);
        positionXEnemie[FOURTH_ENEMIE] = 0;
        positionYEnemie[FOURTH_ENEMIE] = Math.round(viewport.getWorldHeight()/2);
        
        positionEnemie[FIRST_ENEMIE] = new Vector2(positionXEnemie[FIRST_ENEMIE], positionYEnemie[FIRST_ENEMIE]);
        positionEnemie[SECOND_ENEMIE] = new Vector2(positionXEnemie[SECOND_ENEMIE], positionYEnemie[SECOND_ENEMIE]);
        positionEnemie[THIRD_ENEMIE] = new Vector2(positionXEnemie[THIRD_ENEMIE], positionYEnemie[THIRD_ENEMIE]);
        positionEnemie[FOURTH_ENEMIE] = new Vector2(positionXEnemie[FOURTH_ENEMIE], positionYEnemie[FOURTH_ENEMIE]);
    }

    private void scheduleEnemySpawn()
    {

    }

    private void spawnEnemy()
    {

    }

    @Override
    protected void configureDifficultyParameters(float difficulty)
    {

    }

    @Override
    public void onHandlePlayingInput()
    {

    }

    @Override
    public void onUpdate(float dt)
    {
        animationTime += Gdx.graphics.getDeltaTime();
        
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            mainCharacterAnimation[CURRENT_ANIMATION] = mainCharacterAnimation[MOVE_DOWN];
            if (positionMainCharacter.y + animationSpeed < Gdx.graphics.getHeight() - frameHeightDogTexture)
                positionMainCharacter.add(0, animationSpeed);
            //Andar para baixo
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            mainCharacterAnimation[CURRENT_ANIMATION] = mainCharacterAnimation[MOVE_UP];
            if (positionMainCharacter.y - animationSpeed > 0)
                positionMainCharacter.add(0, -animationSpeed);
            //Andar para direita
        } else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            mainCharacterAnimation[CURRENT_ANIMATION] = mainCharacterAnimation[MOVE_RIGHT];
            if (positionMainCharacter.x + animationSpeed < Gdx.graphics.getWidth() - frameWidthDogTexture)
                positionMainCharacter.add(animationSpeed, 0);
            //Andar para esquerda
        } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            mainCharacterAnimation[CURRENT_ANIMATION] = mainCharacterAnimation[MOVE_LEFT];
            if (positionMainCharacter.x - animationSpeed > 0)
                positionMainCharacter.add(-animationSpeed, 0);
        }
    }

    @Override
    public String getInstructions()
    {
        return "Colete dois ossos!";
    }

    @Override
    public void onDrawGame()
    {
        batch.draw((TextureRegion) mainCharacterAnimation[CURRENT_ANIMATION].getKeyFrame(animationTime), positionMainCharacter.x, positionMainCharacter.y);
        batch.draw((TextureRegion) enemiesAnimation[FIRST_ENEMIE][CURRENT_ANIMATION].getKeyFrame(animationTime), positionEnemie[FIRST_ENEMIE].x, positionEnemie[FIRST_ENEMIE].y);
        batch.draw((TextureRegion) enemiesAnimation[SECOND_ENEMIE][CURRENT_ANIMATION].getKeyFrame(animationTime), positionEnemie[SECOND_ENEMIE].x, positionEnemie[SECOND_ENEMIE].y);
        batch.draw((TextureRegion) enemiesAnimation[THIRD_ENEMIE][CURRENT_ANIMATION].getKeyFrame(animationTime), positionEnemie[THIRD_ENEMIE].x, positionEnemie[THIRD_ENEMIE].y);
        batch.draw((TextureRegion) enemiesAnimation[FOURTH_ENEMIE][CURRENT_ANIMATION].getKeyFrame(animationTime), positionEnemie[FOURTH_ENEMIE].x, positionEnemie[FOURTH_ENEMIE].y);
    }

    @Override
    public boolean shouldHideMousePointer()
    {
        return true;
    }
}
