package com.silauras.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture floorTexture;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		floorTexture = new Texture("basic_floor_tile.png");
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		for (int i = 0; i < Gdx.graphics.getWidth(); i+= floorTexture.getWidth()) {
			for (int j = 0; j < Gdx.graphics.getHeight(); j+= floorTexture.getHeight()) {
				batch.draw(floorTexture, i, j);
			}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		floorTexture.dispose();
	}
}
