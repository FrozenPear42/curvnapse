package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.network.message.Message;

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
