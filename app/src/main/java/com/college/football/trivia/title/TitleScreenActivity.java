package com.college.football.trivia.title;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.college.football.trivia.BaseActivity;
import com.college.football.trivia.BuildConfig;
import com.college.football.trivia.LeaderboardActivity;
import com.college.football.trivia.R;
import com.college.football.trivia.util.Constants;
import com.google.android.gms.games.Games;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class TitleScreenActivity extends BaseActivity implements TitleScreenView {


    private TitleScreenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
        ButterKnife.bind(this);
        presenter = new TitleScreenPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_title_screen, menu);
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

    @SuppressWarnings("unused")
    @OnClick({R.id.standardGame, R.id.survivalGame, R.id.practiceGame})
    public void startGame(Button button) {
        int id = button.getId();
        presenter.startGame(id);
    }

}
