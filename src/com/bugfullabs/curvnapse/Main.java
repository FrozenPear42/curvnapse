package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.gui.*;
import com.bugfullabs.curvnapse.network.client.Game;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.server.Server;
import com.bugfullabs.curvnapse.scene.GameLobbyScene;
import com.bugfullabs.curvnapse.scene.MainLobbyScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends Application implements LoginBox.LoginListener {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private Board mBoard;

    private Scene mLoginScene;
    private Scene mGameScene;
    private Stage mMainStage;

    @Override
    public void start(Stage primaryStage) {

        mMainStage = primaryStage;

        HBox loginRoot = new HBox();
        loginRoot.setAlignment(Pos.CENTER);
        LoginBox loginBox = new LoginBox();
        loginBox.setLoginListener(this);
        loginRoot.getChildren().add(loginBox);
        mLoginScene = new Scene(loginRoot);

        FlowPane gameRoot = new FlowPane();
        mBoard = new Board(1000, 800);
        gameRoot.getChildren().add(mBoard);
        mGameScene = new Scene(gameRoot);

        primaryStage.setTitle("Curvnapse");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(500);
        primaryStage.setScene(mLoginScene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
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
            return;
        }
    }

}
