package com.college.football.trivia.Game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.college.football.trivia.Model.Game;
import com.college.football.trivia.Model.Player;
import com.college.football.trivia.R;
import com.college.football.trivia.Result.ResultsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity implements GameView {

    protected Toolbar toolbar;
    protected GamePresenter presenter;
    @Bind(R.id.playerText) TextView playerText;
    @Bind(R.id.teamText) TextView teamText;
    @Bind(R.id.currentScoreText) TextView scoreText;
    @Bind(R.id.gameValueText) TextView gameText;
    @Bind(R.id.highScoreText) TextView highScoreText;
    @Bind(R.id.choice1) Button choice1;
    @Bind(R.id.choice2) Button choice2;
    @Bind(R.id.choice3) Button choice3;
    @Bind(R.id.choice4) Button choice4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        ButterKnife.bind(this);

        Game game = getIntent().getParcelableExtra(Game.EXTRA_KEY);
        presenter = new GamePresenter(this, game);
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
                return true;
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

    @Override
    public void endGame() {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(Game.EXTRA_KEY, presenter.getGame());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public Button[] getButtons() {
        return new Button[]{choice1, choice2, choice3, choice4};
    }

    @Override
    public int getHighScore() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt(presenter.getGame().getHighScoreKey(), 0);
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
        playerText.setText(getString(R.string.question_string, player.getFirst_name(), player.getLast_name(), player.getPosition(), player.getJersey_num()));
        teamText.setText(player.getTeam());
    }

    @Override
    public int getWrongGuessTextColor() {
        return ContextCompat.getColor(this, R.color.incorrect_guess_color);
    }

    @Override
    public int getCorrectGuessTextColor() {
        return ContextCompat.getColor(this, R.color.correct_guess_color);
    }

    @Override
    public int getRegularTextColor() {
        return ContextCompat.getColor(this, R.color.dark_text_color);
    }

    @Override
    public int getQuestionTextColor() {
        return ContextCompat.getColor(this, R.color.question_text_color);
    }

    @Override
    public TextView getGameTextView() {
        return gameText;
    }

}