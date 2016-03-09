package com.college.football.trivia.Title;

import com.college.football.trivia.R;
import com.college.football.trivia.Util.Constants;
import com.college.football.trivia.Util.GameController;

public class TitleScreenPresenter {
    private TitleScreenView view;
    private GameController controller;

    public TitleScreenPresenter(TitleScreenView view ) {
        this.view = view;
        controller = GameController.getInstance();
    }

    public void startGame(int id) {
        switch (id) {
            case R.id.standardGame:
                controller.setCurrent_mode(Constants.standard_game_int);
                launchDifficultyDialog();
                break;
            case R.id.survivalGame:
                controller.setCurrent_mode(Constants.survival_game_int);
                launchDifficultyDialog();
                break;
            case R.id.practiceGame:
                controller.setCurrent_mode(Constants.practice_game_int);
                launchDifficultyDialog();
                break;
        }
    }

    public void launchDifficultyDialog() {
        view.showDifficultyDialog();
    }

    public GameController getController() {
        return controller;
    }
}
