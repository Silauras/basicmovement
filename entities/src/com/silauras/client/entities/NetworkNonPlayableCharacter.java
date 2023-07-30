package com.silauras.client.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class NetworkNonPlayableCharacter extends NetworkCharacter {
    public NetworkNonPlayableCharacter(Vector2 position, Texture texture) {
        super(position, texture);
    }
}
