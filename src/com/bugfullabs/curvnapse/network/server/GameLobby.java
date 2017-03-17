package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.client.Game;
import com.bugfullabs.curvnapse.network.message.Message;

import java.util.LinkedList;

public class GameLobby implements ClientThread.ClientListener {
    private static int UID = 0;
    private LinkedList<ClientThread> mClientThreads;
    private int mUID;
    private String mName;
    private int mPlayers;
    private int mMaxPlayers;

    public GameLobby(String pName, int pMaxPlayers) {
        mUID = UID;
        UID += 1;
        mName = pName;
        mMaxPlayers = pMaxPlayers;
        mPlayers = 0;
        mClientThreads = new LinkedList<>();
    }

    public void addClient(ClientThread pClient) {
        mClientThreads.add(pClient);
        pClient.registerListener(this);
    }

    public int getID() {
        return mUID;
    }

    public Game getGameDescriptor() {
        return new Game(mName, mMaxPlayers);
    }

    @Override
    public void onClientMessage(ClientThread pClientThread, Message pMessage) {
    }

}
