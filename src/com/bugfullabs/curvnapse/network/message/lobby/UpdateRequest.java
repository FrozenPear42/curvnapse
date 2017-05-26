package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Want to know how is game descriptor doing
 */
public class UpdateRequest extends Message {
    public UpdateRequest() {
        super(Type.UPDATE_REQUEST);
    }
}
