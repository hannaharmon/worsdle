package com.example;

import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

// Use this to create the "one at a time" effect when coloring wordboard letters
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class WordleController {
    
    // FXML stuff
    @FXML
    VBox wordleBox;
    @FXML
    VBox keyboardBox;
    @FXML
    Label messageLabel;
    @FXML
    Label saveLoadLabel;

    // Setup the wordboard, keyboard, messengers
    Wordboard wordboard;
    Keyboard keyboard;
    WordStateMessenger wordStateMessenger;
    Messenger saveLoadMessenger;

    // Color constants
    final String CORRECT_LETTER_COLOR = "LightGreen";
    final String WRONG_POS_LETTER_COLOR = "Khaki";
    final String BAD_LETTER_COLOR = "DarkGrey";
    final String DEFAULT_LETTER_COLOR = "Gainsboro";

    // Use this to create the "one at a time" effect when coloring wordboard letters
    final int DELAY = 500; // (in ms)
    
    // Numbers for tracking current row and current letter
    final int FINAL_ROW_INDEX = 5;  // 6 rows
    final int FINAL_COL_INDEX = 4;  // 5 cols
    int currentLetterIndex = 0;
    int currentColIndex = 0;
    int currentRowIndex = 0;

    // Track the current word so we have something to check on press enter
    String currentWord = "";
    
    // Get a default word list to use when choosing words
    WordsList wordsList;

    // Strings for tracking the entered word and soution word
    String solutionWord, enteredWord;

    // Get all the visual objects we need (aka things we have to change throughout the course of the game)
    public void initialize() {
        // Setup the wordboard, keyboard, and words list on first wordle game load
        wordboard = new Wordboard(wordleBox);
        keyboard = new Keyboard(keyboardBox);
        wordsList = new WordsList();
        wordStateMessenger = new WordStateMessenger(messageLabel);
        saveLoadMessenger = new Messenger(saveLoadLabel);
        
        // Grab a solutionWord to use for this round
        solutionWord = wordsList.getWord();

        // Make sure the message is empty
        wordStateMessenger.clearMessage();
        saveLoadMessenger.clearMessage();
    }

    // Call on keyboard button click
    public void enterLetter(ActionEvent e) {
        
        // Figure out which key was pressed
        Object source = e.getSource();
        Button button = (Button) source;
        
        // Get the letter on this key
        char letterToEnter = button.getText().charAt(0);
        
        // Check if we are allowed to enter another letter (is the row full? only enter if it isn't) (have we already used all our chances?)
        if (currentColIndex <= FINAL_COL_INDEX && currentRowIndex <= FINAL_ROW_INDEX) {
            // Clear the message
            wordStateMessenger.clearMessage();
            // Change the current label to display the letterToEnter and update  currentLetterIndex
            wordboard.setLabel(currentLetterIndex++, letterToEnter);
            currentColIndex++;
            // Update the current word
            currentWord += letterToEnter;
        } else {
            if (!(currentRowIndex <= FINAL_ROW_INDEX)) {
                System.out.println("All rows full. Cannot enter any more letters.");
            } else {
                System.out.println("Row is full. Cannot enter another letter.");
            }
        }

    }

    // Call on backspace button click
    public void deleteLetter() {
        // Determine if we are allowed to delete a letter (is the row empty? only delete if it isn't)
        if (currentColIndex != 0) {
            // Clear the message
            wordStateMessenger.clearMessage();
            // Change the label BEFORE the current label back to empty (we want to delete the letter BEFORE the cursor: the letter AT the cursor has nothing)
            wordboard.setLabel(--currentLetterIndex, ' ');
            currentColIndex--;
            // Update the current word
            currentWord = currentWord.substring(0, currentWord.length()-1);
        } else {
            System.out.println("Cannot delete letter. The row is empty.");
        }
    }
    
    // Call on enter button click
    public void submitWord() {
        // Determine if we are allowed to submit this word (is the row full? only submit if it is) (is this word possible? only submit if it is)
        if (wordsList.isWordPossible(currentWord) && currentColIndex > FINAL_COL_INDEX) {
            System.out.println("Submitted Word.");

            // Add the word to the board
            wordboard.addWordToBoard(currentWord);
            // Print the board
            System.out.println("\nCurrent board: \n" + wordboard);

            // Determine which colors to color the labels and keys
            ArrayList<Character> wrongPosLetters = new ArrayList<Character>();
            ArrayList<Character> notPresentLetters =  new ArrayList<Character>();
            ArrayList<Character> correctLetters = new ArrayList<Character>();
            int currentCharIndex = 0;
            for (char letter : currentWord.toCharArray()) {
                if (solutionWord.contains(Character.toString(letter))) {
                    if (solutionWord.charAt(currentCharIndex) == letter) {
                        // Letter is correct
                        System.out.println("Letter at " + currentCharIndex + " is correct.");
                        correctLetters.add(letter);
                    } else {
                        // Letter in wrong pos
                        System.out.println("Letter at " + currentCharIndex + " is in the wrong position.");
                        wrongPosLetters.add(letter);
                    }
                } else {
                    // Letter not in solution
                    System.out.println("Letter at " + currentCharIndex + " is not in the solution.");
                    notPresentLetters.add(letter);
                }
                currentCharIndex++;
            }

            // Lock the keys (can't type while submitting)
            keyboard.lockKeys();

            // Use a timer to wait some time between each color change (for a "one at a time" effect)
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                int currentBoxIndex = currentLetterIndex - 5; // Get first letter of this row
                int currentBoxCol = 0;
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // After changing the colors of all the letters, stop this timer task
                            if (currentBoxCol > FINAL_COL_INDEX) {
                                timer.cancel();
                                timer.purge();

                                // Submit is finished, unlock keys
                                keyboard.unlockKeys();

                                // Increment the current row
                                currentRowIndex++;

                                // Change the keyboard button colors
                                colorKeyboard(wrongPosLetters, notPresentLetters, correctLetters);
                        
                                // If the currentWord is the solution word, finishRound(won = true)
                                if (currentWord.equals(solutionWord)) {
                                    finishRound(true); 
                                }
                        
                                // If the current row is final row, and didn't win round, finishRound(won = false)
                                else if (currentRowIndex > FINAL_ROW_INDEX) {
                                    finishRound(false); 
                                }

                                // Reset the current word and currentColIndex
                                currentWord = "";
                                currentColIndex = 0;
                                return;
                            }
                            // Change the color of the currentBox
                            colorLetter(currentBoxCol++, currentBoxIndex++, wrongPosLetters, notPresentLetters, correctLetters); 
                        }
                    });
                }
            }, 0, DELAY);   // First color change happens immediately, each next one happens after a DELAY

        } else {
            if (currentColIndex <= FINAL_COL_INDEX) {
                System.out.println("Word is not long enough. Only has " + currentColIndex + " letters. Cannot submit.");
            } else {
                // Show in game message telling user that this word does not exist
                System.out.println("Word \"" + currentWord + "\" is not in the list of possible words. Cannot submit.");
                wordStateMessenger.sendWordNotInList(currentWord);
            }
        }
    }

    private void colorLetter(int currentBoxCol, int buttonToColorIndex, ArrayList<Character> wrongPosLetters, ArrayList<Character> notPresentLetters, ArrayList<Character> correctLetters) {
        Character letter = currentWord.charAt(currentBoxCol);
        // If all of the correct letter occurences have already been found, we want to go ahead and mark the extra letter as not in solution
        List<Character> solutionChars = solutionWord.chars().mapToObj((i) -> Character.valueOf(((char)i))).collect(Collectors.toList());
        Boolean overwriteCorrect = (Collections.frequency(correctLetters, letter) > Collections.frequency(solutionChars, letter));
        // Color a word board letter
        if (correctLetters.contains(letter) && !overwriteCorrect) wordboard.setLabelBoxColor(buttonToColorIndex, CORRECT_LETTER_COLOR);
        else if (notPresentLetters.contains(letter) || overwriteCorrect) wordboard.setLabelBoxColor(buttonToColorIndex, BAD_LETTER_COLOR);
        else if (wrongPosLetters.contains(letter)) wordboard.setLabelBoxColor(buttonToColorIndex, WRONG_POS_LETTER_COLOR);
    }
    private void colorKeyboard(ArrayList<Character> wrongPosLetters, ArrayList<Character> notPresentLetters, ArrayList<Character> correctLetters) {
        // If all of the correct letter occurences have already been found, we want to go ahead and mark the extra letter as not in solution
        List<Character> solutionChars = solutionWord.chars().mapToObj((i) -> Character.valueOf(((char)i))).collect(Collectors.toList());
        // Color keyboard letters
        for (Character letter : correctLetters) {
            Boolean overwriteCorrect = (Collections.frequency(correctLetters, letter) > Collections.frequency(solutionChars, letter));
            if (!overwriteCorrect) keyboard.setKeyColor(letter, CORRECT_LETTER_COLOR);
            else {
                keyboard.setKeyColor(letter, BAD_LETTER_COLOR);
                // also disable
                keyboard.lockKey(letter);
            }
        }
        for (Character letter : notPresentLetters) {
            keyboard.setKeyColor(letter, BAD_LETTER_COLOR);
            // also disable
            keyboard.lockKey(letter);
        }
        for (Character letter : wrongPosLetters) keyboard.setKeyColor(letter, WRONG_POS_LETTER_COLOR);
    }

    public void finishRound(boolean won) {
        System.out.println("Round over. Result is: " + (won? "win." : "lose."));
        // Adjust stats based on win/loss and number of guesses
        StatsController.updateStats(won, currentRowIndex, solutionWord);
        
        // Reset the game
        resetGame();

        // Swap to the stats scene
        try {
            App.swapScene("stats");
        } catch (IOException e) {
            System.out.println("Could not swap to scene: stats");
            e.printStackTrace();
        }
    }

    public void giveUp() {
        finishRound(false);
    }

    public void resetGame() {
        // Reset everything that changes throughout a game to defaults
        currentLetterIndex = 0;
        currentColIndex = 0;
        currentRowIndex = 0;
        enteredWord = "";
        wordboard.clearBoard();
    }

    public void saveGame() {
        SaveSystem.saveGame();
        // Print message letting user know data was saved
        saveLoadLabel.setText("Save successful.");
    }
    public void loadGame() {
        Boolean loadWasSuccessful = SaveSystem.loadGame();
        // Print message letting user know load was successful/unsuccessful
        if (loadWasSuccessful) {
            // Reset the game (could be using a word that is now removed from the words list)
            resetGame();
            saveLoadLabel.setText("Load successful.");
        }
        else saveLoadLabel.setText("No save exists.");
    }

}
