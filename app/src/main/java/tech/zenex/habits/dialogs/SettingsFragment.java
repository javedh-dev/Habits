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

import tech.zenex.habits.R;
import tech.zenex.habits.SettingsActivity;

public class SettingsFragment extends PreferenceFragmentCompat {
    private final SettingsActivity activity;

    public SettingsFragment(SettingsActivity activity) {
        this.activity = activity;
    }

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
    }

    private CharSequence getAppVersion() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo =
                getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        return pInfo.versionName + "(" + pInfo.versionCode + ")";
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this.activity);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this.activity);
    }
}