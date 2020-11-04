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

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tech.zenex.habits.database.dao.HabitDAO;
import tech.zenex.habits.database.dao.HabitTrackerDAO;
import tech.zenex.habits.database.dao.JournalEntryDAO;
import tech.zenex.habits.models.database.Habit;
import tech.zenex.habits.models.database.HabitTracker;
import tech.zenex.habits.models.database.JournalEntry;

@Database(version = 1, entities = {Habit.class, JournalEntry.class, HabitTracker.class}, exportSchema = false)
@TypeConverters(value = HabitsTypeConverters.class)
public abstract class HabitsDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile HabitsDatabase INSTANCE;

    public static HabitsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HabitsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HabitsDatabase.class, "habits.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract HabitDAO habitDao();

    public abstract JournalEntryDAO journalDao();

    public abstract HabitTrackerDAO habitTrackerDAO();

}
