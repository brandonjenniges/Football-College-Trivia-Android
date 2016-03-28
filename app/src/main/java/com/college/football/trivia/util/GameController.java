package com.college.football.trivia.Util;

import com.college.football.trivia.Model.College;

import java.util.ArrayList;

public class GameController {

    private static GameController INSTANCE;

    private static boolean hadWrongAnswer; // Achievement for getting your first wrong guess
    private static int startStreak; // Achievement that tracks getting so many correct at start of game
    private static int currentStreak; // Tracker for bestStreak
    private static int bestStreak; // Achievement for so many correct in a row
    private static int currentWrongStreak; // Tracker for longestWrongStreak
    private static int longestWrongStreak;  // Achievement for so many wrong in a row

    private static ArrayList<College> tier1;
    private static ArrayList<College> tier2;
    private static ArrayList<College> tier3;

    private GameController() {}

    public static GameController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameController();
        }
        return INSTANCE;
    }

    public static ArrayList<College> getTier2() {
        return tier2;
    }

    public static void setTier2(ArrayList<College> tier2) {
        GameController.tier2 = tier2;
    }

    public static ArrayList<College> getTier1() {
        return tier1;
    }

    public static void setTier1(ArrayList<College> tier1) {
        GameController.tier1 = tier1;
    }

    public static ArrayList<College> getTier3() {
        return tier3;
    }

    public static void setTier3(ArrayList<College> tier3) {
        GameController.tier3 = tier3;
    }

    public static void reset() {
        hadWrongAnswer = false;
        startStreak = 0;
        currentStreak = 0;
        bestStreak = 0;
        currentWrongStreak = 0;
        longestWrongStreak = 0;
    }

    public boolean hadWrongAnswer() {
        return hadWrongAnswer;
    }

    public void setHadWrongAnswer(Boolean b) {
        GameController.hadWrongAnswer = b;
    }

    public int getStartStreak() {
        return startStreak;
    }

    public void increaseStartStreak() {
        GameController.startStreak++;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public void increaseBestStreak() {
        GameController.bestStreak++;
    }

    public int getLongestWrongStreak() {
        return longestWrongStreak;
    }

    public void increaseLongestWrongStreak() {
        GameController.longestWrongStreak++;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void increaseCurrentStreak() {
        GameController.currentStreak++;
    }

    public int getCurrentWrongStreak() {
        return currentWrongStreak;
    }

    public void resetCurrentWorstStreak() {
        GameController.currentWrongStreak = 0;
    }

    public void increaseCurrentWorstStreak() {
        GameController.currentWrongStreak++;
    }

    public void resetCurrentStreak() {
        GameController.currentStreak = 0;
    }
}
