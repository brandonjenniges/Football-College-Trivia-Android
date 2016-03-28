package com.college.football.trivia.Util;

import com.college.football.trivia.Model.College;
import com.college.football.trivia.Model.Player;

import java.util.ArrayList;

public class GameController {

    private static GameController INSTANCE;

    private static int mod_dif_high;
    private static int current_score;
    private static boolean wrong_answer;
    private static int start_streak;
    private static int best_streak;
    private static int worst_streak;
    private static int current_streak;
    private static int current_worst;
    private static boolean process_post_data;
    private static Player current_player;
    private static boolean playing;

    private static ArrayList<College> tier1;
    private static ArrayList<College> tier2;
    private static ArrayList<College> tier3;

    private GameController() {
    }

    public static GameController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameController();
        }

        return INSTANCE;
    }

    public static ArrayList<Player> getGamePlayers() {
        return new QuestionLoader().easyQuestions();
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

    public int getMod_dif_high() {
        return mod_dif_high;
    }

    public void setMod_dif_high(int mod_dif_high) {
        GameController.mod_dif_high = mod_dif_high;
    }

    public Player getCurrent_player() {
        return current_player;
    }

    public void setCurrent_player(Player current_player) {
        GameController.current_player = current_player;
    }

    public int getCurrent_score() {
        return current_score;
    }

    public void setCurrent_score(int current_score) {
        GameController.current_score = current_score;
    }

    public void addCurrent_score() {
        GameController.current_score++;
    }

    public boolean getWrong_answer() {
        return wrong_answer;
    }

    public void setWrong_answer(Boolean b) {
        GameController.wrong_answer = b;
    }

    public void resetStart_streak() {
        GameController.start_streak = 0;
    }

    public int getStart_streak() {
        return start_streak;
    }

    public void incrementStart_streak() {
        GameController.start_streak++;
    }

    public void resetBest_streak() {
        GameController.best_streak = 0;
    }

    public int getBest_streak() {
        return best_streak;
    }

    public void incrementBest_streak() {
        GameController.best_streak++;
    }

    public void resetWorst_streak() {
        GameController.worst_streak = 0;
    }

    public int getWorst_streak() {
        return worst_streak;
    }

    public void incrementWorst_streak() {
        GameController.worst_streak++;
    }

    public int getCurrent_streak() {
        return current_streak;
    }

    public void resetCurrent_streak() {
        GameController.current_streak = 0;
    }

    public void incrementCurrent_streak() {
        GameController.current_streak++;
    }

    public int getCurrent_worst() {
        return current_worst;
    }

    public void resetCurrent_worst() {
        GameController.current_worst = 0;
    }

    public void incrementCurrent_worst() {
        GameController.current_worst++;
    }

    public boolean getProcess_postData() {
        return GameController.process_post_data;
    }

    public void setProcess_postData(boolean process_post_data) {
        GameController.process_post_data = process_post_data;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        GameController.playing = playing;
    }
}
