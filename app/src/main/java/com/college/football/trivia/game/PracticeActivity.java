package com.college.football.trivia.Game;

import android.os.Bundle;

import com.college.football.trivia.R;

public class PracticeActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameText.setText(R.string.practice_game);
    }
}