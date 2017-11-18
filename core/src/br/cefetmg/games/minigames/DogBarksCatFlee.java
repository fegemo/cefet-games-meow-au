package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Pedro e Arthur ouçam https://www.youtube.com/watch?v=Gfw4yxn_kPQ
 */
public class DogBarksCatFlee extends MiniGame {

    private static final int TILES_COUNT = 18;
    private Dog player;
    private Texture dogTextureStandBy;
    private Texture dogTexture;
    private Texture deadTexture;
    private Texture dogTextureWalking;
    private Animation<TextureRegion> dogWalking;
    private Animation<TextureRegion> dogBarking;
    private MySound barkSound;
    private MySound meawSound;
    private MySound whiningSound;
    private Texture catTexture;
    private Texture catTexture2;
    private Cat enemy;
    private Array<Tiles> tilesVector;
    private Vector2 posicaoInicial;
    private Texture tileTexture[] = new Texture[5];
    private float spawnInterval;
    private float tempoDeAnimacao;
    private int morreu = 0;
    private float difficulty;
    public float telaAnda = 3.5f;
    public int contadorLatidos = 0;
    public boolean consegueOuvir = false;

    public DogBarksCatFlee(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
        this.difficulty = difficulty;
    }

    private void tilesDraw() {
        for (Tiles tile : tilesVector) {
            batch.draw(tileTexture[tile.tileType], tile.position.x, tile.position.y);
        }
    }

    private void updateDraw() {
        if (enemy.getPosition().x - player.getPosition().x >= 2 * (player.getFrameWidth() + enemy.getWidth())) {
            consegueOuvir = true;
            enemy.setPosition(new Vector2(enemy.getPosition().x += -telaAnda, enemy.getPosition().y));
            for (Tiles tile : tilesVector) {
                tile.position.x += -telaAnda;
                if (tile.position.x <= 0 - tileTexture[0].getWidth()) {
                    tile.tileChange();
                    tile.position.x = 1400;
                }
            }
        } else {
            consegueOuvir = false;
        }
    }

    private void playerDraw(float dt) {
        if (enemy.getPosition().x - player.getPosition().x >= 2 * (player.getFrameWidth() + enemy.getWidth())) {
            batch.draw(player.getWalkingAnimationFrame(dt), player.getPosition().x, player.getPosition().y);
        } else if (player.isLatindo()) {
            batch.draw(player.getBarkingAnimationFrame(dt), player.getPosition().x, player.getPosition().y);
        } else {
            batch.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);
        }
    }

    private void catDraw() {
        if (!enemy.isVivo()){
            if(player.barkCounter < enemy.getScareThreshold()/2 ){
                batch.draw(enemy.getTexture(), enemy.getPosition().x, enemy.getPosition().y);
            }else{
                batch.draw(catTexture2,enemy.getPosition().x,enemy.getPosition().y);
            }
        }else{
            batch.draw(deadTexture, enemy.oldPos.x, enemy.oldPos.y);
        }
    }

    @Override
    protected void onStart() {
        tempoDeAnimacao = 0;
        whiningSound = new MySound(assets.get("DogBarksCatFlee/dog-whining-sound.mp3", Sound.class));
        deadTexture = assets.get("DogBarksCatFlee/kitten1-alt_4.png", Texture.class);
        barkSound = new MySound( assets.get("DogBarksCatFlee/BarkSound.wav", Sound.class));
        dogTextureStandBy = assets.get("DogBarksCatFlee/dog_separado_4.png", Texture.class);
        dogTexture = assets.get("DogBarksCatFlee/dog_spritesheet.png", Texture.class);
        dogTextureWalking = assets.get("DogBarksCatFlee/spritesheet2.png", Texture.class);
        TextureRegion[][] quadrosDeAnimacao = TextureRegion.split(dogTexture, 128, 128);
        TextureRegion[][] quadrosDeAnimacao2 = TextureRegion.split(dogTextureWalking, 128, 128);
        dogBarking = new Animation<TextureRegion>(0.3f,
                quadrosDeAnimacao[0][3],
                quadrosDeAnimacao[0][2],
                quadrosDeAnimacao[0][1],
                quadrosDeAnimacao[0][0]);
        dogWalking = new Animation<TextureRegion>(0.3f,
                quadrosDeAnimacao2[0][0],
                quadrosDeAnimacao2[0][1],
                quadrosDeAnimacao2[0][2],
                quadrosDeAnimacao2[0][3],
                quadrosDeAnimacao2[0][4]);
        dogBarking.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        dogWalking.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        catTexture2 = assets.get("DogBarksCatFlee/kitten1-alt_3.png", Texture.class);
        catTexture = assets.get("DogBarksCatFlee/kitten1-alt.png", Texture.class);
        meawSound = new MySound(assets.get("DogBarksCatFlee/cat-meow.wav", Sound.class));

        tileTexture[0] = assets.get("DogBarksCatFlee/tile0.png", Texture.class);
        tileTexture[1] = assets.get("DogBarksCatFlee/tile1.png", Texture.class);
        tileTexture[2] = assets.get("DogBarksCatFlee/tile2.png", Texture.class);
        tileTexture[3] = assets.get("DogBarksCatFlee/tile3.png", Texture.class);
        tileTexture[4] = assets.get("DogBarksCatFlee/tile4.png", Texture.class);

        tilesVector = new Array<Tiles>();

        for (int i = 0; i < TILES_COUNT; i++) {
            tilesVector.add(new Tiles(new Vector2(1 + i * tileTexture[0].getWidth(), 41)));
        }
        initializeDog();
        initializeCat();
    }

    private void updateEnemy() {
        if (enemy.shouldTriggerFleeAction(player.getBarkCounter())) {
            meawSound.play();
            enemy.morreu();
            player.resetBarkCounter();
            super.challengeSolved();
        }
        if (enemy.isVivo() && (enemy.getQuantidadeVidas() > 0)) {
            enemy.spawn();
        }

    }

    private void initializeDog() {
        TextureRegion[][] textureDog = TextureRegion.split(dogTextureStandBy, dogTextureStandBy.getWidth(), dogTextureStandBy.getHeight());
        posicaoInicial = new Vector2(300, 41);
        player = new Dog(3, posicaoInicial, textureDog[0][0], dogBarking, dogWalking);
    }

    private void initializeCat() {
        TextureRegion[][] textureCat = TextureRegion.split(catTexture, catTexture.getWidth(), catTexture.getHeight());
        enemy = new Cat(scareThreshold(difficulty), posicaoInicial, textureCat[0][0]);
        enemy.setQuantidadeVidas(1);
        enemy.setPosition(enemy.getInitialPosition());
        enemy.spawn();
    }

    private int scareThreshold(float difficulty) {
        return (int) DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 3, 20);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        spawnInterval = DifficultyCurve.S.getCurveValue(difficulty);
    }

    @Override
    public void onHandlePlayingInput() {
        if (Gdx.input.justTouched()) {
            player.bark(consegueOuvir);
            if (!consegueOuvir) {
                barkSound.play();
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        if (super.getState() == MiniGameState.PLAYER_FAILED) {
            whiningSound.play();
        }
        if (player.isLatindo()) {
            contadorLatidos++;
            if (contadorLatidos == 25) {
                contadorLatidos = 0;
                player.invertLatindo();
            }
        }
        if (enemy.mostrarGatoMorto && morreu < 10) {
            morreu++;
        } else {
            enemy.mostrarGatoMorto = !enemy.mostrarGatoMorto;
        }

        tempoDeAnimacao += Gdx.graphics.getDeltaTime();
        updateDraw();
        updateEnemy();
    }

    @Override
    public void onDrawGame() {
        tilesDraw();
        playerDraw(tempoDeAnimacao);
        catDraw();
    }

    @Override
    public String getInstructions() {
        return "Lata para espantar o gato";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    static class Cat extends Animal {

        private final int POSICAO_INICIAL_GATO_X = 1400;
        private final int POSICAO_INICIAL_GATO_Y = 51;
        // Precisa de armazenar a sprite para animacao

        private final int scaredThreshold;
        private int quantidadeVidas;
        private boolean morto;
        public boolean mostrarGatoMorto = false;
        public Vector2 oldPos;
        public boolean visivel = true;
        public Texture texturaResabiado1;
        
        // Construtor do Jogo DogBarksCatFlee
        public Cat(int scaredThreshold, Vector2 position, TextureRegion textureCat) {
            super(position, textureCat);
            this.morto = true;
            this.scaredThreshold = scaredThreshold;
            this.quantidadeVidas = 1;
        }

        public void setInvisivel() {
            visivel = false;
        }

        public Vector2 getInitialPosition() {
            return new Vector2(POSICAO_INICIAL_GATO_X, POSICAO_INICIAL_GATO_Y);
        }

        public int getScareThreshold() {
            return this.scaredThreshold;
        }

        public int getQuantidadeVidas() {
            return this.quantidadeVidas;
        }

        public void morreu() {
            this.quantidadeVidas--;
            this.morto = true;  
            oldPos = this.getPosition();
            mostrarGatoMorto = !mostrarGatoMorto;
            //setPosition(new Vector2(POSICAO_INICIAL_GATO_X, POSICAO_INICIAL_GATO_Y));
        }

        public boolean isVivo() {
            return this.morto;
        }

        public void spawn() {
            this.morto = false;
        }

        public void setQuantidadeVidas(float variavelControleDificuldade) {
            this.quantidadeVidas = 1;//(int) (MathUtils.ceil(variavelControleDificuldade * 8));
        }

        public boolean shouldTriggerFleeAction(int barkCounter) {
            // Caso positivo ativa a funcao de sair da dela
            // Presente na classe do jogo
            return barkCounter >= scaredThreshold;
        }

    }

    static class Dog extends Animal {

        private int barkCounter;
        private boolean latindo;
        private final Animation<TextureRegion> animacaoLatindo;
        private final Animation<TextureRegion> animacaoAndando;

        public Dog(int lives, Vector2 position, TextureRegion dogTexture,
                Animation<TextureRegion> animacaoLatindo,
                Animation<TextureRegion> animacaoAndando) {

            super(position, dogTexture);
            this.animacaoLatindo = animacaoLatindo;
            this.animacaoAndando = animacaoAndando;
            barkCounter = 0;
            latindo = false;
        }

        public TextureRegion getBarkingAnimationFrame(float dt) {
            return ((TextureRegion) animacaoLatindo.getKeyFrame(0.93f));
        }

        public TextureRegion getWalkingAnimationFrame(float dt) {
            return ((TextureRegion) animacaoAndando.getKeyFrame(dt));
        }

        public int getFrameWidth() {
            return FRAME_WIDTH;
        }

        public int getFrameHeight() {
            return FRAME_HEIGHT;
        }

        public int getBarkCounter() {
            return barkCounter;
        }

        public boolean isLatindo() {
            return latindo;
        }

        public void invertLatindo() {
            latindo = !latindo;
        }

        public void bark(boolean gatoOuve) {
            if (!gatoOuve) {
                this.barkCounter++;
            } else {
                this.barkCounter = 0;
            }
            latindo = true;
        }

        public void resetBarkCounter() {
            barkCounter = 0;
        }
    }

    static class Animal extends Sprite {

        private Vector2 position;
        static final int FRAME_WIDTH = 131;
        static final int FRAME_HEIGHT = 32;

        public Animal(Vector2 position, TextureRegion spritesheet) {
            super(spritesheet);
            this.position = position;
        }

        public Animal(Sprite animalSprite) {
            super(animalSprite);
        }

        public Vector2 getPosition() {
            return position;
        }

        public void setPosition(Vector2 newPosition) {
            this.position = newPosition;
        }
    }

    static class Tiles {

        public Vector2 position;
        public int tileType;

        public Tiles(Vector2 position) {
            this.position = position;
            this.tileType = MathUtils.random(4);
        }

        public void tileChange() {
            this.tileType = MathUtils.random(4);
        }
    }

}
