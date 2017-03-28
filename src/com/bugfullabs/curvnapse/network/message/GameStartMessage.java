package com.bugfullabs.curvnapse.network.message;

import com.bugfullabs.curvnapse.game.Game;

public class GameStartMessage extends Message {
    Game mGame;

    public GameStartMessage(Game pGame) {
        super(Type.GAME_START);
        mGame = pGame;
    }
    
    public Game getGame() {
        return mGame;
    }
}
