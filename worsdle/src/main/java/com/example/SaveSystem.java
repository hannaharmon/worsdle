package com.example;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class SaveSystem {
    final static String SAVE_FILE_NAME = "save.txt";

    public static void saveGame() {
        // Save the users stats to a txt file
        String statsString = StatsController.getCurrentStatsString();
        String usedWordsString = WordsList.getUsedWordsListString();
        try {
            BufferedWriter saveFile = new BufferedWriter(new FileWriter(SAVE_FILE_NAME));
            saveFile.write(statsString);
            saveFile.write("\n"+usedWordsString);
            saveFile.close();
            // Print a message telling user data was saved
            System.out.println("Saved!");
        } catch (IOException e) {
            System.out.println("Could not write to " + SAVE_FILE_NAME + ".");
        }
        
    }
    public static Boolean loadGame() {
        // If no save exists to load from, print a message 
        File file = new File(SAVE_FILE_NAME);
        if (!file.exists()) {
            System.out.println("No save exists to load from."); return false;
        }
        else System.out.println("Load successful.");
        // Construct the save strings from file
        try {
            BufferedReader saveFile = new BufferedReader(new FileReader(SAVE_FILE_NAME));
            String statsString = saveFile.readLine();
            String usedWordsString = saveFile.readLine();
            saveFile.close();
            // Load the user's data into the stats menu and words list
            StatsController.updateStatsFromStatsString(statsString);
            WordsList.updateWordsListFromString(usedWordsString);
            return true;
        } catch (IOException e) {
            System.out.println("Could not read from " + SAVE_FILE_NAME + ".");
        }
        return false;
    }
}
