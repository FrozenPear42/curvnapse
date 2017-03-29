package com.bugfullabs.curvnapse.game;

import com.bugfullabs.curvnapse.player.Player;
import com.bugfullabs.curvnapse.player.PlayerColor;
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
    private ArrayList<Integer> mTest;
    private transient ColorBank mColorBank;
    private int mBoardWidth;
    private int mBoardHeight;

    public Game(String pName, int pHostID, int pMaxPlayers) {
        mID = UID;
        UID += 1;
        mName = pName;
        mHostID = pHostID;
        mMaxPlayers = pMaxPlayers;
        mPlayers = new ArrayList<>(mMaxPlayers);
        mTest = new ArrayList<>(10);
        mColorBank = new ColorBank();
        mBoardWidth = 500;
        mBoardHeight = 500;
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

    public ArrayList<Integer> getTest() {
        return mTest;
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

    public Player addPlayer(String pName, int pOwner) {
        mTest.add(12);
        if (mPlayers.size() < mMaxPlayers) {
            PlayerColor color = mColorBank.nextColor();
            Player player = new Player(pName, color, pOwner);
            mPlayers.add(player);
            return player;
        }
        return null;
    }
}
