package com.bugfullabs.curvnapse.network.server;

import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.game.Player;
import com.bugfullabs.curvnapse.network.message.GameUpdateMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.control.ServerTextMessage;
import com.bugfullabs.curvnapse.network.message.control.TextMessage;
import com.bugfullabs.curvnapse.network.message.lobby.*;
import com.bugfullabs.curvnapse.utils.SerializableColor;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class for specific GameLobby
 */
public class GameLobby implements ClientThread.ClientMessageListener {
    private LinkedList<ClientThread> mClients;
    private final Game mGame;
    private GameThread mThread;
    private GameLobbyChangeListener mListener;

    private boolean mGameIsRunning;

    private Lobby mRootLobby;

    /**
     * Create new lobby
     *
     * @param pLobby      Main (root) lobby
     * @param pName       Game name
     * @param pHost       Host clientID
     * @param pMaxPlayers max players in game
     */
    public GameLobby(Lobby pLobby, String pName, int pHost, int pMaxPlayers) {
        mRootLobby = pLobby;
        mClients = new LinkedList<>();
        mGame = new Game(pName, pHost, 10, pMaxPlayers);
        mGameIsRunning = false;
    }

    /**
     * Add given client to lobby
     *
     * @param pClient client to be added
     */
    public void addClient(ClientThread pClient) {
        mClients.add(pClient);
        pClient.registerListener(this);
        mGame.getPlayers().forEach(player -> pClient.sendMessage(new GameUpdateMessage(mGame)));
    }

    public int getID() {
        return mGame.getID();
    }

    /**
     * Get game
     *
     * @return Game object
     */
    public Game getGame() {
        return mGame;
    }

    /**
     * Set listener
     *
     * @param pListener listener
     */
    public void setGameLobbyChangeListener(GameLobbyChangeListener pListener) {
        mListener = pListener;
    }

    /**
     * Add new player to Game
     *
     * @param pName player name
     * @param pID   owner clientID
     */
    private void addPlayer(String pName, int pID) {
        Player p = mGame.addPlayer(pName, pID);
        if (p != null) {
            p.setName(pName + p.getID());
            propagateUpdate();
        }
    }

    /**
     * Update given player (replace player with the same ID with the given one)
     *
     * @param pPlayer new version of Player
     */
    private void updatePlayer(Player pPlayer) {
        mGame.getPlayers().replaceAll(p -> p.getID() == pPlayer.getID() ? pPlayer : p);
        propagateUpdate();
    }

    /**
     * Delete player from the lobby
     *
     * @param pPlayer player to delete
     */
    private void deletePlayer(Player pPlayer) {
        mGame.getColorBank().returnColor(pPlayer.getColor());
        mGame.getPlayers().removeIf(p -> p.getID() == pPlayer.getID());
        propagateUpdate();
    }

    /**
     * Remove Client from the Lobby
     *
     * @param pClient client to be removed
     */
    private void leaveGame(ClientThread pClient, boolean pReturnToLobby) {
        mGame.getPlayers().removeIf(player -> player.getOwner() == pClient.getID());
        mClients.remove(pClient);
        pClient.removeListener(this);

        if (mThread != null)
            pClient.removeListener(mThread);

        if (mGame.getHostID() == pClient.getID()) {
            if (mClients.isEmpty()) {
                if (mListener != null)
                    mListener.onLobbyEmpty();
            } else {
                mGame.setHostID(mClients.getFirst().getID());
            }
        }
        if (pReturnToLobby)
            mRootLobby.addClient(pClient);
        if (!mGameIsRunning && !mClients.isEmpty())
            propagateUpdate();
    }

    /**
     * Send upadte message to all clients, and notify main lobby to update game list
     */
    private void propagateUpdate() {
        mClients.forEach(client -> client.sendMessage(new GameUpdateMessage(mGame)));
        if (mListener != null) {
            mListener.onGameUpdate(mGame);
        }
    }

    /**
     * Send server text message to all the clients
     *
     * @param pMessage text to be sent
     */
    private void broadcast(String pMessage) {
        mClients.forEach(client -> client.sendMessage(new ServerTextMessage(pMessage)));
    }


    /**
     * Listener on client messages
     *
     * @param pClient  source client
     * @param pMessage message
     */
    @Override
    public synchronized void onClientMessage(ClientThread pClient, Message pMessage) {
        switch (pMessage.getType()) {
            case TEXT:
                mClients.forEach(clientThread -> clientThread.sendMessage(pMessage));
                break;
            case UPDATE_REQUEST:
                pClient.sendMessage(new GameUpdateMessage(mGame));
                break;
            case NAME_UPDATE_REQUEST:
                mGame.setName(((GameUpdateNameRequest) pMessage).getName());
                broadcast("Game name changed to: " + mGame.getName());
                propagateUpdate();
                break;

            case POWERUP_UPDATE:
                GamePowerUpStateRequest req = (GamePowerUpStateRequest) pMessage;
                mGame.setPowerUpEnabled(req.getPowerType(), req.getState());
                propagateUpdate();
                break;

            case PLAYER_ADD_REQUEST:
                addPlayer(((NewPlayerRequest) pMessage).getName(), pClient.getID());
                break;
            case PLAYER_UPDATE_REQUEST:
                updatePlayer(((PlayerUpdateRequest) pMessage).getPlayer());
                break;

            case PLAYER_DELETE:
                deletePlayer(((PlayerDeleteRequest) pMessage).getPlayer());
                break;

            case PLAYER_COLOR_REQUEST:
                updatePlayerColor(((PlayerColorRotationRequest) pMessage).getPlayer());
                break;

            case DISCONNECT:
                leaveGame(pClient, false);
                break;
            case LEAVE_GAME:
                leaveGame(pClient, true);
                break;

            case GAME_START_REQUEST:
                if (mGame.getPlayers().isEmpty())
                    break;
                if (mGameIsRunning)
                    break;

                mGameIsRunning = true;

                new Timer().schedule(new TimerTask() {
                    private int times = 1;

                    @Override
                    public void run() {
                        mClients.forEach(clientThread -> clientThread.sendMessage(new TextMessage("Server", String.format("Game will start in %d...", times))));
                        if (times == 0) {
                            this.cancel();
                            mGame.getPlayers().forEach(player -> player.setPoints(0));
                            mClients.forEach(clientThread -> clientThread.sendMessage(new GameStartMessage(mGame)));
                            mThread = new GameThread(mGame, mClients, () -> mGameIsRunning = false);
                            mClients.forEach(client -> client.registerListener(mThread));
                        }
                        times--;
                    }
                }, 0, 1000);
                break;
        }
    }

    /**
     * Try to give player new Color
     *
     * @param pPlayer Player to change color
     */
    private void updatePlayerColor(Player pPlayer) {
        SerializableColor color = mGame.getColorBank().nextColor();
        if (color == null)
            return;

        mGame.getColorBank().returnColor(pPlayer.getColor());
        mGame.getPlayers().stream()
                .filter(p -> p.getID() == pPlayer.getID())
                .findFirst()
                .ifPresent(p -> p.setColor(color));
        propagateUpdate();
    }

    /**
     * Returns number of clients in game
     *
     * @return number of clients
     */
    public int clientCount() {
        return mClients.size();
    }

    /**
     * Check if game is running
     *
     * @return is game running?
     */
    public boolean isRunning() {
        return mGameIsRunning;
    }

    /**
     * Listener on GameLobby state changes (Game changes, lobby is empty notification)
     */
    public interface GameLobbyChangeListener {
        /**
         * Invoked on Game update
         *
         * @param pGame game
         */
        void onGameUpdate(Game pGame);

        /**
         * Invoked when last client leaves the GameLobby
         */
        void onLobbyEmpty();
    }

}
