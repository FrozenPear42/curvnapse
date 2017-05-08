package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.player.Player;

public class PlayerDeleteRequest extends Message {
    private Player mPlayer;

    public PlayerDeleteRequest(Player pPlayer) {
        super(Type.PLAYER_DELETE);
        mPlayer = pPlayer;
    }

    public Player getPlayer() {
        return mPlayer;
    }
}
