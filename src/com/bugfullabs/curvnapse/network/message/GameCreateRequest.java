package com.bugfullabs.curvnapse.network.message;

public class GameCreateRequest extends Message {
    private String mName;
    private String mPassword;
    private int mMaxPlayers;

    public GameCreateRequest(String pName, String pPassword, int pMaxPlayers) {
        super(Type.GAME_CREATE);
        mName = pName;
        mPassword = pPassword;
        mMaxPlayers = pMaxPlayers;
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
