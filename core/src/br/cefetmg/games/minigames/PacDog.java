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

    private Array<Sprite> enemies;
    private Sprite target;
    private Texture dogTexture;
    private TextureRegion[][] animationFramesDogTexture;
    private Texture cariesTexture;
    private Texture targetTexture;
    private Sound cariesAppearingSound;
    private Sound cariesDyingSound;
    private int enemiesKilled;
    private int spawnedEnemies;
    
    //Definição de constantes
    private final int frameWidthDogTexture = 32;
    private final int frameHeightDogTexture = 32;
    private final int animationSpeed = 2;
    
    private float initialEnemyScale;
    private float minimumEnemyScale;
    private int totalEnemies;
    private float spawnInterval;

    private Vector2 position;
    private int positionX;
    private int positionY;

    private Animation currentAnimation, walkingForwardAnimation, walkingBackwardAnimation, walkingRightAnimation, walkingLeftAnimation;
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
        
        enemies = new Array<Sprite>();
        dogTexture = assets.get(
                "pac-dog/dog-spritesheet.png", Texture.class);
        
        //Define-se a matriz de quadros da sprite do cachorro (personagem principal)
        animationFramesDogTexture = TextureRegion.split(dogTexture, frameWidthDogTexture, frameHeightDogTexture);
        //Define-se as animações dos quatro tipo de movimentos possíveis
        walkingForwardAnimation = new Animation(0.1f,
                animationFramesDogTexture[0][0],
                animationFramesDogTexture[0][1],
                animationFramesDogTexture[0][2]);
        walkingLeftAnimation = new Animation(0.1f,
                animationFramesDogTexture[1][0],
                animationFramesDogTexture[1][1],
                animationFramesDogTexture[1][2]);
        walkingRightAnimation = new Animation(0.1f,
                animationFramesDogTexture[2][0],
                animationFramesDogTexture[2][1],
                animationFramesDogTexture[2][2]);
        walkingBackwardAnimation = new Animation(0.1f,
                animationFramesDogTexture[3][0],
                animationFramesDogTexture[3][1],
                animationFramesDogTexture[3][2]);
        walkingForwardAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
        walkingLeftAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
        walkingRightAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
        walkingBackwardAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
        currentAnimation = walkingForwardAnimation;

        //Posição inicial da animação
        positionX = Math.round(viewport.getWorldWidth()/2);
        positionY = Math.round(viewport.getWorldHeight()/2);
        position = new Vector2(positionX, positionY);
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
            currentAnimation = walkingBackwardAnimation;
            if (position.y + animationSpeed < Gdx.graphics.getHeight() - frameHeightDogTexture)
                position.add(0, animationSpeed);
            //Andar para baixo
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            currentAnimation = walkingForwardAnimation;
            if (position.y - animationSpeed > 0)
                position.add(0, -animationSpeed);
            //Andar para direita
        } else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            currentAnimation = walkingRightAnimation;
            if (position.x + animationSpeed < Gdx.graphics.getWidth() - frameWidthDogTexture)
                position.add(animationSpeed, 0);
            //Andar para esquerda
        } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            currentAnimation = walkingLeftAnimation;
            if (position.x - animationSpeed > 0)
                position.add(-animationSpeed, 0);
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
        batch.draw((TextureRegion) currentAnimation.getKeyFrame(animationTime), position.x, position.y);
    }

    @Override
    public boolean shouldHideMousePointer()
    {
        return true;
    }
}
