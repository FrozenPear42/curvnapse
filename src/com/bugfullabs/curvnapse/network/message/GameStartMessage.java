package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.player.Player;

import java.util.List;

public class GameStartMessage extends Message {
    private List<Player> mPlayers;

    public GameStartMessage(List<Player> pList) {
        super(Type.GAME_START);
        mPlayers = pList;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }
}
