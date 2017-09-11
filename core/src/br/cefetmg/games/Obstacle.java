package br.cefetmg.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
    private Texture color;
    private Sprite visible;
    private Vector2 position;
    private Rectangle rec;
    private SpriteBatch batch;
    private static float friction = 2;
    public Obstacle(SpriteBatch batch, Vector2 position, float width, float height) {
        this.color = new Texture("avoider/grey.png");
        this.batch = batch;
        this.position = position;
        rec = new Rectangle(this.position.x, this.position.y, width, height);
        visible = new Sprite(color);
        visible.setPosition(this.position.x, this.position.y);
        visible.setSize(width, height);
    }

    public void draw() {
        visible.draw(batch);
    }

    public Rectangle getRec() {
        return rec;
    }

}
