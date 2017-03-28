package com.bugfullabs.curvnapse.network.message;

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
