package com.bugfullabs.curvnapse.network.message;

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
