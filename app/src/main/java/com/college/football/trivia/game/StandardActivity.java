package com.college.football.trivia.Game;

import android.os.Bundle;
import android.os.CountDownTimer;

import com.college.football.trivia.R;

public class StandardActivity extends GameActivity {

    public static MyCount counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        counter = new MyCount(120000, 1000);
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

    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            gameText.setText("0:00");
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

            if(seconds < 10 && minutes == 0) {
                gameText.setTextColor(getQuestionTextColor());
            }
        }
    }
}