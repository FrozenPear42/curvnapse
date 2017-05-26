package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.powerup.PowerUp;

/**
 * Im so tired of playing with this PowerUp, can we please turn it off?
 */
public class GamePowerUpStateRequest extends Message {
    private PowerUp.PowerType pType;
    private boolean mState;

    public GamePowerUpStateRequest(PowerUp.PowerType pType, boolean pState) {
        super(Type.POWERUP_UPDATE);
        this.pType = pType;
        mState = pState;
    }

    public PowerUp.PowerType getPowerType() {
        return pType;
    }

    public boolean getState() {
        return mState;
    }
}
