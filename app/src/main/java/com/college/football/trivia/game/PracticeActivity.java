package com.college.football.trivia.Game;

import android.os.Bundle;

public class PracticeActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameText.setText("Practice");
    }
}