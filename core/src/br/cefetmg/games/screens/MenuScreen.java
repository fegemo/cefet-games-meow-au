package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.transition.TransitionScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

/**
 * Uma tela de Menu Principal do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */



public class MenuScreen extends BaseScreen {

    public static final int BUTTONS_X = 538;
    public static final int BUTTONS_WIDTH = 204;
    public static final int BUTTONS_HEIGHT = 54;
    public static final int PLAY_Y = 360;
    public static final int RANKING_Y = 276;
    public static final int CREDITS_Y = 192;
    public static final int WORLD_Y = 108;
    public static final int EXIT_Y = 24;
    
    public static final int LOGO_X = 160;
    public static final int LOGO_Y = 360;
    public static final int LOGO_WIDTH = 960;
    public static final int LOGO_HEIGHT = 386;
    
    private static final int NUMBER_OF_TILED_BACKGROUND_TEXTURE = 7;
    private TextureRegion background;
    private Texture btnPlay;
    private Texture btnExit;
    private Texture btnRanking;
    private Texture btnCredits;
    private Texture btnWorld;
    private Texture btnNormal;
    private Texture btnSurvival;
    private Texture btnBack;
    private Texture logo;
    private boolean selecionaModo = false;
    private Sound click1;
    private Sound click2;
    
    private Music musicaTema;

    /**
     * Cria uma nova tela de menu.
     *
     * @param game o jogo dono desta tela.
     * @param previous a tela de onde o usuário veio.
     */
    public MenuScreen(Game game, BaseScreen previous) {
        super(game, previous);
    }

    /**
     * Configura parâmetros da tela e instancia objetos.
     */
    @Override
    public void appear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);

        //instancia música tema
        musicaTema = Gdx.audio.newMusic(Gdx.files.internal("menu/meowautheme.mp3"));
        //ativa loop
        musicaTema.setLooping(true);
        //toca musica
        musicaTema.play();
        
        // instancia a textura e a região de textura (usada para repetir)
        background = new TextureRegion(new Texture("menu/menu-background.png"));
        btnPlay = new Texture("menu/button_jogar.png");
        btnExit = new Texture("menu/button_sair.png");
        btnRanking = new Texture("menu/button_ranking.png");
        btnCredits = new Texture("menu/button_creditos.png");
        btnWorld = new Texture("menu/button_mundo.png");
        btnNormal = new Texture("menu/button_normal.png");
        btnSurvival = new Texture("menu/button_survival.png");
        btnBack = new Texture("menu/button_voltar.png");
        logo = new Texture("menu/logo.png");
        click1 = Gdx.audio.newSound(Gdx.files.internal("menu/click1.mp3"));
        click2 = Gdx.audio.newSound(Gdx.files.internal("menu/click2.mp3"));
        // configura a textura para repetir caso ela ocupe menos espaço que o
        // espaço disponível
        background.getTexture().setWrap(
                Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // define a largura da região de desenho de forma que ela seja repetida
        // um número de vezes igual a NUMBER_OF_TILED_BACKGROUND_TEXTURE
        background.setRegionWidth(
                background.getTexture().getWidth()
                        * NUMBER_OF_TILED_BACKGROUND_TEXTURE);
        // idem para altura, porém será repetida um número de vezes igual a
        // NUMBER_OF_TILED_BACKGROUND_TEXTURE * razãoDeAspecto
        background.setRegionHeight(
                (int) (background.getTexture().getHeight()
                        * NUMBER_OF_TILED_BACKGROUND_TEXTURE
                        / Config.DESIRED_ASPECT_RATIO));
        
    }

    /**
     * Recebe <em>input</em> do jogador.
     */
    @Override
    public void handleInput() {
       
        
        //verifica se clique foi em algum botão
        if (Gdx.input.justTouched()) {
            Vector3 tmp=new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);
            camera.unproject(tmp);
            Rectangle playBounds=new Rectangle(BUTTONS_X, PLAY_Y, BUTTONS_WIDTH,BUTTONS_HEIGHT);
            Rectangle rankingBounds=new Rectangle(BUTTONS_X, RANKING_Y, BUTTONS_WIDTH,BUTTONS_HEIGHT);
            Rectangle creditsBounds=new Rectangle(BUTTONS_X, CREDITS_Y, BUTTONS_WIDTH,BUTTONS_HEIGHT);
            Rectangle worldBounds=new Rectangle(BUTTONS_X, WORLD_Y, BUTTONS_WIDTH,BUTTONS_HEIGHT);
            Rectangle exitBounds=new Rectangle(BUTTONS_X, EXIT_Y, BUTTONS_WIDTH,BUTTONS_HEIGHT);
            
            
            if(!selecionaModo){
                if(playBounds.contains(tmp.x,tmp.y)){
                    selecionaModo = true;
                    click2.play();
                }
                if(rankingBounds.contains(tmp.x,tmp.y)){
                    /*
                    
                    CHAMADA DA TELA DE RANKING
                    
                    */
                    transitionScreen(new RankingScreen(super.game, this),
                    TransitionScreen.Effect.FADE_IN_OUT, 1f);
            
                    
                    click2.play();
                }
                if(creditsBounds.contains(tmp.x,tmp.y)){
                    /*
                    
                    CHAMADA DA TELA DE CRÉDITOS
                    
                    */
                    click2.play();
                }
                if(worldBounds.contains(tmp.x,tmp.y)){
                    /*
                    
                    CHAMADA DA TELA OVERWORLD
                    
                    
                    */
                    click2.play();
                }                
                if(exitBounds.contains(tmp.x,tmp.y)){
                    click1.play();
                    System.exit(0);
                }
            }
            else{
                if(playBounds.contains(tmp.x,tmp.y)){
                    //CHAMADA DO MODO NORMAL
                    click2.play();
                    navigateToMicroGameScreen(false);
                }
                if(rankingBounds.contains(tmp.x,tmp.y)){
                    /*
                    
                    CHAMADA DO MODO SURVIVAL
                    
                    */
                    click2.play();
                    navigateToMicroGameScreen(true);
                }
                if(creditsBounds.contains(tmp.x,tmp.y)){
                    //Volta para os botões
                    selecionaModo = false;
                    click1.play();
                }
            }
            
        }
    }

    /**
     * Atualiza a lógica da tela.
     *
     * @param dt Tempo desde a última atualização.
     */
    @Override
    public void update(float dt) {
        float speed = dt * 0.25f;
        background.scroll(speed, -speed);
        
        
    }

    /**
     * Desenha o conteúdo da tela de Menu.
     */
    @Override
    public void draw() {
        batch.begin();
        batch.draw(background, 0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());
        batch.draw(logo, LOGO_X, LOGO_Y,
                LOGO_WIDTH,LOGO_HEIGHT);
        
        if(!selecionaModo){
            batch.draw(btnPlay, BUTTONS_X, PLAY_Y,
                BUTTONS_WIDTH,BUTTONS_HEIGHT);
            batch.draw(btnRanking, BUTTONS_X, RANKING_Y,
                BUTTONS_WIDTH,BUTTONS_HEIGHT);
            batch.draw(btnCredits, BUTTONS_X, CREDITS_Y,
                BUTTONS_WIDTH,BUTTONS_HEIGHT);
            batch.draw(btnWorld, BUTTONS_X, WORLD_Y,
                BUTTONS_WIDTH,BUTTONS_HEIGHT);
            batch.draw(btnExit, BUTTONS_X, EXIT_Y,
                BUTTONS_WIDTH,BUTTONS_HEIGHT);
        }
        else{
            batch.draw(btnNormal, BUTTONS_X, PLAY_Y,
                BUTTONS_WIDTH,BUTTONS_HEIGHT);
            batch.draw(btnSurvival, BUTTONS_X, RANKING_Y,
                BUTTONS_WIDTH,BUTTONS_HEIGHT);
            batch.draw(btnBack, BUTTONS_X, CREDITS_Y,
                BUTTONS_WIDTH,BUTTONS_HEIGHT);
        }
        
       
        /*drawCenterAlignedText("Pressione qualquer tecla para jogar",
                viewport.getWorldHeight() * 0.35f);*/
        batch.end();
    }

    /**
     * Navega para a tela de jogo.
     */
    private void navigateToMicroGameScreen(boolean isSurvival) {
        transitionScreen(new PlayingGamesScreen(super.game, this, isSurvival), 
                        TransitionScreen.Effect.FADE_IN_OUT, 1f);
        
        //game.setScreen(new PlayingGamesScreen(game, this));
    }


    /**
     * Libera os recursos necessários para esta tela.
     */
    @Override
    public void cleanUp() {
        background.getTexture().dispose();
        musicaTema.stop();
    }

}