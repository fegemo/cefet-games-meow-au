/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.Animals.Cat;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 *
 * @author Pedro
 */
public class ClickFindCat extends MiniGame {
    
    private Texture CatTexture;
    private Texture MiraTexture;
    private Sprite MiraSprite;
    private Sprite CatSprite;
    private Sound MeawSound;
    private float initialCatScale;
    private float HipotenuzaDaTela;

    public ClickFindCat(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        HipotenuzaDaTela = viewport.getScreenWidth() * viewport.getScreenWidth()
                + viewport.getScreenHeight() * viewport.getScreenHeight();
        initialCatScale = .1f;
        CatTexture = assets.get("DogBarksCatFlee/gatinho-grande.png", Texture.class);
        
        MiraTexture = assets.get("DogBarksCatFlee/Dog_separado_1.png", Texture.class);
        MiraSprite = new Sprite(MiraTexture);
        MiraSprite.setScale(0.1f);
        MiraSprite.setOriginCenter();
        MeawSound = assets.get("DogBarksCatFlee/cat-meow.wav", Sound.class);
        initializeCat();
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        
    }

    public void initializeCat () {
        Vector2 PosicaoInicial;
        do {
            // pega x e y entre 0 e 1
            PosicaoInicial = new Vector2(rand.nextFloat(), rand.nextFloat());
            // multiplica x e y pela largura e altura da tela
            PosicaoInicial.scl(
                    viewport.getWorldWidth() - CatTexture.getWidth() * initialCatScale,
                    viewport.getWorldHeight() - CatTexture.getHeight() * initialCatScale);
        }while(PosicaoInicial.x > 0 && PosicaoInicial.x < viewport.getWorldWidth()
                && PosicaoInicial.y > 0 && PosicaoInicial.y < viewport.getWorldHeight());
        
        CatSprite = new Sprite (CatTexture);
        CatSprite.setPosition(PosicaoInicial.x, PosicaoInicial.y);
        CatSprite.setScale(initialCatScale);
        
        
    }
    
    @Override
    public void onHandlePlayingInput() {
        Vector2 click = new Vector2 (Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        this.MiraSprite.setPosition(click.x - this.MiraSprite.getWidth() / 2, click.y - this.MiraSprite.getHeight()/ 2);
        if (Gdx.input.justTouched()) {
            System.out.println(CatSprite.getBoundingRectangle());
            if (CatSprite.getBoundingRectangle().overlaps(MiraSprite.getBoundingRectangle())){
                super.challengeSolved();
            } else {
                float distancia = click.dst2(CatSprite.getX(), CatSprite.getY());
                float intensidade = (float) Math.pow((1 - distancia/HipotenuzaDaTela),4);
                MeawSound.play(intensidade);
              
            }
                
                
        }
    }

    @Override
    public void onUpdate(float dt) {
        
    }

    @Override
    public void onDrawGame() {
        //CatSprite.draw(batch);
        //MiraSprite.draw(batch);

    }

    @Override
    public String getInstructions() {
        return "Ache o gato invisivel";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
    
}
