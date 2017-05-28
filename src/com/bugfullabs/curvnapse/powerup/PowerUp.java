package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

/**
 * Abstract class for PowerUp logic instance
 */
public abstract class PowerUp {
    /**
     * Enum with all  the PowerUps types
     */
    public enum PowerType {
        SELF_FAST,
        SELF_SHRINK,
        SELF_SLOW,
        SELF_INVISIBLE,
        GLOBAL_RANDOM_DEATH,
        SELF_SMALL_RADIUS,
        GLOBAL_ERASE,
        ENEMY_FAST,
        ENEMY_GROW,
        ENEMY_SLOW,
        ENEMY_CONFUSION,
        ENEMY_LARGE_RADIUS,
//        ALL_TRAVERSE,
//        GLOBAL_RANDOM_DEATH,
//        GLOBAL_RAIN,
//        GLOBAL_XD
    }

    /**
     * PowerUp target
     */
    public enum Target {
        SELF,
        OTHERS,
        ALL,
        GLOBAL
    }

    private Target mTarget;
    private double mDuration;

    /**
     * Create new PowerUp
     * @param pTarget target of the PowerUp
     * @param pDuration duration in ms of the PowerUp
     */
    PowerUp(Target pTarget, double pDuration) {
        mTarget = pTarget;
        mDuration = pDuration;
    }

    /**
     * Returns PowerUp Target(s)
     *
     * @return PowerUp target
     */
    public Target getTarget() {
        return mTarget;
    }

    /**
     * Returns PowerUp duration in ms
     *
     * @return PowerUp duration in milliseconds
     */
    public double getDuration() {
        return mDuration;
    }

    /**
     * Called on PowerUp activation
     *
     * @param pSnake Snake affected with PowerUp
     */
    public abstract void onBegin(Snake pSnake);

    /**
     * Called on PowerUp end
     *
     * @param pSnake Snake affected with PowerUp
     */
    public abstract void onEnd(Snake pSnake);
}
