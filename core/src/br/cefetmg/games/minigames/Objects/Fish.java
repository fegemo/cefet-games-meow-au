/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.Objects;

import br.cefetmg.games.Config;
import br.cefetmg.games.collision.Collidable;
import br.cefetmg.games.collision.Collision;
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

    private int lado;

    private Sprite sprite;
    private Rectangle rectangle;
    private ShapeRenderer shapeRenderer;

    public Fish(Texture texture) {
        this.sprite = new Sprite(texture);
        this.shapeRenderer = new ShapeRenderer();
        this.sprite.setPosition(20.0f, 220.0f);
        this.rectangle = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update(float x, float y) {
        this.sprite.setPosition(x,y);
        this.rectangle.x = x;
        this.rectangle.y = y;
        
    }

    public void render(SpriteBatch sb) {
        this.sprite.draw(sb);
    }
    
    public void updateAccordingToTheMouse(float x , float y){
        Rectangle c1 = new Rectangle(x, y, 1,1);
        Collision cc = new Collision();
        if (Gdx.input.isTouched()||Gdx.input.justTouched()) {     
        //se clicar com o mouse sobre o objeto Fish
            if (cc.rectsOverlap(rectangle,c1)) {
            // se o ponteiro do mouse estiver dentro da area de colisão
                update(x-sprite.getWidth()/2,y-sprite.getHeight()/2);
                float ultimo_x = this.sprite.getX();
                if( ultimo_x > x){
                    // da um mirror 
                    this.sprite.flip(true,false);
                }else if( ultimo_x < x){
                    this.sprite.flip(true,false);
                }
            }
        }
    }

    public void render_area_collision() {

        // metodo para mostrar o circulo de colisão
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.identity();
        this.shapeRenderer.setColor(Color.RED);
        this.shapeRenderer.rect(this.rectangle.x, this.rectangle.y, this.rectangle.width,this.rectangle.height);
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
        return rectangle;
    }

    @Override
    public Circle getMinimumEnclosingBall() {
        throw new UnsupportedOperationException("Not supported yet.");
       // return this.circle;
    }
}
