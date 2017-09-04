/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.Objects;

import br.cefetmg.games.Config;
import br.cefetmg.games.collision.Collidable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 *
 * @author Thepe
 */
public class Fish extends Sprite implements Collidable {

    private Vector2 dposi;
    private int lado;
    //private final Texture texture;

    static final int FRAME_WIDTH = 28;
    static final int FRAME_HEIGHT = 36;

    public Fish(Texture fishSprite) {
        super(fishSprite);
    }

    public void update(float dt) {
        float auxX = dposi.x > 0 ? min(Config.WORLD_WIDTH, super.getX() + dposi.x) : max(0, super.getX() + dposi.x);
        float auxY = dposi.y > 0 ? min(Config.WORLD_HEIGHT, super.getY() + dposi.y) : max(0, super.getY() + dposi.y);
        super.setPosition(auxX, auxY);
        setDposi(0, 0);
    }

    public void setDposi(float x, float y) {
        this.dposi.x = x;
        this.dposi.y = y;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
