package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

/**
 *
 * @author Luiza-Pedro
 */
public class SpyFish extends MiniGame {

    //texturas
    private final Texture texturaFish;
    private Texture texturaFundo;
    private final Texture texturaMemoCard;
    private final Texture textureFishSheet;
   
    private ArrayList<MemoryChip> chip;

    private static SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    //elementos de logica
    private Fish fish;

    private int MAX_CHIPS;
    private int NUM_CHIPS_TO_TAKE;
    private float VELOCIDADE_MAX_CHIP;
    private static int NUM_DE_CHIPS_PERDIDO = 0;

    public SpyFish(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 20000f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);

        this.texturaFish = assets.get("spy-fish/fish.png", Texture.class);
        this.texturaMemoCard = assets.get("spy-fish/card.png", Texture.class);
        this.texturaFundo = assets.get("spy-fish/ocean.jpeg", Texture.class);
        this.textureFishSheet = assets.get("spy-fish/fishsheet.png", Texture.class);

        this.texturaFundo = assets.get("spy-fish/ocean.jpeg", Texture.class);
   
        batch = new SpriteBatch();

    }

    @Override
    protected void onStart() {
        chip = new ArrayList<MemoryChip>();

        for (int i = 0; i < this.MAX_CHIPS; i++) {
            chip.add(new MemoryChip(texturaMemoCard, this.VELOCIDADE_MAX_CHIP));
        }

        this.fish = new Fish(this.texturaFish);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.MAX_CHIPS = (int) DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty, 7, 15);
        this.NUM_CHIPS_TO_TAKE = (int) DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 1, 5);
        this.VELOCIDADE_MAX_CHIP = (float) DifficultyCurve.S.getCurveValueBetween(difficulty, 1, 9);
    }

    @Override
    public void onHandlePlayingInput() {
        // move o peixe
        this.fish.updateAccordingToTheMouse(getMousePosInGameWorld().x, getMousePosInGameWorld().y);
    }

    @Override
    public void onUpdate(float dt) {
        fish.update(dt);
        for (Iterator<MemoryChip> iterator = chip.iterator(); iterator.hasNext();) {
            MemoryChip mc = iterator.next();
            if (mc.collidesWith(this.fish)) {
                //se o peixe pegar um cartão de memoria
                iterator.remove();
                if (chip.size() == (this.MAX_CHIPS - this.NUM_CHIPS_TO_TAKE)) {
                    super.challengeSolved();
                }
            }

            if (mc.getPositionMemoryCard().y < -1) {
                NUM_DE_CHIPS_PERDIDO++;
                if (NUM_DE_CHIPS_PERDIDO > (this.MAX_CHIPS + this.NUM_CHIPS_TO_TAKE)) {
                    // se chegar nessa parte do codigo, é pq não tem como mais pegar o numero minimo
                    //de chips
                    super.challengeFailed();
                    break;
                }
            }
        }
    }

    private Vector3 getMousePosInGameWorld() {
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        return click;
    }

    @Override
    public void onDrawGame() {
        Vector3 mause = getMousePosInGameWorld();

        update(Gdx.graphics.getDeltaTime());
        batch.begin();
        batch.draw(texturaFundo, 0f, 0f, 1280f, 720f);

        this.fish.render(batch, getMousePosInGameWorld().x, getMousePosInGameWorld().y);
        for (MemoryChip chip : chip) {
            chip.render(batch);
        }
        batch.end();


        /*this.fish.render_area_collision();
        for (MemoryChip chip : this.chip) {
            //mostra os circulos de colisão
            chip.render_area_collision();
        }*/
        //this.fish.render_area_collision();
//        for (MemoryChip chip : this.chip) {
//            //mostra os circulos de colisão
//            chip.render_area_collision();
//        }
    }

    @Override
    public String getInstructions() {
        if (this.NUM_CHIPS_TO_TAKE == 1) {
            return "Pegue pelo menos um cartão de memória";
        } else if (this.NUM_CHIPS_TO_TAKE == 2) {
            return "Pegue pelo menos dois cartões de memória";
        } else if (this.NUM_CHIPS_TO_TAKE == 3) {
            return "Pegue pelo menos três cartões de memória";
        } else if (this.NUM_CHIPS_TO_TAKE == 4) {
            return "Pegue pelo menos quatro cartões de memória";
        } else if (this.NUM_CHIPS_TO_TAKE == 5) {
            return "Pegue pelo menos cinco cartões de memória";
        } else {
            return "";
        }
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }

}

class Fish extends Sprite implements Collidable {

    private Vector2 alvo;
    private Pose pose;

    private int lado;
    private Sprite sprite;
    private TextureRegion[][] region;
    private Circle circle;
    private ShapeRenderer shapeRenderer;

    private float x_tempo = 0.0f;
    private boolean aux = true;

    public Fish(Texture texture) {

        this.sprite = new Sprite(texture);
        this.sprite = new Sprite(texture);
        this.shapeRenderer = new ShapeRenderer();
        this.sprite.setPosition(20.0f, 220.0f);
        this.circle = new Circle(this.sprite.getX() + (this.sprite.getWidth() / 2), this.sprite.getY() + (this.sprite.getHeight() / 2),
                (float) Math.sqrt((Math.pow(this.sprite.getHeight() / 2, 2)) + Math.pow(this.sprite.getWidth() / 2, 2)));
        alvo = new Vector2(20, 220);
        this.pose = new Pose(alvo);
    }

    public void update(float x, float y) {
        //atualiza a posição do peixe
        this.sprite.setPosition(x, y);
        //atualiza a posicao do retangulo de colisao
        this.circle.setPosition(x + (this.sprite.getWidth() / 2), y + (this.sprite.getHeight() / 2));
        if (aux) {
            this.x_tempo = x;
            aux = !aux;
        }

        if (this.x_tempo > x) {
            if (!this.sprite.isFlipX()) {
                this.sprite.flip(true, false);
            }
        } else if (this.sprite.isFlipX()) {
            this.sprite.flip(true, false);
        }

    }

    public void update(float dt) {
        // pergunta ao algoritmo de movimento (comportamento) 
        // para onde devemos ir
        Buscar b = new Buscar();
        if (this.alvo != null) {
            Direcionamento direcionamento = b.guiar(this.pose, this.alvo, this.circle.radius, 10, 1);
            // faz a simulação física usando novo estado da entidade cinemática
            pose.atualiza(direcionamento, dt);
            update(pose.posicao.x, pose.posicao.y);
        }
    }

    public void render(SpriteBatch sb, float x, float y) {
        float x_sprite = this.sprite.getX();
        float y_sprite = this.sprite.getY();
        if ((1280 >= x_sprite && 0 <= x_sprite) && (720 >= y_sprite && 0 <= y_sprite)) {
            //se estiver dentro da tela
            //update(x,y);
            if ((1280 >= this.sprite.getX() && 0 <= this.sprite.getX()) && (720 >= this.sprite.getY() && 0 <= this.sprite.getY())) {
                // se dentro da tela
                this.sprite.draw(sb);
            }
        }
    }

    /*    public void updateAccordingToTheMouse(float x , float y){
        if (Gdx.input.isTouched()||Gdx.input.justTouched()) {     
        //se clicar com o mouse sobre o objeto Fish
            if ( (x >= (this.circle.x-this.circle.radius) && x <= (this.circle.x+this.circle.radius))
                    && (y >= (this.circle.y-this.circle.radius) && y <= (this.circle.y+this.circle.radius)) ){
                // coloca o ponteiro do mouse no centro da sprite
                float delta_x = (x - this.circle.x);
                float delta_y = (y - this.circle.y);
                
                update( this.sprite.getX() + delta_x , this.sprite.getY() + delta_y );
                
                }
            }     
        }
     */
    public void updateAccordingToTheMouse(float x, float y) {
        if (Gdx.input.isTouched() || Gdx.input.justTouched()) {
            alvo = new Vector2(x, y);
        }
    }

    public void render_area_collision() {

        // metodo para mostrar circulo de colisão
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.identity();
        this.shapeRenderer.setColor(Color.RED);
        this.shapeRenderer.circle(this.circle.x, this.circle.y, this.circle.radius);
        this.shapeRenderer.end();

    }

    @Override
    public boolean collidesWith(Collidable other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isOutOfBounds(Rectangle area) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Rectangle getMinimumBoundingRectangle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Circle getMinimumEnclosingBall() {
        return this.circle;
    }
}

class MemoryChip implements Collidable {

    private float radius;
    private final Vector2 position = new Vector2();

    private final Circle circle;
    private final Sprite sprite;

    private final Float Velocidade_Queda;
    private final Float Rotation;

    private ShapeRenderer shapeRenderer;

    public MemoryChip(Texture texture, float velocidade) {
        Random r = new Random();

        this.sprite = new Sprite(texture);
        this.circle = new Circle();
        this.shapeRenderer = new ShapeRenderer();

        this.position.x = (float) new Random().nextInt(1271);
        this.position.y = 600.0f + (float) new Random().nextInt(120);

        this.circle.x = this.position.x + 12.5f;
        this.circle.y = this.position.y + 17f;

        this.Velocidade_Queda = 1 + (float) new Random().nextFloat() * (velocidade - 1);
        this.Rotation = (-30) + (float) new Random().nextInt(20);

        this.circle.radius = 21.1f;

        this.sprite.setPosition(this.position.x, this.position.y);

    }

    public void update() {

        //atualiza posição do memo card
        this.position.y -= this.Velocidade_Queda;
        this.sprite.rotate((float) 10);
        this.sprite.setPosition(this.position.x, this.position.y);

        // atualiza a area de colisão
        this.circle.y = this.position.y + 17f;
        this.circle.x = this.position.x + 12.5f;

    }

    public void render_area_collision() {

        // metodo para mostrar o circulo de colisão
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.identity();
        this.shapeRenderer.setColor(Color.RED);
        this.shapeRenderer.circle(this.circle.x, this.circle.y, this.circle.radius);
        this.shapeRenderer.end();

    }

    public void render(SpriteBatch sb) {

        // se dentro da tela e sem colisão com other - desenha
        if (this.position.y >= -35) {
            this.sprite.draw(sb);
            update();
        }
    }

    public Vector2 getPositionMemoryCard() {
        return this.position;
    }

    @Override
    public boolean collidesWith(Collidable other) {
        if (other instanceof Fish) {
            // se ocorrer colisão com objeto Fish
            return Collision.circlesOverlap(other.getMinimumEnclosingBall(), this.circle);

        } else {
            return false;
        }
    }

    @Override
    public Circle getMinimumEnclosingBall() {
        return this.circle;
    }

    @Override
    public boolean isOutOfBounds(Rectangle area) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Rectangle getMinimumBoundingRectangle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

class Pose {

    public Vector2 posicao;
    public Vector2 velocidade;
    public float orientacao;

    public Pose() {
        this(new Vector2(0, 0), 0);
    }

    public Pose(Vector2 position) {
        this.posicao = position;
        this.velocidade = new Vector2(0, 0);
    }

    public Pose(Vector2 posicao, float orientacao) {
        this.posicao = posicao;
        this.orientacao = orientacao;
    }

    /**
     * Retorna um vetor que representa a direção que o ângulo "orientação" desta
     * pose representa.
     *
     * @return um vetor na mesma direção que o ângulo.
     */
    public Vector2 getOrientacaoComoVetor() {
        return new Vector2(
                (float) Math.cos(orientacao),
                (float) Math.sin(orientacao));
    }

    /**
     * Define a orientação de forma que ela seja a mesma da direção do vetor
     * velocidade.
     *
     * @param velocidade vetor velocidade, indicando a direção para onde esta
     * pose deve se orientar (rotacionar).
     */
    public void olharNaDirecaoDaVelocidade(Vector2 velocidade) {
        if (velocidade.len2() > 0) {
            orientacao = (float) Math.atan2(velocidade.y, velocidade.x);
        }
    }

    public void atualiza(Direcionamento guia, float delta) {
        // em vez de usar as componentes (ficar "sujando as mãos"), sempre
        // prefira programar de forma encapsulada ;)
        velocidade.add(guia.aceleracao.scl(delta));
        posicao.add(velocidade.scl(delta));
//        posicao.x += guia.velocidade.x * delta;
//        posicao.y += guia.velocidade.y * delta;
//        posicao.z += guia.velocidade.z * delta;
        orientacao += guia.rotacao * delta;
        orientacao = orientacao % ((float) Math.PI * 2);
    }
}

class Direcionamento {

    public Vector2 aceleracao;  // velocidade linear
    public double rotacao;      // velocidade angular

    public Direcionamento() {
        aceleracao = new Vector2();
        rotacao = 0;
    }
}

/**
 * Guia o agente na direção do alvo.
 *
 * @inspirado no tp de cinematica
 */
class Buscar {

    private final float maxAceleracao;
    private final float constanteVelocidade;

    public Buscar(float aceleracaoMax, float velocidadeConst) {
        this.maxAceleracao = aceleracaoMax;
        this.constanteVelocidade = velocidadeConst;
    }

    public Buscar() {
        this.maxAceleracao = 5000;
        this.constanteVelocidade = 0;
    }

    public Direcionamento guiar(Pose agente, Vector2 alvo) {
        Direcionamento output = new Direcionamento();
        Vector2 aux = alvo;
        //acho q é algo assim
        aux.sub(agente.posicao);
        if (aux.len2() > maxAceleracao * maxAceleracao) {//verifica o tamanho do vetor se for mto grande normaliza e multiplica pela maxAceleracao
            aux.nor();
            aux.scl(maxAceleracao);
        }
        output.aceleracao = aux;
        Vector2 auxV = new Vector2(agente.velocidade);
        output.aceleracao.sub(auxV.scl(constanteVelocidade));//aceleracao = forca/m - kv
        //a rotacao ou direcao do bichinho nos podemos considerar q esta na direcao da velocidade
        //mas por meio da duvida acho q seria algo assim
//        output.rotacao=output.aceleracao.angleRad();
        output.rotacao = 0;
        return output;
    }

    public Direcionamento guiar(Pose agente, Vector2 alvo, float raioAgente, float raioAlvoDesacelerar, float raioAlvoChegar) {
        Direcionamento output = new Direcionamento();
        Vector2 posicaoDoAlvo = new Vector2(alvo);
        //acho q é algo assim
        posicaoDoAlvo.sub(agente.posicao);
        if (posicaoDoAlvo.len2() > maxAceleracao * maxAceleracao) {
            //verifica o tamanho do vetor se for mto grande normaliza e multiplica pela maxAceleracao
            posicaoDoAlvo.nor();
            posicaoDoAlvo.scl(maxAceleracao);
        }
        Vector2 posicaoAgente = new Vector2(agente.posicao);
        if (posicaoAgente.dst2(alvo) < (Math.pow(raioAgente + raioAlvoDesacelerar, 2))) {
            if (posicaoAgente.dst2(alvo) < ((raioAgente + raioAlvoChegar) * (raioAgente + raioAlvoChegar))) {
                posicaoDoAlvo = new Vector2(0, 0);
            } else {
                posicaoDoAlvo.scl(1 / 10);
            }
        }
        output.aceleracao = posicaoDoAlvo;
        Vector2 auxV = new Vector2(agente.velocidade);
        output.aceleracao.sub(auxV.scl(constanteVelocidade));//aceleracao = forca/m - kv

        //a rotacao ou direcao do bichinho nos podemos considerar q esta na direcao da velocidade
        //mas por meio da duvida acho q seria algo assim
//        output.rotacao=output.aceleracao.angleRad();
        output.rotacao = 0;
        return output;
    }
}
// -------------------------------------------------------------------------------------------------------------------------------------------------------

class Collision {

    private static Vector2 vector_c1;
    private static Vector2 vector_c2;

    /**
     * Verifica se dois círculos em 2D estão colidindo.
     *
     * @param c1 círculo 1
     * @param c2 círculo 2
     * @return true se há colisão ou false, do contrário.
     */
    public static final boolean circlesOverlap(Circle c1, Circle c2) {

        vector_c1 = new Vector2(c1.x, c1.y);
        vector_c2 = new Vector2(c2.x, c2.y);

        //se a distância entre os centros de cada Circle for menor ou igual a soma dos raios dos
        //Circles, então ocorreu uma colisão(true)
        if (vector_c1.dst2(vector_c2) <= ((c1.radius + c2.radius) * (c1.radius + c2.radius))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se dois retângulos em 2D estão colidindo. Esta função pode
     * verificar se o eixo X dos dois objetos está colidindo e então se o mesmo
     * ocorre com o eixo Y.
     *
     * @param r1 retângulo 1
     * @param r2 retângulo 2
     * @return true se há colisão ou false, do contrário.
     */
    /**
     * Verifica se dois retângulos em 2D estão colidindo. Esta função pode
     * verificar se o eixo X dos dois objetos está colidindo e então se o mesmo
     * ocorre com o eixo Y.
     *
     * @param a
     * @param b
     * @param r1 retângulo 1
     * @param r2 retângulo 2
     * @return true se há colisão ou false, do contrário.
     */
    public static final boolean lineOverlap(float aMin, float aMax, float bMin, float bMax) {
        //vector2 x=max y=min
        if (aMin < bMin && bMin <= aMax) {
            return true;
        }
        return bMin < aMin && aMin <= bMax;
    }

    /*
    public static final boolean lineOverlap(float aMin, float aMax, float bMin, float bMax){
        return aMax >= bMin && aMin <= bMax;
    }
    public static final boolean rectsOverlap(Rectangle r1, Rectangle r2) {
        return lineOverlap(, 0, 0, 0)
    }*/
    public static final boolean rectsOverlap(Rectangle r1, Rectangle r2) {
        if (lineOverlap(r1.x, r1.width + r1.x, r2.x, r2.width + r2.x)) {
            return lineOverlap(r1.y, r1.height + r1.y, r2.y, r2.height + r2.y);
        }
        return false;
    }

    public static final boolean circleRectCollision(Rectangle r2, Circle c1) {

        Vector2 ponto = new Vector2();
        Vector2 centroRet = new Vector2(r2.x + r2.width / 2, r2.y + r2.height / 2);
        //distancia entre centro do circulo e o centro do quadrado
        Vector2 dist = centroRet.sub(c1.x, c1.y);
        Vector2 eixoX = new Vector2(dist.x, 0);
        Vector2 eixoY = new Vector2(0, dist.y);
        //"clamped " acha um ponto na superficie do quadrado q é menor q a distancia
        eixoX.clamp(0, r2.width / 2);
        eixoY.clamp(0, r2.height / 2);
        ponto.x = eixoX.x;
        ponto.y = eixoY.y;

        //usa a funcao do circulo com o circulo e o ponto
        return circlesOverlap(c1, new Circle(ponto, 0));
    }
}

// ----------------------------------------------------------------------------------------------------------------------------

interface Collidable {

    /**
     * Verifica se este objeto está colidindo com outro.
     *
     * @param other outro objeto para verificar se este está colidindo.
     * @return true/false se está colidindo ou não.
     */
    boolean collidesWith(Collidable other);

    /**
     * Verifica se este objeto está fora de uma região retangular.
     *
     * @param area área retangular.
     * @return true/false se está pelo menos parcialmente fora da região.
     */
    boolean isOutOfBounds(Rectangle area);

    /**
     * Retorna um retângulo mínimo que contenha a entidade.
     *
     * @return um retângulo.
     */
    Rectangle getMinimumBoundingRectangle();

    /**
     * Retorna um círculo mínimo que contenha a entidade.
     *
     * @return um círculo.
     */
    Circle getMinimumEnclosingBall();
}
