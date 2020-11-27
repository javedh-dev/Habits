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

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileWriter;
import java.security.Key;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import ir.androidexception.roomdatabasebackupandrestore.Backup;
import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.database.HabitsDatabase;

public class DatabaseBackupUtil {


    private DatabaseBackupUtil() {
    }

    public static void backup(AppCompatActivity context) {
        new Backup.Init()
                .database(HabitsDatabase.getDatabase(context))
                .path(context.getFilesDir().getAbsolutePath())
                .fileName("database.bkp")
                .onWorkFinishListener((success, message) -> Toast.makeText(context, "Backup finished",
                        Toast.LENGTH_SHORT).show()).execute();

        writeToCloud(new File(context.getFilesDir().getAbsolutePath(), "database.bkp"));
    }

    private static void writeToCloud(File file) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference userStorage = storageRef.child("/user/" + uid + "/" + file.getName());
        UploadTask task = userStorage.putFile(Uri.fromFile(file));
        task.addOnCompleteListener(task1 -> Log.d("Upload", String.valueOf(task1.isSuccessful())));
    }

    private static LiveData<List<HabitDetails>> getHabits(Context context) {
        return HabitsDatabase.getDatabase(context).habitDao().getAllHabits();
    }

    private static String encrypt(String original) {
        try {
            String key = "Bar12345Bar12345";
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(original.getBytes());
            return new String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return original;
        }
    }

    //Create a Method in your Activity
    private static void writeToStorage(Activity mContext, String fileName, String jsonContent) {
        File mFolder = mContext.getExternalFilesDir(null);
        assert mFolder != null;
        Log.d("Backup", "External directory is : " + mFolder.getAbsolutePath());
        if (!mFolder.exists()) mFolder.mkdirs();

        try {
            File mFile = new File(mFolder, fileName);
            Log.d("Backup", "File is written at : " + mFile.getAbsolutePath());
            FileWriter writer = new FileWriter(mFile);
            writer.append(jsonContent);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}