package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.network.message.control.ServerTextMessage;
import com.bugfullabs.curvnapse.network.message.game.GameRemovedMessage;
import com.bugfullabs.curvnapse.network.message.lobby.GameCreateRequest;
import com.bugfullabs.curvnapse.network.message.lobby.JoinMessage;
import com.bugfullabs.curvnapse.network.message.lobby.JoinRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Class representong main lobby - place with public chat, game list, and game sub-lobbies
 */
public class Lobby implements ClientThread.ClientMessageListener {
    private static final Logger LOG = Logger.getLogger(Lobby.class.getName());

    private LinkedList<ClientThread> mClients;
    private ArrayList<GameLobby> mGameLobbies;

    private int mMaxGames;

    private Server mServer;

    /**
     * Create new lobby within a server
     *
     * @param pServer   server
     * @param pMaxGames maximum number of games
     */
    public Lobby(Server pServer, int pMaxGames) {
        mServer = pServer;
        mMaxGames = pMaxGames;
        mClients = new LinkedList<>();
        mGameLobbies = new ArrayList<>(mMaxGames);
    }

    /**
     * Add new client to lobby
     *
     * @param pClientThread {@link ClientThread} of client
     */
    public void addClient(ClientThread pClientThread) {
        LOG.info(String.format("Client %d %s has joined", pClientThread.getID(), pClientThread.getUsername()));
        mClients.forEach(client -> client.sendMessage(new ServerTextMessage(pClientThread.getUsername() + " has joined")));
        mClients.add(pClientThread);
        pClientThread.registerListener(this);
    }

    /**
     * Try to create new game
     *
     * @param pRequest {@link GameCreateRequest} with params of game to be created
     * @return GameLobby on success, null if failed
     */
    private GameLobby createGame(GameCreateRequest pRequest) {
        if (mGameLobbies.size() < mMaxGames) {
            GameLobby lobby = new GameLobby(this, pRequest.getName(), pRequest.getHost(), pRequest.getMaxPlayers());
            mGameLobbies.add(lobby);
            mClients.forEach(client -> client.sendMessage(new GameUpdateMessage(lobby.getGame())));
            lobby.setGameLobbyChangeListener(new GameLobby.GameLobbyChangeListener() {
                @Override
                public void onGameUpdate(Game pGame) {
                    mClients.forEach(client -> client.sendMessage(new GameUpdateMessage(pGame)));
                }

                @Override
                public void onLobbyEmpty() {
                    mGameLobbies.remove(lobby);
                    mClients.forEach(client -> client.sendMessage(new GameRemovedMessage(lobby.getGame().getID())));
                }
            });
            return lobby;
        }
        return null;
    }

    /**
     * Client message Listener
     *
     * @param pClient  source
     * @param pMessage message
     */
    @Override
    public synchronized void onClientMessage(ClientThread pClient, Message pMessage) {
        switch (pMessage.getType()) {
            case TEXT:
                mClients.forEach(client -> client.sendMessage(pMessage));
                break;

            case UPDATE_REQUEST:
                mGameLobbies.stream()
                        .filter(lobby -> lobby.clientCount() > 0)
                        .forEach(gameLobby -> pClient.sendMessage(new GameUpdateMessage(gameLobby.getGame())));
                break;

            case GAME_CREATE:
                GameLobby game = createGame((GameCreateRequest) pMessage);
                if (game != null) {
                    game.addClient(pClient);
                    pClient.sendMessage(new JoinMessage(game.getGame()));

                    mClients.remove(pClient);
                    pClient.removeListener(this);
                }
                break;

            case GAME_JOIN_REQUEST:
                JoinRequest msg = (JoinRequest) pMessage;
                LOG.info("Join request");
                mGameLobbies.stream()
                        .filter(gameLobby -> msg.getID() == gameLobby.getID())
                        .findFirst()
                        .ifPresent(lobby -> {
                            lobby.addClient(pClient);
                            pClient.sendMessage(new JoinMessage(lobby.getGame()));

                            mClients.remove(pClient);
                            pClient.removeListener(this);
                        });
                break;
            default:
                break;
        }

    }


}
