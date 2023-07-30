package com.silauras.server;

public class CircleBot {private final int id;
    private final float radius;
    private final float speed;
    private float angle = 0;

    public CircleBot(int id, float radius, float speed) {
        this.id = id;
        this.radius = radius;
        this.speed = speed;
    }

    public void update(float delta) {
        // Увеличиваем угол на основе скорости и времени прошедшего с предыдущего обновления
        angle += speed * delta;

        // Если угол стал больше или равен 360 градусам, сбрасываем его обратно на 0
        if (angle >= 360) {
            angle -= 360;
        }
    }

    public int getId() {
        return id;
    }

    public float getPositionX() {
        // Вычисляем положение бота по X на основе радиуса и текущего угла
        return radius * (float) Math.cos(Math.toRadians(angle));
    }

    public float getPositionY() {
        // Вычисляем положение бота по Y на основе радиуса и текущего угла
        return radius * (float) Math.sin(Math.toRadians(angle));
    }
}