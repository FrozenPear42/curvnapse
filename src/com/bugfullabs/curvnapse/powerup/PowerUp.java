package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

public abstract class PowerUp {
    public enum PowerType {
        FAST_SELF,
        SHRINK_SELF,
        SLOW_SELF,
        INVISIBLE,
        NO_BORDER,
        SMALL_TURNS,
        ERASE,
        FAST_ENEMY,
        GROW_ENEMY,
        SLOW_ENEMY,
        CONFUSION_ENEMY,
        LARGE_TURNS_ENEMY,
        BORDERS_ALL,
        RANDOM_DEATH,
        RAIN,
        XD
    }
    public abstract void onBegin(Snake pSnake);
    public abstract void onEnd(Snake pSnake);
    public abstract double getDuration();
}
