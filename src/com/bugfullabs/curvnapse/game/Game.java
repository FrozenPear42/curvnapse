package com.bugfullabs.curvnapse.game;

import com.bugfullabs.curvnapse.player.Player;
import com.bugfullabs.curvnapse.player.PlayerColor;
import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.utils.ColorBank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable, Cloneable {
    private static int UID = 0;

    private int mID;
    private String mName;
    private int mMaxPlayers;
    private int mHostID;
    private ArrayList<Player> mPlayers;
    private transient ColorBank mColorBank;

    private boolean[] mPowerUps;
    private int mBoardWidth;
    private int mBoardHeight;

    public Game(String pName, int pHostID, int pMaxPlayers) {
        mID = UID;
        UID += 1;
        mName = pName;
        mHostID = pHostID;
        mMaxPlayers = pMaxPlayers;
        mPlayers = new ArrayList<>(mMaxPlayers);
        mColorBank = new ColorBank();

        mBoardWidth = 500;
        mBoardHeight = 500;
        mPowerUps = new boolean[PowerUp.PowerType.values().length];
        for (int i = 0; i < mPowerUps.length; i++)
            mPowerUps[i] = true;
    }

    public int getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public int getHostID() {
        return mHostID;
    }

    public void setName(String pName) {
        mName = pName;
    }

    public int getBoardWidth() {
        return mBoardWidth;
    }

    public int getBoardHeight() {
        return mBoardHeight;
    }

    public int getMaxPlayers() {
        return mMaxPlayers;
    }

    public boolean[] getPowerUps() {
        return mPowerUps;
    }

    public Player addPlayer(String pName, int pOwner) {
        if (mPlayers.size() < mMaxPlayers) {
            PlayerColor color = mColorBank.nextColor();
            Player player = new Player(pName, color, pOwner);
            mPlayers.add(player);
            return player;
        }
        return null;
    }

    public void setPowerUpEnabled(PowerUp.PowerType pType, boolean pEnabled) {
        mPowerUps[pType.ordinal()] = pEnabled;
    }
}
