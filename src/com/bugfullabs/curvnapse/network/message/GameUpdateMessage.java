package com.bugfullabs.curvnapse.network.message;


import com.bugfullabs.curvnapse.network.client.Game;

public class GameUpdateMessage extends Message {
    private static int UID = 0;
    private int mUID;
    private Game mGame;

    public GameUpdateMessage(Game pGame) {
        super(Type.GAME_UPDATE);
        mGame = pGame;
        mUID = UID;
        UID += 1;
    }

    public Game getGame() {
        return mGame;
    }
}
