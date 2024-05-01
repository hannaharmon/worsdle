package com.example;

// For randomly choosing a word from a list
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

// For reading the words from the words.txt file
import java.io.BufferedReader;
import java.io.FileReader; 
import java.io.IOException;
import java.util.stream.Stream;

public class WordsList {

    final static String WORDS_FILE_NAME = "words.txt";

    // Make these static because they have to stay the same accross rounds (all instances of word list share the same vars)
    static List<String> words;     // Stores the array of usable words
    static ArrayList<String> usedWords = new ArrayList<String>();     // Stores the array of used words
    static ArrayList<String> possibleWords =  new ArrayList<String>();    // Stores the array of possible words (so the player can't enter giberrish)

    public WordsList() {
        // Construct a default words list by adding every possible word to its word list
        // If the file is there, read a file, make a stream of lines, make a stream of words, then finally convert the words stream into a words list
        if (words != null) return;
        try {
            FileReader wordsFile = new FileReader(WORDS_FILE_NAME); // Open file
            System.out.println("Opened file.");
            BufferedReader wordsReader = new BufferedReader(wordsFile); // Create buffered reader (to use lines() method)
            System.out.println("Created BufferedReader.");
            Stream<String> linesStream = wordsReader.lines();   // Make a stream where each element is a line
            Stream<String> wordsStream = linesStream.flatMap(line -> Stream.of(line.split("\\W+"))); // Make a stream where each element is a word
            System.out.println("Created Stream.");
            words = wordsStream.map(String::toUpperCase).collect(Collectors.toList());  // Convert the word stream to a word list
            for (String word : words) possibleWords.add(word);
            wordsReader.close();
        } catch (IOException e) {
            System.out.println("Could not read from " + WORDS_FILE_NAME);
            e.printStackTrace();
        }
    }

    /**
    Gets a random word from the list of usable words and marks it as used by removing it from the usable words list.
    @return a random String from the list of usable words
    */
    public String getWord() {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, words.size()); // gets random int in range [arg0, arg1)
        String wordToUse = words.get(randomIndex);
        System.out.println("Getting word. Already used words are: " + usedWords);
        markWordAsUsed(wordToUse);
        System.out.println("Chosen word: " + wordToUse);
        return wordToUse;
    }

    /**
    Checks if a word is in the list of possible words
    @param String the word to test
    @return a Boolean indicating whether or not the word is possible 
    */
    public Boolean isWordPossible(String word) {
        return possibleWords.contains(word);
    }

    /**
    Marks a word as used by removing it from the usable words list.
    @param wordToMark the word to remove from the list of usable words
    */
    private static void markWordAsUsed(String wordToUse) {
        usedWords.add(wordToUse);
        if (words.contains(wordToUse)) {
            words.remove(words.indexOf(wordToUse));
        }
    }

    public static void updateWordsListFromString(String usedWordsString) {
        words = possibleWords;
        usedWords.clear();
        Scanner usedWordScanner = new Scanner(usedWordsString);
        while (usedWordScanner.hasNext()) {
            markWordAsUsed(usedWordScanner.next());
        }
        usedWordScanner.close();
    }

    public static String getUsedWordsListString() {
        String usedWordsListString = "";
        for (String word : usedWords) {
            usedWordsListString += word + " ";
        }
        return usedWordsListString;
    }

}
