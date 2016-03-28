package com.college.football.trivia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.college.football.trivia.Util.Constants;
import com.college.football.trivia.Util.GameController;
import com.google.android.gms.games.Games;


public class LeaderboardActivity extends BaseActivity implements View.OnClickListener {
    protected GameController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = GameController.getInstance();
        initializeView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_leaderboard_screen, menu);
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

    @Override
    public void onClick(View v) {
        showLeaderboard(v);
    }

    public void initializeView() {
        setContentView(R.layout.activity_leaderboard);
    }

    public void showLeaderboard(View view) {
        if (BuildConfig.DEBUG) {
            return;
        }

        if (mGoogleApiClient.isConnected()) {
            int tagValue = Integer.parseInt((String) view.getTag());
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, Constants.leaderboards[tagValue < 4 ? 0 : 1][tagValue < 4 ? tagValue : tagValue - 4]), 4);
        } else {
            mGoogleApiClient.connect();
        }
    }
}

