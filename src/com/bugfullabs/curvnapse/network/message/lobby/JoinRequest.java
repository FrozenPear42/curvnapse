package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.network.message.Message;

public class JoinRequest extends Message {
    private int mID;

    public JoinRequest(int pID) {
        super(Type.GAME_JOIN_REQUEST);
        mID = pID;
    }

    public int getID() {
        return mID;
    }
}
