package com.college.football.trivia.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
    public static final String EXTRA_KEY = "GAME_EXTRA_KEY";
    private Mode mode;
    private Difficulty difficulty;

    public Game(Mode mode, Difficulty difficulty) {
        this.mode = mode;
        this.difficulty = difficulty;
    }

    protected Game(Parcel in) {
        int tmpMode = in.readInt();
        this.mode = tmpMode == -1 ? null : Mode.values()[tmpMode];
        int tmpDifficulty = in.readInt();
        this.difficulty = tmpDifficulty == -1 ? null : Difficulty.values()[tmpDifficulty];
    }

    public Mode getMode() {
        return mode;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mode == null ? -1 : this.mode.ordinal());
        dest.writeInt(this.difficulty == null ? -1 : this.difficulty.ordinal());
    }

    public enum Mode {
        Standard, Survival, Practice
    }

    public enum Difficulty {
        Rookie, Starter, Veteran, AllPro
    }

    public static Difficulty difficultyFromInt(int value) {
        switch (value) {
            case 0:
                return Difficulty.Rookie;
            case 1:
                return Difficulty.Starter;
            case 2:
                return Difficulty.Veteran;
            default:
                return Difficulty.AllPro;
        }
    }

    public static int intForDifficulty(Difficulty difficulty) {
        switch (difficulty) {
            case Rookie:
                return 0;
            case Starter:
                return 1;
            case Veteran:
                return 2;
            case AllPro:
                return 3;
        }
        return 0;
    }

    public static int intForMode(Mode mode) {
        switch (mode) {
            case Standard:
                return 0;
            case Survival:
                return 1;
            case Practice:
                return 2;
        }
        return 0;
    }

    public String getHighScoreKey() {
        String prefix = "FootballScore";
        int difficultyInt = Game.intForDifficulty(difficulty) + 1; // Legacy code had difficulties be 1-4
        int modeInt = Game.intForMode(mode);
        return prefix + (modeInt * 4) + difficultyInt;
    }
}
