package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class Wordboard {

    // Lists for holding modifiable scene elements
    ObservableList<HBox> labelBoxes;    // For changing letter color after entering word
    ObservableList<Label> labels;       // For changing letters after entering word

    char[][] currentBoard = new char[6][5];   // row major
    char nullChar;
    int currentRowIndex = 0;

    public Wordboard(VBox wordleBox) {
        // Get the things for the entering letters into the wordle box
        ObservableList<Node> rows = wordleBox.getChildren();    // Get all the wordle rows (there are 6)
        
        // Here we will store all of the HBoxes that contain labels (to change their colors)
        labelBoxes = FXCollections.observableArrayList();
        // Here we will store all of the Labels (to enter letters)
        labels = FXCollections.observableArrayList();
        
        // For each row, add all its label boxes and labels to the lists
        for (Node row : rows) {
            // Get all the label boxes in the row
            ObservableList<Node> rowBoxes = ((HBox)row).getChildren();

            // Add each label box in this row to labelBoxes, and add each label box's label to labels
            for (Node box : rowBoxes) {
                labelBoxes.add((HBox)box);
                ObservableList<Node> boxLabels = ((HBox)box).getChildren();
                labels.add((Label)boxLabels.get(0));    // Add the label of this row box to the labels list
            }
        }
    }

    public void setLabelBoxColor(int labelBoxIndex, String color) {
        labelBoxes.get(labelBoxIndex).setStyle("-fx-background-color: " + color + ";");
    }
    public void setLabel(int labelIndex, char labelToSet) {
        labels.get(labelIndex).setText(String.valueOf(labelToSet));
    }

    public char[][] getCurrentBoard() {
        return currentBoard;
    }
    public String toString() {
        // Print the currentBoard
        String boardString = "\n";
        for (char[] word : currentBoard) {
            for (char letter : word) {
                if (letter == nullChar) boardString += "_" + " ";
                else boardString += letter + " ";
            }
            boardString += "\n";
        }
        return boardString;
    }
    public void addWordToBoard(String word) {
        currentBoard[currentRowIndex] = word.toCharArray();
        currentRowIndex++;
    }
    public void clearBoard() {
        currentBoard = new char[6][5];
    }
}
