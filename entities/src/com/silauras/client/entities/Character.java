package com.silauras.client.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract  class Character {

    private final Texture texture;
    private Vector2 position;

    public Character(Vector2 position, Texture texture){
        this.position = position;
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

}
