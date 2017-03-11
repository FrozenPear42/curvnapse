package com.bugfullabs.curvnapse.gui;


import com.bugfullabs.curvnapse.network.message.TextMessage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MessageBox extends VBox {
    private MessageList mMessageList;
    private HBox mInputBox;
    private TextField mTextBox;
    private Button mSendButton;
    private MessageSendListener mListener;

    public MessageBox() {
        super();
        mMessageList = new MessageList();
        mInputBox = new HBox();
        mTextBox = new TextField();
        mSendButton = new Button(">");

        mInputBox.getChildren().add(mTextBox);
        mInputBox.getChildren().add(mSendButton);

        mSendButton.setOnAction(event -> {
            mListener.onMessageSend(mTextBox.getText());
            mMessageList.addMessage(new TextMessage(mTextBox.getText()));
            mTextBox.setText("");
        });

    getChildren().addAll(mMessageList, mInputBox);
    }

    public void setSendListener(MessageSendListener pListener) {
        mListener = pListener;
    }

    public void addMessage(TextMessage pMessage) {
        mMessageList.addMessage(pMessage);
    }

    public interface MessageSendListener {
        void onMessageSend(String pMessage);
    }
}
