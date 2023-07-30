package com.silauras.client;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.silauras.client.entities.Character;
import com.silauras.client.entities.NetworkCharacter;
import com.silauras.shared.response.PlayerCharacterPositionResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameClient {
    private final Client client;
    private final Map<Integer, NetworkCharacter> characters;

    private Vector2 lastSentPosition = new Vector2();

    public GameClient() {
        client = new Client();
        characters = new HashMap<>();
        registerClasses();
        addListeners();
    }

    private void registerClasses() {
        // Зарегистрируем классы, которые будут использоваться для обмена данными
        client.getKryo().register(PlayerCharacterPositionResponse.class);
    }

    private void addListeners() {
        // Добавим слушателя для обработки входящих сообщений
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof PlayerCharacterPositionResponse) {
                    PlayerCharacterPositionResponse response = (PlayerCharacterPositionResponse) object;
                    Character character = characters.get(response.getId());
                    if (character != null) {
                        // Обновляем положение персонажа на основе данных от сервера
                        character.setPosition(new Vector2(response.getPositionX(), response.getPositionY()));
                    }
                }
            }
        });
    }

    public void connect() throws IOException {
        // Устанавливаем соединение с сервером (указываем IP и порт сервера)
        client.start();
        client.connect(5000, "localhost", 9009, 9099);
    }

    public void addCharacter(int id, NetworkCharacter character) {
        characters.put(id, character);
    }

    // Создаем ExecutorService с одним рабочим потоком
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void sendPosition(float positionX, float positionY) {
        // Проверяем, изменилась ли позиция персонажа
        if (positionX != lastSentPosition.x || positionY != lastSentPosition.y) {
            // Отправляем данные на сервер в отдельном потоке
            executorService.submit(() -> {
                PlayerCharacterPositionResponse positionResponse = new PlayerCharacterPositionResponse(0, positionX, positionY);
                client.sendTCP(positionResponse);
            });

            // Обновляем значение последнего отправленного положения
            lastSentPosition.set(positionX, positionY);
        }
    }

    @SneakyThrows
    public void update() {
        new Thread(() -> {
            try {
                client.update(50);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    // Метод для получения всех персонажей
    public Map<Integer, NetworkCharacter> getCharacters() {
        return characters;
    }

    public Vector2 getPosition(int characterId) {
        return characters.get(characterId).getPosition();
    }
}
