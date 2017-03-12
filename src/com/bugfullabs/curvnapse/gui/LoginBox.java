package com.bugfullabs.curvnapse.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginBox extends VBox {
    private CheckBox mHost;
    private LabeledTextBox mNameBox;
    private LabeledTextBox mIPBox;
    private LabeledTextBox mPortBox;
    private Button mJoinButton;
    private LoginListener mListener;

    public LoginBox() {
        super();
        setPadding(new Insets(10.0f));
        setAlignment(Pos.CENTER);
        setSpacing(5.0f);

        mHost = new CheckBox("Host?");
        mNameBox = new LabeledTextBox("Name:");
        mIPBox = new LabeledTextBox("IP:");
        mPortBox = new LabeledTextBox("Port:");
        mJoinButton = new Button("Join");
        getChildren().addAll(mHost, mNameBox, mIPBox, mPortBox, mJoinButton);

        mHost.setOnAction(event -> {
            if (mHost.isSelected()) {
                mIPBox.setDisable(true);
                mJoinButton.setText("Host");
                mIPBox.setText("");
            } else {
                mIPBox.setDisable(false);
                mJoinButton.setText("Join");
            }
        });

        mIPBox.setText("127.0.0.1");
        mPortBox.setText("1337");

        mJoinButton.setOnAction(event -> {
            mListener.onLogin(mNameBox.getText(), mIPBox.getText(), mPortBox.getText(), mHost.isSelected());
        });
    }

    public void setLoginListener(LoginListener pListener) {
        mListener = pListener;
    }

    class LabeledTextBox extends VBox {
        private Label mLabel;
        private TextField mText;

        LabeledTextBox(String pLabel) {
            super();
            setAlignment(Pos.CENTER);
            mLabel = new Label(pLabel);
            mText = new TextField();
            getChildren().addAll(mLabel, mText);
        }

        String getText() {
            return mText.getText();
        }

        void setText(String pText) {
            mText.setText(pText);
        }
    }

    public interface LoginListener {
        void onLogin(String pName, String pIP, String pPort, boolean pHost);
    }
}
