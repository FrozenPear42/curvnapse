package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

public class GrowPowerUp extends PowerUp {
    double mOldSize;

    @Override
    public void onBegin(Snake pSnake) {
        mOldSize = pSnake.getSize();
        pSnake.setSize(mOldSize + 3.0f);
    }

    @Override
    public void onEnd(Snake pSnake) {
        pSnake.setSize(mOldSize);
    }

    @Override
    public double getDuration() {
        return 5000;
    }
}
