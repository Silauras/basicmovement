package com.silauras.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.silauras.server.entity.PlayerCharacter;
import com.silauras.shared.request.PlayerCharacterCreatedRequest;
import com.silauras.shared.request.PlayerCharacterPositionRequest;
import com.silauras.shared.response.PlayerCharacterCreatedResponse;
import com.silauras.shared.response.PlayerCharacterPositionResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final List<PlayerCharacter> playerCharacters = new ArrayList<>();
    private static final List<CircleBot> circleBots = new ArrayList<>();
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static final Server server = new Server();


    public static void main(String[] args) throws IOException {
        server.start();
        server.bind(9009, 9099);


        listenPlayerCreation();
        listenPlayerPosition();

        // Создаем и запускаем ботов
        spawnCircleBots();

        // Обновляем положение ботов каждые 200 миллисекунд
        executor.scheduleAtFixedRate(Application::updateBotsPosition, 0, 200, TimeUnit.MILLISECONDS);
    }

    private static void listenPlayerPosition() {
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof PlayerCharacterPositionRequest) {
                    PlayerCharacterPositionRequest request = (PlayerCharacterPositionRequest)object;
                    System.out.println(request);
                    PlayerCharacter player = playerCharacters.get(request.getId());
                    player.setPositionX(request.getPositionX());
                    player.setPositionY(request.getPositionY());

                    List<PlayerCharacterPositionResponse> response = new ArrayList<>();
                    playerCharacters
                            .forEach(playerCharacter -> response
                                    .add(new PlayerCharacterPositionResponse(
                                            playerCharacter.getId(),
                                            playerCharacter.getPositionX(),
                                            playerCharacter.getPositionY())));

                    System.out.println(playerCharacters);
                    connection.sendUDP(response);
                }
            }
        });
    }

    private static void listenPlayerCreation() {
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof PlayerCharacterCreatedRequest) {
                    PlayerCharacterCreatedRequest request = (PlayerCharacterCreatedRequest)object;
                    System.out.println(request);

                    int id = playerCharacters.size(); //I know that is very bad idea for multithreading
                    playerCharacters.add(new PlayerCharacter(id, request.getName(), 0, 0));
                    PlayerCharacterCreatedResponse response = new PlayerCharacterCreatedResponse(id);

                    connection.sendTCP(response);
                }
            }
        });
    }

    private static void spawnCircleBots() {
        // Создаем несколько ботов и добавляем их в список
        // Здесь можно настроить количество ботов и их характеристики
        int numBots = 3;
        float radius = 100;
        float speed = 30;

        for (int i = 0; i < numBots; i++) {
            CircleBot bot = new CircleBot(i, radius, speed);
            circleBots.add(bot);
        }
    }

    private static void updateBotsPosition() {
        // Обновляем положение всех ботов
        float delta = 0.2f; // Время, прошедшее с предыдущего обновления (в секундах)
        circleBots.forEach(bot -> bot.update(delta));

        // Отправляем обновленное положение ботов на всех клиентов
        List<PlayerCharacterPositionResponse> responses = new ArrayList<>();
        circleBots.forEach(bot -> responses.add(new PlayerCharacterPositionResponse(bot.getId(), bot.getPositionX(), bot.getPositionY())));
        server.sendToAllUDP(responses);
    }


}