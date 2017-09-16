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

    private int lado;

    private Sprite sprite;
<<<<<<< HEAD
    
=======
>>>>>>> 4597d3ee9f729307fdea8d8b3c0b552bbb17bfd3
    private TextureRegion[][] region;
    private Rectangle rectangle;
    private Circle circle;
    private ShapeRenderer shapeRenderer;
    private SeekDynamic movimentacao;
    
    private Animation normal;

    public Fish(Texture texture) {
        this.sprite = new Sprite(texture);
<<<<<<< HEAD
=======
        
>>>>>>> 4597d3ee9f729307fdea8d8b3c0b552bbb17bfd3
        /*this.region = TextureRegion.split(texture , 188 , 240);
        this.normal = new Animation(0.01f,this.region[1][0],this.region[1][1],this.region[1][2],
        this.region[1][3],this.region[1][4],this.region[1][5],this.region[1][6],this.region[1][7],
        this.region[1][8]);*/
        
        this.circle = new Circle();
        this.shapeRenderer = new ShapeRenderer();
        this.sprite.setPosition(20.0f, 220.0f);
        this.rectangle = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        this.circle = new Circle(sprite.getX()+sprite.getWidth()/2,sprite.getY()+ sprite.getHeight()/2,sprite.getWidth()>sprite.getHeight()?sprite.getWidth()/2:sprite.getHeight()/2);
        this.movimentacao=new SeekDynamic(new Vector2(sprite.getX(),sprite.getY()));
    }

    public void update(float x, float y) {
        this.sprite.setPosition(x,y);
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.circle.x=x;
        this.circle.y=y;
        
    }

    public void render(SpriteBatch sb, float x, float y) {
        
        this.sprite.draw(sb);
        //sb.draw((TextureRegion) this.normal.getKeyFrame(0.1f), this.position.x, this.position.y);;
    }
    public void render(SpriteBatch sb) {
        this.sprite.draw(sb);
    }
    /* vo modificar a movimentacao toda se der certo a gente apaga isso
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
                    //this.sprite.flip(true,false);
                }else if( ultimo_x < x){
                    //this.sprite.flip(true,false);
                }
            }
        }
    }*/
    
    public void updateAccordingToTheMouse(float x , float y){
        Rectangle c1 = new Rectangle(x, y, 1,1);
        Collision cc = new Collision();
        if (Gdx.input.isTouched()||Gdx.input.justTouched())      
            movimentacao.Calculate(new Vector2(x, y));
        else
            movimentacao.Calculate();
        update(movimentacao.getPos().getPosicao().x,movimentacao.getPos().getPosicao().y);
    }

    public void render_area_collision() {

        // metodo para mostrar o retangulo e circulo de colisão
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.identity();
        this.shapeRenderer.setColor(Color.RED);
        this.shapeRenderer.rect(this.rectangle.x, this.rectangle.y, this.rectangle.width,this.rectangle.height);
        this.shapeRenderer.circle(this.circle.x+this.sprite.getWidth()/2,this.circle.y+this.getHeight()/2, this.circle.radius);
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
       return this.circle;
    }
}
