package com.college.football.trivia.game;

import android.os.Bundle;
import android.view.View;

public class PracticeActivity extends GameActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameText.setText("Practice");
    }
}