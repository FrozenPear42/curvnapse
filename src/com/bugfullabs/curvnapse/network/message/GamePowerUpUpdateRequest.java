package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.powerup.PowerUp;

public class GamePowerUpUpdateRequest extends Message {
    private PowerUp.PowerType pType;
    private boolean mState;

    public GamePowerUpUpdateRequest(PowerUp.PowerType pType, boolean pState) {
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
