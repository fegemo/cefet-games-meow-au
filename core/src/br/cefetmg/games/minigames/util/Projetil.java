/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author NataliaNatsumy
 */
public class Projetil {
    
    public float maxVelocity = 200;
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    float targetX;
    float targetY;
    Texture texture;
    public Sprite projeSprite;
    
    public Projetil(Texture texture){
        this.texture=texture;
        projeSprite = new Sprite(texture);
    }
    
    public void shoot(float targerX, float targetY){
        velocity.set(targetX - position.x, targetY - position.y).nor().scl(Math.min(position.dst(targetX, targetY), maxVelocity));
    }
    
    public void shoot2(float targerX, float targetY){
        velocity.set(position.x+10, targetY - position.y).nor().scl(Math.min(position.dst(targetX, targetY), maxVelocity));
    }
    
    public void update(float deltaTime){
        position.add(velocity.x*deltaTime, velocity.y*deltaTime);
        //velocity.scl(1 - (0.98f * deltaTime));
        projeSprite.setPosition(position.x,position.y);
    }
    
    public void setPosition(float x, float y){
        position.set(x, y);
        projeSprite.setPosition(position.x,position.y);
    }
    
}
