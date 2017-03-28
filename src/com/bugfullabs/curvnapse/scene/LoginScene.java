package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.gui.LoginBox;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.server.Server;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sun.rmi.runtime.Log;

import java.lang.management.PlatformLoggingMXBean;
import java.util.logging.Logger;

public class LoginScene implements LoginBox.LoginListener {
    private static final Logger LOG = Logger.getLogger(MainLobbyScene.class.getName());
    private HBox mRoot;
    private Scene mScene;
    private LoginBox mLoginBox;

    public LoginScene() {
        mRoot = new HBox();
        mRoot.setAlignment(Pos.CENTER);
        mLoginBox = new LoginBox();
        mLoginBox.setLoginListener(this);
        mRoot.getChildren().add(mLoginBox);
        mScene = new Scene(mRoot);
    }

    public Scene getScene() {
        return mScene;
    }

    @Override
    public void onLogin(String pName, String pIP, String pPort, boolean pHost) {
        if (pName.isEmpty())
            return;

        if (pIP.isEmpty())
            pIP = "localhost";

        if (pHost) {
            if (!FlowManager.getInstance().createServer(Integer.parseInt(pPort), 100)) {
                new Alert(Alert.AlertType.ERROR, "Cannot create server").showAndWait();
            }
        }
        if (!FlowManager.getInstance().connectToServer(pIP, Integer.parseInt(pPort))) {
            new Alert(Alert.AlertType.ERROR, "Cannot join server").showAndWait();
        }

        FlowManager.getInstance().getConnector().handshake(pName, pID ->
                Platform.runLater(() -> {
                    if (pID == -1) {
                        new Alert(Alert.AlertType.ERROR, "Ups...").showAndWait();
                    } else {
                        FlowManager.getInstance().login(pName, pID);
                        FlowManager.getInstance().mainLobby();
                    }
                }));
    }

}