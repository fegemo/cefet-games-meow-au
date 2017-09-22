/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

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
    
    private Texture CatTexture;
    private Texture MiraTexture;
    private Sprite MiraSprite;
    private Sprite CatSprite;
    private Sound MeawSound;
    private Sound ScaredMeawSound;
    private Sound HappyMeawSound;
    private float initialCatScale;
    private float HipotenuzaDaTela;

    public ClickFindCat(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        HipotenuzaDaTela = viewport.getScreenWidth() * viewport.getScreenWidth()
                + viewport.getScreenHeight() * viewport.getScreenHeight();
        initialCatScale = .2f;
        CatTexture = assets.get("ClickFindCat/gatinho-grande.png", Texture.class);
        
        MiraTexture = assets.get("ClickFindCat/target.png", Texture.class);
        MiraSprite = new Sprite(MiraTexture);
        MiraSprite.setScale(1.0f);
        MiraSprite.setOriginCenter();
        MeawSound = assets.get("ClickFindCat/cat-meow.wav", Sound.class);
        ScaredMeawSound = assets.get("ClickFindCat/ScaredCat.wav", Sound.class);
        HappyMeawSound = assets.get("ClickFindCat/YAY.mp3", Sound.class);
        initializeCat();
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        
    }

    public void initializeCat () {
        Vector2 PosicaoInicial = new Vector2 ( MathUtils.random(0, viewport.getWorldWidth() - CatTexture.getWidth()/2),
                MathUtils.random(0, viewport.getWorldHeight() - CatTexture.getHeight()/2));
        
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
        if (super.getState() == MiniGameState.PLAYER_FAILED) ScaredMeawSound.play();
        else if (rand.nextInt() % 4 == 1 && super.getState() == MiniGameState.PLAYER_SUCCEEDED) HappyMeawSound.play();
    }

    @Override
    public void onDrawGame() {
        if (super.getState() == MiniGameState.PLAYER_FAILED || super.getState() == MiniGameState.PLAYER_SUCCEEDED ) {
            CatSprite.draw(batch); 
        }
     
        //Desenha a Mira
        MiraSprite.draw(batch);

    }

    @Override
    public String getInstructions() {
        return "Ache o gato invisivel";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
}
