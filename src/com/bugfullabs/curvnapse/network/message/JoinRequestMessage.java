package com.bugfullabs.curvnapse.network.message;

public class JoinRequestMessage extends Message {
    private int mID;

    public JoinRequestMessage(int pID) {
        super(Type.GAME_JOIN_REQUEST);
        mID = pID;
    }

    public int getID() {
        return mID;
    }
}
