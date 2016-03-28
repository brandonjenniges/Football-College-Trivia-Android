package com.college.football.trivia.Game;

import android.os.Bundle;
import android.os.CountDownTimer;

import com.college.football.trivia.R;

public class StandardActivity extends GameActivity {

    public static ClockTimer counter;
    private static final long interval = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        counter = new ClockTimer(interval * 60 * 2, interval);
        counter.start();
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

    public class ClockTimer extends CountDownTimer {
        public ClockTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            gameText.setText(R.string.clockFinished);
            endGame();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int seconds = (int) (millisUntilFinished / interval) % 60;
            int minutes = (int) ((millisUntilFinished / (interval * 60)) % 60);
            gameText.setText(getString(R.string.clockText, minutes, seconds));
            if(seconds < 10 && minutes == 0) {
                gameText.setTextColor(getQuestionTextColor());
            }
        }
    }
}