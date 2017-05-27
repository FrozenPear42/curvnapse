package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Everyone is ready so Lets go! Begin the game now!
 */
public class GameStartRequest extends Message {
    public GameStartRequest() {
        super(Type.GAME_START_REQUEST);
    }
}
