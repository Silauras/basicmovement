package com.silauras.client;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tilemap {
    private final int mapWidth;
    private final int mapHeight;
    private final int tileWidth;
    private final int tileHeight;
    private final Tile[][] tiles;

    public Tilemap(int mapWidth, int mapHeight, int tileWidth, int tileHeight, Texture tileTexture) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        tiles = new Tile[mapWidth][mapHeight];
        initializeTiles(tileTexture);
    }

    private void initializeTiles(Texture tileTexture) {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Vector2 position = new Vector2(x * tileWidth, y * tileHeight);
                tiles[x][y] = new Tile(position, tileTexture);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                tiles[x][y].draw(batch);
            }
        }
    }

    public void dispose() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                tiles[x][y].dispose();
            }
        }
    }
}

