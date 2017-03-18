package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.gui.LoginBox;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.server.Server;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sun.rmi.runtime.Log;

import java.util.logging.Logger;

public class LoginScene implements LoginBox.LoginListener {
    private static final Logger LOG = Logger.getLogger(MainLobbyScene.class.getName());
    private HBox mRoot;
    private Scene mScene;
    private LoginBox mLoginBox;

    private Stage mMainStage;

    public LoginScene(Stage pMainStage) {
        mRoot = new HBox();
        mRoot.setAlignment(Pos.CENTER);
        mLoginBox = new LoginBox();
        mLoginBox.setLoginListener(this);
        mRoot.getChildren().add(mLoginBox);
        mScene = new Scene(mRoot);

        mMainStage = pMainStage;
    }

    public Scene getScene() {
        return mScene;
    }

    @Override
    public void onLogin(String pName, String pIP, String pPort, boolean pHost) {
        if (pName.isEmpty()) {
            return;
        }

        if (pHost) {
            try {
                Server server = new Server(Integer.parseInt(pPort), 100);
                server.start();
                mMainStage.setOnCloseRequest(event -> {
                    LOG.info("Closing app!");
                    server.close();
                });
            } catch (Exception e) {
                LOG.warning("Could not start server");
                return;
            }
        }

        try {
            ServerConnector connector = new ServerConnector(pIP, Integer.parseInt(pPort), pName);
            connector.start();
            mMainStage.setScene(new MainLobbyScene(mMainStage, connector).getScene());
        } catch (Exception e) {
            LOG.warning("Could not connect to server");
        }
    }

}