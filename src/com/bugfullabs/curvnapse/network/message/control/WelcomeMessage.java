package com.bugfullabs.curvnapse.network.message.control;

import com.bugfullabs.curvnapse.network.message.Message;

/**
 * sserver accepts client and gives it unique client ID
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
