package com.bugfullabs.curvnapse.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Control class for displaying login box
 */
public class LoginBox extends VBox {
    private CheckBox mHost;
    private LabeledTextBox mNameBox;
    private LabeledTextBox mIPBox;
    private LabeledTextBox mPortBox;
    private Button mJoinButton;
    private LoginListener mListener;

    /**
     * create new login box
     */
    public LoginBox() {
        super(10.0);
        setPadding(new Insets(10.0f));
        setAlignment(Pos.CENTER);
        setSpacing(10.0f);
        setMaxWidth(220.0f);

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

        //FIXME: good or not
        mNameBox.setText("User");
        mIPBox.setText("127.0.0.1");
        mPortBox.setText("1337");

        mJoinButton.setOnAction(event -> {
            mListener.onLogin(mNameBox.getText(), mIPBox.getText(), mPortBox.getText(), mHost.isSelected());
        });
    }

    /**
     * Set login listener
     *
     * @param pListener listener
     */
    public void setLoginListener(LoginListener pListener) {
        mListener = pListener;
    }

    /**
     * login listener interface
     */
    public interface LoginListener {
        /**
         * invoked on login request
         *
         * @param pName username
         * @param pIP   IP
         * @param pPort Port number
         * @param pHost create server?
         */
        void onLogin(String pName, String pIP, String pPort, boolean pHost);
    }

    /**
     * Control class for {@link TextField} with {@link Label}
     */
    class LabeledTextBox extends VBox {
        private Label mLabel;
        private TextField mText;

        /**
         * Create new {@link TextField} with given label
         *
         * @param pLabel label
         */
        LabeledTextBox(String pLabel) {
            super(5.0f);
            setAlignment(Pos.CENTER);
            mLabel = new Label(pLabel);
            mText = new TextField();
            getChildren().addAll(mLabel, mText);
        }

        /**
         * get text in the {@link TextField}
         *
         * @return entered text
         */
        String getText() {
            return mText.getText();
        }

        /**
         * Set text in the {@link TextField}
         *
         * @param pText text to be set
         */
        void setText(String pText) {
            mText.setText(pText);
        }
    }
}
