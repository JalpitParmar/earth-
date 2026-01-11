package com.scarycat.earth.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


public class GameApp extends Application {

    private int startedActivities = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStarted(Activity activity) {
                if (startedActivities == 0) {
                    MusicManager.onAppForeground();
                }
                startedActivities++;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                startedActivities--;
                if (startedActivities == 0) {
                    MusicManager.onAppBackground();
                }
            }

            // ❌ Unused but required
            public void onActivityCreated(Activity a, Bundle b) {}
            public void onActivityResumed(Activity a) {}
            public void onActivityPaused(Activity a) {}
            public void onActivitySaveInstanceState(Activity a, Bundle b) {}
            public void onActivityDestroyed(Activity a) {}
        });
    }
}