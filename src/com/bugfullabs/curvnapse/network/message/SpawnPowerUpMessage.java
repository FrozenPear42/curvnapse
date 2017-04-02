package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.powerup.PowerUpEntity;
import com.bugfullabs.curvnapse.utils.Vec2;

public class SpawnPowerUpMessage extends Message {

    private PowerUpEntity mPowerUp;

    public SpawnPowerUpMessage(PowerUpEntity pPowerUp) {
        super(Type.SPAWN_POWERUP);
        mPowerUp = pPowerUp;
    }

    public PowerUpEntity getPowerUp() {
        return mPowerUp;
    }
}
