package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.network.client.Game;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.server.Server;
import com.bugfullabs.curvnapse.scene.GameLobbyScene;
import com.bugfullabs.curvnapse.scene.GameScene;
import com.bugfullabs.curvnapse.scene.LoginScene;
import com.bugfullabs.curvnapse.scene.MainLobbyScene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FlowManager {
    private static FlowManager mInstance = new FlowManager();

    private Stage mMainStage;
    private Server mServer;
    private ServerConnector mServerConnector;
    private String mUsername;
    private int mUserID;

    public static FlowManager getInstance() {
        return mInstance;
    }

    public void initialize(Stage pMainStage) {
        mMainStage = pMainStage;
    }

    public boolean createServer(int pPort, int pMaxGames) {
        try {
            mServer = new Server(pPort, pMaxGames);
            mServer.start();
            mMainStage.setOnCloseRequest(event -> mServer.close());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean connectToServer(String pIP, int pPort) {
        try {
            mServerConnector = new ServerConnector(pIP, pPort);
            mServerConnector.start();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void login(String pName, int pUserID) {
        mUsername = pName;
        mUserID = pUserID;
    }

    public void loginScene() {
        mMainStage.setScene(new LoginScene().getScene());
    }

    public void mainLobby() {
        mMainStage.setScene(new MainLobbyScene().getScene());
    }

    public void gameLobby(Game pGame) {
        mMainStage.setScene(new GameLobbyScene(pGame).getScene());
    }

    public void gameScene() {
        mMainStage.setScene(new GameScene(new ArrayList<>()).getScene());
    }

    public ServerConnector getConnector() {
        return mServerConnector;
    }

    public String getUsername() {
        return mUsername;
    }

    public int getUserID() {
        return mUserID;
    }
}
