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
import com.badlogic.gdx.InputAdapter;
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
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.Random;

/**
 *
 * @author Thepe
 */
public class Fish extends Sprite implements Collidable {

    private Vector2 alvo;

    private int lado;

    private Sprite sprite;
    private TextureRegion[][] region;
    private Rectangle rectangle;
    private ShapeRenderer shapeRenderer;
    private SeekDynamic movimentacao;

    public Fish(Texture texture) {
        this.sprite = new Sprite(texture);
        this.shapeRenderer = new ShapeRenderer();
        this.sprite.setPosition(20.0f, 220.0f);
        this.rectangle = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());

        this.movimentacao = new SeekDynamic(new Vector2(sprite.getX(), sprite.getY()));
    }

    public void update(float x, float y) {
        //atualiza a posição do peixe
        this.sprite.setPosition(x, y);
        //atualiza a posicao do retangulo de colisao
        this.rectangle.x = x;
        this.rectangle.y = y;

    }

    /*public void update(float dt) {
        if(alvo==null)
            movimentacao.Calculate(dt);
        else{
            movimentacao.Calculate(new Vector2(alvo.x, alvo.y),dt);
        System.out.println(alvo.x +"  alvos  "+alvo.y);}
        System.out.println(movimentacao.getPos().getPosicao().x+"   possicao  "+movimentacao.getPos().getPosicao().y);
        update(movimentacao.getPos().getPosicao().x,movimentacao.getPos().getPosicao().y);

    }*/
    public void render(SpriteBatch sb, float x, float y) {

        float x_sprite = this.sprite.getX();
        float y_sprite = this.sprite.getY();

        if ((1280 >= x_sprite && 0 <= x_sprite) && (720 >= y_sprite && 0 <= y_sprite)) {
            //se estiver dentro da tela
            this.sprite.draw(sb);
        }
    }

    // vo modificar a movimentacao toda se der certo a gente apaga isso
    /*public void updateAccordingToTheMouse(float x , float y){
        Circle c_mouse = this.circle;
        if (Gdx.input.isTouched()||Gdx.input.justTouched()) {     
        //se clicar com o mouse sobre o objeto Fish
            if ( (x >= (this.circle.x-this.circle.radius) && x <= (this.circle.x+this.circle.radius))
                    && (y >= (this.circle.y-this.circle.radius) && y <= (this.circle.y+this.circle.radius)) ){
                float delta_x = x - this.sprite.getX();
                float delta_y = y - this.sprite.getY();
                update(x , y );
            } 
                
        }
    }*/
    public void updateAccordingToTheMouse(float x, float y) {
        if ((x >= (this.rectangle.x) && x <= (this.rectangle.x + this.rectangle.width))
                && (y >= (this.rectangle.y) && y <= (this.rectangle.y + this.rectangle.height))) {
            if (Gdx.input.isTouched() || Gdx.input.isTouched()) {
                float delta_x = x - this.sprite.getX();
                float delta_y = y - this.sprite.getY();
                
                update((x - (x - this.sprite.getX())), (y - (y - this.sprite.getY())));

                System.out.println("here");
            }

        }
    }


    /*public void updateAccordingToTheMouse(float x, float y) {
        if (Gdx.input.isTouched() || Gdx.input.justTouched()) {
            float raio = 0.5f;
            while ((this.sprite.getX() <= (x - raio)) || (this.sprite.getX() >= (x + raio))) {
                if (this.sprite.getX() < x) {
                    update(this.sprite.getX() + 0.00005f, this.sprite.getY());
                } else if (this.sprite.getX() > x) {
                    update(this.sprite.getX() - 0.00005f, this.sprite.getY());
                }
            }
        }
    }*/

 /*public void updateAccordingToTheMouse(float x , float y){
        Rectangle c1 = new Rectangle(x, y, 1,1);
        Collision cc = new Collision();
        if (Gdx.input.isTouched()||Gdx.input.justTouched())
            alvo= new Vector2(x, y);
            //movimentacao.Calculate(new Vector2(x, y),dt);
        //else
            //movimentacao.Calculate(dt);
        //update(movimentacao.getPos().getPosicao().x,movimentacao.getPos().getPosicao().y);
    }*/
    public void render_area_collision() {

        // metodo para mostrar o retangulo e circulo de colisão
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.identity();
        this.shapeRenderer.setColor(Color.RED);
        this.shapeRenderer.rect(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
        this.shapeRenderer.end();

    }

    @Override
    public boolean collidesWith(Collidable other) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    }
}
