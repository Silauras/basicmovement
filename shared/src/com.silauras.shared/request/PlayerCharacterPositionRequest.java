package com.silauras.shared.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PlayerCharacterPositionRequest {
    private int id;
    private float positionX;
    private float positionY;

}
