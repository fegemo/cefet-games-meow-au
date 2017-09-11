/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.Objects;

import br.cefetmg.games.Config;
import br.cefetmg.games.collision.Collidable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.Random;

/**
 *
 * @author Thepe
 */
public class Fish extends Sprite implements Collidable {

    private Vector2 position = new Vector2();
    ;
    private int lado;

    private Sprite sprite;
    private Circle circle;
    private ShapeRenderer shapeRenderer;

    public Fish(Texture texture) {
        this.sprite = new Sprite(texture);
        this.circle = new Circle();
        this.shapeRenderer = new ShapeRenderer();

        this.position.x = 20.0f;
        this.position.y = 220.0f;

        this.circle.x = this.position.x + 64.0f;
        this.circle.y = this.position.y + 59.0f;
        this.circle.radius = 88.576f;

        this.sprite.setPosition(this.position.x, this.position.y);
    }

    public void update(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        this.circle.x = this.position.x + 64.0f;
        this.circle.y = this.position.y + 59.0f;
        this.sprite.setPosition(this.position.x, this.position.y);
    }

    public void render(SpriteBatch sb, float x, float y) {

        this.sprite.draw(sb);

        if (((x <= (this.circle.x + this.circle.radius)) && (x >= (this.circle.x - this.circle.radius)))
                && ((y <= (this.circle.y + this.circle.radius)) && (y >= (this.circle.y - this.circle.radius)))) {
            // se o ponteiro do mouse estiver dentro da area de colisão
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                //se clicar com o mouse sobre o objeto Fish
                update(x,y);
            }
        }

    }

    public void render_area_collision() {

        // metodo para mostrar o circulo de colisão
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.identity();
        this.shapeRenderer.setColor(Color.RED);
        this.shapeRenderer.circle(this.circle.x, this.circle.y, this.circle.radius);
        this.shapeRenderer.end();

    }

    @Override
    public boolean collidesWith(Collidable other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
