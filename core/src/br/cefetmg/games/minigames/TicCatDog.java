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
public class TicCatDog extends MiniGame
{
    private Texture backgroundTexture; //Imagem de fundo ilustrativa
    private Texture whiteSquareTexture; //Quadrado em branco para composição do jogo da velha
    private Texture catSquareTexture; //Quadrado com um gato no centro 
    private Texture dogSquareTexture; //Quadrado com um cachorro no centro
    private Sprite mouseArrowSprite;
    
    private Sprite [][]ticTacToeSprites; //Sprites que compõe os quadrados do jogo da velha
    private int [][]ticCatDogMatrix; //Matriz que armazena estado atual de cada quadrado
    private final int EMPTY_SQUARE = 0, CAT_SQUARE = 1, DOG_SQUARE = 2;
    
    private final float squareHeight = viewport.getWorldHeight()/5;
    private final float squareWidth = viewport.getWorldWidth()/5;
    private final double hypotenuse = Math.sqrt(Math.pow(squareHeight,2) + Math.pow(squareWidth,2));
    private final float initialScaleMouse = (float)0.05;
    
    private final int CAT_TURN = 1, DOG_TURN = 2;
    private int TURN = DOG_TURN;

    public TicCatDog(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty)
    {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart()
    { 
        backgroundTexture = assets.get(
                "tic-cat-dog/main-background.jpg", Texture.class);
        whiteSquareTexture = assets.get(
                "tic-cat-dog/white-square.png", Texture.class);
        catSquareTexture = assets.get(
                "tic-cat-dog/cat-square.png", Texture.class);
        dogSquareTexture = assets.get(
                "tic-cat-dog/dog-square.png", Texture.class);
        mouseArrowSprite = new Sprite (assets.get(
                "tic-cat-dog/mouse-arrow.png", Texture.class));
        mouseArrowSprite.setOriginCenter();
        mouseArrowSprite.setScale(initialScaleMouse, initialScaleMouse);
        
        //Inicializa-se o jogo da velha do jogo com quadrados brancos
        ticTacToeSprites = new Sprite[3][3];
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++) {
                ticTacToeSprites[i][j] = new Sprite(whiteSquareTexture);
                ticTacToeSprites[i][j].setBounds((j+1)*squareWidth, (i+1)*squareHeight, squareWidth, squareHeight);                         
            }
        
        //Inicializa-se a matriz que contém o estado do jogo
        ticCatDogMatrix = new int [3][3];
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                ticCatDogMatrix[i][j] = EMPTY_SQUARE;
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
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        this.mouseArrowSprite.setPosition(click.x - this.mouseArrowSprite.getWidth() / 2, 
                -click.y + this.mouseArrowSprite.getHeight() / 2);

        boolean clickHiSquare = false;
        if(Gdx.input.justTouched()) 
            for(int i = 2; i >= 0 && !clickHiSquare; i--)
                for(int j = 2; j >= 0 && !clickHiSquare; j--)
                    if(ticTacToeSprites[i][j].getBoundingRectangle().overlaps(
                        mouseArrowSprite.getBoundingRectangle())) {
                        ticCatDogMatrix[i][j] = DOG_SQUARE;
                        ticTacToeSprites[i][j].setTexture(dogSquareTexture);
                        clickHiSquare = true;
                    }
    }
    
    @Override
    public void onUpdate(float dt)
    {

    }

    @Override
    public String getInstructions()
    {
        return "Jogo da velha em CINCO SEGUNDOS!";
    }

    @Override
    public void onDrawGame()
    {        
        //Background padrão com cachorro & gato
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        
        //Desenha-se os quadrados do jogo da velha na tela
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                ticTacToeSprites[i][j].draw(batch);
        
        //Seta do mouse
        mouseArrowSprite.draw(batch);
    }

    @Override
    public boolean shouldHideMousePointer()
    {
        return true;
    }
}
