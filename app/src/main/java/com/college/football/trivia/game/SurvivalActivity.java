package com.college.football.trivia.Game;

import android.os.Bundle;

import com.college.football.trivia.R;

public class SurvivalActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameText.setTextColor(getResources().getColor(R.color.incorrect_guess_color));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

}