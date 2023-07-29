package com.silauras.client;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tile {
    private final Texture texture;
    private final Vector2 position;

    public Tile(Vector2 position, Texture texture) {
        this.position = position;
        this.texture = texture;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose(){
        this.texture.dispose();
    }
}
