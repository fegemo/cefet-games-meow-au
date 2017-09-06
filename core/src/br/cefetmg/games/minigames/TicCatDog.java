package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 *
 */
public class TicCatDog extends MiniGame
{
    private Texture backgroundTexture;

    public TicCatDog(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty)
    {
        super(screen, observer, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart()
    {
        backgroundTexture = assets.get(
                "tic-cat-dog/background.jpg", Texture.class);
    }

    private void scheduleEnemySpawn()
    {

    }

    private void spawnEnemy()
    {

    }

    @Override
    protected void configureDifficultyParameters(float difficulty)
    {

    }

    @Override
    public void onHandlePlayingInput()
    {

    }

    @Override
    public void onUpdate(float dt)
    {

    }

    @Override
    public String getInstructions()
    {
        return "Jogue jogo da velha em 5 SEGUNDOS!";
    }

    @Override
    public void onDrawGame()
    {        
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    @Override
    public boolean shouldHideMousePointer()
    {
        return true;
    }
}
