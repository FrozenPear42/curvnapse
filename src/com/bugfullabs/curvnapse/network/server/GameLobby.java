package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.player.Player;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class GameLobby implements ClientThread.ClientListener {
    private static Logger LOG = Logger.getLogger(GameLobby.class.getName());
    private LinkedList<ClientThread> mClients;
    private final Game mGame;
    private GameThread mThread;
    private GameUpdateListener mListener;

    private Lobby mRootLobby;

    public GameLobby(Lobby pLobby, String pName, int pHost, int pMaxPlayers) {
        mRootLobby = pLobby;
        mClients = new LinkedList<>();
        mGame = new Game(pName, pHost, pMaxPlayers);
    }

    public void addClient(ClientThread pClient) {
        mClients.add(pClient);
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

    private void addPlayer(String pName, int pID) {
        Player p = mGame.addPlayer(pName, pID);
        if (p != null)
            propagateUpdate();
    }

    private void updatePlayer(Player pPlayer) {
        mGame.getPlayers().replaceAll(p -> p.getID() == pPlayer.getID() ? pPlayer : p);
        propagateUpdate();
    }

    private void leaveGame(ClientThread pClient) {
        mGame.getPlayers().removeIf(player -> player.getOwner() == pClient.getID());
        mClients.remove(pClient);
        pClient.removeListener(this);
        if (mGame.getHostID() == pClient.getID()) {
            if (mClients.isEmpty()) {
                //TODO: Remove whole game
            } else {
                mGame.setHostID(mClients.getFirst().getID());
            }
        }
        mRootLobby.addClient(pClient);
        propagateUpdate();
    }

    private void propagateUpdate() {
        mClients.forEach(client -> client.sendMessage(new GameUpdateMessage(mGame)));
        if (mListener != null) {
            mListener.onGameUpdate(mGame);
        }
    }

    private void broadcast(String pMessage) {
        mClients.forEach(client -> client.sendMessage(new ServerTextMessage(pMessage)));
    }

    @Override
    public void onClientMessage(ClientThread pClientThread, Message pMessage) {
        switch (pMessage.getType()) {
            case TEXT:
                mClients.forEach(clientThread -> clientThread.sendMessage(pMessage));
                break;
            case UPDATE_REQUEST:
                pClientThread.sendMessage(new GameUpdateMessage(mGame));
                break;
            case NAME_UPDATE_REQUEST:
                mGame.setName(((GameUpdateNameRequest) pMessage).getName());
                broadcast("Game name changed to: " + mGame.getName());
                propagateUpdate();
                break;

            case POWERUP_UPDATE:
                GamePowerUpUpdateRequest req = (GamePowerUpUpdateRequest) pMessage;
                mGame.setPowerUpEnabled(req.getPowerType(), req.getState());
                propagateUpdate();
                break;

            case PLAYER_ADD_REQUEST:
                addPlayer(((NewPlayerRequest) pMessage).getName(), pClientThread.getID());
                break;
            case PLAYER_UPDATE_REQUEST:
                updatePlayer(((PlayerUpdateRequest) pMessage).getPlayer());
                break;

            case LEAVE_GAME:
                leaveGame(pClientThread);
                break;

            case GAME_START_REQUEST:
                if (mGame.getPlayers().isEmpty())
                    break;

                //FIXME: Move timer as member, allow cancel
                new Timer("asd").schedule(new TimerTask() {
                    private int times = 1;

                    @Override
                    public void run() {
                        mClients.forEach(clientThread -> clientThread.sendMessage(new TextMessage("Server", String.format("Game will start in %d...", times))));
                        if (times == 0) {
                            this.cancel();
                            mClients.forEach(clientThread -> clientThread.sendMessage(new GameStartMessage(mGame)));
                            mThread = new GameThread(mGame, mClients);
                            mClients.forEach(client -> client.registerListener(mThread));
                        }
                        times--;
                    }
                }, 0, 1000);
                break;
        }
    }

    public interface GameUpdateListener {
        void onGameUpdate(Game pGame);
    }

}
