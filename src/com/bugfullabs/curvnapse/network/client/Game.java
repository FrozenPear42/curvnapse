package com.bugfullabs.curvnapse.network.client;

import com.bugfullabs.curvnapse.network.message.NewPlayerMessage;
import com.bugfullabs.curvnapse.network.message.NewPlayerRequestMessage;
import com.bugfullabs.curvnapse.player.Player;
import com.bugfullabs.curvnapse.player.PlayerColor;
import com.bugfullabs.curvnapse.utils.ColorBank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {
    private static int UID = 0;

    private int mID;
    private String mName;
    private int mMaxPlayers;
    private List<Player> mPlayers;
    private ColorBank mColorBank;

    public Game(String pName, int pMaxPlayers) {
        mID = UID;
        UID += 1;
        mName = pName;
        mMaxPlayers = pMaxPlayers;
        mPlayers = new ArrayList<>(mMaxPlayers);
        mColorBank = new ColorBank();
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

    public int getMaxPlayers() {
        return mMaxPlayers;
    }

    public Player addPlayer(String pName, int pOwner) {
        if (mPlayers.size() < mMaxPlayers) {
            PlayerColor color = mColorBank.nextColor();
            Player player = new Player(pName, color, true);
            mPlayers.add(player);
            return player;
        }
        return null;
    }
}
