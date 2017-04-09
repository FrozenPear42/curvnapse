package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

public abstract class PowerUp {
    public enum PowerType {
        SELF_FAST,
        SELF_SHRINK,
        SELF_SLOW,
        SELF_INVISIBLE,
        SELF_TRAVERSE,
        SELF_SMALL_RADIUS,
        GLOBAL_ERASE,
        ENEMY_FAST,
        ENEMY_GROW,
        ENEMY_SLOW,
        ENEMY_CONFUSION,
        ENEMY_LARGE_RADIUS,
        ALL_TRAVERSE,
        GLOBAL_RANDOM_DEATH,
        GLOBAL_RAIN,
        GLOBAL_XD
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
            case SELF_FAST:
            case SELF_SHRINK:
            case SELF_SLOW:
            case SELF_INVISIBLE:
            case SELF_TRAVERSE:
            case SELF_SMALL_RADIUS:
                return Target.SELF;
            case ENEMY_FAST:
            case ENEMY_GROW:
            case ENEMY_SLOW:
            case ENEMY_CONFUSION:
            case ENEMY_LARGE_RADIUS:
                return Target.OTHERS;
            case ALL_TRAVERSE:
                return Target.ALL;
            case GLOBAL_RAIN:
            case GLOBAL_ERASE:
            case GLOBAL_RANDOM_DEATH:
            case GLOBAL_XD:
            default:
                return Target.GLOBAL;
        }
    }

    public static Action getAction(PowerType pType) {
        switch (pType) {
            case SELF_FAST:
            case ENEMY_FAST:
                return Action.SPEED;
            case SELF_SLOW:
            case ENEMY_SLOW:
                return Action.SLOW;
            case SELF_SHRINK:
            case SELF_INVISIBLE:
            case SELF_TRAVERSE:
            case SELF_SMALL_RADIUS:
            case ENEMY_GROW:
            case ENEMY_CONFUSION:
            case ENEMY_LARGE_RADIUS:
            case ALL_TRAVERSE:
            case GLOBAL_RAIN:
            case GLOBAL_ERASE:
            case GLOBAL_RANDOM_DEATH:
            case GLOBAL_XD:
            default:
                return Action.NO_ACTION;
        }
    }

    public static PowerUp fromType(PowerType pType) {
        switch (pType) {
            case SELF_FAST:
            case ENEMY_FAST:
                return new FastPowerUp();
            case SELF_SLOW:
            case ENEMY_SLOW:
                return new SlowPowerUp();
            case ENEMY_GROW:
                return new GrowPowerUp();
            case SELF_SMALL_RADIUS:
                return new ShrinkRadiusPowerUp();
            case ENEMY_LARGE_RADIUS:
                return new GrowRadiusPowerUp();
            case SELF_SHRINK:
            case SELF_INVISIBLE:
            case SELF_TRAVERSE:
            case ENEMY_CONFUSION:
            case ALL_TRAVERSE:
            case GLOBAL_RAIN:
            case GLOBAL_ERASE:
            case GLOBAL_RANDOM_DEATH:
            case GLOBAL_XD:
            default:
                return new PowerUp() {
                    @Override
                    public void onBegin(Snake pSnake) {
                    }

                    @Override
                    public void onEnd(Snake pSnake) {
                    }

                    @Override
                    public double getDuration() {
                        return 0;
                    }
                };
        }
    }

    public abstract void onBegin(Snake pSnake);

    public abstract void onEnd(Snake pSnake);

    public abstract double getDuration();
}
