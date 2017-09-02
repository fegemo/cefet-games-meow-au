
package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.MemoryChip;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.Random;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;

/**
 *
 * @author Luiza-Pedro
 */
public class SpyFish extends MiniGame {

    //texturas
    private Texture texturaFish;
    private Texture texturaCard;
    private Texture texturaFundo;
    private Texture texturaFcontrol;
    private Texture texturaMcontrol;
    private Texture texturaCardd;
    private ArrayList<MemoryChip> chip;
    private static final int MAX_CHIPS = 5;
    private static SpriteBatch batch;
    //elementos de logica
    private Fish fish;
    private Control control;
    private MemoryCard memoryCard;

    public SpyFish(BaseScreen screen,MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10000,TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
        batch = new SpriteBatch();
        // cria um numero MAX_CHIPS de objetos MemoryCard
        chip = new ArrayList<MemoryChip>();
        for (int i = 0; i < MAX_CHIPS; i++) {
            chip.add(new MemoryChip(texturaCardd));
        }
    }

    @Override
    protected void challengeSolved() {
        super.challengeSolved(); 
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onStart() {
        this.texturaFish = assets.get("spy-fish/fish.png", Texture.class);
        this.texturaCard = assets.get("spy-fish/memory-card.png", Texture.class);
        this.texturaFundo = assets.get("spy-fish/fundo.png", Texture.class);
        this.texturaFcontrol = assets.get("spy-fish/fundo-controle.png", Texture.class);
        this.texturaMcontrol = assets.get("spy-fish/controle-principal.png", Texture.class);
        this.texturaCardd = assets.get("spy-fish/card.png", Texture.class);

        fish = new Fish(texturaFish);
        memoryCard = new MemoryCard(texturaCard);
        control = new Control(texturaFcontrol, texturaMcontrol);

    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

        //throw new UnsupportedOperationException("Not supported yet."); 
        //To change body of generated methods, choose Tools | Templates.
        
    }

    @Override
    public void onHandlePlayingInput() {
        /*Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        if(Gdx.input.isTouched())
            control.update(click);
        else
            control.update();
         */// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onUpdate(float dt) {
        /*fish.update(dt);
        memoryCard.update();*/

    }

    @Override
    public void onDrawGame() {

        update(Gdx.graphics.getDeltaTime());
        batch.begin();
        batch.draw(texturaFish, 0, 0);
        batch.draw(texturaFundo, 100, 100);
        fish.draw(batch);
        control.draw();
        memoryCard.draw();
        batch.end();
        for (MemoryChip chip : chip) {
            batch.begin();
            chip.render(batch);
            batch.end();
        }

    }

    @Override
    public String getInstructions() {
        return "Pegue o cartão de memória";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    class Fish extends Sprite {

        private Vector2 dposi;
        private int lado;
        //private final Texture texture;

        static final int FRAME_WIDTH = 28;
        static final int FRAME_HEIGHT = 36;

        public Fish(Texture fishSprite) {
            super(fishSprite);
        }

        public void update(float dt) {
            float auxX = dposi.x > 0 ? min(Config.WORLD_WIDTH, super.getX() + dposi.x) : max(0, super.getX() + dposi.x);
            float auxY = dposi.y > 0 ? min(Config.WORLD_HEIGHT, super.getY() + dposi.y) : max(0, super.getY() + dposi.y);
            super.setPosition(auxX, auxY);
            setDposi(0, 0);
        }

        public void setDposi(float x, float y) {
            this.dposi.x = x;
            this.dposi.y = y;
        }
    }

    class MemoryCard {

        private final Texture card;
        private Vector2 posicao = new Vector2();
        private Random rand = new Random();

        public MemoryCard(Texture card) {
            this.card = card;
            this.posicao.y = Config.WORLD_HEIGHT / 2;
            this.posicao.x = rand.nextInt() % Config.WORLD_WIDTH / 2;
        }

        public void draw() {
            batch.draw(card, posicao.x, posicao.y);
        }

        public void update() {
            //  this.posicao.y--;
        }
    }

    class Control {

        private Texture fundo;
        private Texture meio;
        private Vector2 centro;
        private Vector2 centroMeio;

        public Control(Texture fundo, Texture meio) {
            this.fundo = fundo;
            this.meio = meio;
            this.centro = new Vector2(Config.WORLD_WIDTH / 2 + 100, 100);//canto inferior que não é o 00 
            this.centroMeio = new Vector2(centro);
        }

        public void draw() {
            batch.draw(fundo, centro.x, centro.y);
            batch.draw(meio, centroMeio.x, centroMeio.y);
        }

        public void update(Vector2 click) {
            //verifica se esta pressionando a bolinha de dentro se esta é possivel arrasta-la ate a borda da bola grande
        }

        public void update() {
            centroMeio = centro;
        }
    }
}
