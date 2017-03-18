package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.client.Game;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.NewPlayerMessage;
import com.bugfullabs.curvnapse.network.message.NewPlayerRequestMessage;
import com.bugfullabs.curvnapse.player.Player;
import com.bugfullabs.curvnapse.player.PlayerColor;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

public class GameLobby implements ClientThread.ClientListener {
    private static Logger LOG = Logger.getLogger(GameLobby.class.getName());
    private static int UID = 0;
    private LinkedList<ClientThread> mClientThreads;
    private HashMap<Integer, Player> mPlayers;
    private int mUID;
    private String mName;
    private int mPlayersCount;
    private int mPlayersMax;

    public GameLobby(String pName, int pMaxPlayers) {
        mUID = UID;
        UID += 1;
        mName = pName;
        mPlayersMax = pMaxPlayers;
        mPlayersCount = 0;
        mClientThreads = new LinkedList<>();
        mPlayers = new HashMap<>(mPlayersMax);
    }

    public void addClient(ClientThread pClient) {
        mClientThreads.add(pClient);
        pClient.registerListener(this);
    }

    public int getID() {
        return mUID;
    }

    public Game getGameDescriptor() {
        return new Game(mName, mPlayersMax);
    }

    @Override
    public void onClientMessage(ClientThread pClientThread, Message pMessage) {
        switch (pMessage.getType()) {
            case TEXT:
                mClientThreads.forEach(clientThread -> clientThread.sendMessage(pMessage));
                break;
            case PLAYER_ADD_REQUEST:
                if (mPlayersCount < mPlayersMax) {
                    Player player = new Player(((NewPlayerRequestMessage) pMessage).getName(), new PlayerColor(Color.RED), true);
                    mPlayers.put(player.getID(), player);
                    mPlayersCount += 1;
                    mClientThreads.forEach(clientThread -> clientThread.sendMessage(new NewPlayerMessage(player)));
                }
                break;
        }
    }

}
