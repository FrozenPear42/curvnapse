package com.bugfullabs.curvnapse.gui;


import com.bugfullabs.curvnapse.network.message.TextMessage;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MessageBox extends VBox {
    private ListView<TextMessage> mList;

    private HBox mInputBox;
    private TextField mTextBox;
    private Button mSendButton;
    private MessageSendListener mListener;
    private Label mTitle;


    public MessageBox() {
        super(10.0f);
        setPadding(new Insets(10.0f));
        setAlignment(Pos.TOP_CENTER);

        mTitle = new Label("Chat");
        mTitle.setStyle("-fx-font-size: 2em; -fx-font-weight: bold");

        mList = new ListView<>();
        mList.setCellFactory(pTextMessageListView -> new MessageListElement());

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


        getChildren().addAll(mTitle, mList, mInputBox);
        setMaxWidth(320);
    }

    public void setSendListener(MessageSendListener pListener) {
        mListener = pListener;
    }

    public void setMessages(ObservableList<TextMessage> pMessages) {
        mList.setItems(pMessages);
        pMessages.addListener((ListChangeListener<TextMessage>) pChange -> {
            pChange.next();
            mList.scrollTo(pChange.getTo() - 1);
        });
    }

    public interface MessageSendListener {
        void onMessageSend(String pMessage);
    }

    class MessageListElement extends ListCell<TextMessage> {
        @Override
        public void updateItem(TextMessage pMessage, boolean pEmpty) {
            super.updateItem(pMessage, pEmpty);
            HBox box = new HBox();
            if (pMessage != null) {
                Label name = new Label(pMessage.getAuthor() + ": ");
                Label text = new Label(pMessage.getMessage());
                text.setMaxWidth(220);
                text.setWrapText(true);
                box.getChildren().add(name);
                box.getChildren().add(text);
            }
            setGraphic(box);
        }
    }

}
