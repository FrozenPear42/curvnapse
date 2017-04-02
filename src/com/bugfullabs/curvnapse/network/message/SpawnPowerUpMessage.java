package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.powerup.PowerUpEntity;

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
