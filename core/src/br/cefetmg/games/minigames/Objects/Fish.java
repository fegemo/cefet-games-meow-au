/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.Objects;

import br.cefetmg.games.Config;
import br.cefetmg.games.collision.Collidable;
import br.cefetmg.games.collision.Collision;
import br.cefetmg.games.movement.Buscar;
import br.cefetmg.games.movement.Direcionamento;
import br.cefetmg.games.movement.Pose;
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
 * @author Luiza-Pedro
 */
public class Fish extends Sprite implements Collidable {

    private Vector2 alvo;
    private Pose pose;
    
    private int lado;
    private Sprite sprite;
    private TextureRegion[][] region;
    private Circle circle;
    private ShapeRenderer shapeRenderer;
    
    private float x_tempo = 0.0f;
    private boolean aux = true;

    public Fish(Texture texture) {
        
        this.sprite = new Sprite(texture);
        this.shapeRenderer = new ShapeRenderer();
        this.sprite.setPosition(20.0f, 220.0f);
        this.circle = new Circle( this.sprite.getX() + (this.sprite.getWidth()/2), this.sprite.getY() + (this.sprite.getHeight()/2),
                (float) Math.sqrt( (Math.pow(this.sprite.getHeight()/2,2)) + Math.pow(this.sprite.getWidth()/2,2)));
        alvo=new Vector2(20, 220);
        this.pose=new Pose(alvo);
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

    public void update(float dt) {
            // pergunta ao algoritmo de movimento (comportamento) 
            // para onde devemos ir
            Buscar b = new Buscar();
            if(this.alvo != null){
                Direcionamento direcionamento = b.guiar(this.pose,this.alvo,this.circle.radius,10,1);
                // faz a simulação física usando novo estado da entidade cinemática
                pose.atualiza(direcionamento, dt);
                update(pose.posicao.x,pose.posicao.y);
            }
    }
    
    public void render(SpriteBatch sb, float x, float y) {
        float x_sprite = this.sprite.getX();
        float y_sprite = this.sprite.getY();
        if ((1280 >= x_sprite && 0 <= x_sprite) && (720 >= y_sprite && 0 <= y_sprite)) {
            //se estiver dentro da tela
            //update(x,y);
        if ((1280 >= this.sprite.getX() && 0 <= this.sprite.getX()) && (720 >= this.sprite.getY() && 0 <= this.sprite.getY())) {
            // se dentro da tela
            this.sprite.draw(sb);
        }
        }}
    public void updateAccordingToTheMouse(float x , float y){
        Rectangle c1 = new Rectangle(x, y, 1,1);
        Collision cc = new Collision();
        if (Gdx.input.isTouched()||Gdx.input.justTouched())
            alvo= new Vector2(x, y);
    }
    
    
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
