package com.example;

import javafx.scene.control.Label;

public class Messenger {
    
    Label messageLabel = new Label();

    public Messenger(Label messageLabel) {
        this.messageLabel = messageLabel;
    }

    public void clearMessage() {
        messageLabel.setText("");
    }
    public void setMessage(String text) {
        messageLabel.setText(text);
    }
    
}
