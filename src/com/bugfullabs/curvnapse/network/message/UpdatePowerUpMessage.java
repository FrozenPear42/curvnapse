package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.powerup.PowerUpEntity;

import java.util.LinkedList;

public class UpdatePowerUpMessage extends Message {

    private LinkedList<PowerUpEntity> mPowerUp;

    public UpdatePowerUpMessage(LinkedList<PowerUpEntity> pPowerUp) {
        super(Type.SPAWN_POWERUP);
        mPowerUp = pPowerUp;
    }

    public LinkedList<PowerUpEntity> getPowerUp() {
        return mPowerUp;
    }
}
