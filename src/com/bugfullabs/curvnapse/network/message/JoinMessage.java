package com.bugfullabs.curvnapse.network.message;

public class JoinMessage extends Message {
    private int mID;

    public JoinMessage(int pID) {
        super(Type.GAME_JOIN);
        mID = pID;
    }

    public int getID() {
        return mID;
    }
}
