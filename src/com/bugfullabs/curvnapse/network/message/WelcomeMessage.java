package com.bugfullabs.curvnapse.network.message;

/**
 * Created by wojciech on 28.03.17.
 */
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
