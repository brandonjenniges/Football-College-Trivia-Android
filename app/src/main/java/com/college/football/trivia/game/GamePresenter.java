package com.college.football.trivia.Game;

import android.os.Handler;
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
    private Player player;

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

        GameController.reset();

        view.setScoreText(String.valueOf(game.getScore()));
        view.setHighScoreText(String.valueOf(game.getHighScore()));
        view.setGameText("");
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
                    .equals(player.getCollege())) {
                view.getGameTextView().setTextColor(view.getWrongGuessTextColor());
                handledClick = true;
                game.decreaseScore();
                view.setScoreText(String.valueOf(game.getScore()));
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
                .equals(player.getCollege())) {
            updateStrikes();
        }
        checkGuess(button);
    }

    private void handleGuess(final Button button) {
        if (button.getText().toString()
                .equals(player.getCollege())) {
            correctAnswer();
            YoYo.with(Techniques.Pulse).duration(400).playOn(button);
            button.setTextColor(view.getCorrectGuessTextColor());


            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                button.setTextColor(view.getRegularTextColor());
                setQuestion();

            }, 400);

        } else {
            wrongAnswer();
            YoYo.with(Techniques.Shake).duration(1000).playOn(button);
            YoYo.with(Techniques.Pulse).duration(1000).playOn(correctButton);
            button.setTextColor(view.getWrongGuessTextColor());
            correctButton.setTextColor(view.getCorrectGuessTextColor());
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                button.setTextColor(view.getRegularTextColor());
                correctButton.setTextColor(view.getRegularTextColor());
                setQuestion();

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
        game.increaseScore();
        if (!controller.hadWrongAnswer()) {
            controller.increaseStartStreak();
        }
        controller.increaseCurrentStreak();
        if (controller.getCurrentStreak() > controller.getBestStreak()) {
            controller.increaseBestStreak();
        }
        controller.resetCurrentWorstStreak();
        view.setScoreText(String.valueOf(game.getScore()));
    }

    protected void wrongAnswer() {
        controller.setHadWrongAnswer(true);
        controller.resetCurrentStreak();
        controller.increaseCurrentWorstStreak();

        if (controller.getCurrentWrongStreak() > controller.getLongestWrongStreak()) {
            controller.increaseLongestWrongStreak();
        }
    }

    public void getQuestions() {
        questions = Player.getPlayers(game.getDifficulty());
    }

    public void fillChoices() {
        for (int i = 0; i < questions.length; i++) {
            choices.add(i);
        }
    }

    public void setQuestion() {
        canGuess = true;

        if (choices.size() == 0) {
            fillChoices();
        }

        int l = r.nextInt(choices.size());
        player = questions[choices.get(l)];
        choices.remove(l);

        view.setQuestionText(player);
        getAnswers();
        sortAnswers();
        displayAnswers();

    }

    public void getAnswers() {
        for (int i = 0; i < sortedChoices.length; i++) {
            sortedChoices[i] = "";
        }
        sortedChoices[0] = player.getCollege();
        String college;
        int temp;

        for (int i = 1; i <= 3; i++) {
            do {
                temp = r.nextInt(tierArray.get(player.getCollege_tier() - 1)
                        .size());
                college = tierArray
                        .get(player.getCollege_tier() - 1)
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
            if (answer.equals(player.getCollege())) {
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
