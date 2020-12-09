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

package tech.zenex.habits.database;

import android.content.Context;

import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;

public class HabitsRepository {

    public static void upsertHabit(HabitEntity habitEntity, Context context) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            long rowId = HabitsDatabase.getDatabase(context).habitDao().upsert(habitEntity);
            habitEntity.setHabitID(HabitsDatabase.getDatabase(context).habitDao().getIdFromRowId(rowId));
        });
    }

    public static void addHabitTracker(HabitTrackerEntity habitTrackerEntity, Context context) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            long rowId = HabitsDatabase.getDatabase(context).habitTrackerDAO().insert(habitTrackerEntity);
            habitTrackerEntity.setId(HabitsDatabase.getDatabase(context).habitTrackerDAO().getIdFromRowId(rowId));
        });
    }

    public static void addJournalEntry(JournalEntryEntity journalEntryEntity, Context context) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> {
            long rowID = HabitsDatabase.getDatabase(context).journalDao().insert(journalEntryEntity);
            journalEntryEntity.setJournalId(HabitsDatabase.getDatabase(context).journalDao().getIdFromRowId(rowID));
        });
    }

    public static void deleteHabit(int habitID, Context context) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> HabitsDatabase.getDatabase(context).habitDao().deleteHabit(habitID));
    }
}
