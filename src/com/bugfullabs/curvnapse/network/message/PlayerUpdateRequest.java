package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.player.Player;

public class PlayerUpdateRequest extends Message {
    private Player mPlayer;

    public PlayerUpdateRequest(Player pPlayer) {
        super(Type.PLAYER_UPDATE_REQUEST);
        mPlayer = pPlayer;
    }

    public Player getPlayer() {
        return mPlayer;
    }
}
