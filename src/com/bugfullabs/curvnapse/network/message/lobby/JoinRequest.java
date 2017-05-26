package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Client likes the description of the Game and wants to join it
 */
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
