/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.database;

import android.content.Context;

import androidx.annotation.Nullable;

import dev.javed.habits.database.entities.HabitEntity;
import dev.javed.habits.database.entities.HabitTrackerEntity;
import dev.javed.habits.database.entities.JournalEntryEntity;
import dev.javed.habits.utils.DatabaseResult;
import dev.javed.habits.utils.TaskCompletionListener;

public class HabitsRepository {

    public static void upsertHabit(HabitEntity habitEntity, Context context,
                                   @Nullable TaskCompletionListener<DatabaseResult> completionListener) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            DatabaseResult result = new DatabaseResult();
            try {
                habitEntity.setHabitID(habitEntity.getName().hashCode());
                long row = HabitsDatabase.getDatabase(context).habitDao().upsert(habitEntity);
                if (row == -1) {
                    result.setSuccessful(false);
                    result.setMessage("Habit already exist...");
                } else {
                    result.setSuccessful(true);
                }
            } catch (Exception e) {
                result.setSuccessful(false);
                result.setException(e);
                result.setMessage(e.getMessage());
            }
            if (completionListener != null) {
                completionListener.onTaskCompleted(result);
            }
        });
    }

    public static void addHabitTracker(HabitTrackerEntity habitTrackerEntity, Context context,
                                       @Nullable TaskCompletionListener<DatabaseResult> completionListener) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            DatabaseResult result = new DatabaseResult();
            try {
                long rowId = HabitsDatabase.getDatabase(context).habitTrackerDAO().insert(habitTrackerEntity);
                if (rowId == -1) {
                    result.setSuccessful(false);
                    result.setMessage("Check-in failed...");
                } else {
                    habitTrackerEntity.setId(HabitsDatabase.getDatabase(context).habitTrackerDAO()
                            .getIdFromRowId(rowId));
                    result.setSuccessful(true);
                }

            } catch (Exception e) {
                result.setSuccessful(false);
                result.setException(e);
                result.setMessage(e.getMessage());
            }
            if (completionListener != null) {
                completionListener.onTaskCompleted(result);
            }
        });
    }

    public static void addJournalEntry(JournalEntryEntity journalEntryEntity, Context context,
                                       @Nullable TaskCompletionListener<DatabaseResult> completionListener) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            DatabaseResult result = new DatabaseResult();
            try {
                long row = HabitsDatabase.getDatabase(context).journalDao().insert(journalEntryEntity);
                if (row == -1) {
                    result.setSuccessful(false);
                    result.setMessage("Check-in failed...");
                } else {
                    journalEntryEntity.setJournalId(HabitsDatabase.getDatabase(context).journalDao()
                            .getIdFromRowId(row));
                    result.setSuccessful(true);
                }
            } catch (Exception e) {
                result.setSuccessful(false);
                result.setException(e);
                result.setMessage(e.getMessage());
            }
            if (completionListener != null) {
                completionListener.onTaskCompleted(result);
            }
        });
    }

    public static void deleteHabit(int habitID, Context context,
                                   @Nullable TaskCompletionListener<DatabaseResult> completionListener) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            DatabaseResult result = new DatabaseResult();
            try {
                HabitsDatabase.getDatabase(context).habitDao().deleteHabit(habitID);
                result.setSuccessful(true);
            } catch (Exception e) {
                result.setSuccessful(false);
                result.setException(e);
                result.setMessage(e.getMessage());
            }
            if (completionListener != null) {
                completionListener.onTaskCompleted(result);
            }
        });
    }
}
