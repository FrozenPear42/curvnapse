package com.bugfullabs.curvnapse.network.message.game;

import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.powerup.PowerUpEntity;

import java.util.LinkedList;

/**
 * Oh baby, we have new PowerUp, better draw it on board then!
 */
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
