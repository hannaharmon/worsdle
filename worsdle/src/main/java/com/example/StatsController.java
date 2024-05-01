package com.example;

import java.io.IOException;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatsController {

    final String WIN_TITLE = "You win!";
    final String LOSE_TITLE = "You lose.";

    @FXML
    Label title;
    @FXML
    Label solutionWordLabel;
    @FXML
    Label winLabel;
    @FXML
    Label loseLabel;
    @FXML
    Label winRateLabel;
    @FXML
    Label gamesPlayedLabel;
    @FXML
    Label avgGuessLabel;
    @FXML
    Label guessLabel;
    @FXML
    Label saveLoadLabel;

    Messenger saveLoadMessenger;

    static Boolean youWon;
    static String solutionString;
    static Integer wins = 0;
    static Integer losses = 0;
    static Float winRate = 0f;
    static Integer yourGuesses = 0;
    static Integer gamesPlayed = 0;
    static Integer totalGuess = 0;
    static Float avgGuess = 0f;

    public void initialize() {
        saveLoadMessenger = new Messenger(saveLoadLabel);
        setLabels();
        saveLoadMessenger.clearMessage();
    }
    private void setLabels() {
        // Update labels to match stored values
        title.setText((youWon? WIN_TITLE : LOSE_TITLE));
        solutionWordLabel.setText(solutionString);
        winLabel.setText(wins.toString());
        loseLabel.setText(losses.toString());
        winRateLabel.setText(winRate.toString() + "%");
        gamesPlayedLabel.setText(gamesPlayed.toString());
        avgGuessLabel.setText(avgGuess.toString());
        guessLabel.setText(yourGuesses.toString());
    }

    public static void updateStats(Boolean won, int guesses, String solutionWord) {
        // Update stored values
        solutionString = solutionWord;
        gamesPlayed++;
        youWon = won;
        wins += (won? 1 : 0);
        losses += (won? 0 : 1);
        winRate = ((float)wins/gamesPlayed) * 100;
        yourGuesses = guesses;
        totalGuess += guesses;
        avgGuess = (won? (float)totalGuess/wins : avgGuess);
    }
    public static void updateStatsFromStatsString(String statsString) {
        Scanner statsScanner = new Scanner(statsString);
        gamesPlayed = Integer.parseInt(statsScanner.next());
        wins = Integer.parseInt(statsScanner.next());
        losses = Integer.parseInt(statsScanner.next());
        winRate = Float.parseFloat(statsScanner.next());
        avgGuess = Float.parseFloat(statsScanner.next());
        statsScanner.close();
    }

    // Call on press play again button
    public void playAgain() {
        // Swap to the wordle scene
        try {
            App.swapScene("wordle");
        } catch (IOException e) {
            System.out.println("Could not swap to scene: wordle");
            e.printStackTrace();
        }
    }
    public static String getCurrentStatsString() {
        return gamesPlayed + " " + wins + " " + losses + " " + winRate + " " + avgGuess;
    }

    public void saveGame() {
        SaveSystem.saveGame();
        // Print message letting user know data was saved
        saveLoadLabel.setText("Save successful.");
    }
    public void loadGame() {
        Boolean loadWasSuccessful = SaveSystem.loadGame();
        // Print message letting user know load was successful/unsuccessful
        if (loadWasSuccessful) saveLoadLabel.setText("Load successful.");
        else saveLoadLabel.setText("No save exists.");
        // Update the stats menu
        setLabels();
    }
}
