package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Pedro
 */
public class ClickFindCat extends MiniGame {

    private Texture catTexture;
    private Texture miraTexture;
    private Sprite miraSprite;
    private Sprite catSprite;
    private Sound meawSound;
    private Sound scaredMeawSound;
    private Sound happyMeawSound;
    private float initialCatScale;
    private float hipotenuzaDaTela;
    private float difficulty;
    
    public ClickFindCat(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
        this.difficulty = difficulty;
    }

    @Override
    protected void onStart() {
        hipotenuzaDaTela = viewport.getScreenWidth() * viewport.getScreenWidth()
                + viewport.getScreenHeight() * viewport.getScreenHeight();
        initialCatScale = 0.5f * DifficultyCurve.LINEAR_NEGATIVE.getCurveValue(difficulty);
        catTexture = assets.get("ClickFindCat/gatinho-grande.png", Texture.class);

        miraTexture = assets.get("ClickFindCat/target.png", Texture.class);
        miraSprite = new Sprite(miraTexture);
        miraSprite.setScale(1.0f);
        miraSprite.setOriginCenter();
        meawSound = assets.get("ClickFindCat/cat-meow.wav", Sound.class);
        scaredMeawSound = assets.get("ClickFindCat/ScaredCat.wav", Sound.class);
        happyMeawSound = assets.get("ClickFindCat/YAY.mp3", Sound.class);
        initializeCat();
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

    }

    public void initializeCat() {
        Vector2 posicaoInicial = new Vector2(MathUtils.random(0, viewport.getWorldWidth() - catTexture.getWidth() / 2),
                MathUtils.random(0, viewport.getWorldHeight() - catTexture.getHeight() / 2));

        catSprite = new Sprite(catTexture);
        catSprite.setPosition(posicaoInicial.x, posicaoInicial.y);
        catSprite.setScale(initialCatScale);

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
    }

    @Override
    public void onDrawGame() {
        if (super.getState() == MiniGameState.PLAYER_FAILED || super.getState() == MiniGameState.PLAYER_SUCCEEDED) {
            catSprite.draw(batch);
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

}
