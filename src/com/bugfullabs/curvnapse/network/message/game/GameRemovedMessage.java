package com.bugfullabs.curvnapse.network.message.game;

import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Game was removed, remove it from the list
 */
public class GameRemovedMessage extends Message {
    private final int mID;

    public GameRemovedMessage(int pID) {
        super(Type.GAME_REMOVED);
        mID = pID;
    }

    public int getID() {
        return mID;
    }
}
