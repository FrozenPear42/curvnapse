package com.bugfullabs.curvnapse.network.message.game;

import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.powerup.PowerUpEntity;

import java.util.LinkedList;
import java.util.List;

/**
 * Oh baby, we have new PowerUp, better draw it on board then!
 */
public class UpdatePowerUpMessage extends Message {

    private List<PowerUpEntity> mPowerUp;

    public UpdatePowerUpMessage(List<PowerUpEntity> pPowerUp) {
        super(Type.SPAWN_POWERUP);
        mPowerUp = pPowerUp;
    }

    public List<PowerUpEntity> getPowerUp() {
        return mPowerUp;
    }
}
