package com.bugfullabs.curvnapse.network.client;

import java.io.Serializable;

public class Game implements Serializable {
    private int mID;
    private String mName;
    private int mPlayers;
    private int mMaxPlayers;

    public Game(String pName, int pMaxPlayers) {
        mName = pName;
        mMaxPlayers = pMaxPlayers;
    }

    public int getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public int getPlayers() {
        return mPlayers;
    }

    public int getMaxPlayers() {
        return mMaxPlayers;
    }
}
