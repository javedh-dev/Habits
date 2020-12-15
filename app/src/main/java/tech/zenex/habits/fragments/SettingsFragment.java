/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import tech.zenex.habits.R;
import tech.zenex.habits.SettingsActivity;
import tech.zenex.habits.utils.HabitsConstants;
import tech.zenex.habits.utils.HabitsPreferenceListener;

public class SettingsFragment extends PreferenceFragmentCompat implements HabitsPreferenceListener {

    private final SettingsActivity activity;
    private SwitchPreferenceCompat backup;

    public SettingsFragment(SettingsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference version = findPreference(HabitsConstants.PREFERENCE_VERSION);
        try {
            assert version != null;
            version.setSummary(getAppVersion());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setupListeners();
    }

    private void setupListeners() {
        activity.registerPreferenceListener(this);
        backup = findPreference(HabitsConstants.PREFERENCE_BACKUP);
        if (backup != null) {
            backup.setOnPreferenceChangeListener((preference, newValue) -> {
                if (newValue instanceof Boolean && (Boolean) newValue) {
                    activity.verifyLogin();
                }
                return true;
            });
        }

    }

    private CharSequence getAppVersion() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo =
                activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
        return pInfo.versionName + " (" + pInfo.versionCode + ")";
    }

    @Override
    public void onPreferenceResult(String key, boolean isSuccessful) {
        if (HabitsConstants.PREFERENCE_BACKUP.equals(key)) {
            if (!isSuccessful) {
                backup.setChecked(false);
            }
        }
    }
}