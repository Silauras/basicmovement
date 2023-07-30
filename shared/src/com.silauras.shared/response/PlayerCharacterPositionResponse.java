package com.silauras.shared.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PlayerCharacterPositionResponse {

    private int id;
    private float positionX;
    private float positionY;
}
