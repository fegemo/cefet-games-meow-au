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
import java.util.ArrayList;
import com.badlogic.gdx.math.Circle;

/**
 *
 * @author Pedro @author Arthurrr
 */
public class ClickFindCat extends MiniGame {

    private Texture catTexture;
    private Texture ratTexture;
    private Texture miraTexture;
    private Sprite miraSprite;
    private Sprite catSprite;
    private Rat rat;
    private ArrayList<Rat> Ratos;
    private Sound meawSound;
    private Sound scaredMeawSound;
    private Sound happyMeawSound;
    private float numeroDeRatos = 100;
    private float initialCatScale;
    private float CatScaleX;
    private float CatScaleY;
    private float hipotenuzaDaTela;
    private float difficulty;
    private float tempoDeAnimacao;
    float height;
    float width;

    public ClickFindCat(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
        this.difficulty = difficulty;
    }

    @Override
    protected void onStart() {

        Ratos = new ArrayList<Rat>();

        tempoDeAnimacao = 0;
        hipotenuzaDaTela = viewport.getScreenWidth() * viewport.getScreenWidth()
                + viewport.getScreenHeight() * viewport.getScreenHeight();

        initialCatScale = 0.4f * DifficultyCurve.LINEAR_NEGATIVE.getCurveValue(difficulty);
        CatScaleX = initialCatScale * (float) viewport.getWorldWidth() / viewport.getScreenWidth();
        CatScaleY = initialCatScale * (float) viewport.getWorldHeight() / viewport.getScreenHeight();

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
        //initializeRat();
        for (int i = 0; i < numeroDeRatos; i++) {
            Ratos.add(initializeRat());
        }
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

    }

    public void initializeCat() {
        Vector2 posicaoInicial = new Vector2(MathUtils.random(0, viewport.getWorldWidth() - catTexture.getWidth()),
                MathUtils.random(0, viewport.getWorldHeight() - catTexture.getHeight()));
        catSprite = new Sprite(catTexture);
        catSprite.setPosition(posicaoInicial.x, posicaoInicial.y);
        catSprite.setScale(CatScaleX, CatScaleY);

    }
    
    public float randomBinomial() {
            return (float) (Math.random() - Math.random());
    }
    
    public Rat initializeRat() {
        // Os seguintes processos utilizados foram criados para distribuir melhor a concentração de Ratos na tela;
        float alturaDaTela=viewport.getWorldHeight() - ratTexture.getHeight();
        float larguraDaTela=viewport.getWorldWidth() - ratTexture.getWidth();
        float posicaoX;
        float posicaoY;
        
        if( randomBinomial() > randomBinomial()){
           posicaoY=MathUtils.random(0,alturaDaTela/2);
        }else{
           posicaoY= MathUtils.random(alturaDaTela/2,alturaDaTela);
        }
        if( randomBinomial()> randomBinomial()){
            posicaoX=MathUtils.random(0,larguraDaTela/2);
        }else{
            posicaoX=MathUtils.random(larguraDaTela/2,larguraDaTela);
        }
        //
        Vector2 posicaoInicial = new Vector2(posicaoX,posicaoY);
        Vector2 alvo = new Vector2(catSprite.getX() + (catSprite.getWidth() / 2), catSprite.getY() + (catSprite.getHeight() / 2));// centro do gato.
        return new Rat(ratTexture, alvo, posicaoInicial);
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

    public void CheckCatRatDistance() {
        for (Rat Rato : Ratos) {
            Rato.checkDistance();
            if (Rato.ratWasRunning) {
                Rato.fuga(miraSprite.getX(), miraSprite.getY());
            } else {
                Rato.vagabundo();
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        CheckCatRatDistance();
        if (super.getState() == MiniGameState.PLAYER_FAILED) {
            scaredMeawSound.play();
        } else if (rand.nextInt() % 4 == 1 && super.getState() == MiniGameState.PLAYER_SUCCEEDED) {
            happyMeawSound.play();
        }
        tempoDeAnimacao += Gdx.graphics.getDeltaTime();
        for (Rat Rato : Ratos) {
            Rato.movimento(dt, viewport.getWorldWidth(), viewport.getWorldHeight());
        }
    }

    @Override
    public void onDrawGame() {
        //catSprite.draw(batch);
        if (super.getState() == MiniGameState.PLAYER_FAILED || super.getState() == MiniGameState.PLAYER_SUCCEEDED) {
            catSprite.draw(batch);
            //System.out.println("Achou achou");
        }
        for (Rat Rato : Ratos) {
            Rato.render(batch, tempoDeAnimacao);
        }

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
        private final Animation<TextureRegion> andarParaDireita;
        private final Animation<TextureRegion> andarParaTras;
        private final Animation<TextureRegion> andarParaEsquerda;
        private final Animation<TextureRegion> andarParaCima;
        private Vector2 posicao;
        private Direcao direcao;
        private Vector2 Steering;
        private Vector2 velocidade;
        public TipoDeMovimento tipoDeMovimento;
        private Vector2 alvo;
        public boolean ratWasRunning;
        public int ratRunning;

        public Rat(Texture SpriteSheet, Vector2 alvo, Vector2 posicao) {
            this.ratWasRunning = false;
            this.ratRunning = 0;
            this.alvo = new Vector2(alvo.x + 16, alvo.y + 16);
            this.posicao = posicao;
            direcao = Direcao.CIMA;
            tipoDeMovimento = TipoDeMovimento.FUGIR;
            quadrosDeAnimacao = TextureRegion.split(SpriteSheet, 42, 32);
            //System.out.println(+quadrosDeAnimacao.length);
            andarParaTras = new Animation<TextureRegion>(0.1f,
                    quadrosDeAnimacao[0][0],
                    quadrosDeAnimacao[0][1],
                    quadrosDeAnimacao[0][2]);
            andarParaTras.setPlayMode(PlayMode.LOOP_PINGPONG);

            andarParaDireita = new Animation<TextureRegion>(0.1f, new TextureRegion[]{
                quadrosDeAnimacao[2][0],
                quadrosDeAnimacao[2][1],
                quadrosDeAnimacao[2][2]
            });
            andarParaDireita.setPlayMode(PlayMode.LOOP_PINGPONG);

            andarParaEsquerda = new Animation<TextureRegion>(0.1f, new TextureRegion[]{
                quadrosDeAnimacao[1][0],
                quadrosDeAnimacao[1][1],
                quadrosDeAnimacao[1][2]
            });
            andarParaEsquerda.setPlayMode(PlayMode.LOOP_PINGPONG);

            andarParaCima = new Animation<TextureRegion>(0.1f, new TextureRegion[]{
                quadrosDeAnimacao[3][0],
                quadrosDeAnimacao[3][1],
                quadrosDeAnimacao[3][2]
            });
            andarParaCima.setPlayMode(PlayMode.LOOP_PINGPONG);
        }

        public void fuga(float x, float y) {
            Steering = alvo;
            this.tipoDeMovimento = TipoDeMovimento.FUGIR;
        }

        public void vagabundo() {
            this.tipoDeMovimento = TipoDeMovimento.VAGAR;
        }

        public void movimento(float dt, float larguraDoMundo, float alturaDoMundo) {
            checkDistance();
            switch (tipoDeMovimento) {
                case VAGAR:
                    MudarDirecao();
                    andar(dt, larguraDoMundo, alturaDoMundo);
                    break;
                case FUGIR:
                    fugir(dt);
                    break;
                default:
                    break;
            }
        }

        public void checkDistance() {
            Vector2 centroDoGato = new Vector2(alvo.x, alvo.y);
            float distanciaGatoRato = centroDoGato.dst(new Vector2(this.posicao));
            float minDistanciaStartRunning = 200;
            if (distanciaGatoRato <= minDistanciaStartRunning) {
                this.ratWasRunning = true;
            } else if (distanciaGatoRato > 1.2*minDistanciaStartRunning) {
                this.ratWasRunning = false;
            }
        }

        public void andar(float dt, float larguraDoMundo, float alturaDoMundo) {
            float ande = randomBinomial();
            float passo = 50 * (float) Math.random();
            passo += passo* dt;
            if (ande > 0.6) {
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
            }
            if (posicao.x < 10) {
                posicao.x = 50;
                direcao = Direcao.DIREITA;
                saiuDaTela();
            } else if (posicao.x > larguraDoMundo - 10) {
                posicao.x = larguraDoMundo - 50;
                direcao = Direcao.ESQUERDA;
                saiuDaTela();
            }

            if (posicao.y < 10) {
                posicao.y = 50;
                direcao = Direcao.CIMA;
                saiuDaTela();
                posicao.add(velocidade);
            } else if (posicao.y > larguraDoMundo - 100) {
                posicao.y = larguraDoMundo - 50;
                direcao = Direcao.BAIXO;
                saiuDaTela();
                posicao.add(velocidade);
            }

        }

        public void MudarDirecao() {
            float chance = (float) Math.random();
            if (chance < 0.25) {
                direcao = Direcao.DIREITA;
            } else if (chance < 0.5) {
                direcao = Direcao.CIMA;
            } else if (chance < 0.75) {
                direcao = Direcao.BAIXO;
            } else {
                direcao = Direcao.ESQUERDA;
            }
        }

        public void fugir(float dt) {

            if (ratRunning < 2) {
                Vector2 position = new Vector2(posicao.x, posicao.y);
                velocidade = position.sub(alvo);
                velocidade.nor();
                velocidade.scl(10 * (float) Math.random()+10 * (float) Math.random()*dt);
            }
            posicao.add(velocidade);
            this.ratRunning++;
            if (ratRunning == 12) {
                this.tipoDeMovimento = TipoDeMovimento.VAGAR;
                this.ratRunning = 0;
            }
        }

        public void saiuDaTela() {
            Vector2 alvo1 = new Vector2(this.alvo.x, this.alvo.y);
            Vector2 ajuda = new Vector2(posicao.x, posicao.y);
//            velocidade.nor();
            velocidade = ajuda.add(alvo1);
        }

        public float randomBinomial() {
            return (float) (Math.random() - Math.random());
        }

        public void render(SpriteBatch batch, float tempoDeAnimacao) {
            switch (direcao) {
                case DIREITA:
                    batch.draw((TextureRegion) andarParaDireita.getKeyFrame(tempoDeAnimacao), posicao.x, posicao.y);
                    break;
                case BAIXO:
                    batch.draw((TextureRegion) andarParaTras.getKeyFrame(tempoDeAnimacao), posicao.x, posicao.y);
                    break;
                case CIMA:
                    batch.draw((TextureRegion) andarParaCima.getKeyFrame(tempoDeAnimacao), posicao.x, posicao.y);
                    break;
                case ESQUERDA:
                    batch.draw((TextureRegion) andarParaEsquerda.getKeyFrame(tempoDeAnimacao), posicao.x, posicao.y);
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
