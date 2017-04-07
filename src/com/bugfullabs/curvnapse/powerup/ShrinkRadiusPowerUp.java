package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

public class ShrinkRadiusPowerUp extends PowerUp {
    private static final double MULTIPLIER = 0.7;
    @Override
    public void onBegin(Snake pSnake) {
        pSnake.setTurnRadius(pSnake.getTurnRadius() * MULTIPLIER);
    }

    @Override
    public void onEnd(Snake pSnake) {
        pSnake.setTurnRadius(pSnake.getTurnRadius() / MULTIPLIER);
    }

    @Override
    public double getDuration() {
        return 5000;
    }
}
