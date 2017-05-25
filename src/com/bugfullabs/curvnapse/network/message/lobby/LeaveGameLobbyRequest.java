package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.network.message.Message;

public class LeaveGameLobbyRequest extends Message {
    public LeaveGameLobbyRequest() {
        super(Type.LEAVE_GAME);
    }
}
