package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

import java.util.HashMap;

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

    public enum Target {
        SELF,
        OTHERS,
        ALL,
        GLOBAL
    }

    public enum Action {
        SPEED,
        SLOW,
        NO_ACTION,
    }

    public static Target getTarget(PowerType pType) {
        switch (pType) {
            case FAST_SELF:
            case SHRINK_SELF:
            case SLOW_SELF:
            case INVISIBLE:
            case NO_BORDER:
            case SMALL_TURNS:
                return Target.SELF;
            case FAST_ENEMY:
            case GROW_ENEMY:
            case SLOW_ENEMY:
            case CONFUSION_ENEMY:
            case LARGE_TURNS_ENEMY:
                return Target.OTHERS;
            case BORDERS_ALL:
                return Target.ALL;
            case RAIN:
            case ERASE:
            case RANDOM_DEATH:
            case XD:
            default:
                return Target.GLOBAL;
        }
    }

    public static Action getAction(PowerType pType) {
        switch (pType) {
            case FAST_SELF:
            case FAST_ENEMY:
                return Action.SPEED;
            case SLOW_SELF:
            case SLOW_ENEMY:
                return Action.SLOW;
            case SHRINK_SELF:
            case INVISIBLE:
            case NO_BORDER:
            case SMALL_TURNS:
            case GROW_ENEMY:
            case CONFUSION_ENEMY:
            case LARGE_TURNS_ENEMY:
            case BORDERS_ALL:
            case RAIN:
            case ERASE:
            case RANDOM_DEATH:
            case XD:
            default:
                return Action.NO_ACTION;
        }
    }

    public abstract void onBegin(Snake pSnake);

    public abstract void onEnd(Snake pSnake);

    public abstract double getDuration();
}
