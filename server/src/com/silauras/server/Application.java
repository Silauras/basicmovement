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

public class Application {

    private static final List<PlayerCharacter> playerCharacters = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
        server.bind(9009, 9099);


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


}