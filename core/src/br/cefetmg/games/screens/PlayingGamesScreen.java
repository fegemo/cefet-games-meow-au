package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.transition.TransitionScreen;
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
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import java.util.Set;

/**
 *
 * @inspirado no tp de cinematica
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
    private Sound gameWonSound;
    private Sound gameOverSound;
    private Sound youLoseSound;
    private Sound youWinSound;
     private Music intergames;

    public PlayingGamesScreen(Game game, BaseScreen previous, int nGames, Set<MiniGameFactory> games,
            float initialDifficulty, float finalDifficulty) {
        super(game, previous);
        state = PlayScreenState.PLAYING;
        lives = Config.MAX_LIVES;
        sequencer = new GameSequencer(nGames, games, initialDifficulty, finalDifficulty, this, this);
        hud = new Hud(this, this);
        inputMultiplexer = new InputMultiplexer();
    }

    @Override
    public void appear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        TextureParameter linearFilter = new TextureLoader.TextureParameter();
        linearFilter.minFilter = Texture.TextureFilter.Linear;
        linearFilter.magFilter = Texture.TextureFilter.Linear;
        assets.load("hud/countdown.png", Texture.class, linearFilter);
        assets.load("hud/gray-mask.png", Texture.class);
        assets.load("hud/unpause-button.png", Texture.class, linearFilter);
        assets.load("hud/pause-button.png", Texture.class, linearFilter);
        assets.load("hud/lifeTexture.png", Texture.class, linearFilter);
        assets.load("hud/explodeLifeTexture.png", Texture.class, linearFilter);
        assets.load("hud/clock.png", Texture.class, linearFilter);
        assets.load("hud/tick-tock.mp3", Sound.class);
        assets.load("hud/back-menu-button.png", Texture.class, linearFilter);
        assets.load("hud/confirm-button.png", Texture.class, linearFilter);
        assets.load("hud/unnconfirmed-button.png", Texture.class, linearFilter);
        assets.load("sound/gamewon.mp3", Sound.class);
        assets.load("sound/gameover.wav", Sound.class);
        assets.load("sound/youwin.wav", Sound.class);
        assets.load("sound/youlose.wav", Sound.class);
        assets.load("hud/intergames.wav", Music.class);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
    
    @Override
    protected void assetsLoaded() {
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
                 transitionScreen(new OverworldScreen(super.game, this),
                        TransitionScreen.Effect.FADE_IN_OUT, 1f);
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
        } // se não há mais jogos, o jogador concluiu a sequência e ainda possui
        // vidas
        else {
            // mostra mensagem de vitória
            gameWonSound.play();
            this.transitionTo(PlayScreenState.FINISHED_WON);
        }
    }

    private void loadNextGame() {
        if (currentGame == null) {
            // carrega o primeiro jogo (pede ao sequenciador o próximo)
            currentGame = sequencer.nextGame();
            currentGame.start();

            // atualiza o número de sequência do jogo atual na HUD
            hud.setGameIndex(sequencer.getGameNumber());
        } else {
            transitionGame(TransitionScreen.Effect.FADE_IN_OUT, 0.5f, new Task() {
                @Override
                public void run() {
                    currentGame = sequencer.nextGame();
                    currentGame.start();

                    // atualiza o número de sequência do jogo atual na HUD
                    hud.setGameIndex(sequencer.getGameNumber());
                }
            });
        }
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

            gameWonSound = assets.get("sound/gamewon.mp3");
            gameOverSound = assets.get("sound/gameover.wav");
            youLoseSound = assets.get("sound/youlose.wav");
            youWinSound = assets.get("sound/youwin.wav");

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
            case BACK_MENU:
                super.game.setScreen(new MenuScreen(super.game, this));
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
                intergames = assets.get("hud/intergames.wav", Music.class);
                intergames.play();
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
                youWinSound.play();
            // deixa passar para próximo caso (esta foi
            // uma decisão consciente =)

            case PLAYER_FAILED:
                hud.hidePauseButton();
                hud.showMessage(state == MiniGameState.PLAYER_FAILED ? "Falhou!" : "Conseguiu!");
                if (state == MiniGameState.PLAYER_FAILED) {
                    loseLife();

                    if (lives == 0) {
                        gameOverSound.play();
                    } else {
                        youLoseSound.play();
                    }
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
            case BACK_MENU:
                transitionTo(PlayScreenState.BACK_MENU);
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
    
    @Override
    public void showMessage(String strMessage) {
        hud.showMessage(strMessage);
    }
    // </editor-fold>

    enum PlayScreenState {
        PLAYING,
        FINISHED_GAME_OVER,
        FINISHED_WON,
        BACK_MENU
    }
}
