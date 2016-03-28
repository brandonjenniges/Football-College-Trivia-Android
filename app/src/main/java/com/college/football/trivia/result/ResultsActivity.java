package com.college.football.trivia.Result;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.college.football.trivia.Util.ScoreManager;
import com.google.android.gms.games.Games;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultsActivity extends BaseActivity {
    protected GameController controller;
    @Bind (R.id.result_score) TextView resultScore;
    private Game game;
    private boolean postedGMSData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view);
        ButterKnife.bind(this);

        game = getIntent().getParcelableExtra(Game.EXTRA_KEY);
        resultScore.setText(getString(R.string.scoreDisplay, game.getScore()));
        controller = GameController.getInstance();

        saveLocalScore();

        if (!BuildConfig.DEBUG) {
            if (mGoogleApiClient.isConnected()) {
                processAchievements();
                processLeaderBoard();
                postedGMSData = true;
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
                return true;
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
        int score = game.getScore();

        if (score > 0) {
            Games.Achievements.unlock(mGoogleApiClient, Constants.achievement_boom);
            // Score level achievements
            Games.Achievements.increment(mGoogleApiClient, Constants.achievement_just_getting_started, score);
            Games.Achievements.increment(mGoogleApiClient, Constants.achievement_established_starter, score);
            Games.Achievements.increment(mGoogleApiClient, Constants.achievement_seasoned_pro, score);
            Games.Achievements.increment(mGoogleApiClient, Constants.achievement_grizzly_veteran, score);
            Games.Achievements.increment(mGoogleApiClient, Constants.achievement_gridiron_legend, score);
            Games.Achievements.increment(mGoogleApiClient, Constants.achievement_never_gonna_give_your_up, score);
        }

        if (controller.hadWrongAnswer()) {
            Games.Achievements.unlock(mGoogleApiClient, Constants.achievement_oops);
        }

        if (controller.getBestStreak() >= 3) {
            Games.Achievements.unlock(mGoogleApiClient, Constants.achievement_tictactoe);
        }

        if (controller.getLongestWrongStreak() >= 3) {
            Games.Achievements.unlock(mGoogleApiClient, Constants.achievement_tictacouch);
        }

        if (controller.getStartStreak() >= 10) {
            Games.Achievements.unlock(mGoogleApiClient, Constants.achievement_jumping_the_snap_count);
        }
    }

    public void processLeaderBoard() {
        Game.Mode gameMode = game.getMode();
        if (gameMode == Game.Mode.Standard || gameMode == Game.Mode.Survival) {
            Games.Leaderboards.submitScore(mGoogleApiClient,
                    Constants.leaderboards[Game.intForMode(gameMode)][Game.intForDifficulty(game.getDifficulty())], game.getScore());
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.playAgainButton)
    public void playAgain(Button button) {
        startGame();
        finish();
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
        intent.putExtra(Game.EXTRA_KEY, game.reset());
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        if (!postedGMSData) {
            processAchievements();
            processLeaderBoard();
            postedGMSData = true;
        }
    }

    public void saveLocalScore() {
        int score = game.getScore();
        if (score > game.getHighScore()) {
            game.setHighScore(score);
            ScoreManager.saveHighScore(game, this);
        }
    }
}