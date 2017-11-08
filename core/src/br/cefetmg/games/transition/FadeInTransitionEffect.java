
package br.cefetmg.games.Transition;

import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FadeInTransitionEffect extends TransitionEffect{
    
    protected float duration;
    Color color = new Color();
    
    public FadeInTransitionEffect(float duration) {
        super(duration);
        this.duration = duration;
        alpha = 0;
    }
    
    @Override
    public void render(BaseScreen current) {
        current.render(delta);
        
        color.a = alpha;
        alpha = timeElapsed / duration;
        drawFade(current.viewport);
        
        if (timeElapsed >= duration)
            isFinished = true;
    }
    
    private void drawFade(Viewport viewport) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        shapeRenderer.end();
        
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
