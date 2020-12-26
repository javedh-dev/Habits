/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.utils;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Objects;

import ir.androidexception.roomdatabasebackupandrestore.Backup;
import ir.androidexception.roomdatabasebackupandrestore.Restore;
import tech.zenex.habits.MainActivity;
import tech.zenex.habits.R;
import tech.zenex.habits.database.HabitsDatabase;

public class DatabaseBackupUtil {

    private DatabaseBackupUtil() {
    }

    public static void backup(MainActivity context) {
        String habitsDatabaseBkpFileName = HabitsConstants.DATABASE_BACKUP_FILENAME;
        HabitsLoadingDialog loader = getHabitsLoadingDialog(context);
        new Backup.Init()
                .database(HabitsDatabase.getDatabase(context))
                .path(context.getFilesDir().getAbsolutePath())
                .secretKey(getSecretKey(context))
                .fileName(habitsDatabaseBkpFileName)
                .onWorkFinishListener((success, message) -> uploadToCloud(context,
                        habitsDatabaseBkpFileName, loader)).execute();

    }

    private static void uploadToCloud(Context context, String bkpFileName, HabitsLoadingDialog loader) {
        File file = new File(context.getFilesDir().getAbsolutePath(), bkpFileName);
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference userStorage = getStoragePath(file, uid, storage);
        UploadTask task = userStorage.putFile(Uri.fromFile(file));
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                HabitsBasicUtil.notifyUser(context, R.string.backup_completion_message);
            } else {
                HabitsBasicUtil.notifyUser(context, R.string.backup_failed_message);
            }
            loader.dismiss();
        });
    }

    @NonNull
    private static StorageReference getStoragePath(File file, String uid,
                                                   FirebaseStorage storage) {
        return storage.getReference().child(HabitsConstants.REMOTE_STORAGE_BASE_DIR + uid +
                File.separator + file.getName());
    }

    private static String getSecretKey(Context context) {
        return HabitsBasicUtil.getDefaultSharedPreference(context).getString(
                HabitsConstants.PREFERENCE_BACKUP_KEY, HabitsConstants.EMPTY_STRING);
    }

    @NonNull
    private static HabitsLoadingDialog getHabitsLoadingDialog(MainActivity context) {
        HabitsLoadingDialog loader = new HabitsLoadingDialog(context,
                context.getString(R.string.backing_up_message));
        loader.show();
        return loader;
    }

    public static void restore(MainActivity context) {
        String habitsDatabaseBkpFileName = HabitsConstants.DATABASE_BACKUP_FILENAME;
        HabitsLoadingDialog loader = new HabitsLoadingDialog(context,
                context.getString(R.string.restoring_message));
        loader.show();
        File file = new File(context.getFilesDir().getAbsolutePath(), habitsDatabaseBkpFileName);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference userStorage = getStoragePath(file, uid, storage);
            FileDownloadTask task = userStorage.getFile(Uri.fromFile(file));
            task.addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) restoreDatabase(context, loader, file);
                else {
                    HabitsBasicUtil.notifyUser(context, R.string.restore_failed_message);
                    loader.dismiss();
                }
            });
        } else {
            HabitsBasicUtil.notifyUser(context, R.string.restore_failed_message);
            loader.dismiss();
        }
    }

    private static void restoreDatabase(MainActivity context, HabitsLoadingDialog loader, File file) {
        new Restore.Init()
                .database(HabitsDatabase.getDatabase(context))
                .backupFilePath(file.getAbsolutePath())
                .secretKey(getSecretKey(context))
                .onWorkFinishListener((success, message) -> {
                    loader.dismiss();
                    HabitsBasicUtil.notifyUser(context, R.string.restore_completion_message);
                    context.recreate();
                }).execute();
    }

}
