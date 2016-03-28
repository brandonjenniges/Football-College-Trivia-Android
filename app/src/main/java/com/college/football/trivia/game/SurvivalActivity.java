package com.college.football.trivia.Game;

import android.os.Bundle;

public class SurvivalActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameText.setTextColor(getWrongGuessTextColor());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }
}