package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.utils.Vec2;

public class SpawnPowerUpMessage extends Message {

    private PowerUp.PowerType mPowerType;
    private Vec2 mPosition;

    public SpawnPowerUpMessage(PowerUp.PowerType pPowerType, Vec2 pPosition) {
        super(Type.SPAWN_POWERUP);
        mPowerType = pPowerType;
        mPosition = pPosition;
    }

    public PowerUp.PowerType getPowerType() {
        return mPowerType;
    }

    public Vec2 getPosition() {
        return mPosition;
    }
}
