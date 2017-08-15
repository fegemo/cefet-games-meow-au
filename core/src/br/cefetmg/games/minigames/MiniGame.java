package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Random;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public abstract class MiniGame {

//    protected final BaseScreen screen;
    protected final AssetManager assets;
    protected final Viewport viewport;
    protected final SpriteBatch batch;
    protected long remainingTime;
    protected float maxDuration;
    private float timeSpentOnInstructions;
    private float timeSpentPlaying;
    private MiniGameState state;
    protected Random rand;
    protected final Timer timer;
    private boolean isPaused;

//    private final BitmapFont messagesFont;
    private boolean challengeSolved;
    private MiniGameStateObserver stateObserver;
    private long timeWhenPausedLastTime;
    private InputProcessor miniGameInputProcessor;
    private float difficulty;

//    public MiniGame(AssetManager assets, Viewport viewport, float difficulty,
//            float maxDuration, TimeoutBehavior endOfGameSituation, 
//            final MiniGameStateObserver observer) {
    public MiniGame(BaseScreen screen, MiniGameStateObserver observer,
            float difficulty, float maxDuration, 
            TimeoutBehavior endOfGameSituation) {
        if (difficulty < 0 || difficulty > 1) {
            throw new IllegalArgumentException(
                    "A dificuldade (difficulty) de um minigame deve ser um "
                    + "número entre 0 e 1. Você passou o número " + difficulty
                    + ".");
        }

//        this.screen = screen;
        this.assets = screen.assets;
        this.viewport = screen.viewport;
        this.batch = screen.batch;
        this.challengeSolved = endOfGameSituation
                == TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS;
        this.maxDuration = maxDuration;
        this.timeSpentPlaying = 0;
        this.timeSpentOnInstructions = 0;
        this.stateObserver = observer;
//        this.messagesFont = this.screen.assets.get("fonts/sawasdee-50.fnt");
//        this.messagesFont.getRegion().getTexture().setFilter(
//                Texture.TextureFilter.Linear,
//                Texture.TextureFilter.Linear);
        this.rand = new Random();
        this.timer = new Timer();
        this.timer.stop();
        this.difficulty = difficulty;
        state = MiniGameState.SHOWING_INSTRUCTIONS;
    }

    public final void start() {
        this.configureDifficultyParameters(this.difficulty);
        transitionTo(MiniGameState.SHOWING_INSTRUCTIONS);
    }

    public final void handleInput() {
        // deixa o MiniGame lidar com o input apenas se estivermos no estado
        // de jogo propriamente dito e sem pausa
        if (this.state == MiniGameState.PLAYING && !isPaused) {
            onHandlePlayingInput();
        }
    }

    public final void pause() {
        isPaused = true;

        // interrompe o timer do minigame, salvando o momento em
        // que o jogo foi pausado
        this.timer.stop();
        this.timeWhenPausedLastTime = TimeUtils.nanosToMillis(
                TimeUtils.nanoTime());

        // libera o cursor do mouse
        Gdx.input.setCursorCatched(false);
    }

    public final void resume() {
        isPaused = false;

        // retoma o timer, atrasando-o pelo tempo que o jogo ficou pausado
        this.timer.start();
        this.timer.delay(TimeUtils.nanosToMillis(
                TimeUtils.nanoTime()) - this.timeWhenPausedLastTime);

        // se a pausa foi feita durante o jogo (fora das instruções
        // ou do final do jogo), oculta novamente o cursor
        if (state == MiniGameState.PLAYING) {
            Gdx.input.setCursorCatched(shouldHideMousePointer());
        }

    }

    public final void update(float dt) {
        if (isPaused) {
            return;
        }

        switch (this.state) {
            case SHOWING_INSTRUCTIONS:
                this.timeSpentOnInstructions += dt;
                if (timeSpentOnInstructions
                        > Config.TIME_SHOWING_MINIGAME_INSTRUCTIONS) {
                    transitionTo(MiniGameState.PLAYING);
                }

                break;

            case PLAYING:
                timeSpentPlaying += dt;
                if (timeSpentPlaying > maxDuration) {
                    transitionTo(challengeSolved
                            ? MiniGameState.PLAYER_SUCCEEDED
                            : MiniGameState.PLAYER_FAILED);
                }
                onUpdate(dt);
                break;
        }
    }

//    protected void drawMessage(String message, float scale) {
//        messagesFont.setColor(Color.BLACK);
//        this.screen.drawCenterAlignedText(message, scale,
//                this.screen.viewport.getWorldHeight() / 2);
//    }
//

    public final void draw() {
        switch (this.state) {
            case PLAYING:
                onDrawGame();
                break;

            case PLAYER_FAILED:
            case PLAYER_SUCCEEDED:
                onDrawGame();
                break;
        }
    }

    public final boolean isPaused() {
        return isPaused;
    }

    public final InputProcessor getInputProcessor() {
        return miniGameInputProcessor;
    }

    protected final void useInputProcessor(InputProcessor processor) {
        miniGameInputProcessor = processor;
    }

    protected final MiniGameState getState() {
        return state;
    }

    private void transitionTo(MiniGameState newState) {
        switch (newState) {
            case PLAYING:
                this.onStart();

                this.timer.scheduleTask(new Task() {
                    @Override
                    public void run() {
                        stateObserver.onTimeEnding();
                    }
                }, (maxDuration - Config.MINIGAME_COUNTDOWN_ON_HUD_BEGIN_AT));

                timer.start();
                break;

            case PLAYER_SUCCEEDED:
            case PLAYER_FAILED:
                this.onEnd();
                timer.stop();
                break;
        }
        this.state = newState;
        this.stateObserver.onStateChanged(state);
    }

    protected void challengeFailed() {
        this.challengeSolved = false;
        transitionTo(MiniGameState.PLAYER_FAILED);
    }

    protected void challengeSolved() {
        this.challengeSolved = true;
        transitionTo(MiniGameState.PLAYER_SUCCEEDED);
    }

    /**
     * Inicializa o jogo - jogador, inimigos, timers etc.
     */
    protected abstract void onStart();

    /**
     * Executa rotinas necessárias quando o jogo é terminado.
     * Pode ser sobrescrito, mas não é necessário em todo jogo.
     */
    protected void onEnd() {
        
    }

    /**
     * Configura os parâmetros de dificuldade do jogo como, por exemplo,
     * a quantidade de inimigos, a velocidade deles etc.
     * Este método é chamado pela própria MiniGame e, dentro dele, você deve
     * apenas configurar suas próprias variáveis relativas à dificuldade.
     * 
     * @param difficulty a dificuldade, entre [0, 1].
     */
    protected abstract void configureDifficultyParameters(float difficulty);

    /**
     * Chamada o tempo todo, deve ser usada para detectar se o jogador 
     * interagiu com a tela/teclado/mouse de alguma forma e, então, agir de 
     * acordo.
     */
    public abstract void onHandlePlayingInput();

    /**
     * Chamada o tempo todo, deve ser usada para atualizar a lógica do jogo.
     * @param dt tempo (em segundos) desde a última vez que onUpdate(dt) 
     * foi chamada.
     */
    public abstract void onUpdate(float dt);

    /**
     * Chamada o tempo todo, deve ser usada para desenhar o jogo.
     */
    public abstract void onDrawGame();

    /**
     * Chamada pelo próprio MiniGame para mostrar as instruções do jogo ao
     * jogador na tela. Basta retornar uma string com as instruções (que devem
     * ser curtas!).
     * @return as instruções (no máximo, 3-5 palavras
     */
    public abstract String getInstructions();

    /**
     * Chamada pelo próprio jogo para saber se, durante o seu MiniGame, ele 
     * deve esconder o ponteiro do mouse (no caso de um dispositivo com mouse). 
     * @return true/false para falar se deve esconder o ponteiro do mouse 
     * ou não.
     */
    public abstract boolean shouldHideMousePointer();


}
