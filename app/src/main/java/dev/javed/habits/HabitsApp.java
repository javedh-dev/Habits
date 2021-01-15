/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits;

import android.app.Application;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

import dev.javed.habits.utils.HabitsBasicUtil;
import dev.javed.habits.utils.HabitsConstants;

public class HabitsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupFirebase();
        Thread t = new Thread(this::setupDatabase);
        t.start();
    }

    private void setupDatabase() {
        if (HabitsBasicUtil.getDefaultSharedPreference(getApplicationContext()).getBoolean(
                HabitsConstants.PREFERENCE_IS_FIRST_RUN, true)) {
            if (BuildConfig.DEBUG)
                HabitsBasicUtil.generateTestData(getApplicationContext());
            HabitsBasicUtil.getDefaultSharedPreference(getApplicationContext()).edit().putBoolean(
                    HabitsConstants.PREFERENCE_IS_FIRST_RUN, false).apply();
        }
    }

    private void setupFirebase() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e(HabitsConstants.FIREBASE_LOG, HabitsConstants.FIREBASE_ERROR_GETTING_TOKEN);
                    } else {
                        Log.d(HabitsConstants.FIREBASE_LOG,
                                String.format(HabitsConstants.FIREBASE_TOKEN_ACQUIRED, task.getResult()));
                    }
                });
        FirebaseMessaging.getInstance().subscribeToTopic("quote");
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG);
    }
}
