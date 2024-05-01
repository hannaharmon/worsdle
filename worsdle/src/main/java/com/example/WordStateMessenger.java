package com.example;

import javafx.scene.control.Label;

public class WordStateMessenger extends Messenger{
    
    public WordStateMessenger(Label messageLabel) {
        super(messageLabel);
    }
    public void sendWordNotInList(String word) {
        messageLabel.setText(word + " is not in words list. Cannot submit.");
    }
}
