package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.client.Game;
import com.bugfullabs.curvnapse.network.message.*;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

public class Lobby implements ClientThread.ClientListener {
    private static final Logger LOG = Logger.getLogger(Lobby.class.getName());
    private LinkedList<ClientThread> mClients;
    private ArrayList<GameLobby> mGameLobbies;

    private int mMaxGames;


    public Lobby(int pMaxGames) {
        mMaxGames = pMaxGames;
        mClients = new LinkedList<>();
        mGameLobbies = new ArrayList<>(mMaxGames);
    }

    public void addClient(ClientThread pClientThread, String pName) {
        for (ClientThread client : mClients)
            client.sendMessage(new HandshakeMessage(pName));

        mClients.add(pClientThread);
        pClientThread.registerListener(this);
    }

    @Override
    public void onClientMessage(ClientThread pClientThread, Message pMessage) {
        switch (pMessage.getType()) {
            case TEXT:
                TextMessage textMessage = (TextMessage) pMessage;
                LOG.info(textMessage.getAuthor() + "  " + textMessage.getMessage());
                for (ClientThread client : mClients)
                    client.sendMessage(textMessage);
                break;

            case GAME_CREATE:
                GameCreateRequestMessage gameCreateRequest = (GameCreateRequestMessage) pMessage;
                if (mGameLobbies.size() < mMaxGames) {
                    LOG.info("Game created"); //TODO: more detailed log
                    GameLobby lobby = new GameLobby(gameCreateRequest.getName(), gameCreateRequest.getMaxPlayers());
                    mGameLobbies.add(lobby);
                    lobby.addClient(pClientThread);
                    mClients.remove(pClientThread);
                    pClientThread.sendMessage(new JoinMessage(lobby.getID()));
                    for (ClientThread client : mClients)
                        client.sendMessage(new GameUpdateMessage(lobby.getGameDescriptor()));
                }
                break;

            case GAME_JOIN_REQUEST:
                JoinRequestMessage msg = (JoinRequestMessage) pMessage;
                LOG.info("Join request");
                //pClientThread.sendMessage(new JoinMessage());
                break;
            default:
                LOG.warning("Unsupported message");
        }

    }
}
