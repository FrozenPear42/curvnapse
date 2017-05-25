package com.bugfullabs.curvnapse.network.message;


import com.bugfullabs.curvnapse.game.Game;

public class GameUpdateMessage extends Message {
    private Game mGame;

    public GameUpdateMessage(Game pGame) {
        super(Type.GAME_UPDATE);
        mGame = pGame;
    }

    public Game getGame() {
        return mGame;
    }
}
