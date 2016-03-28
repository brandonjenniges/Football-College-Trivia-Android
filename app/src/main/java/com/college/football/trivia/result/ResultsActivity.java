package com.college.football.trivia.Result;

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

import com.college.football.trivia.BaseActivity;
import com.college.football.trivia.BuildConfig;
import com.college.football.trivia.Game.PracticeActivity;
import com.college.football.trivia.Game.StandardActivity;
import com.college.football.trivia.Game.SurvivalActivity;
import com.college.football.trivia.LeaderboardActivity;
import com.college.football.trivia.Model.Game;
import com.college.football.trivia.R;
import com.college.football.trivia.Util.Constants;
import com.college.football.trivia.Util.GameController;
import com.google.android.gms.games.Games;


public class ResultsActivity extends BaseActivity implements View.OnClickListener {
    private TextView resultScore;

    protected GameController controller;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        game = getIntent().getParcelableExtra(Game.EXTRA_KEY);
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
        Game.Mode gameMode = game.getMode();
        if (gameMode == Game.Mode.Standard || gameMode == Game.Mode.Survival) {
            Games.Leaderboards.submitScore(mGoogleApiClient,
                    Constants.leaderboards[Game.intForMode(gameMode)][Game.intForDifficulty(game.getDifficulty())], controller.getCurrent_score());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playAgainButton:
                startGame();
                finish();
                break;
        }
    }

    private void startGame() {
        Intent intent;
        if (game.getMode() == Game.Mode.Standard) {
            intent = new Intent(this, StandardActivity.class);
        } else if (game.getMode() == Game.Mode.Survival) {
            intent = new Intent(this, SurvivalActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, PracticeActivity.class);
        }
        intent.putExtra(Game.EXTRA_KEY, game);
        startActivity(intent);
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

        String highScoreKey = game.getHighScoreKey();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int bestScore = prefs.getInt(highScoreKey, 0);


        if(controller.getCurrent_score() > bestScore){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(highScoreKey, controller.getCurrent_score());
            editor.commit();
        }
    }
}

