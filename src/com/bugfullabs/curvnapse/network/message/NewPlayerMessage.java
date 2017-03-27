package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.player.Player;

/** @deprecated fuj, bez sensu*/
public class NewPlayerMessage extends Message {
    private Player mPlayer;

    public NewPlayerMessage(Player pPlayer) {
        super(Type.PLAYER_ADD);
        mPlayer = pPlayer;
    }

    public Player getPlayer() {
        return mPlayer;
    }
}
