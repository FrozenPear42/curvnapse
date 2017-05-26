package com.bugfullabs.curvnapse.network.message.lobby;

import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.network.message.Message;

/**
 * Yeah, Server has successfully processed Game Start Request, lets go, here grab this new Game descriptor to up with
 * all the changes!
 */
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
