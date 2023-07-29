package com.silauras.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Character character;
	private Tilemap tilemap;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		character = new Character(new Vector2(10, 10), new Texture("basic_character.png"));
		tilemap = new Tilemap(10, 10, 32, 32, new Texture("basic_floor_tile.png"));
	}

	@Override
	public void render() {
		handleKeyboardInput();

		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		tilemap.draw(batch);
		batch.draw(character.getTexture(), character.getPosition().x, character.getPosition().y);
		batch.end();
	}

	private void handleKeyboardInput() {
		float speed = 5f;

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			character.getPosition().x -= speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			character.getPosition().x += speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			character.getPosition().y += speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			character.getPosition().y -= speed;
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		character.getTexture().dispose();
		tilemap.dispose();
	}
}
