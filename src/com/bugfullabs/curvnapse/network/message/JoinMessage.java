package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.game.Game;

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
