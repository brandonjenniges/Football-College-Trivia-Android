package com.college.football.trivia.Util;

import com.college.football.trivia.BuildConfig;

public class Constants {

    // JSON Data
    public static final String PLAYERS_JSON_FILE = "all_players.txt";
    public static final String COLLEGE_JSON_FILE = "all_colleges.txt";

    public static final String[] diffs = {"Rookie", "Starter", "Veteran",
            "All-Pro"};

    public static final String[][] leaderboards = {
            {BuildConfig.leaderboard_standard_rookie,
                    BuildConfig.leaderboard_standard_starter,
                    BuildConfig.leaderboard_standard_veteran,
                    BuildConfig.leaderboard_standard_allpro},
            {BuildConfig.leaderboard_survival_rookie,
                    BuildConfig.leaderboard_survival_starter,
                    BuildConfig.leaderboard_survival_veteran,
                    BuildConfig.leaderboard_survival_allpro}};


    public static final String achievement_boom = BuildConfig.achievement_boom;
    public static final String achievement_oops = BuildConfig.achievement_oops;
    public static final String achievement_tictactoe = BuildConfig.achievement_tictactoe;
    public static final String achievement_tictacouch = BuildConfig.achievement_tictacouch;
    public static final String achievement_jumping_the_snap_count = BuildConfig.achievement_jumping_the_snap_count;
    public static final String achievement_pro_rate = BuildConfig.achievement_pro_rate;
    public static final String achievement_just_getting_started = BuildConfig.achievement_just_getting_started;
    public static final String achievement_established_starter = BuildConfig.achievement_established_starter;
    public static final String achievement_seasoned_pro = BuildConfig.achievement_seasoned_pro;
    public static final String achievement_grizzly_veteran = BuildConfig.achievement_grizzly_veteran;
    public static final String achievement_gridiron_legend = BuildConfig.achievement_gridiron_legend;
    public static final String achievement_never_gonna_give_your_up = BuildConfig.achievement_never_gonna_give_your_up;

    // Google Play Store
    public static final String app_url = "https://play.google.com/store/apps/details?id=com.college.football.trivia";
}