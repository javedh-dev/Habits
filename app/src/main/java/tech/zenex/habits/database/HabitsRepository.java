/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.database;

import android.content.Context;

import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;

public class HabitsRepository {

    public static void upsertHabit(HabitEntity habitEntity, Context context) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            long rowId = HabitsDatabase.getDatabase(context).habitDao().upsert(habitEntity);
            habitEntity.setHabitID(HabitsDatabase.getDatabase(context).habitDao()
                    .getIdFromRowId(rowId));
        });
    }

    public static void addHabitTracker(HabitTrackerEntity habitTrackerEntity, Context context) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            long rowId = HabitsDatabase.getDatabase(context).habitTrackerDAO().insert(habitTrackerEntity);
            habitTrackerEntity.setId(HabitsDatabase.getDatabase(context).habitTrackerDAO()
                    .getIdFromRowId(rowId));
        });
    }

    public static void addJournalEntry(JournalEntryEntity journalEntryEntity, Context context) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            long rowID = HabitsDatabase.getDatabase(context).journalDao().insert(journalEntryEntity);
            journalEntryEntity.setJournalId(HabitsDatabase.getDatabase(context).journalDao()
                    .getIdFromRowId(rowID));
        });
    }

    public static void deleteHabit(int habitID, Context context) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> HabitsDatabase.getDatabase(context).habitDao()
                .deleteHabit(habitID));
    }
}
