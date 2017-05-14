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

/**
 * Control class for displaying message box
 */
public class MessageBox extends VBox {
    private ListView<TextMessage> mList;

    private TextField mTextBox;
    private MessageSendListener mListener;

    /**
     * Create new message box
     */
    public MessageBox() {
        super(10.0f);
        setPadding(new Insets(10.0f));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Chat");
        title.setStyle("-fx-font-size: 2em; -fx-font-weight: bold");

        mList = new ListView<>();
        mList.setCellFactory(pTextMessageListView -> new MessageListElement());

        HBox inputBox = new HBox();
        mTextBox = new TextField();
        Button sendButton = new Button(">");
        inputBox.getChildren().add(mTextBox);
        inputBox.getChildren().add(sendButton);

        inputBox.setAlignment(Pos.CENTER);
        inputBox.setSpacing(5.0f);

        mTextBox.setPrefColumnCount(20);

        inputBox.setOnKeyPressed(pKeyEvent -> {
            if (pKeyEvent.getCode() == KeyCode.ENTER) {
                if (mListener != null)
                    mListener.onMessageSend(mTextBox.getText());
                mTextBox.setText("");
            }
        });

        sendButton.setOnAction(event -> {
            if (mListener != null)
                mListener.onMessageSend(mTextBox.getText());
            mTextBox.setText("");
        });


        getChildren().addAll(title, mList, inputBox);
        setMaxWidth(320);
    }

    /**
     * Set listener on send events
     *
     * @param pListener listener
     */
    public void setSendListener(MessageSendListener pListener) {
        mListener = pListener;
    }

    /**
     * Set messages source ({@link ObservableList}
     *
     * @param pMessages messages
     */
    public void setMessages(ObservableList<TextMessage> pMessages) {
        mList.setItems(pMessages);
        pMessages.addListener((ListChangeListener<TextMessage>) pChange -> {
            pChange.next();
            mList.scrollTo(pChange.getTo() - 1);
        });
    }

    /**
     * Listerner on message being sent
     */
    public interface MessageSendListener {
        /**
         * invoked when message was sent
         *
         * @param pMessage message string
         */
        void onMessageSend(String pMessage);
    }

    /**
     * Inner class for list cell
     */
    class MessageListElement extends ListCell<TextMessage> {
        /**
         * invoked when list is redrawn
         *
         * @param pMessage {@link TextMessage} to be drawn
         * @param pEmpty   is cell empty
         */
        @Override
        public void updateItem(TextMessage pMessage, boolean pEmpty) {
            super.updateItem(pMessage, pEmpty);
            HBox box = new HBox();
            if (pMessage != null) {
                Label name = new Label(pMessage.getAuthor() + ": ");
                Label text = new Label(pMessage.getMessage());
                name.setTextFill(pMessage.getTextColor().toFXColor());
                text.setTextFill(pMessage.getTextColor().toFXColor());
                text.setMaxWidth(220);
                text.setWrapText(true);
                box.getChildren().add(name);
                box.getChildren().add(text);
            }
            setGraphic(box);
        }
    }

}
