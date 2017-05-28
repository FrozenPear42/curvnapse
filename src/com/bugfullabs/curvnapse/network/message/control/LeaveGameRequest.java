package com.bugfullabs.curvnapse.network.message.control;

import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Client decides he/she hates all the players in GameLobby and wants to leave it ASAP!
 */
public class LeaveGameRequest extends Message {
    public LeaveGameRequest() {
        super(Type.LEAVE_GAME);
    }
}
