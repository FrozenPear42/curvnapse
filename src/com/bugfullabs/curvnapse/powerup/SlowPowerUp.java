package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

public class SlowPowerUp extends PowerUp {
    private static final double MULTIPLIER = 0.5;

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
