package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.network.message.Message;

public class NewPlayerRequest extends Message {
    private String mName;

    public NewPlayerRequest(String pName) {
        super(Type.PLAYER_ADD_REQUEST);
        mName = pName;
    }

    public String getName() {
        return mName;
    }
}
