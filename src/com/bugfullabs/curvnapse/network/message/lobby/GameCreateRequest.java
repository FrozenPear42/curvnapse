package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.network.message.Message;

public class GameCreateRequest extends Message {
    private int mHost;
    private String mName;
    private String mPassword;
    private int mMaxPlayers;

    public GameCreateRequest(int pHost, String pName, String pPassword, int pMaxPlayers) {
        super(Type.GAME_CREATE);
        mName = pName;
        mPassword = pPassword;
        mMaxPlayers = pMaxPlayers;
        mHost = pHost;
    }

    public int getHost() {
        return mHost;
    }

    public String getName() {
        return mName;
    }

    public String getPassword() {
        return mPassword;
    }

    public int getMaxPlayers() {
        return mMaxPlayers;
    }
}
