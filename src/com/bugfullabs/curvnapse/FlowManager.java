package com.bugfullabs.curvnapse;

import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.server.Server;
import javafx.stage.Stage;

public class FlowManager {
    private static FlowManager mInstance = new FlowManager();

    private Stage mMainStage;
    private Server mServer;
    private ServerConnector mServerConnector;
    private String mUsername;

    public static FlowManager getInstance() {
        return mInstance;
    }

    public void initialize(Stage pMainStage) {
        mMainStage = pMainStage;
    }
}
