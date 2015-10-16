package com.college.football.trivia.game;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import com.college.football.trivia.R;

public class StandardActivity extends GameActivity implements View.OnClickListener {

    public static MyCount counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        counter = new MyCount(120000, 1000);
        counter.start();

    }

    @Override
    public void onClick(final View v) {
        if (!handledClick) {

            if (!((Button) v).getText().toString()
                    .equals(controller.getCurrent_player().getCollege())) {
                gameText.setTextColor(getResources().getColor(R.color.incorrect_guess_color));
                handledClick = true;
                controller.setCurrent_score(controller.getCurrent_score() - 1);
                scoreText.setText(String.valueOf(controller.getCurrent_score()));
                guessMade(v);
                gameText.setTextColor(getResources().getColor(R.color.question_text_color));
            } else {
                handledClick = true;
                super.onClick(v);
            }

        }
    }

    @Override
    protected void onPause() {
        counter.cancel();
        counter.onFinish();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        counter.cancel();
        counter.onFinish();
        super.onBackPressed();
    }

    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            gameText.setText("0:00 ");
            endGame();

        }

        @Override
        public void onTick(long millisUntilFinished) {

            int seconds = (int) (millisUntilFinished / 1000) % 60;
            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);

            if (seconds < 10) {
                gameText.setText(minutes + ":0" + seconds);
            } else {
                gameText.setText(minutes + ":" + seconds);
            }

            if(seconds < 10 && minutes == 0){
                gameText.setTextColor(getResources().getColor(R.color.question_text_color));
            }
        }
    }
}