package com.college.football.trivia.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.college.football.trivia.Model.Game;

public class ScoreManager {

    public static int getHighScore(Game game, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(game.getHighScoreKey(), 0);
    }

    public static int getHighScore(Game.Mode mode, Game.Difficulty difficulty, Context context) {
        return getHighScore(new Game(mode, difficulty, 0), context);
    }

    public static void saveHighScore(Game game, Context context) {
        String highScoreKey = game.getHighScoreKey();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int bestScore = prefs.getInt(highScoreKey, 0);
        int score = game.getScore();

        if (score > bestScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(highScoreKey, score);
            editor.apply();
        }
    }
}