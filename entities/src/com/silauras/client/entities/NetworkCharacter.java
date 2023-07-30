package com.silauras.client.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NetworkCharacter extends Character {

    private int id;
    public NetworkCharacter(Vector2 position, Texture texture) {
        super(position, texture);
    }
}
