package com.silauras.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerCharacter {
    private int id;
    private String name;
    private float positionX;
    private float positionY;
}
