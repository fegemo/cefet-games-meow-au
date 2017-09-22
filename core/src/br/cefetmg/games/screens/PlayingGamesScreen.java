package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.graphics.hud.Hud;
import br.cefetmg.games.logic.chooser.BaseGameSequencer;
import br.cefetmg.games.logic.chooser.GameSequencer;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.factories.*;
import br.cefetmg.games.minigames.util.MiniGameState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.Arrays;
import java.util.HashSet;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

/**
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayingGamesScreen extends BaseScreen
        implements MiniGameStateObserver {

    private MiniGame currentGame;
    private final BaseGameSequencer sequencer;
    private final Hud hud;
    private PlayScreenState state;
    private int lives;
    private boolean hasPreloaded;
    private final InputMultiplexer inputMultiplexer;

    public PlayingGamesScreen(Game game, BaseScreen previous) {
        super(game, previous);
        state = PlayScreenState.PLAYING;
        lives = Config.MAX_LIVES;
        sequencer = new GameSequencer(5, new HashSet<MiniGameFactory>(
                Arrays.asList(
                        // flávio
                       // new ShootTheCariesFactory(),
                     //   new ShooTheTartarusFactory()
                        new RatoaJatoFactory(),
                        new MouseAttackFactory()
                )
        ), 0, 1, this, this);
        hud = new Hud(this, this);
        inputMultiplexer = new InputMultiplexer();
    }

    @Override
    public void appear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        assets.load("hud/countdown.png", Texture.class);
        assets.load("hud/gray-mask.png", Texture.class);
        assets.load("hud/unpause-button.png", Texture.class);
        assets.load("hud/pause-button.png", Texture.class);
        assets.load("hud/lives.png", Texture.class);
        assets.load("hud/clock.png", Texture.class);
        assets.load("hud/tick-tock.mp3", Sound.class);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void cleanUp() {
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(null);
        assets.dispose();
    }

    @Override
    public void handleInput() {
        if (currentGame != null) {
            currentGame.handleInput();
        }

        if (state == PlayScreenState.FINISHED_WON
                || state == PlayScreenState.FINISHED_GAME_OVER) {
            if (Gdx.input.justTouched()) {
                // volta para o menu principal
                super.game.setScreen(new MenuScreen(super.game, this));
            }
        }
    }

    @Override
    public void update(float dt) {
        if (preload()) {
            currentGame.update(dt);
            hud.update(dt);
        }
    }

    @Override
    public void draw() {
        super.batch.begin();
        if (currentGame != null) {
            currentGame.draw();
        }
        if (state != PlayScreenState.PLAYING) {
            drawEndGame();
        }
        super.batch.end();
        hud.draw();
    }

    private void advance() {
        if (this.state == PlayScreenState.FINISHED_WON
                || this.state == PlayScreenState.FINISHED_GAME_OVER) {
            // se deu gameover ou terminou a sequencia com sucesso,
            // não deixa avançar para próximo minigame
            return;
        }

        // se há um próximo jogo na sequência, carrega-o
        if (this.sequencer.hasNextGame()) {
            loadNextGame();            
        } 
        // se não há mais jogos, o jogador concluiu a sequência e ainda possui
        // vidas
        else {
            // mostra mensagem de vitória
            this.transitionTo(PlayScreenState.FINISHED_WON);
        }
    }
    
    private void loadNextGame() {
        // carrega o novo jogo (pede ao sequenciador o próximo)
        currentGame = sequencer.nextGame();
        currentGame.start();
        
        // atualiza o número de sequência do jogo atual na HUD
        hud.setGameIndex(sequencer.getGameNumber());
    }

    private void drawEndGame() {
        super.drawCenterAlignedText("Toque para voltar ao Menu",
                super.viewport.getWorldHeight() * 0.35f);
    }

    private boolean preload() {
        if (super.assets.update() && !hasPreloaded) {
            hud.create();
            inputMultiplexer.addProcessor(hud.getInputProcessor());
            if (state == PlayScreenState.PLAYING && currentGame == null) {
                advance();
            }

            hasPreloaded = true;
        }
        return hasPreloaded;
    }

    private void loseLife() {
        lives--;
        hud.setLives(lives);
        if (lives == 0) {
            transitionTo(PlayScreenState.FINISHED_GAME_OVER);
        }
    }

    private void transitionTo(PlayScreenState newState) {
        switch (newState) {
            case FINISHED_GAME_OVER:
                Gdx.input.setCursorCatched(false);
                break;

        }
        this.state = newState;
    }

    // <editor-fold defaultstate="expanded" desc="Implementação da interface MiniGameStateObserver">
    @Override
    public void onStateChanged(MiniGameState state) {
        switch (state) {
            case SHOWING_INSTRUCTIONS:
                hud.showGameInstructions(currentGame.getInstructions());
                hud.startInitialCountdown();
                hud.showPauseButton();
                break;

            case PLAYING:
                hud.hideGameInstructions();
                Gdx.input.setCursorCatched(currentGame.shouldHideMousePointer());
                if (currentGame.getInputProcessor() != null) {
                    inputMultiplexer.addProcessor(
                            currentGame.getInputProcessor());
                }
                break;

            case PLAYER_SUCCEEDED:
                if (sequencer.hasNextGame()) {
                    Gdx.input.setCursorCatched(false);
                }
            // deixa passar para próximo caso (esta foi
            // uma decisão consciente =)

            case PLAYER_FAILED:
                hud.hidePauseButton();
                hud.showMessage(state == MiniGameState.PLAYER_FAILED ? "Falhou!" : "Conseguiu!");
                if (state == MiniGameState.PLAYER_FAILED) {
                    loseLife();
                }
                
                inputMultiplexer.removeProcessor(currentGame.getInputProcessor());
                Timer.instance().scheduleTask(new Task() {
                    @Override
                    public void run() {
                        advance();
                    }

                }, 1.5f);

                Gdx.input.setCursorCatched(false);
                hud.cancelEndingTimer();
                break;
        }
    }

    @Override
    public void onTimeEnding() {
        hud.startEndingTimer();
    }

    @Override
    public void onGamePaused() {
        currentGame.pause();

        // desabilita até que o jogo seja despausado
        inputMultiplexer.removeProcessor(currentGame.getInputProcessor());
    }

    @Override
    public void onGameResumed() {
        currentGame.resume();

        // recupera o possível processador de input do minigame
        if (currentGame.getInputProcessor() != null) {
            inputMultiplexer.addProcessor(currentGame.getInputProcessor());
        }
    }
    // </editor-fold>

    enum PlayScreenState {
        PLAYING,
        FINISHED_GAME_OVER,
        FINISHED_WON
    }

}
