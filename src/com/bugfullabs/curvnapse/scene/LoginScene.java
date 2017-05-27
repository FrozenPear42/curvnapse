package com.bugfullabs.curvnapse.scene;

import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.gui.LoginBox;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.logging.Logger;


/**
 * Represents Login screen in application
 */
public class LoginScene implements LoginBox.LoginListener {
    private static final Logger LOG = Logger.getLogger(MainLobbyScene.class.getName());
    private Scene mScene;
    private BorderPane mRoot;
    private HBox mTopBox;
    private Label mTitle;
    private LoginBox mLoginBox;

    /**
     * Create new login screen
     */
    public LoginScene() {
        mRoot = new BorderPane();

        mLoginBox = new LoginBox();
        mLoginBox.setLoginListener(this);

        mTopBox = new HBox(10.0f);
        mTopBox.setPadding(new Insets(50.0f, 10.0f, 10.0f, 10.0f));
        mTopBox.setAlignment(Pos.CENTER);
        mTitle = new Label("Curvenapse");
        mTitle.setStyle("-fx-font-size: 5em; -fx-font-weight: bold");
        mTopBox.getChildren().add(mTitle);

        mRoot.setTop(mTopBox);
        mRoot.setCenter(mLoginBox);
        mScene = new Scene(mRoot);
        mScene.getStylesheets().add("resources/JMetro.css");
    }

    /**
     * Get JFX Scene of the screen
     * @return JFX Scene
     */
    public Scene getScene() {
        return mScene;
    }

    /**
     * Callback called when login data is provided
     * @param pName username
     * @param pIP   IP
     * @param pPort Port number
     * @param pHost create server?
     */
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