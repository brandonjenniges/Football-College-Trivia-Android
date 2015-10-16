package com.college.football.trivia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.college.football.trivia.game.PracticeActivity;
import com.college.football.trivia.game.StandardActivity;
import com.college.football.trivia.game.SurvivalActivity;
import com.college.football.trivia.util.Constants;
import com.college.football.trivia.util.GameController;
import com.google.android.gms.games.Games;


public class ResultsActivity extends BaseActivity implements View.OnClickListener {
    private TextView resultScore;

    protected GameController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        controller = GameController.getInstance();
        initializeView();

        saveLocalScore();

        if (!BuildConfig.DEBUG) {
            if (mGoogleApiClient.isConnected()) {
                processAchievements();
                processLeaderboard();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_results_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_rate:
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse(Constants.app_url)));
                return  true;
            case R.id.action_leaderboard:
                if (BuildConfig.DEBUG || mGoogleApiClient.isConnected()) {
                    startActivity(new Intent(getApplicationContext(),
                            LeaderboardActivity.class));
                } else {
                    mGoogleApiClient.connect();
                }
                return true;
            case R.id.action_achievements:
                if (BuildConfig.DEBUG) {
                    return true;
                }

                if (mGoogleApiClient.isConnected()) {
                    startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                            5);
                } else {
                    mGoogleApiClient.connect();
                }
                return true;
            /*
            case R.id.action_about:
                Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                return true;
                */
        }

        return super.onOptionsItemSelected(item);
    }

    public void processAchievements() {

        if (controller.getCurrent_score() > 0) {
            Games.Achievements.unlock(mGoogleApiClient, Constants.achievement_boom);
            Games.Achievements.increment(mGoogleApiClient,
                            Constants.achievement_just_getting_started,
                    controller.getCurrent_score());
            Games.Achievements.increment(mGoogleApiClient,
                            Constants.achievement_established_starter,
                    controller.getCurrent_score());

            Games.Achievements.increment(mGoogleApiClient,
                            Constants.achievement_seasoned_pro,
                    controller.getCurrent_score());


            Games.Achievements.increment(mGoogleApiClient,
                    Constants.achievement_grizzly_veteran,
                    controller.getCurrent_score());

            Games.Achievements.increment(mGoogleApiClient,
                    Constants.achievement_gridiron_legend,
                    controller.getCurrent_score());

            Games.Achievements.increment(mGoogleApiClient,
                            Constants.achievement_never_gonna_give_your_up,
                    controller.getCurrent_score());
        }

        if (controller.getWrong_answer()) {
            Games.Achievements.unlock(mGoogleApiClient, Constants.achievement_oops);
        }

        if (controller.getBest_streak() >= 3) {
            Games.Achievements.unlock(mGoogleApiClient, Constants.achievement_tictactoe);
        }

        if (controller.getWorst_streak() >= 3) {
            Games.Achievements.unlock(mGoogleApiClient, Constants.achievement_tictacouch);
        }

        if (controller.getStart_streak() >= 10) {
            Games.Achievements.unlock(mGoogleApiClient, Constants.achievement_jumping_the_snap_count);
        }
    }

    public void processLeaderboard() {
        if (controller.getCurrent_mode() > 0
                && controller.getCurrent_mode() < 3) {
            Games.Leaderboards.submitScore(mGoogleApiClient,
                                    Constants.leaderboards[controller.getCurrent_mode() - 1][controller.getCurrent_diff() - 1], controller.getCurrent_score());

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playAgainButton:
                startGame(controller.getCurrent_mode());
                finish();
                break;
        }
    }

    /**
     * Starts Game based on mode stored in GameController
     *
     * @param i
     *            Game mode referenced in Constants.java
     */
    private void startGame(int i) {
        if (i == 1) {
            Intent intent = new Intent(this, StandardActivity.class);
            startActivity(intent);
        } else if (i == 2) {
            Intent intent = new Intent(this, SurvivalActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PracticeActivity.class);
            startActivity(intent);
        }
        finish();
    }

    public void initializeView() {

        setContentView(R.layout.result_view);

        controller.setPlaying(false);

        resultScore = (TextView) findViewById(R.id.result_score);
        Button playAgain = (Button) findViewById(R.id.playAgainButton);

        playAgain.setOnClickListener(this);

        resultScore.setText("Score: " + controller.getCurrent_score());

    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        if (controller.getProcess_postData()) {
            processAchievements();
            processLeaderboard();
        }
        controller.setProcess_postData(false);

    }

    public void saveLocalScore() {
        resultScore.setText("Score: " + controller.getCurrent_score());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int bestScore = prefs.getInt("FootballScore" + ((controller
                .getCurrent_mode() - 1) * 4)
                + controller.getCurrent_diff(),0);


        if(controller.getCurrent_score() > bestScore){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("FootballScore" + ((controller
                    .getCurrent_mode() - 1) * 4)
                    + controller.getCurrent_diff(), controller.getCurrent_score());
            editor.commit();
        }
    }
}

