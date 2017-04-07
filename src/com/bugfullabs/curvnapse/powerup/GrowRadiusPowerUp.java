package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

public class GrowRadiusPowerUp extends PowerUp {
    private static final double MULTIPLIER = 1.5;
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
