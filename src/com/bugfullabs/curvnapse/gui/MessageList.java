package com.bugfullabs.curvnapse.gui;

import com.bugfullabs.curvnapse.network.message.TextMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class MessageList extends ListView<TextMessage> {
    private ObservableList<TextMessage> mMessages;

    public MessageList() {
        super();
        mMessages = FXCollections.observableArrayList();
        setCellFactory(list -> new MessageListElement());
        setItems(mMessages);
    }

    public void addMessage(TextMessage pMessage) {
        Platform.runLater(() -> {
            mMessages.add(pMessage);
            scrollTo(mMessages.size());
        });
    }

    class MessageListElement extends ListCell<TextMessage> {
        @Override
        public void updateItem(TextMessage pMessage, boolean pEmpty) {
            super.updateItem(pMessage, pEmpty);
            HBox box = new HBox();
            if (pMessage != null) {
                Label name = new Label(pMessage.getAuthor() + ": ");
                Label text = new Label(pMessage.getMessage());
                box.getChildren().add(name);
                box.getChildren().add(text);
                setGraphic(box);
            }
        }
    }
}
