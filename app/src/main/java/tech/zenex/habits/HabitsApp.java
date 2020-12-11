/*
 * Copyright (c) 2020.  Zenex.Tech@ https://zenex.tech
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.zenex.habits;

import android.app.Application;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import tech.zenex.habits.utils.HabitsPreferencesUtil;

public class HabitsApp extends Application {
    private static final String TAG = "Firebase Token";

    @Override
    public void onCreate() {
        super.onCreate();
        setupFirebase();
        Thread t = new Thread(this::setupDatabase);
        t.start();
    }

    private void setupDatabase() {
        if (HabitsPreferencesUtil.getDefaultSharedPreference(getApplicationContext()).getBoolean(
                "is_first_run", true)) {
//            HabitsBasicUtil.generateTestData(getApplicationContext());
            HabitsPreferencesUtil.getDefaultSharedPreference(getApplicationContext()).edit().putBoolean(
                    "is_first_run", false).apply();
        }
    }

    private void setupFirebase() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Error getting token");
                    } else {
                        Log.d(TAG, Objects.requireNonNull(task.getResult()));
                    }
                });
    }
}
