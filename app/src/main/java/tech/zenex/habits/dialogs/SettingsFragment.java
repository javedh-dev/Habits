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

package tech.zenex.habits.dialogs;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import tech.zenex.habits.R;
import tech.zenex.habits.SettingsActivity;
import tech.zenex.habits.utils.HabitsPreferenceListener;

public class SettingsFragment extends PreferenceFragmentCompat implements HabitsPreferenceListener {

    private SwitchPreferenceCompat backup;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference version = findPreference("version");
        try {
            assert version != null;
            version.setSummary(getAppVersion());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setupListeners();
    }

    private void setupListeners() {
        ((SettingsActivity) getActivity()).registerPreferenceListener(this);
        backup = findPreference("backup");
        backup.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue instanceof Boolean && (Boolean) newValue) {
                ((SettingsActivity) getActivity()).verifyLogin();
            }
            return true;
        });

    }

    private CharSequence getAppVersion() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo =
                getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        return pInfo.versionName + "(" + pInfo.versionCode + ")";
    }

    @Override
    public void onPreferenceResult(String key, boolean isSuccessful) {
//        Toast.makeText(getContext(), String.format("Key : %s, isSuccessful : %s", key, isSuccessful),
//                Toast.LENGTH_SHORT).show();
        if ("backup".equals(key)) {
            if (!isSuccessful) {
                backup.setChecked(false);
            }
        }
    }
}