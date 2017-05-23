package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.GameCreateRequest;
import com.bugfullabs.curvnapse.network.message.GameStartRequest;
import com.bugfullabs.curvnapse.network.message.GameUpdateMessage;
import com.bugfullabs.curvnapse.network.message.Message.Type;
import com.bugfullabs.curvnapse.network.message.NewPlayerRequest;
import com.bugfullabs.curvnapse.network.server.Server;
import com.bugfullabs.curvnapse.scene.*;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Singleton class to manage client-side program flow
 */
public class FlowManager {
    private static FlowManager mInstance = new FlowManager();

    private Stage mMainStage;
    private Server mServer;
    private ServerConnector mServerConnector;
    private String mUsername;
    private int mUserID;

    /**
     * Returns instance of {@link FlowManager}
     *
     * @return instance
     */
    public static FlowManager getInstance() {
        return mInstance;
    }

    /**
     * Initialize function - must be called before using {@link FlowManager}
     *
     * @param pMainStage main window
     */
    public void initialize(Stage pMainStage) {
        mMainStage = pMainStage;
        mMainStage.setOnCloseRequest(event -> exit());
    }

    /**
     * Launches server thread with given params
     *
     * @param pPort     port
     * @param pMaxGames max count of running games
     * @return success flag
     */
    public boolean createServer(int pPort, int pMaxGames) {
        try {
            mServer = new Server(pPort, pMaxGames);
            mServer.start();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Try to connect to the server
     *
     * @param pIP   server IP
     * @param pPort server port
     * @return success flag
     */
    public boolean connectToServer(String pIP, int pPort) {
        try {
            mServerConnector = new ServerConnector(pIP, pPort);
            mServerConnector.start();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Login method - must be valled in login success callback
     *
     * @param pName   username
     * @param pUserID userID
     */
    public void login(String pName, int pUserID) {
        mUsername = pName;
        mUserID = pUserID;
    }

    /**
     * Put application in login mode
     */
    public void loginScene() {
        mMainStage.setScene(new LoginScene().getScene());
    }

    /**
     * Put application in lobby mode
     */
    public void mainLobby() {
        mMainStage.setScene(new MainLobbyScene().getScene());
    }

    /**
     * Put application in game lobby mode for given game
     *
     * @param pGame game for lobby
     */
    public void gameLobby(Game pGame) {
        mMainStage.setScene(new GameLobbyScene(pGame).getScene());
    }

    /**
     * Put application in game mode for given game
     *
     * @param pGame game
     */
    public void gameScene(Game pGame) {
        mMainStage.setScene(new GameScene(pGame).getScene());
    }

    /**
     * Put application in game over mode for given game
     *
     * @param pGame game
     */
    public void gameOverScene(Game pGame) {
        mMainStage.setScene(new GameOverScene(pGame).getScene());
    }

    /**
     * Run simple test mode - for testing purposes
     */
    public void testMode() {
        String ip = "localhost";
        int port = 1337;
        Game game;
        createServer(port, 10);
        connectToServer(ip, port);
        mServerConnector.registerListener(message -> {
            if (message.getType() == Type.GAME_UPDATE) {
                mServerConnector.sendMessage(new GameStartRequest());
                Platform.runLater(() -> gameScene(((GameUpdateMessage) message).getGame()));
            }
            if (message.getType() == Type.GAME_JOIN) {
                mServerConnector.sendMessage(new NewPlayerRequest("asd"));
                //mServerConnector.sendMessage(new NewPlayerRequest("asd2"));
            }
        });
        mServerConnector.handshake("test", pID -> mUserID = pID);
        mServerConnector.sendMessage(new GameCreateRequest(mUserID, "asd", "", 10));
    }

    /**
     * Ends all the connections and closes the app
     */
    private void exit() {
        if (mServerConnector != null)
            mServerConnector.disconnect();
        if (mServer != null)
            mServer.close();
    }

    /**
     * Returns client side server connector
     *
     * @return connector
     */
    public ServerConnector getConnector() {
        return mServerConnector;
    }

    /**
     * Returns username used to log in
     *
     * @return username
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * Returns userID generated in login process
     *
     * @return userID
     */
    public int getUserID() {
        return mUserID;
    }

}
