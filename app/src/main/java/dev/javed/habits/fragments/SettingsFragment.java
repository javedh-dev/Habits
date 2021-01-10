/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Map;

import dev.javed.habits.R;
import dev.javed.habits.SettingsActivity;
import dev.javed.habits.utils.HabitsBasicUtil;
import dev.javed.habits.utils.HabitsConstants;
import dev.javed.habits.utils.TaskCompletionListener;

public class SettingsFragment extends PreferenceFragmentCompat implements TaskCompletionListener<Map<String
        , Boolean>> {

    private SwitchPreferenceCompat backup;


    public SettingsFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        EditTextPreference streakLimit = getPreferenceManager().findPreference("streak_limit");
        if (streakLimit != null) {
            streakLimit.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED));
            streakLimit.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean result =
                        !newValue.toString().isEmpty() && Integer.parseInt(newValue.toString()) >= 21;
                if (!result) HabitsBasicUtil.notifyUser(getContext(), R.string.streak_limit_message);
                return result;
            });
        }
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
        SettingsActivity activity = (SettingsActivity) getActivity();
        if (activity != null) {
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
    }

    private CharSequence getAppVersion() throws PackageManager.NameNotFoundException {
        if (getActivity() != null) {
            PackageInfo pInfo =
                    getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            return pInfo.versionName;
        } else
            return HabitsConstants.EMPTY_STRING;
    }


    @Override
    public void onTaskCompleted(@NonNull Map<String, Boolean> result) {
        Map.Entry<String, Boolean> res = result.entrySet().iterator().next();
        if (HabitsConstants.PREFERENCE_BACKUP.equals(res.getKey())) {
            if (!res.getValue()) {
                backup.setChecked(false);
            }
        }
    }
}