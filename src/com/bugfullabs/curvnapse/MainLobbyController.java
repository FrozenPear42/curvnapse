package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.server.Server;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;

public class MainLobbyController implements ServerConnector.MessageListener {
    Scene mScene;
    FlowPane mRoot;
    ServerConnector mServerConnector;


    public MainLobbyController(ServerConnector pServerConnector) {
        mRoot = new FlowPane();
        mScene = new Scene(mRoot);

        mServerConnector = pServerConnector;
        mServerConnector.registerListener(this);
    }

    @Override
    public void onClientMessage(Message pMessage) {
    }
}
