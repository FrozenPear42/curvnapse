package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Success, Client has successfully joined the Game, let him know!
 */
public class JoinMessage extends Message {
    private Game mGame;

    public JoinMessage(Game pGame) {
        super(Type.GAME_JOIN);
        mGame = pGame;
    }

    public Game getGame() {
        return mGame;
    }
}
