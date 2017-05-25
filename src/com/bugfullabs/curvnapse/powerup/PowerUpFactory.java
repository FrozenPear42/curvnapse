package com.bugfullabs.curvnapse.powerup;

import com.bugfullabs.curvnapse.snake.Snake;

/**
 * Class for creating new PowerUps of given type
 */
public class PowerUpFactory {

    private static final double RADIUS_FACTOR = 1.6;
    private static final double SPEED_FACTOR = 1.6;
    private static final double SIZE_FACTOR = 1.6;
    private static final double DEFAULT_TIME = 4000;


    /**
     * Create new PowerUp of given type
     * @param pType PowerUp type
     * @return new, un used PowerUp
     */
    public static PowerUp fromType(PowerUp.PowerType pType) {
        switch (pType) {
            case SELF_FAST:
                return build(
                        PowerUp.Target.SELF,
                        DEFAULT_TIME,
                        s -> s.setSpeed(s.getSpeed() * SPEED_FACTOR),
                        s -> s.setSpeed(s.getSpeed() / SPEED_FACTOR));

            case ENEMY_FAST:
                return build(
                        PowerUp.Target.OTHERS,
                        DEFAULT_TIME,
                        s -> s.setSpeed(s.getSpeed() * SPEED_FACTOR),
                        s -> s.setSpeed(s.getSpeed() / SPEED_FACTOR));

            case SELF_SLOW:
                return build(
                        PowerUp.Target.SELF,
                        DEFAULT_TIME,
                        s -> s.setSpeed(s.getSpeed() / SPEED_FACTOR),
                        s -> s.setSpeed(s.getSpeed() * SPEED_FACTOR));

            case ENEMY_SLOW:
                return build(
                        PowerUp.Target.OTHERS,
                        DEFAULT_TIME,
                        s -> s.setSpeed(s.getSpeed() / SPEED_FACTOR),
                        s -> s.setSpeed(s.getSpeed() * SPEED_FACTOR));

            case ENEMY_GROW:
                return build(
                        PowerUp.Target.OTHERS,
                        DEFAULT_TIME,
                        s -> s.setSize(s.getSize() * SIZE_FACTOR),
                        s -> s.setSize(s.getSize() / SIZE_FACTOR));

            case SELF_SHRINK:
                return build(
                        PowerUp.Target.SELF,
                        DEFAULT_TIME,
                        s -> s.setSize(s.getSize() / SIZE_FACTOR),
                        s -> s.setSize(s.getSize() * SIZE_FACTOR));

            case SELF_SMALL_RADIUS:
                return build(
                        PowerUp.Target.SELF,
                        DEFAULT_TIME,
                        s -> s.setTurnRadius(s.getTurnRadius() / RADIUS_FACTOR),
                        s -> s.setTurnRadius(s.getTurnRadius() * RADIUS_FACTOR));

            case ENEMY_LARGE_RADIUS:
                return build(
                        PowerUp.Target.OTHERS,
                        DEFAULT_TIME,
                        s -> s.setTurnRadius(s.getTurnRadius() * RADIUS_FACTOR),
                        s -> s.setTurnRadius(s.getTurnRadius() / RADIUS_FACTOR));

            case ENEMY_CONFUSION:
                return build(
                        PowerUp.Target.OTHERS,
                        DEFAULT_TIME,
                        s -> s.setConfused(true),
                        s -> s.setConfused(false));

            case SELF_INVISIBLE:
                return build(
                        PowerUp.Target.SELF,
                        DEFAULT_TIME,
                        s -> s.setInvisible(true),
                        s -> s.setInvisible(false));

            case SELF_TRAVERSE:
            case ALL_TRAVERSE:
            case GLOBAL_RAIN:
            case GLOBAL_ERASE:
            case GLOBAL_RANDOM_DEATH:
            case GLOBAL_XD:
            default:
                return build(PowerUp.Target.GLOBAL, 0, null, null);
        }
    }

    private static PowerUp build(PowerUp.Target pTarget, double pDuration, OnStart pStart, OnEnd pEnd) {
        return new PowerUp(pTarget, pDuration) {
            @Override
            public void onBegin(Snake pSnake) {
                if (pStart != null)
                    pStart.onStart(pSnake);
            }

            @Override
            public void onEnd(Snake pSnake) {
                if (pEnd != null)
                    pEnd.onEnd(pSnake);
            }
        };
    }

    private interface OnStart {
        void onStart(Snake pSnake);
    }

    private interface OnEnd {
        void onEnd(Snake pSnake);
    }

}
