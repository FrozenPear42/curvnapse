package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.game.Player;
import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Want to change player settings
 */
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
