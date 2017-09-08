package br.cefetmg.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
    private Vector2 position;
    private Rectangle rec;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private static float friction = 2;
    public Obstacle(SpriteBatch batch, Vector2 position, float width, float height) {
        this.batch = batch;
        this.position = position;
        rec = new Rectangle(this.position.x, this.position.y, width, height);
        shapeRenderer = new ShapeRenderer();
    }

    public void draw() {
        batch.end();
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(position.x, position.y, rec.getWidth(), rec.getHeight());
        shapeRenderer.end();
        batch.begin();
    }

    public Rectangle getRec() {
        return rec;
    }

}
