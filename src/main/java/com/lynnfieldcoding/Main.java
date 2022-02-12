package com.lynnfieldcoding;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SplittableRandom;

public class Main {

    // a list of possible characters, they get removed as they are guessed
    private static final List<Character> possibleCharacters = new ArrayList<>();
    // scanner that can read console input
    private static final Scanner scanner = new Scanner(System.in);

    private static final SplittableRandom RANDOM = new SplittableRandom();

    // uninitialized array of guessed characters
    static char[] correctCharacters;

    // this is the start method for every Java program
    public static void main(String[] args) {
        populateGuessingCharacters();
        start();
    }

    // this sends the welcome message, and asks for the length of the word
    private static void start() {
        String welcomeMessage = "Welcome to the guessing game!\n" +
                "Here are the instructions: " +
                "I will ask you questions, and you must answer honestly, " +
                "or else the game won't be fun for either of us.";
        System.out.println(welcomeMessage);
        System.out.println("How many characters is your word?");
        int length = scanner.nextInt();
        // initializes array with the length of the word
        correctCharacters = new char[length];
        // calls the startGuessing method
        startGuessing();
    }

    // this sends the message that the console will begin guessing, and then begins the guessing
    private static void startGuessing() {
        System.out.println("I will now begin my guesses!");
        guess();
    }

    private static void guess() {
        // if the list of possible characters is 0, no more characters can be guessed
        if (possibleCharacters.size() == 0) {
            checkGuessAnswer();
            return;
        }

        // calls the overloaded method with the random char
        guess(getRandomChar());
    }

    // You may have noticed there is already a method called guess
    // this is allowed because this method has different parameters than the first method called guess
    // this method sends the message asking if the word has a character, then checks it
    private static void guess(char randomChar) {
        System.out.println("Does your word have a(n) " + randomChar + "? (y/n)");
        checkGuess(scanner.next(), randomChar);
    }


    private static void checkGuess(String input, char guessed) {
        // strings should be compared with the method .equals()
        if (input.equalsIgnoreCase("y")) {
            // checks if the letter appears in the word multiple times
            if (!checkMultiplePositions(guessed)) {
                System.out.println("I knew it! What is the index of the letter? (1 - " + correctCharacters.length + ")");
                // index of arrays start at 0
                int index = scanner.nextInt() - 1;
                // sets the correct character in the array
                correctCharacters[index] = guessed;
                // removes the character from characters that can be guessed, because it can't be guessed twice
                possibleCharacters.remove((Character) guessed);
            }

            System.out.println("The current word is: " + String.valueOf(correctCharacters));
            // checks if the word is correct
            checkWord();
        } else if (input.equalsIgnoreCase("n")) {
            System.out.println("I knew that wasn't correct, I just felt bad for you.");
            possibleCharacters.remove((Character) guessed);
            // calls the guess method again
            guess();
        } else {
            System.out.println("That input was not correct, please try again.");
            // calls the overloaded guess method with the current guess, because the input was incorrect
            guess(guessed);
        }
    }

    // checks if the letter appears in the word multiple times
    // it returns true if there are multiple indices, false if not
    private static boolean checkMultiplePositions(char guess) {
        System.out.println("Does your letter have multiple indices? (y/n)");
        String input = scanner.next();
        if (input.equalsIgnoreCase("y")) {
            System.out.println("Please list the indices (1 - " + correctCharacters.length + "). Type -1 to exit.");
            int index;
            // while the index of the letter is not -1, it should keep adding it to the correct word
            while ((index = scanner.nextInt()) != -1) {
                if (index >= correctCharacters.length) {
                    System.out.println("That index is too large! It cannot be greater than " +
                            (correctCharacters.length - 1));
                    // goes back to the top of the loop
                    continue;
                }
                correctCharacters[index - 1] = guess;
                possibleCharacters.remove((Character) guess);
            }
            System.out.println("Thanks for all the letters.");
            return true;
        } else if (input.equalsIgnoreCase("n")) {
            return false;
        } else {
            System.out.println("That input is invalid!");
            return checkMultiplePositions(guess);
        }
    }

    private static void checkWord() {
        // enhanced for loop, iterates over all characters in the array
        for (char c : correctCharacters) {
            // checks if the character is the null character, if it is, not all the letters
            // have been guessed
            if (c == '\u0000') {
                guess();
                // returns in a void method exit the method, no value is actually returned
                return;
            }
        }
        // if the method is not returned out of, the word must have been guessed
        checkGuessAnswer();
    }

    // this checks if the guess was correct
    private static void checkGuessAnswer() {
        String guess = String.valueOf(correctCharacters);
        System.out.println("Is your word " + guess + "? (y/n)");
        String input = scanner.next();
        if (input.equalsIgnoreCase("y")) {
            System.out.println("I knew it!");
           checkPlayAgain();
        } else if (input.equalsIgnoreCase("n")) {
            // if the word was guessed incorrectly, that means the player input something wrong
            // at some point in the program
            System.out.println("You must have messed something up, check your word again!");
            checkPlayAgain();
        } else {
            System.out.println("That input is invalid, it should be (y/n)");
            checkGuessAnswer();
        }
    }

    // checks if the player wants to play again
    private static void checkPlayAgain() {
        System.out.println("If you would like to start a game, type \"play again\". " +
                "If you would like to exit, type \"exit\".");
        String playAgain = scanner.next();
        if (playAgain.equalsIgnoreCase("play again")) {
            // resets the game
            correctCharacters = null;
            possibleCharacters.clear();
            start();
        }
    }

    // returns a random character from the list of possible characters
    private static char getRandomChar() {
        return possibleCharacters.get(RANDOM.nextInt(0, possibleCharacters.size()));
    }

    // adds letters from a-z to the possible letters
    private static void populateGuessingCharacters() {
        // fills the possible guesses list
        // this may be confusing, because I said before that the type must match the value
        // when declaring the variable
        // in Java, characters can also be integers
        for (int i = 'a'; i <= 'z'; i++) {
            possibleCharacters.add((char) i);
        }
    }
}
