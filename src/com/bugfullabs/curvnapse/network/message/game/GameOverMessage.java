package com.bugfullabs.curvnapse.network.message.game;

import com.bugfullabs.curvnapse.network.message.Message;

public class GameOverMessage extends Message {
    public GameOverMessage() {
        super(Type.GAMEOVER);
    }
}
