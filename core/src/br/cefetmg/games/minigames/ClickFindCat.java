/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.Animals.Cat;
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
    private Sprite CatSprite;
    private Texture MiraTexture;
    private Sprite MiraSprite;
    private Cat gato;
    private Sound MeawSound;
    private Vector2 a;
    

    public ClickFindCat(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        CatTexture = assets.get("DogBarksCatFlee/dog_separado_4.png", Texture.class);
        CatSprite = new Sprite (CatTexture);
        CatSprite.setOriginCenter();
        MiraTexture = assets.get("DogBarksCatFlee/dog_separado_4.png", Texture.class);
        MiraSprite = new Sprite(MiraTexture);
        MiraSprite.setOriginCenter();
        //MeawSound = assets.get("DogBarksCatFlee/cat-meow.wav", Sound.class);
        Vector2 PosicaoInicial = new Vector2 (MathUtils.random(0, viewport.getScreenWidth()),
                                        MathUtils.random(0, viewport.getScreenHeight()));
        a = PosicaoInicial;
        //gato = new Cat(PosicaoInicial, CatTexture);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        
    }

    @Override
    public void onHandlePlayingInput() {
        Vector2 click = new Vector2 (Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        this.MiraSprite.setPosition(click.x - this.MiraSprite.getWidth() / 2, click.y - this.MiraSprite.getHeight()/ 2);
        if (Gdx.input.justTouched()) {
            System.out.println(+click.x+ " " + click.y);
            System.out.println(+a.x +" "+ a.y);
            Sprite sprite = CatSprite;
            if (sprite.getBoundingRectangle().overlaps(MiraSprite.getBoundingRectangle())){
                super.challengeSolved();
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        
    }

    @Override
    public void onDrawGame() {
        
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
