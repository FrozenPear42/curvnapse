package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.game.Player;
import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Want new color for my player
 */
public class PlayerColorRotationRequest extends Message {
    private Player mPlayer;

    public PlayerColorRotationRequest(Player pPlayer) {
        super(Type.PLAYER_COLOR_REQUEST);
        mPlayer = pPlayer;
    }

    public Player getPlayer() {
        return mPlayer;
    }
}
