package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Client decides he/she hates all the players in GameLobby and wants to leave it ASAP!
 */
public class LeaveGameLobbyRequest extends Message {
    public LeaveGameLobbyRequest() {
        super(Type.LEAVE_GAME);
    }
}
