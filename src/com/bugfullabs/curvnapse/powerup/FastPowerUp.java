package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

public class FastPowerUp extends PowerUp {

    double previousSpeed;

    @Override
    public void onBegin(Snake pSnake) {
        previousSpeed = pSnake.getSpeed();
        pSnake.setSpeed(previousSpeed * 2.0f);
    }

    @Override
    public void onEnd(Snake pSnake) {
        pSnake.setSpeed(previousSpeed);
    }

    @Override
    public double getDuration() {
        return 5000.0f;
    }
}
