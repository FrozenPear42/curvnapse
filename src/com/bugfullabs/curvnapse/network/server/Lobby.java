package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.client.Game;
import com.bugfullabs.curvnapse.network.message.*;
import javafx.application.Platform;
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

    public void addClient(ClientThread pClientThread) {
        for (ClientThread client : mClients)
            client.sendMessage(new HandshakeMessage(pClientThread.getName()));

        mClients.add(pClientThread);
        pClientThread.registerListener(this);
        mGameLobbies.forEach(gameLobby -> pClientThread.sendMessage(new GameUpdateMessage(gameLobby.getGameDescriptor())));
    }

    public void removeClient(ClientThread pClientThread) {
        //TODO: implement
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
                    lobby.setListener(game -> mClients.forEach(client -> client.sendMessage(new GameUpdateMessage(game))));
                    mGameLobbies.add(lobby);
                    lobby.addClient(pClientThread);
                    mClients.remove(pClientThread);
                    pClientThread.removeListener(this);
                    pClientThread.sendMessage(new JoinMessage(lobby.getGameDescriptor()));
                    for (ClientThread client : mClients)
                        client.sendMessage(new GameUpdateMessage(lobby.getGameDescriptor()));
                }
                break;

            case GAME_JOIN_REQUEST:
                JoinRequestMessage msg = (JoinRequestMessage) pMessage;
                LOG.info("Join request");
                GameLobby lobby =
                        mGameLobbies.stream()
                                .filter(gameLobby -> msg.getID() == gameLobby.getID())
                                .findFirst()
                                .get();
                lobby.addClient(pClientThread);
                mClients.remove(pClientThread);
                pClientThread.removeListener(this);
                pClientThread.sendMessage(new JoinMessage(lobby.getGameDescriptor()));
                break;
            default:
                LOG.warning("Unsupported message");
        }

    }
}
