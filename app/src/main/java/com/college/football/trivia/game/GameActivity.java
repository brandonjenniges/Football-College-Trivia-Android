package com.college.football.trivia.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.college.football.trivia.FootballTriviaApplication;
import com.college.football.trivia.R;
import com.college.football.trivia.ResultsActivity;
import com.college.football.trivia.model.College;
import com.college.football.trivia.model.Player;
import com.college.football.trivia.util.GameController;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    protected Toolbar toolbar;

    private TextView playerText;
    private TextView teamText;
    protected TextView scoreText;
    protected TextView gameText;
    protected TextView highScoreText;

    protected Button choice1;
    protected Button choice2;
    protected Button choice3;
    protected Button choice4;

    private List<ArrayList<College>> tierArray = new ArrayList<>();

    private Random r;
    private ArrayList<Player> questions;

    private ArrayList<Button> buttons = new ArrayList<>();

    private ArrayList<Integer> choices = new ArrayList<>();
    private String[] sortedChoices = new String[4];

    protected boolean handledClick;
    private Button correctButton;
    private boolean canGuess;

    protected GameController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = GameController.getInstance();
        initializeView();

        ArrayList<College> tier1 = GameController.getTier1();
        ArrayList<College> tier2 = GameController.getTier2();
        ArrayList<College> tier3 = GameController.getTier3();
        tierArray.add(tier1);
        tierArray.add(tier2);
        tierArray.add(tier3);

        getQuestions();

        r = new Random();

        setQuestion();

        FootballTriviaApplication application = (FootballTriviaApplication) getApplication();
        if (application.getDefaultTracker() != null) {
            Tracker mTracker = application.getDefaultTracker();
            mTracker.setScreenName(this.getLocalClassName());
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
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


    @Override
    public void onClick(View v) {
        if (canGuess) {
            canGuess = false;
            guessMade(v);
        }
    }

    /**
     * Method called when a guess is made, to see if guess was correct
     * <p/>
     * v - button clicked on guess attempt made
     */
    public void guessMade(final View v) {
        if (((Button) v).getText().toString()
                .equals(controller.getCurrent_player().getCollege())) {
            correctAnswer();
            YoYo.with(Techniques.Pulse).duration(400).playOn(v);
            ((Button) v).setTextColor(getResources().getColor(R.color.correct_guess_color));


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((Button) v).setTextColor(getResources().getColor(R.color.dark_text_color));
                    setQuestion();

                }
            }, 400);

        } else {
            wrongAnswer();
            YoYo.with(Techniques.Shake).duration(1000).playOn(v);
            YoYo.with(Techniques.Pulse).duration(1000).playOn(correctButton);
            ((Button) v).setTextColor(getResources().getColor(R.color.incorrect_guess_color));
            correctButton.setTextColor(getResources().getColor(R.color.correct_guess_color));
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((Button) v).setTextColor(getResources().getColor(R.color.dark_text_color));
                    correctButton.setTextColor(getResources().getColor(R.color.dark_text_color));
                    setQuestion();

                }
            }, 1000);
        }

    }

    protected void correctAnswer() {
        controller.addCurrent_score();
        if (!controller.getWrong_answer()) {
            controller.incrementStart_streak();
        }
        controller.incrementCurrent_streak();
        if (controller.getCurrent_streak() > controller.getBest_streak()) {
            controller.incrementBest_streak();
        }
        controller.resetCurrent_worst();
        scoreText.setText(controller.getCurrent_score() + "");
    }

    protected void wrongAnswer() {
        controller.setWrong_answer(true);
        controller.resetCurrent_streak();
        controller.incrementCurrent_worst();

        if (controller.getCurrent_worst() > controller.getWorst_streak()) {
            controller.incrementWorst_streak();
        }
    }

    public void initializeView() {
        setContentView(R.layout.game);

        playerText = (TextView) findViewById(R.id.playerText);
        teamText = (TextView) findViewById(R.id.teamText);
        gameText = (TextView) findViewById(R.id.gameValueText);
        scoreText = (TextView) findViewById(R.id.currentScoreText);
        highScoreText = (TextView) findViewById(R.id.highScoreText);

        choice1 = (Button) findViewById(R.id.choice1);
        choice2 = (Button) findViewById(R.id.choice2);
        choice3 = (Button) findViewById(R.id.choice3);
        choice4 = (Button) findViewById(R.id.choice4);

        buttons.add(choice1);
        buttons.add(choice2);
        buttons.add(choice3);
        buttons.add(choice4);

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setOnClickListener(this);
        }

        controller.setPlaying(true);
        controller.setCurrent_score(0);
        controller.setWrong_answer(false);
        controller.resetStart_streak();
        controller.resetCurrent_streak();
        controller.resetCurrent_worst();
        controller.resetBest_streak();
        controller.resetWorst_streak();
        controller.setProcess_postData(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int bestScore = prefs.getInt("FootballScore" + ((controller
                .getCurrent_mode() - 1) * 4)
                + controller.getCurrent_diff(), 0);

        controller
                .setMod_dif_high(bestScore);


        scoreText.setText(String.valueOf(controller.getCurrent_score()));
        highScoreText.setText(String.valueOf(controller.getMod_dif_high()));
        gameText.setText("");
    }

    /**
     * Get a list of all the questions for game mode
     */
    public void getQuestions() {
        questions = GameController.getGamePlayers();
        Log.d("QUESTION Count", "" + questions.size());
    }

    public void fillChoices() {
        for (int i = 0; i < questions.size(); i++) {
            choices.add(i);
        }
    }

    /**
     * Sets question to view
     */
    public void setQuestion() {
        canGuess = true;

        if (choices.size() == 0) {
            fillChoices();
        }

        int l = r.nextInt(choices.size());
        controller.setCurrent_player(questions.get(choices.get(l)));
        choices.remove(l);

        Log.d("TAG", choices.size() + ", " + l);

        playerText.setText(controller.getCurrent_player().getFirst_name() + " "
                + controller.getCurrent_player().getLast_name() + " "
                        + controller.getCurrent_player().getPosition() +" #"
                + controller.getCurrent_player().getJersey_num()
                );
        teamText.setText(controller.getCurrent_player().getTeam());

        getAnswers();
        sortAnswers();
        displayAnswers();

    }

    public void getAnswers() {
        for (int i = 0; i < sortedChoices.length; i++) {
            sortedChoices[i] = "";
        }
        sortedChoices[0] = controller.getCurrent_player().getCollege();
        String college;
        int temp;

        for (int i = 1; i <= 3; i++) {
            do {
                temp = r.nextInt(tierArray.get(
                        controller.getCurrent_player().getCollege_tier() - 1)
                        .size());
                college = tierArray
                        .get(controller.getCurrent_player().getCollege_tier() - 1)
                        .get(temp).getName();
            }
            while (sortedChoices[0].equals(college) || sortedChoices[1].equals(college) || sortedChoices[2].equals(college) || sortedChoices[3].equals(college));
            sortedChoices[i] = college;
        }

    }

    public void sortAnswers() {
        Arrays.sort(sortedChoices);
    }

    public void displayAnswers() {
        int i = 0;
        for (String answer : sortedChoices) {
            if (answer.equals(controller.getCurrent_player().getCollege())) {
                correctButton = buttons.get(i);
            }
            buttons.get(i).setText(answer);
            i++;
        }

        handledClick = false;
    }

    /**
     * Game is over, end game and go to Results screen
     */
    public void endGame() {
        Intent i = new Intent(this, ResultsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}