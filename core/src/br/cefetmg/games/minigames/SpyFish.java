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

    private ArrayList<MemoryChip> chips;

    //elementos de logica
    private Fish fish;

    private int maxChips;
    private int numberOfChipsToTake;
    private float chipMaxSpeed;
    private int numberOfLostChips = 0;

    public SpyFish(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 20000f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
        this.texturaFish = assets.get("spy-fish/fish.png", Texture.class);
        this.texturaMemoCard = assets.get("spy-fish/card.png", Texture.class);
        this.texturaFundo = assets.get("spy-fish/ocean.png", Texture.class);
    }

    @Override
    protected void onStart() {
        chips = new ArrayList<MemoryChip>();
        for (int i = 0; i < this.maxChips; i++) {
            chips.add(new MemoryChip(texturaMemoCard, this.chipMaxSpeed));
        }
        this.fish = new Fish(this.texturaFish);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.maxChips = (int) DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 7, 15);
        this.numberOfChipsToTake = (int) DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 1, 5);
        this.chipMaxSpeed = (float) DifficultyCurve.S
                .getCurveValueBetween(difficulty, 1, 9);
    }

    @Override
    public void onHandlePlayingInput() {
        // move o peixe
        this.fish.updateAccordingToTheMouse(
                getMousePosInGameWorld().x, getMousePosInGameWorld().y);
    }

    @Override
    public void onUpdate(float dt) {
        fish.update(dt);
        for (Iterator<MemoryChip> iterator = chips.iterator(); iterator.hasNext();) {
            MemoryChip mc = iterator.next();
            if (mc.position.y >= -35) {
                mc.update();
            }
            if (mc.collidesWith(this.fish)) {
                //se o peixe pegar um cartão de memoria
                iterator.remove();
                if (chips.size() == (this.maxChips - this.numberOfChipsToTake)) {
                    super.challengeSolved();
                }
            }
            if (mc.getPositionMemoryCard().y < -1) {
                numberOfLostChips++;
                if (numberOfLostChips > (this.maxChips + this.numberOfChipsToTake)) {
                    // se chegar nessa parte do codigo, é pq não tem como 
                    // mais pegar o numero minimo de chips
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
        update(Gdx.graphics.getDeltaTime());
        batch.draw(texturaFundo, 0f, 0f, 1280f, 720f);
        this.fish.render(batch, getMousePosInGameWorld().x, getMousePosInGameWorld().y);
        for (MemoryChip chip : chips) {
            chip.render(batch);
        }
    }

    private String getNumberSpelled(int number) {
        switch (number) {
            case 1:
                return "um";
            case 2:
                return "dois";
            case 3:
                return "três";
            case 4:
                return "quatro";
            case 5:
                return "cinco";
            default:
                return String.valueOf(number);
        }
    }

    @Override
    public String getInstructions() {
        String instruction = String.format(
                "Pegue pelo menos %1$s %2$s de memória",
                getNumberSpelled(numberOfChipsToTake),
                numberOfChipsToTake > 1 ? "cartões" : "cartão");
        return instruction;
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }

    // <editor-fold desc="Classes internas da SpyFish" defaultstate="collapsed">
    static class Fish extends Sprite implements Collidable {

        private Vector2 alvo;
        private Pose pose;
        private Sprite sprite;
        private Circle circle;
        private ShapeRenderer shapeRenderer;

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
            float spriteX = this.sprite.getX();
            float spriteY = this.sprite.getY();
            if ((1280 >= spriteX && 0 <= spriteX) && (720 >= spriteY && 0 <= spriteY)) {
                //se estiver dentro da tela
                //update(x,y);
                if ((1280 >= this.sprite.getX() && 0 <= this.sprite.getX()) && (720 >= this.sprite.getY() && 0 <= this.sprite.getY())) {
                    // se dentro da tela
                    this.sprite.draw(sb);
                }
            }
        }

        public void updateAccordingToTheMouse(float x, float y) {
            if (Gdx.input.isTouched() || Gdx.input.justTouched()) {
                alvo = new Vector2(x, y);
            }
        }

        public void renderCollisionArea() {
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
        public Circle getMinimumEnclosingBall() {
            return this.circle;
        }
    }

    static class MemoryChip implements Collidable {

        private final Vector2 position = new Vector2();
        private final Circle circle;
        private final Sprite sprite;
        private final Float velocidadeQueda;
        private ShapeRenderer shapeRenderer;

        public MemoryChip(Texture texture, float velocidade) {
            this.sprite = new Sprite(texture);
            this.circle = new Circle();
            this.shapeRenderer = new ShapeRenderer();
            this.position.x = (float) new Random().nextInt(1271);
            this.position.y = 600.0f + (float) new Random().nextInt(120);
            this.circle.x = this.position.x + 12.5f;
            this.circle.y = this.position.y + 17f;
            this.velocidadeQueda = 1 + (float) new Random().nextFloat() * (velocidade - 1);
            this.circle.radius = 21.1f;
            this.sprite.setPosition(this.position.x, this.position.y);
        }

        public void update() {
            //atualiza posição do memo card
            this.position.y -= this.velocidadeQueda;
            this.sprite.rotate((float) 10);
            this.sprite.setPosition(this.position.x, this.position.y);
            // atualiza a area de colisão
            this.circle.y = this.position.y + 17f;
            this.circle.x = this.position.x + 12.5f;
        }

        public void renderCollisionArea() {
            // metodo para mostrar o circulo de colisão
            this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            this.shapeRenderer.identity();
            this.shapeRenderer.setColor(Color.RED);
            this.shapeRenderer.circle(this.circle.x, this.circle.y, this.circle.radius);
            this.shapeRenderer.end();
        }

        public void render(SpriteBatch sb) {
            // se dentro da tela e sem colisão com other - desenha
                this.sprite.draw(sb);
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
    }

    static class Pose {

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
         * Retorna um vetor que representa a direção que o ângulo "orientação"
         * desta pose representa.
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
         * @param velocidade vetor velocidade, indicando a direção para onde
         * esta pose deve se orientar (rotacionar).
         */
        public void olharNaDirecaoDaVelocidade(Vector2 velocidade) {
            if (velocidade.len2() > 0) {
                orientacao = (float) Math.atan2(velocidade.y, velocidade.x);
            }
        }

        public void atualiza(Direcionamento guia, float delta) {
            Vector2 aux = new Vector2(guia.aceleracao);
            aux.scl(delta);
            velocidade.add(aux);
            Vector2 auxVelocidade = new Vector2(velocidade);
            auxVelocidade.scl(delta);
            posicao.add(auxVelocidade);
            orientacao += guia.rotacao * delta;
            orientacao = orientacao % ((float) Math.PI * 2);
        }
    }

    static class Direcionamento {

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
     * inspirado no tp de cinematica
     */
    static class Buscar {

        private final float maxAceleracao;
        private final float constanteVelocidade;

        public Buscar(float aceleracaoMax, float velocidadeConst) {
            this.maxAceleracao = aceleracaoMax;
            this.constanteVelocidade = velocidadeConst;
        }

        public Buscar() {
            this.maxAceleracao = 2000;
            this.constanteVelocidade = 2;
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
            output.rotacao = 0;
            return output;
        }

    }

    static class Collision {

        /**
         * Verifica se dois círculos em 2D estão colidindo.
         *
         * @param c1 círculo 1
         * @param c2 círculo 2
         * @return true se há colisão ou false, do contrário.
         */
        public static final boolean circlesOverlap(Circle c1, Circle c2) {
            return c1.overlaps(c2);
        }

        /**
         * Verifica se dois retângulos em 2D estão colidindo. Esta função pode
         * verificar se o eixo X dos dois objetos está colidindo e então se o
         * mesmo ocorre com o eixo Y.
         *
         * @param r1 retângulo 1
         * @param r2 retângulo 2
         * @return true se há colisão ou false, do contrário.
         */
        /**
         * Verifica se dois retângulos em 2D estão colidindo. Esta função pode
         * verificar se o eixo X dos dois objetos está colidindo e então se o
         * mesmo ocorre com o eixo Y.
         *
         * @param a
         * @param b
         * @param r1 retângulo 1
         * @param r2 retângulo 2
         * @return true se há colisão ou false, do contrário.
         */
        public static final boolean rectsOverlap(Rectangle r1, Rectangle r2) {
            return r1.contains(r2);
        }

        public static final boolean circleRectCollision(Rectangle r2, Circle c1) {
            return r2.contains(c1);
        }
    }

    interface Collidable {

        /**
         * Verifica se este objeto está colidindo com outro.
         *
         * @param other outro objeto para verificar se este está colidindo.
         * @return true/false se está colidindo ou não.
         */
        boolean collidesWith(Collidable other);

        /**
         * Retorna um círculo mínimo que contenha a entidade.
         *
         * @return um círculo.
         */
        Circle getMinimumEnclosingBall();
    }

    // </editor-fold>
}