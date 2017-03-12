package com.bugfullabs.curvnapse.gui;


import com.bugfullabs.curvnapse.network.message.TextMessage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
        setPadding(new Insets(10.0f));
        setSpacing(5.0f);
        mMessageList = new MessageList();
        mInputBox = new HBox();
        mTextBox = new TextField();
        mSendButton = new Button(">");
        mInputBox.getChildren().add(mTextBox);
        mInputBox.getChildren().add(mSendButton);

        mInputBox.setAlignment(Pos.CENTER);
        mInputBox.setSpacing(5.0f);

        mTextBox.setPrefColumnCount(20);

        mInputBox.setOnKeyPressed(pKeyEvent -> {
            if (pKeyEvent.getCode() == KeyCode.ENTER) {
                if (mListener != null)
                    mListener.onMessageSend(mTextBox.getText());
                mTextBox.setText("");
            }
        });

        mSendButton.setOnAction(event -> {
            if (mListener != null)
                mListener.onMessageSend(mTextBox.getText());
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
