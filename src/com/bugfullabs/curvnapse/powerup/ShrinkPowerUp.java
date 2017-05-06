package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

public class ShrinkPowerUp extends PowerUp {
    private static final double MULTIPLIER = 0.6;

    @Override
    public void onBegin(Snake pSnake) {
        pSnake.setSize(pSnake.getSize() * MULTIPLIER);
    }

    @Override
    public void onEnd(Snake pSnake) {
        pSnake.setSize(pSnake.getSize() / MULTIPLIER);
    }

    @Override
    public double getDuration() {
        return 5000;
    }
}
