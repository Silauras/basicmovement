package com.silauras.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.silauras.client.entities.Character;
import com.silauras.client.entities.NetworkCharacter;
import com.silauras.client.entities.NetworkPlayerCharacter;
import com.silauras.client.entities.PlayerCharacter;

import java.io.IOException;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private NetworkCharacter character;
	private Tilemap tilemap;

	private OrthographicCamera camera;
	private Vector2 desiredPosition; // Позиция, к которой стремится камера
	private float lerpSpeed = 0.1f; // Скорость интерполяции (значение от 0 до 1)
	private float cameraZoom = 1.0f; // Масштаб камеры

	private GameClient gameClient;


	@Override
	public void create () {
		batch = new SpriteBatch();
		character = new NetworkPlayerCharacter(new Vector2(10, 10), new Texture(TextureConstants.BASIC_CHARACTER_TEXTURE_PATH));
		tilemap = new Tilemap(10, 10, 32, 32, new Texture(TextureConstants.BASIC_FLOOR_TILE_TEXTURE_PATH));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
		camera.update();

		// Инициализируем желаемую позицию камеры
		desiredPosition = new Vector2(camera.position.x, camera.position.y);

		// Инициализируем клиент и устанавливаем соединение с сервером
		gameClient = new GameClient();
		try {
			gameClient.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Добавляем персонажа клиента в список персонажей
		gameClient.addCharacter(0, character);
	}

	@Override
	public void render() {
		// Обновляем камеру и пользовательский ввод
		updateCamera();
		handleInput();

		// Устанавливаем матрицу проекции для SpriteBatch
		batch.setProjectionMatrix(camera.combined);

		// Обновляем клиент
		gameClient.update();

		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		tilemap.draw(batch);
		batch.draw(character.getTexture(), character.getPosition().x, character.getPosition().y);
		batch.end();

		// Обрабатываем ответ от сервера
		handleServerResponse();
	}

	@Override
	public void dispose() {
		batch.dispose();
		character.getTexture().dispose();
		tilemap.dispose();
	}

	private void updateCamera() {
		// Обновляем желаемую позицию камеры на основе позиции персонажа
		desiredPosition.set(character.getPosition().x + character.getTexture().getWidth() / 2f,
				character.getPosition().y + character.getTexture().getHeight() / 2f);

		// Интерполируем текущую позицию камеры к желаемой позиции
		float lerpX = Interpolation.linear.apply(camera.position.x, desiredPosition.x, lerpSpeed);
		float lerpY = Interpolation.linear.apply(camera.position.y, desiredPosition.y, lerpSpeed);
		camera.position.set(lerpX, lerpY, 0);

		// Устанавливаем масштаб камеры
		camera.zoom = cameraZoom;

		camera.update();
	}

	private void handleInput() {
		// Обработка клавиш + и - для управления масштабом камеры
		if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS) || Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
			cameraZoom += 0.1f;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
			cameraZoom -= 0.1f;
			if (cameraZoom < 0.1f) cameraZoom = 0.1f;
		}

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

	private void handleServerResponse() {
		for (NetworkCharacter character : gameClient.getCharacters().values()) {
			// Обновляем положение персонажа на основе данных от сервера
			character.setPosition(gameClient.getPosition(character.getId()));
		}
	}
}
