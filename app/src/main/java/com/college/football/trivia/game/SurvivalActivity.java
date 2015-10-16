package com.college.football.trivia.game;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.college.football.trivia.R;

public class SurvivalActivity extends GameActivity implements View.OnClickListener {

    private int strikes = 0;
    private boolean freePause = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        gameText.setTextColor(getResources().getColor(R.color.incorrect_guess_color));
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString()
                .equals(controller.getCurrent_player().getCollege())) {
            updateStrikes();
        }
        super.onClick(v);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (freePause) {
            freePause = false;
        } else {
            updateStrikes();
        }
    }

    public void updateStrikes() {
        strikes++;
        if (strikes == 3) {
            endGame();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strikes; i++) {
            sb.append("X ");
        }

        gameText.setText(sb.toString());
    }
}