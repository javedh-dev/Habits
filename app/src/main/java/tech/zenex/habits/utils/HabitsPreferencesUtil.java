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

package tech.zenex.habits.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class HabitsPreferencesUtil {

    private HabitsPreferencesUtil() {
    }

    /*public static SharedPreferences getSharedPreference(String fileName, Context context) throws
    GeneralSecurityException,
            IOException {
        MasterKey masterKey = new
                MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS).
                setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
        return EncryptedSharedPreferences
                .create(
                        context,
                        fileName,
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
    }*/

    public static SharedPreferences getDefaultSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
