package com.college.football.trivia.Game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.college.football.trivia.Model.Player;
import com.college.football.trivia.R;
import com.college.football.trivia.Result.ResultsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity implements GameView {

    protected Toolbar toolbar;

    @Bind(R.id.playerText) TextView playerText;
    @Bind(R.id.teamText) TextView teamText;
    @Bind(R.id.currentScoreText) TextView scoreText;
    @Bind(R.id.gameValueText) TextView gameText;
    @Bind(R.id.highScoreText) TextView highScoreText;

    @Bind(R.id.choice1) Button choice1;
    @Bind(R.id.choice2) Button choice2;
    @Bind(R.id.choice3) Button choice3;
    @Bind(R.id.choice4) Button choice4;

    protected GamePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        ButterKnife.bind(this);
        presenter = new GamePresenter(this);
        presenter.setup();
    }

    @Override
    protected void onStart() {
        super.onStart();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @Override
      public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_menu:
                finish();
                return  true;
            case R.id.action_quit:
                endGame();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("unused")
    @OnClick({R.id.choice1, R.id.choice2, R.id.choice3, R.id.choice4})
    public void onClick(Button button) {
        presenter.guessMade(button);
    }

    /**
     * Game is over, end game and go to Results screen
     */
    @Override
    public void endGame() {
        Intent i = new Intent(this, ResultsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public Button[] getButtons() {
        return new Button[] { choice1, choice2, choice3, choice4 };
    }

    @Override
    public int getHighScore() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int bestScore = prefs.getInt("FootballScore" + ((presenter.getController()
                .getCurrent_mode() - 1) * 4)
                + presenter.getController().getCurrent_diff(), 0);
        return bestScore;
    }

    @Override
    public void setScoreText(String text) {
        scoreText.setText(text);
    }

    @Override
    public void setHighScoreText(String text) {
        highScoreText.setText(text);
    }

    @Override
    public void setGameText(String text) {
        gameText.setText(text);
    }

    @Override
    public void setQuestionText(Player player) {
        playerText.setText(player.getFirst_name() + " "
                        + player.getLast_name() + " "
                        + player.getPosition() + " #"
                        + player.getJersey_num()
        );
        teamText.setText(player.getTeam());
    }

    @Override
    public int getWrongGuessTextColor() {
        return getResources().getColor(R.color.incorrect_guess_color);
    }

    @Override
    public int getCorrectGuessTextColor() {
        return getResources().getColor(R.color.correct_guess_color);
    }

    @Override
    public int getRegularTextColor() {
        return getResources().getColor(R.color.dark_text_color);
    }

    @Override
    public int getQuestionTextColor() {
        return getResources().getColor(R.color.question_text_color);
    }

    @Override
    public TextView getGameTextView() {
        return gameText;
    }

}