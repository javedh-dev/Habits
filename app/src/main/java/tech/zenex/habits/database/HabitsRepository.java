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

import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;

import static tech.zenex.habits.database.HabitsDatabase.getDatabase;

public class HabitsRepository {

    private static final HabitsDatabase habitsDatabase;

    static {
        habitsDatabase = getDatabase(null);
    }

    public static void upsertHabit(HabitEntity habitEntity) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> habitsDatabase.habitDao().upsert(habitEntity));
    }

    public static void addHabitTracker(HabitTrackerEntity habitTrackerEntity) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> habitsDatabase.habitTrackerDAO().insert(habitTrackerEntity));
    }

    public static void addJournalEntry(JournalEntryEntity journalEntryEntity) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> habitsDatabase.journalDao().insert(journalEntryEntity));
    }

    public static void deleteHabit(int habitID) {
        HabitsDatabase.databaseWriteExecutor.execute(() -> habitsDatabase.habitDao().deleteHabit(habitID));
    }
}
