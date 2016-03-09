package com.college.football.trivia;

import android.app.Application;

import com.college.football.trivia.Util.QuestionLoader;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class FootballTriviaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.USE_CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
        }

        // Load players and questions from JSON files
        QuestionLoader.loadData(getApplicationContext());
    }
}
