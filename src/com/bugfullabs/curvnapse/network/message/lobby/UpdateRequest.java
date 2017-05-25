package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.network.message.Message;

public class UpdateRequest extends Message {
    public UpdateRequest() {
        super(Type.UPDATE_REQUEST);
    }
}
