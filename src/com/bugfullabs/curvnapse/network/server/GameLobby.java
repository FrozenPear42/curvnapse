package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.network.client.Game;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.player.Player;

import java.util.*;
import java.util.logging.Logger;

public class GameLobby implements ClientThread.ClientListener {
    private static Logger LOG = Logger.getLogger(GameLobby.class.getName());
    private LinkedList<ClientThread> mClientThreads;
    private Game mGame;

    private GameUpdateListener mListener;

    public GameLobby(String pName, int pHost, int pMaxPlayers) {
        mClientThreads = new LinkedList<>();
        mGame = new Game(pName, pHost, pMaxPlayers);
    }

    public void addClient(ClientThread pClient) {
        mClientThreads.add(pClient);
        pClient.registerListener(this);
        mGame.getPlayers().forEach(player -> pClient.sendMessage(new GameUpdateMessage(mGame)));
    }

    public int getID() {
        return mGame.getID();
    }

    public Game getGame() {
        return mGame;
    }

    public void setListener(GameUpdateListener pListener) {
        mListener = pListener;
    }

    @Override
    public void onClientMessage(ClientThread pClientThread, Message pMessage) {
        switch (pMessage.getType()) {
            case TEXT:
                mClientThreads.forEach(clientThread -> clientThread.sendMessage(pMessage));
                break;
            case UPDATE_REQUEST:
                pClientThread.sendMessage(new GameUpdateMessage(mGame));
                break;
            case PLAYER_ADD_REQUEST:
                Player p = mGame.addPlayer(((NewPlayerRequest) pMessage).getName(), pClientThread.getID());
                if (p != null) {
                    mClientThreads.forEach(client -> client.sendMessage(new GameUpdateMessage(mGame)));
                    mClientThreads.forEach(client -> client.sendMessage(new TextMessage("NOWY", Integer.toString(mGame.getPlayers().size()))));
                }
                if (mListener != null) {
                    mListener.onGameUpdate(mGame);
                    mClientThreads.forEach(client -> client.sendMessage(new TextMessage("LISTE", Integer.toString(mGame.getPlayers().size()))));
                }
                mClientThreads.forEach(client -> client.sendMessage(new TextMessage("AAA", Integer.toString(mGame.getPlayers().size()))));
                break;
            case GAME_START_REQUEST:
                //FIXME: Move timer as member, allow cancel
                new Timer("asd").schedule(new TimerTask() {
                    private int times = 1;

                    @Override
                    public void run() {
                        mClientThreads.forEach(clientThread -> clientThread.sendMessage(new TextMessage("Server", String.format("Game will start in %d...", times))));
                        if (times == 0) {
                            this.cancel();
                            mClientThreads.forEach(clientThread -> clientThread.sendMessage(new GameStartMessage(mGame)));
                        }
                        times--;
                    }
                }, 0, 1000);
                break;

            case CONTROL_UPDATE:
                LOG.info("Control");
                break;
        }
    }

    public interface GameUpdateListener {
        void onGameUpdate(Game pGame);
    }

}
