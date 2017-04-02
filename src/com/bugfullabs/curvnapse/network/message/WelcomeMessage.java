package com.bugfullabs.curvnapse.network.message;

public class WelcomeMessage extends Message {

    private int mUserID;

    public WelcomeMessage(int pUserID) {
        super(Type.WELCOME);
        mUserID = pUserID;
    }

    public int getUserID() {
        return mUserID;
    }
}
