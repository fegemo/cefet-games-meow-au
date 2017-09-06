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
    private Texture [][]ticTacToeSquares; //Quadrados que compõe o estado atual do jogo da velha
    private Vector2 [][]positionSquares;
    
    private final float squareHeight = viewport.getWorldHeight()/5;
    private final float squareWidth = viewport.getWorldWidth()/5;

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
        
        //Inicializa-se o jogo da velha do jogo com quadrados brancos
        ticTacToeSquares = new Texture[3][3];
        ticTacToeSquares[0][0] = whiteSquareTexture;
        ticTacToeSquares[0][1] = whiteSquareTexture;
        ticTacToeSquares[0][2] = whiteSquareTexture;
        ticTacToeSquares[1][0] = whiteSquareTexture;
        ticTacToeSquares[1][1] = whiteSquareTexture;
        ticTacToeSquares[1][2] = whiteSquareTexture;
        ticTacToeSquares[2][0] = whiteSquareTexture;
        ticTacToeSquares[2][1] = whiteSquareTexture;
        ticTacToeSquares[2][2] = whiteSquareTexture;
        
        //Inicializa-se a posição dos quadrados que compõe o jogo da velha
        positionSquares = new Vector2[3][3];
        positionSquares[0][0] = new Vector2(squareWidth, squareHeight);
        positionSquares[0][1] = new Vector2(2*squareWidth, squareHeight);
        positionSquares[0][2] = new Vector2(3*squareWidth, squareHeight);
        
        positionSquares[1][0] = new Vector2(squareWidth, 2*squareHeight);
        positionSquares[1][1] = new Vector2(2*squareWidth, 2*squareHeight);
        positionSquares[1][2] = new Vector2(3*squareWidth, 2*squareHeight);
        
        positionSquares[2][0] = new Vector2(squareWidth, 3*squareHeight);
        positionSquares[2][1] = new Vector2(2*squareWidth, 3*squareHeight);
        positionSquares[2][2] = new Vector2(3*squareWidth, 3*squareHeight);
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

    }

    @Override
    public String getInstructions()
    {
        return "Jogo da velha em CINCO SEGUNDOS!";
    }

    @Override
    public void onDrawGame()
    {        
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        
        batch.draw(ticTacToeSquares[0][0], positionSquares[0][0].x, positionSquares[0][0].y, squareWidth, squareHeight);
        batch.draw(ticTacToeSquares[0][1], positionSquares[0][1].x, positionSquares[0][1].y, squareWidth, squareHeight);
        batch.draw(ticTacToeSquares[0][2], positionSquares[0][2].x, positionSquares[0][2].y, squareWidth, squareHeight);
        batch.draw(ticTacToeSquares[1][0], positionSquares[1][0].x, positionSquares[1][0].y, squareWidth, squareHeight);
        batch.draw(ticTacToeSquares[1][1], positionSquares[1][1].x, positionSquares[1][1].y, squareWidth, squareHeight);
        batch.draw(ticTacToeSquares[1][2], positionSquares[1][2].x, positionSquares[1][2].y, squareWidth, squareHeight);
        batch.draw(ticTacToeSquares[2][0], positionSquares[2][0].x, positionSquares[2][0].y, squareWidth, squareHeight);
        batch.draw(ticTacToeSquares[2][1], positionSquares[2][1].x, positionSquares[2][1].y, squareWidth, squareHeight);
        batch.draw(ticTacToeSquares[2][2], positionSquares[2][2].x, positionSquares[2][2].y, squareWidth, squareHeight);
    }

    @Override
    public boolean shouldHideMousePointer()
    {
        return true;
    }
}
