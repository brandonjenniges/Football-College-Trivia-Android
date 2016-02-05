package com.college.football.trivia;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;

public class FootballTriviaApplication extends Application {

    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.USE_CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
        }
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            if (BuildConfig.USE_ANALYTICS) {
                mTracker = analytics.newTracker(BuildConfig.GOOGLE_ANALYTICS_KEY);
                analytics.setLocalDispatchPeriod(20);
                analytics.enableAutoActivityReports(this);
            }
        }
        return mTracker;
    }
}
