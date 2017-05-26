package com.bugfullabs.curvnapse.network.message.game;

import com.bugfullabs.curvnapse.network.message.Message;

/**
 * That was GoodGame (GG), but everything has to come to the end...
 */
public class GameOverMessage extends Message {
    public GameOverMessage() {
        super(Type.GAMEOVER);
    }
}
