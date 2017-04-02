package com.bugfullabs.curvnapse.powerup;


import com.bugfullabs.curvnapse.utils.Vec2;

public class PowerUpEntity {
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
    
    public boolean isCollision(Vec2 pPoint) {
        return Math.abs(pPoint.x - mPosition.x) <= WIDTH / 2 && Math.abs(pPoint.y - mPosition.y) <= WIDTH / 2;
    }
}
