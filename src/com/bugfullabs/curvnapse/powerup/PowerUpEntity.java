package com.bugfullabs.curvnapse.powerup;


import com.bugfullabs.curvnapse.utils.Vec2;

import java.io.Serializable;

/**
 * Class for PowertUp that shows on the board -has it own position and can collide
 */
public class PowerUpEntity implements Serializable {
    private Vec2 mPosition;
    private PowerUp.PowerType mType;

    public static final int HEIGHT = 36;
    public static final int WIDTH = 36;

    /**
     * Create new entity for PowerUp of given type on given position
     *
     * @param pPosition position for entity to be placed
     * @param pType     PowerUp type
     */
    public PowerUpEntity(Vec2 pPosition, PowerUp.PowerType pType) {
        mPosition = pPosition;
        mType = pType;
    }

    /**
     * Returns PowerUp's type
     *
     * @return PowerUp's type
     */
    public PowerUp.PowerType getType() {
        return mType;
    }

    /**
     * Returns position
     *
     * @return position
     */
    public Vec2 getPosition() {
        return mPosition;
    }

    /**
     * Check for collision with given point
     * @param pPoint point bo check collision with
     * @return true if collision, false if not
     */
    public boolean isCollision(Vec2 pPoint) {
        double dx = pPoint.x - mPosition.x;
        double dy = pPoint.y - mPosition.y;
        return (((dx * dx) + (dy * dy)) <= ((WIDTH * WIDTH) / 4));
    }
}
