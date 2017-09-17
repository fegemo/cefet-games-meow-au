package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.ticCatDog.Move;
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
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private Sound catMeowingSound;
    private Sound dogBarkingSound;
    
    private Sprite [][]ticTacToeSprites; //Sprites que compõe os quadrados do jogo da velha
    private int [][]ticCatDogMatrix; //Matriz que armazena estado atual de cada quadrado
    private final int EMPTY_SQUARE = 0, CAT_SQUARE = 1, DOG_SQUARE = 2;
    
    private final float squareHeight = viewport.getWorldHeight()/5;
    private final float squareWidth = viewport.getWorldWidth()/5;
    private final double hypotenuse = Math.sqrt(Math.pow(squareHeight,2) + Math.pow(squareWidth,2));
    private final float initialScaleMouse = (float)0.05;
    
    private final int CAT_TURN = 1, DOG_TURN = 2;
    private int turn;
    
    private Random generator = new Random();

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
        
        catMeowingSound = assets.get("tic-cat-dog/cat-meowing.wav", Sound.class);
        dogBarkingSound = assets.get("tic-cat-dog/dog-barking.wav", Sound.class);
        
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
        if(difficulty < 1)
            turn = DOG_TURN;
        else {
            turn = CAT_TURN;
            super.maxDuration /= 2;
        }
    }
    
    private boolean isThereAvailableSquare(int[][] matrix) {
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                if(matrix[i][j] == EMPTY_SQUARE)
                    return true;
        return false;
    }
    
    private Move minimax(int[][] matrix, int player)
    {
        if (winning(matrix, DOG_SQUARE))
            return new Move(-10);
        else if (winning(matrix, CAT_SQUARE))
            return new Move(10);
        else if (!isThereAvailableSquare(matrix))
            return new Move(0);
        
        ArrayList<Move> moves = new ArrayList<Move>();
        int[][] newMatrix = matrix.clone();
        
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(matrix[i][j] == EMPTY_SQUARE) {
                    //Haverá um objeto por espaço vazio
                    Move move = new Move(i, j);
                    
                    //Define o espaço vazio para o player atual
                    newMatrix[i][j] = player;
                    
                    //Armazena-se a pontuação para o minimax p/ o oponente
                    if(player == DOG_TURN) {
                        int result = minimax(newMatrix, CAT_TURN).getScore();
                        move.setScore(result);
                    } else {
                        int result = minimax(newMatrix, DOG_TURN).getScore();
                        move.setScore(result);
                    }
                    
                    //Reseta a posição forçada
                    matrix[i][j] = EMPTY_SQUARE;
                    
                    //Armazena-se o movimento
                    moves.add(move);
                }
            }
        }
        
        Move bestMove = moves.get(0);
        
        //Armazena-se o melhor movimento:
        //Se for o gato, tende-se a maximizar seus pontos
        //Se for o cachorro, tende-se a minimizar seus pontos
        if(player == CAT_TURN) {
            for(int i = 1; i < moves.size(); i++)
                if(moves.get(i).getScore() > bestMove.getScore())
                    bestMove = moves.get(i);
        } else {
            for(int i = 1; i < moves.size(); i++)
                if(moves.get(i).getScore() < bestMove.getScore())
                    bestMove = moves.get(i);
        }
        
        //Sorteia-se um movimento que possui melhor score
        while(true) {
            int randomMovePosition = generator.nextInt(moves.size());
            if(moves.get(randomMovePosition).getScore() == bestMove.getScore())
                return moves.get(randomMovePosition);
        }
    }

    @Override
    public void onHandlePlayingInput()
    {
        //Recebe a posição do mouse e atualiza a Sprite de mouse
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        this.mouseArrowSprite.setPosition(click.x - this.mouseArrowSprite.getWidth() / 2, 
                -click.y + this.mouseArrowSprite.getHeight() / 2);

        //Verifica se o usuário clicou em algum quadrado do tic-tac-toe
        boolean clickHiSquare = false;
        if(Gdx.input.justTouched() && turn == DOG_TURN) {
            for(int i = 2; i >= 0 && !clickHiSquare; i--)
                for(int j = 2; j >= 0 && !clickHiSquare; j--)
                    if(ticCatDogMatrix[i][j] == EMPTY_SQUARE && 
                            ticTacToeSprites[i][j].getBoundingRectangle().overlaps(
                                    mouseArrowSprite.getBoundingRectangle())) {
                        
                        //Movimento realizado
                        ticCatDogMatrix[i][j] = DOG_SQUARE;
                        ticTacToeSprites[i][j].setTexture(dogSquareTexture);
                        
                        //Passa a vez
                        clickHiSquare = true;
                        turn = CAT_TURN;
                        
                        //Condições de fim de partida
                        if (winning(ticCatDogMatrix, DOG_TURN)) {
                            //Som de cachorro latindo
                            dogBarkingSound.play();
                            super.challengeSolved();
                        }
                        else if (!isThereAvailableSquare(ticCatDogMatrix)) {    
                            //Som de cachorro latindo
                            dogBarkingSound.play();
                            super.challengeSolved();
                        }
                    }
        }
        //Movimento do gato
        else if (turn == CAT_TURN && isThereAvailableSquare(ticCatDogMatrix)) {
            //Inteligência artificial: melhor movimento selecionado
            Move move = minimax(ticCatDogMatrix, CAT_TURN);
            
            //Realiza-se o movimento
            ticCatDogMatrix[move.getX()][move.getY()] = CAT_SQUARE;
            ticTacToeSprites[move.getX()][move.getY()].setTexture(catSquareTexture);
            
            //Condições de fim de partida
            if(winning(ticCatDogMatrix, CAT_TURN)) {
                //Som de gato miando
                catMeowingSound.play();
                super.challengeFailed();
            } else if(!isThereAvailableSquare(ticCatDogMatrix)) {
                //Som de cachorro latindo
                dogBarkingSound.play();
                super.challengeSolved();
            }
            //Passa a vez para o cachorro
            turn = DOG_TURN;
        }
    }
    
    @Override
    public void onUpdate(float dt)
    {

    }

    @Override
    public String getInstructions()
    {
        return "SOBREVIVA ao jogo da velha!";
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
    
    public boolean winning(int[][] matrix, int player) {
        if (
                //Horizontal
        (matrix[0][0] == player && matrix[0][1] == player && matrix[0][2] == player) ||
        (matrix[1][0] == player && matrix[1][1] == player && matrix[1][2] == player) ||
        (matrix[2][0] == player && matrix[2][1] == player && matrix[2][2] == player) ||
                //Vertical
        (matrix[0][0] == player && matrix[1][0] == player && matrix[2][0] == player) ||
        (matrix[0][1] == player && matrix[1][1] == player && matrix[2][1] == player) ||
        (matrix[0][2] == player && matrix[1][2] == player && matrix[2][2] == player) ||
                //Diagonal
        (matrix[0][0] == player && matrix[1][1] == player && matrix[2][2] == player) ||
        (matrix[0][2] == player && matrix[1][1] == player && matrix[2][0] == player)
        )
            return true;
        return false;
    }
}
