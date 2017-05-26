package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.game.Player;
import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Hate tthat player, wanna remove it from the game
 */
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
