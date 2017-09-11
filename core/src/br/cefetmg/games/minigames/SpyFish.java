package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.collision.Collidable;
import br.cefetmg.games.minigames.Objects.Fish;
import br.cefetmg.games.minigames.Objects.MemoryChip;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private Texture texturaMemoCard;

    private ArrayList<MemoryChip> chip;

    private static SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    //elementos de logica
    private Fish fish;
    private Control control;
    private MemoryCard memoryCard;

    private static int MAX_CHIPS;

    private static float diff;

    public SpyFish(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);

        this.texturaFish = assets.get("spy-fish/fish.png", Texture.class);
        this.texturaCard = assets.get("spy-fish/memory-card.png", Texture.class);
        this.texturaFundo = assets.get("spy-fish/fundo.png", Texture.class);
        this.texturaFcontrol = assets.get("spy-fish/fundo-controle.png", Texture.class);
        this.texturaMcontrol = assets.get("spy-fish/controle-principal.png", Texture.class);
        this.texturaMemoCard = assets.get("spy-fish/card.png", Texture.class);
        this.fish = new Fish(texturaFish);
        this.memoryCard = new MemoryCard(texturaCard);
        this.control = new Control(texturaFcontrol, texturaMcontrol);

        batch = new SpriteBatch();

        diff = difficulty;

    }

    @Override
    protected void challengeSolved() {
        super.challengeSolved();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onStart() {
        if (diff == 0.047425874f) {

            chip = new ArrayList<MemoryChip>();
            MAX_CHIPS = 50;
            for (int i = 0; i < MAX_CHIPS; i++) {
                chip.add(new MemoryChip(texturaMemoCard));
            }
        } else {
            chip = new ArrayList<MemoryChip>();
            MAX_CHIPS = 100;
            for (int i = 0; i < MAX_CHIPS; i++) {
                chip.add(new MemoryChip(texturaMemoCard));
            }
        }
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
        this.fish.draw(batch);
        this.control.draw();
        this.memoryCard.draw();

        for (MemoryChip chip : chip) {

            chip.render(batch, this.fish);

        }
        batch.end();

        for (MemoryChip chip : this.chip) {
            //mostra os circulos de colisão
            chip.render_area_collision();
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
