package com.bugfullabs.curvnapse.network.message;

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
