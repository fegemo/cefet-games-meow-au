package br.cefetmg.games.minigames.Objects;

import br.cefetmg.games.collision.Collidable;
import br.cefetmg.games.collision.Collision;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class MemoryChip implements Collidable {

    private float radius;
    private final Vector2 position = new Vector2();

    private final Circle circle;
    private final Sprite sprite;
    
    private final Float Velocidade_Queda;
    private final Float Rotation;

    private ShapeRenderer shapeRenderer;

    public MemoryChip(Texture texture) {

        this.sprite = new Sprite(texture);
        this.circle = new Circle();
        this.shapeRenderer = new ShapeRenderer();

        this.position.x = (float) new Random().nextInt(1271);
        this.position.y = 600.0f + (float) new Random().nextInt(120) ;

        this.circle.x = this.position.x + 12.5f;
        this.circle.y = this.position.y + 17f;
        
        this.Velocidade_Queda = 1 + (float) new Random().nextInt(7);
        this.Rotation = (-30) + (float) new Random().nextInt(20);

        //25x34 dimensão do png, o raio de colisão é 21.1
        this.circle.radius = 21.1f;

        this.sprite.setPosition(this.position.x, this.position.y);

    }

    public void update(float dt) {

        //atualiza posição do memo card
        this.position.y -= this.Velocidade_Queda;
        this.sprite.rotate((float) 10);
        this.sprite.setPosition(this.position.x, this.position.y);

        // atualiza a area de colisão
        this.circle.y = this.position.y + 17f;
        this.circle.x = this.position.x + 12.5f;

    }

    public void render_area_collision() {

        // metodo para mostrar o circulo de colisão
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.identity();
        this.shapeRenderer.setColor(Color.RED);
        this.shapeRenderer.circle(this.circle.x, this.circle.y, this.circle.radius);
        this.shapeRenderer.end();

    }

    public void render(SpriteBatch sb) {

        // se dentro da tela e sem colisão com other - desenha
        if (this.position.y >= -35) {
            this.sprite.draw(sb);
        }
        

    }

    @Override
    public boolean collidesWith(Collidable other) {
        if (other instanceof Fish) {
            // se ocorrer colisão com objeto Fish
            return Collision.circlesOverlap(other.getMinimumEnclosingBall(),circle);
            
        } else {
            return false;
        }
    }

    @Override
    public boolean isOutOfBounds(Rectangle area) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Rectangle getMinimumBoundingRectangle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Circle getMinimumEnclosingBall() {
        return this.circle;
    }

}
