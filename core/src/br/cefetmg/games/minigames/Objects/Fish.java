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
    private Circle circle;
    private ShapeRenderer shapeRenderer;
    private SeekDynamic movimentacao;
    
    private float x_tempo = 0.0f;
    private boolean aux = true;

    public Fish(Texture texture) {
        
        this.sprite = new Sprite(texture);
        
        this.shapeRenderer = new ShapeRenderer();
        this.sprite.setPosition(20.0f, 220.0f);
        this.circle = new Circle( this.sprite.getX() + (this.sprite.getWidth()/2), this.sprite.getY() + (this.sprite.getHeight()/2),
                (float) Math.sqrt( (Math.pow(this.sprite.getHeight()/2,2)) + Math.pow(this.sprite.getWidth()/2,2)));
        

        this.movimentacao = new SeekDynamic(new Vector2(sprite.getX(), sprite.getY()));
    }

    public void update(float x, float y) {
        
        //atualiza a posição do peixe
        this.sprite.setPosition(x, y);
        //atualiza a posicao do retangulo de colisao
        this.circle.setPosition(x + (this.sprite.getWidth()/2) , y + (this.sprite.getHeight()/2));
        
        if( aux ){
            this.x_tempo = x;
            aux = !aux;
        }
        
        if( this.x_tempo > x){
            if(!this.sprite.isFlipX()){
                this.sprite.flip(true,false);
            }
        }else{
            if(this.sprite.isFlipX()){
               this.sprite.flip(true,false); 
            }
            
        }
        
        
        

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
        if ((1280 >= this.sprite.getX() && 0 <= this.sprite.getX()) && (720 >= this.sprite.getY() && 0 <= this.sprite.getY())) {
            // se dentro da tela
            this.sprite.draw(sb);
        }
    }

    public void updateAccordingToTheMouse(float x , float y){
        if (Gdx.input.isTouched()||Gdx.input.justTouched()) {     
        //se clicar com o mouse sobre o objeto Fish
            if ( (x >= (this.circle.x-this.circle.radius) && x <= (this.circle.x+this.circle.radius))
                    && (y >= (this.circle.y-this.circle.radius) && y <= (this.circle.y+this.circle.radius)) ){
                // coloca o ponteiro do mouse no centro da sprite
                float delta_x = (x - this.circle.x);
                float delta_y = (y - this.circle.y);
                
                update( this.sprite.getX() + delta_x , this.sprite.getY() + delta_y );
                
                //this.sprite.flip(true, false);
                System.out.println( this.sprite.getX() + " _ " + this.sprite.getY() + " " +
                        this.sprite.getOriginX() + " " + this.sprite.isFlipX());
                
                }
            }     
        }


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

        // metodo para mostrar circulo de colisão
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.identity();
        this.shapeRenderer.setColor(Color.RED);
        this.shapeRenderer.circle(this.circle.x, this.circle.y, this.circle.radius);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Circle getMinimumEnclosingBall() {
        return this.circle;
    }
}
