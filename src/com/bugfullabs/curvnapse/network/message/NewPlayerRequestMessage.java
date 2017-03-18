package com.bugfullabs.curvnapse.network.message;

public class NewPlayerRequestMessage extends Message {
    private String mName;

    public NewPlayerRequestMessage(String pName) {
        super(Type.PLAYER_ADD_REQUEST);
        mName = pName;
    }

    public String getName() {
        return mName;
    }
}
