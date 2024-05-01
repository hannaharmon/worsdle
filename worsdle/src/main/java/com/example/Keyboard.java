package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.Arrays;

public class Keyboard {

    // Key constants
    final int BACK_KEY_INDEX = 19;
    final int ENTER_KEY_INDEX = 27;
    final ArrayList<String> KEYBOARD_LAYOUT = new ArrayList<String>(Arrays.asList("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Back", "Z", "X", "C", "V", "B", "N", "M", "Enter"));
    
    ObservableList<Button> keyButtons;    // For changing key color after entering word

    public Keyboard(VBox keyboardBox) {
        // Get the things for changing colors of the keyboard keys
        ObservableList<Node> rows = keyboardBox.getChildren();    // Get all the keyboard rows (there are 3)

        // Here we will store all of the buttons
        keyButtons = FXCollections.observableArrayList();

        // For each row, add all its Buttons to the list
        for (Node row : rows) {
            // Get all the buttons in the row
            ObservableList<Node> rowButtons = ((HBox)row).getChildren();

            // For each row, add the buttons to keyButtons
            for (Node button : rowButtons) {
                keyButtons.add((Button)button);
            }
        }
    }

    public ObservableList<Button> getKeyButtons() {
        return keyButtons;
    }
    public void setKeyColor(int keyIndex, String color) {
        keyButtons.get(keyIndex).setStyle("-fx-background-color: " + color + ";");
    }
    public void setKeyColor(char letter, String color) {
        setKeyColor(KEYBOARD_LAYOUT.indexOf(Character.toString(letter)), color);
    }
    public void lockDelete() {
        keyButtons.get(BACK_KEY_INDEX).setDisable(true);
    }
    public void unlockDelete() {
        keyButtons.get(BACK_KEY_INDEX).setDisable(false);
    }
    public void lockKeys() {
        for (Button key : keyButtons) {
            key.setDisable(true);
        }
    }
    public void unlockKeys() {
        for (Button key : keyButtons) {
            key.setDisable(false);
        }
    }
    public void lockKey(Character letter) {
        keyButtons.get(KEYBOARD_LAYOUT.indexOf(Character.toString(letter))).setDisable(true);
    }
}
