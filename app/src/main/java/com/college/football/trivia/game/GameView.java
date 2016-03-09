package com.college.football.trivia.Game;

import android.widget.Button;
import android.widget.TextView;

import com.college.football.trivia.Model.Player;

interface GameView {
    Button[] getButtons();

    int getHighScore();

    void setScoreText(String text);
    void setHighScoreText(String text);
    void setGameText(String text);

    void setQuestionText(Player current_player);

    int getWrongGuessTextColor();
    int getCorrectGuessTextColor();
    int getRegularTextColor();
    int getQuestionTextColor();

    TextView getGameTextView();

    void endGame();
}