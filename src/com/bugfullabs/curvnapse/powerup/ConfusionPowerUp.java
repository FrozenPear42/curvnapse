package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

public class ConfusionPowerUp extends PowerUp {

    @Override
    public void onBegin(Snake pSnake) {
        pSnake.setConfused(true);
    }

    @Override
    public void onEnd(Snake pSnake) {
        pSnake.setConfused(false);
    }

    @Override
    public double getDuration() {
        return 5000;
    }
}
