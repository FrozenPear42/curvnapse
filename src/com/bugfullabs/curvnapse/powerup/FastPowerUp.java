package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

public class FastPowerUp extends PowerUp {
    private static final double MULTIPLIER = 2.0;

    @Override
    public void onBegin(Snake pSnake) {
        pSnake.setSpeed(pSnake.getSpeed() * MULTIPLIER);
    }

    @Override
    public void onEnd(Snake pSnake) {
        pSnake.setSpeed(pSnake.getSpeed() / MULTIPLIER);
    }

    @Override
    public double getDuration() {
        return 5000.0f;
    }
}
