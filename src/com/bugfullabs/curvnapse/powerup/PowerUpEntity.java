package com.bugfullabs.curvnapse.powerup;


import com.bugfullabs.curvnapse.utils.Vec2;

import java.io.Serializable;

public class PowerUpEntity implements Serializable {
    private Vec2 mPosition;
    private PowerUp.PowerType mType;

    public static final int HEIGHT = 36;
    public static final int WIDTH = 36;

    public PowerUpEntity(Vec2 pPosition, PowerUp.PowerType pType) {
        mPosition = pPosition;
        mType = pType;
    }

    public PowerUp.PowerType getType() {
        return mType;
    }

    public Vec2 getPosition() {
        return mPosition;
    }

    public boolean isCollision(Vec2 pPoint) {
        double dx = pPoint.x - mPosition.x;
        double dy = pPoint.y - mPosition.y;
        return (((dx * dx) + (dy * dy)) <= ((WIDTH * WIDTH) / 4));
    }
}
