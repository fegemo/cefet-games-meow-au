package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.transition.TransitionScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Uma tela do jogo.
 * 
 * O jogo é dividido em várias telas (Splash, Menu, PlayingGame etc.) e o 
 * código relativo a cada uma delas é uma instância de uma subclasse de
 * BaseScreen.
 * 
 * Cada BaseScreen possui uma {@link SpriteBatch} própria, bem como duas fontes
 * ({@link BitmapFont}) padrão para escrever texto na tela: Sawasdee 24pt e 
 * Sawasdee 50pt.
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public abstract class BaseScreen extends ScreenAdapter {

    public final Game game;
    private final BaseScreen previous;
    public final SpriteBatch batch;
    public final OrthographicCamera camera;
    public Viewport viewport;
    public Rectangle visibleWorldBounds;
    public final AssetManager assets;
    private BitmapFont messagesFont;
    private float deviceAspectRatioDivergenceFromDesired;
    private boolean wasJustDisposed = false;
    private boolean assetsFinishedLoading = false;
    
    /**
     * Cria uma instância de tela.
     * 
     * @param game O jogo do qual a nova instância pertence.
     * @param previous A tela anterior, que levou a esta. Caso esta seja 
     * a primeira tela, o valor deve ser null.
     */
    public BaseScreen(Game game, BaseScreen previous) {
        this.game = game;
        this.previous = previous;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = chooseBestViewport();
        this.assets = new AssetManager();

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));


        // fonte para mensagens
        FreeTypeFontLoaderParameter messagesFontParams = new FreeTypeFontLoaderParameter();
        messagesFontParams.fontFileName = "fonts/snaphand-v1-free.ttf";
        messagesFontParams.fontParameters.color = Color.WHITE;
        messagesFontParams.fontParameters.size = 50;
        messagesFontParams.fontParameters.minFilter = Texture.TextureFilter.Linear;
        messagesFontParams.fontParameters.magFilter = Texture.TextureFilter.Linear;
        messagesFontParams.fontParameters.borderWidth = 2;
        messagesFontParams.fontParameters.borderColor = Color.BLACK;
        assets.load("snaphand-v1-free.ttf", BitmapFont.class, messagesFontParams);
    }
    
    /**
     * Determina qual é o melhor tipo de viewport para ser usado considerando
     * que o jogo foi projetado para 16:9, mas o dispositivo pode ter uma razão
     * de aspecto diferente.
     *
     * Se a razão real do dispositivo for próxima da razão ideal, escolhe uma
     * FillViewport, que preenche todo o espaço do dispositivo mas pode deixar
     * alguma coisinha de fora; se a razão do dispositivo for muito diferente,
     * usa uma FitViewport e coloca o conteúdo do jogo completamente dentro da
     * tela do dispositivo, o que provoca o surgimento de barras pretas laterais
     * ou em cima/baixo.
     *
     * @return a viewport que será usada.
     */
    private Viewport chooseBestViewport() {
        this.deviceAspectRatioDivergenceFromDesired
                = (Config.DESIRED_ASPECT_RATIO - getDeviceAspectRatio())
                / Config.DESIRED_ASPECT_RATIO;

        defineVisibleWorldBounds();

        if (shouldFillDeviceScreen()) {
            return new FillViewport(
                    Config.WORLD_WIDTH,
                    Config.WORLD_HEIGHT,
                    this.camera
            );
        } else {
            return new FitViewport(
                    Config.WORLD_WIDTH,
                    Config.WORLD_HEIGHT,
                    this.camera
            );
        }
    }

    private boolean shouldFillDeviceScreen() {
        return (Math.abs((Config.DESIRED_ASPECT_RATIO - getDeviceAspectRatio())
                / Config.DESIRED_ASPECT_RATIO) < Config.MAXIMUM_ASPECTO_RATIO_DIFFERENCE);
    }

    private float getDeviceAspectRatio() {
        int deviceWidth = Gdx.graphics.getWidth();
        int deviceHeight = Gdx.graphics.getHeight();
        return ((float) deviceWidth) / deviceHeight;
    }

    public void defineVisibleWorldBounds() {
        if (!shouldFillDeviceScreen()) {
            visibleWorldBounds = new Rectangle(
                    0, 0, Config.WORLD_WIDTH, Config.WORLD_HEIGHT);
        }
        float factor = this.deviceAspectRatioDivergenceFromDesired;
        boolean lastingSideways = factor > 0;
        if (lastingSideways) {
            visibleWorldBounds = new Rectangle(
                    0 + (factor / 2) * Config.WORLD_WIDTH,
                    0,
                    Config.WORLD_WIDTH - (factor / 2) * Config.WORLD_WIDTH,
                    Config.WORLD_HEIGHT
            );

        } else {
            visibleWorldBounds = new Rectangle(
                    0,
                    0 + (-factor / 2) * Config.WORLD_HEIGHT,
                    Config.WORLD_WIDTH,
                    Config.WORLD_HEIGHT - (-factor / 2) * Config.WORLD_HEIGHT
            );
        }
    }
    
    public Rectangle getVisibleWorldBounds() {
        return visibleWorldBounds;
    }

    @Override
    public final void show() {
        if (previous != null) {
            previous.dispose();
        }
        this.appear();
    }

    /**
     * Atualiza as dimensões da tela de pintura ({@link Viewport}).
     *
     * @param width nova largura da janela.
     * @param height nova altura da janela.
     */
    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
    }

    /**
     * Invoca as funções de atualização de lógica, recepção de <em>input</em> 
     * e de desenho.
     * 
     * Além disso, assegura de que os desenhos
     *
     * @param dt Quanto tempo se passou desde a última vez que a função foi
     * chamada.
     */
    @Override
    public final void render(float dt) {
        if (assets.update()) {
            if (!assetsFinishedLoading) {
                messagesFont = assets.get("snaphand-v1-free.ttf");
                messagesFont.getData().markupEnabled = true;
                assetsLoaded();
                assetsFinishedLoading = true;
            }

            // chama função para gerenciar o input
            handleInput();
            
            // chama função para atualizar a lógica da tela
            update(dt);

            // a tela pode ter sido "disposed" durante este último update, então
            // verificamos se isso aconteceu para saber se seguimos adiante
            if (wasJustDisposed) {
                return;
            }

            // define o sistema de coordenadas (projeção) a ser usada pelo
            // spriteBatch
            this.batch.setProjectionMatrix(this.camera.combined);

            // limpa a tela para que possa ser redesenhada
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // desenha o conteúdo da tela
            draw();
        }
    }
    
    public void transitionScreen(BaseScreen screen, TransitionScreen.Effect effect, float duration) {
        TransitionScreen transitionScreen = TransitionScreen.getInstance(this, screen);
        transitionScreen.execute(effect, duration);
    }
    
    public void transitionGame(TransitionScreen.Effect effect, float duration, Timer.Task task) {
        TransitionScreen transitionScreen = TransitionScreen.getInstance(this);
        transitionScreen.execute(effect, duration, task);
    }

    /**
     * Escreve um texto centralizado na tela, com uma escala {@code scale} e
     * na altura {@code y}.
     * 
     * @param text O texto a ser escrito.
     * @param y A altura do mundo de jogo onde o texto deve ser renderizado. 
     * Deve estar entre [0 (baixo), Config.WORLD_HEIGHT-altura-do-texto]. 
     */
    public void drawCenterAlignedText(String text, float y) {
        final float horizontalPadding = 0.05f;
        messagesFont.setColor(Color.WHITE);

        final float worldWidth = this.viewport.getWorldWidth();
        messagesFont.draw(
                this.batch,
                text,
                0 + horizontalPadding * worldWidth,
                y,
                worldWidth * (1 - horizontalPadding * 2),
                Align.center,
                true);
    }
    
    @Override
    public final void dispose() {
        if (!wasJustDisposed) {
            wasJustDisposed = true;
            batch.dispose();
            this.cleanUp();
        }
    }
    
    /**
     * Executa ações de carregamento da tela. Esta função é chamada assim que a
     * tela vai ser exibida pela primeira vez.
     *
     * Esta função deve ser usada em vez do método {@code show()}.
     */
    public abstract void appear();
    
    /**
     * Executa ações assim que todos os assets foram carregados. Ela é chamada
     * apenas uma vez, depois de appear(), assim que todos os assets foram 
     * carregados.
     * 
     * Esta função pode ser usada para carregar os elementos do jogo que 
     * dependem dos assets que foram carregados (eg, uma sprite precisa de uma
     * textura).
     */
    protected abstract void assetsLoaded();

    /**
     * Executa as ações de limpeza e descarregamento de recursos e é chamada
     * automaticamente quando a tela não está mais sendo usada.
     */
    public abstract void cleanUp();

    /**
     * Executa ações relativas ao <em>input</em> do jogador.
     * 
     * Use {@code Gdx.input.*} para perguntar se eventos de <em>input</em> 
     * estão acontecendo.
     */
    public abstract void handleInput();

    /**
     * Atualiza a lógica da tela.
     * 
     * @param dt Quanto tempo se passou desde a última vez que a função foi
     * chamada.
     */
    public abstract void update(float dt);

    /**
     * Desenha o conteúdo da tela.
     */
    public abstract void draw();

}