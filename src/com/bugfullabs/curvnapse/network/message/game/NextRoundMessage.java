package com.bugfullabs.curvnapse.network.message.game;

import com.bugfullabs.curvnapse.network.message.Message;

public class NextRoundMessage extends Message {

    private int mRoundNumber;

    public NextRoundMessage(int pRoundNumber) {
        super(Type.ROUND_UPDATE);
        mRoundNumber = pRoundNumber;
    }

    public int getRoundNumber() {
        return mRoundNumber;
    }
}
