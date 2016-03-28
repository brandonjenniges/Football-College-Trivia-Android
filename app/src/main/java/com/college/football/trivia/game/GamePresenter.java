package com.college.football.trivia.Game;

import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.college.football.trivia.Model.College;
import com.college.football.trivia.Model.Game;
import com.college.football.trivia.Model.Player;
import com.college.football.trivia.Util.GameController;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GamePresenter {
    protected GameView view;
    protected GameController controller;

    private Button[] buttons = new Button[4];

    private List<ArrayList<College>> tierArray = new ArrayList<>();
    private Random r;
    private Player[] questions;

    private ArrayList<Integer> choices = new ArrayList<>();
    private String[] sortedChoices = new String[4];

    protected boolean handledClick;
    private Button correctButton;
    private boolean canGuess;

    private int strikes = 0;
    private boolean freePause = true;

    private Game game;

    public GamePresenter(GameView view, Game game) {
        this.view = view;
        this.game = game;
    }

    public void setup() {
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
    }

    public void onResume() {
        if (freePause) {
            freePause = false;
        } else {
            updateStrikes();
        }
    }

    public void initializeView() {
        buttons = view.getButtons();

        controller.setPlaying(true);
        controller.setCurrent_score(0);
        controller.setWrong_answer(false);
        controller.resetStart_streak();
        controller.resetCurrent_streak();
        controller.resetCurrent_worst();
        controller.resetBest_streak();
        controller.resetWorst_streak();
        controller.setProcess_postData(true);

        int bestScore = view.getHighScore();

        controller.setMod_dif_high(bestScore);

        view.setScoreText(String.valueOf(this.getController().getCurrent_score()));
        view.setHighScoreText(String.valueOf(this.getController().getMod_dif_high()));
        view.setGameText("");
    }

    public GameController getController() {
        return controller;
    }

    public void checkGuess(Button button) {
        if (canGuess) {
            canGuess = false;
            handleGuess(button);
        }
    }

    /**
     * Method called when a guess is made, to see if guess was correct
     * <p/>
     * v - button clicked on guess attempt made
     */
    public void guessMade(final Button button) {
        if (game.getMode() == Game.Mode.Survival) {
            handleSurvivalGuess(button);
        } else if (game.getMode() == Game.Mode.Standard) {
            handleStandardGuess(button);
        } else {
            handleGuess(button);
        }
    }

    private void handleStandardGuess(final Button button) {
        if (!handledClick) {
            if (!button.getText().toString()
                    .equals(controller.getCurrent_player().getCollege())) {
                view.getGameTextView().setTextColor(view.getWrongGuessTextColor());
                handledClick = true;
                controller.setCurrent_score(controller.getCurrent_score() - 1);
                view.setScoreText(String.valueOf(controller.getCurrent_score()));
                handleGuess(button);
                view.getGameTextView().setTextColor(view.getQuestionTextColor());
            } else {
                handledClick = true;
                checkGuess(button);
            }
        }
    }

    private void handleSurvivalGuess(final Button button) {
        if (!button.getText().toString()
                .equals(controller.getCurrent_player().getCollege())) {
            updateStrikes();
        }
        checkGuess(button);
    }

    private void handleGuess(final Button button) {
        if (button.getText().toString()
                .equals(controller.getCurrent_player().getCollege())) {
            correctAnswer();
            YoYo.with(Techniques.Pulse).duration(400).playOn(button);
            button.setTextColor(view.getCorrectGuessTextColor());


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setTextColor(view.getCorrectGuessTextColor());
                    setQuestion();

                }
            }, 400);

        } else {
            wrongAnswer();
            YoYo.with(Techniques.Shake).duration(1000).playOn(button);
            YoYo.with(Techniques.Pulse).duration(1000).playOn(correctButton);
            button.setTextColor(view.getWrongGuessTextColor());
            correctButton.setTextColor(view.getCorrectGuessTextColor());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setTextColor(view.getRegularTextColor());
                    correctButton.setTextColor(view.getCorrectGuessTextColor());
                    setQuestion();

                }
            }, 1000);
        }
    }

    public void updateStrikes() {
        strikes++;
        if (strikes == 3) {
            view.endGame();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strikes; i++) {
            sb.append("X ");
        }

        view.setGameText(sb.toString());
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
        view.setScoreText(controller.getCurrent_score() + "");
    }

    protected void wrongAnswer() {
        controller.setWrong_answer(true);
        controller.resetCurrent_streak();
        controller.incrementCurrent_worst();

        if (controller.getCurrent_worst() > controller.getWorst_streak()) {
            controller.incrementWorst_streak();
        }
    }

    /**
     * Get a list of all the questions for game mode
     */
    public void getQuestions() {
        questions = Player.getPlayers(game.getDifficulty());

        Log.d("QUESTION Count", "" + questions.length);
    }

    public void fillChoices() {
        for (int i = 0; i < questions.length; i++) {
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
        controller.setCurrent_player(questions[choices.get(l)]);
        choices.remove(l);

        Log.d("TAG", choices.size() + ", " + l);
        view.setQuestionText(controller.getCurrent_player());
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
                correctButton = buttons[i];
            }
            buttons[i].setText(answer);
            i++;
        }

        handledClick = false;
    }

    public Game getGame() {
        return game;
    }
}
