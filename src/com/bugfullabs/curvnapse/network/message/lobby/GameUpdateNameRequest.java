package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.network.message.Message;

/**
 * You know, game name kinda sucks, lats try something better.
 */
public class GameUpdateNameRequest extends Message {
    private String mName;

    public GameUpdateNameRequest(String pName) {
        super(Type.NAME_UPDATE_REQUEST);
        mName = pName;
    }

    public String getName() {
        return mName;
    }
}
