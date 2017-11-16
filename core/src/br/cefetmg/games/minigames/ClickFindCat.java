package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import static com.badlogic.gdx.graphics.VertexAttribute.Position;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer.Task;

/**
 *
 * @author Pedro
 */
public class ClickFindCat extends MiniGame {

    private Texture catTexture;
    private Texture ratTexture;
    private Texture miraTexture;
    private Sprite miraSprite;
    private Sprite catSprite;
    private Rat rat;
    private Sound meawSound;
    private Sound scaredMeawSound;
    private Sound happyMeawSound;
    private float initialCatScale;
    private float CatScaleX;
    private float CatScaleY;
    private float hipotenuzaDaTela;
    private float difficulty;
    private float tempoDeAnimacao;
    
    public ClickFindCat(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
        this.difficulty = difficulty;
    }

    @Override
    protected void onStart() {
        tempoDeAnimacao = 0;
        hipotenuzaDaTela = viewport.getScreenWidth() * viewport.getScreenWidth()
                + viewport.getScreenHeight() * viewport.getScreenHeight();
        
        initialCatScale = 0.5f*DifficultyCurve.LINEAR_NEGATIVE.getCurveValue(difficulty);
        CatScaleX = initialCatScale*(float) Math.pow((double)(viewport.getWorldWidth()/1280),(double)initialCatScale);
        CatScaleY = initialCatScale*(float) Math.pow((double)(viewport.getWorldHeight()/720),(double)initialCatScale);
        catTexture = assets.get("ClickFindCat/gatinho-grande.png", Texture.class);
        ratTexture = assets.get("ClickFindCat/crav_rat.png", Texture.class);
        miraTexture = assets.get("ClickFindCat/target.png", Texture.class);
        miraSprite = new Sprite(miraTexture);
        miraSprite.setScale(1.0f);
        miraSprite.setOriginCenter();
        meawSound = assets.get("ClickFindCat/cat-meow.wav", Sound.class);
        scaredMeawSound = assets.get("ClickFindCat/ScaredCat.wav", Sound.class);
        happyMeawSound = assets.get("ClickFindCat/YAY.mp3", Sound.class);
        initializeCat();
        initializeRat();
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

    }

    public void initializeCat() {
        Vector2 posicaoInicial = new Vector2(MathUtils.random(0, viewport.getWorldWidth() - catTexture.getWidth()),
                MathUtils.random(0, viewport.getWorldHeight() - catTexture.getHeight()));

        catSprite = new Sprite(catTexture);
        catSprite.setPosition(posicaoInicial.x, posicaoInicial.y);
        System.out.println(difficulty+" "+initialCatScale +" " + CatScaleX + " " + CatScaleY);
        catSprite.setScale(CatScaleX, CatScaleY);
        
    }
    
    public void initializeRat() {
        rat = new Rat(ratTexture);
        
        
    }

    @Override
    public void onHandlePlayingInput() {
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        this.miraSprite.setPosition(click.x - this.miraSprite.getWidth() / 2, click.y - this.miraSprite.getHeight() / 2);
        if (Gdx.input.justTouched()) {
            if (catSprite.getBoundingRectangle().overlaps(miraSprite.getBoundingRectangle())) {
                super.challengeSolved();
            } else {
                float distancia = click.dst2(catSprite.getX(), catSprite.getY());
                float intensidade = (float) Math.pow((1 - distancia / hipotenuzaDaTela), 4);
                meawSound.play(intensidade);

            }

        }
    }

    @Override
    public void onUpdate(float dt) {
        if (super.getState() == MiniGameState.PLAYER_FAILED) {
            scaredMeawSound.play();
        } else if (rand.nextInt() % 4 == 1 && super.getState() == MiniGameState.PLAYER_SUCCEEDED) {
            happyMeawSound.play();
        }
        tempoDeAnimacao += Gdx.graphics.getDeltaTime();
        timer.scheduleTask(new Task () {
            @Override
            public void run () {
                rat.movimento();
            }
        }, 0, 60);
        rat.andar(viewport.getWorldWidth(), viewport.getWorldHeight());
        
    }

    @Override
    public void onDrawGame() {
        catSprite.draw(batch);
        if (super.getState() == MiniGameState.PLAYER_FAILED || super.getState() == MiniGameState.PLAYER_SUCCEEDED) {
            //catSprite.draw(batch);
            //System.out.println("Achou achou");
        }
         rat.render(batch,tempoDeAnimacao);
        //Desenha a Mira
        miraSprite.draw(batch);

    }

    @Override
    public String getInstructions() {
        return "Ache o gato invisível";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
    static class Rat {
        
        private final TextureRegion[][] quadrosDeAnimacao;
        private final Animation<TextureRegion> andarParaFrente;
        private final Animation<TextureRegion> andarParaTras;
        private final Animation<TextureRegion> andarParaDireita;
        private final Animation<TextureRegion> andarParaEsquerda;
        private Vector2 posicao;
        private Direcao direcao;
        private TipoDeMovimento tipoDeMovimento;
        
        public Rat (Texture SpriteSheet) {
         posicao = new Vector2(0,0);
         direcao = Direcao.CIMA;
         tipoDeMovimento = TipoDeMovimento.VAGAR;
         quadrosDeAnimacao = TextureRegion.split (SpriteSheet,42,32);
            System.out.println(+quadrosDeAnimacao.length);
         andarParaTras = new Animation<TextureRegion>(0.1f, 
        		quadrosDeAnimacao[0][0],
        		quadrosDeAnimacao[0][1],
        		quadrosDeAnimacao[0][2]);
        andarParaTras.setPlayMode(PlayMode.LOOP_PINGPONG);
        
        andarParaFrente = new Animation<TextureRegion>(0.1f, new TextureRegion[]{
        		quadrosDeAnimacao[2][0],
        		quadrosDeAnimacao[2][1],
        		quadrosDeAnimacao[2][2]
        });
        andarParaFrente.setPlayMode(PlayMode.LOOP_PINGPONG);
        
        andarParaDireita = new Animation<TextureRegion>(0.1f, new TextureRegion[]{
        		quadrosDeAnimacao[1][0],
        		quadrosDeAnimacao[1][1],
        		quadrosDeAnimacao[1][2]
        });
        andarParaDireita.setPlayMode(PlayMode.LOOP_PINGPONG);
        
        andarParaEsquerda = new Animation<TextureRegion>(0.1f, new TextureRegion[]{
        		quadrosDeAnimacao[3][0],
        		quadrosDeAnimacao[3][1],
        		quadrosDeAnimacao[3][2]
        });
        andarParaEsquerda.setPlayMode(PlayMode.LOOP_PINGPONG);
        }
        
        public void movimento () {
            switch(tipoDeMovimento) {
                case VAGAR:
                    MudarDirecao();
                    break;
                case FUGIR:
                    fugir();
                    break;
                default:
                    break;
            }
        }
        
        public void andar (float larguraDoMundo, float alturaDoMundo) {
            float passo = 50;
            switch (direcao) {
                case DIREITA:
                    posicao.add(passo, 0);
                    break;
                case ESQUERDA:
                    posicao.add(-passo, 0);
                    break;
                case CIMA:
                    posicao.add(0, passo);
                    break;
                case BAIXO:
                    posicao.add(0, -passo);
                    break;
                    default:
                        break;
                    
            }
             if (posicao.x < 0) {posicao.x = 0; direcao = Direcao.DIREITA;}
            else if (posicao.x > larguraDoMundo) {posicao.x = larguraDoMundo; direcao = Direcao.ESQUERDA; }

            if (posicao.y < 0) {posicao.y = 0; direcao = Direcao.CIMA;}
            else if (posicao.y > larguraDoMundo) posicao.y = larguraDoMundo; direcao = Direcao.BAIXO;
        }
        
        public void MudarDirecao () {
            float chance = (float) Math.random();
            switch (direcao) {
                case DIREITA:
                    if (chance < 0.25) {
                        direcao = Direcao.DIREITA;
                    } else if (chance < 0.5) {
                        direcao = Direcao.CIMA;
                    } else if (chance < 0.75) {
                        direcao = Direcao.BAIXO;
                    } else {
                        direcao = Direcao.ESQUERDA;
                    }   break;
                case ESQUERDA:
                    if (chance < 0.25) {
                        direcao = Direcao.ESQUERDA;
                    } else if (chance < 0.5) {
                        direcao = Direcao.BAIXO;
                    } else if (chance < 0.75) {
                        direcao = Direcao.CIMA;
                    } else {
                        direcao = Direcao.DIREITA;
                    }   break;
                case CIMA:
                    if (chance < 0.25) {
                        direcao = Direcao.CIMA;
                    } else if (chance < 0.5) {
                        direcao = Direcao.ESQUERDA;
                    } else if (chance < 0.75) {
                        direcao = Direcao.DIREITA;
                    } else {
                        direcao = Direcao.BAIXO;
                    }   break;
                case BAIXO:
                    if (chance < 0.25) {
                        direcao = Direcao.BAIXO;
                    } else if (chance < 0.5) {
                        direcao = Direcao.DIREITA;
                    } else if (chance < 0.75) {
                        direcao = Direcao.ESQUERDA;
                    } else {
                        direcao = Direcao.CIMA;
                    }   break;
                default:
                    break;
            }
            
           
        }
        
        public void fugir () {
            
        }
        
        public float randomBinomial() {
            return (float)(Math.random() - Math.random());
        }
        
        public void render (SpriteBatch batch, float tempoDeAnimacao) {
            switch (direcao) {
                case CIMA:
                    // Cima
                    batch.draw((TextureRegion)andarParaFrente.getKeyFrame(tempoDeAnimacao),posicao.x,posicao.y);
                    break;
                case BAIXO:
                    // Baixo
                    batch.draw((TextureRegion)andarParaTras.getKeyFrame(tempoDeAnimacao),posicao.x,posicao.y);
                    break;
                case ESQUERDA:
                    // Esquerda
                    batch.draw((TextureRegion)andarParaEsquerda.getKeyFrame(tempoDeAnimacao),posicao.x,posicao.y);
                    break;
                case DIREITA:
                    // Direita
                    batch.draw((TextureRegion)andarParaDireita.getKeyFrame(tempoDeAnimacao),posicao.x,posicao.y);
                    break;
                default:
                    break;
            }
        }
        
        static enum Direcao {
            CIMA, BAIXO, ESQUERDA, DIREITA;
        }
        static enum TipoDeMovimento {
            VAGAR, FUGIR;
        }
        
    }
}
