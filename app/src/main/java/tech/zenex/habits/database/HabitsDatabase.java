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

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tech.zenex.habits.database.dao.HabitDAO;
import tech.zenex.habits.database.dao.HabitTrackerDAO;
import tech.zenex.habits.database.dao.JournalEntryDAO;
import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;
import tech.zenex.habits.database.entities.NotificationEntity;

@Database(version = 1, entities = {HabitEntity.class, JournalEntryEntity.class, HabitTrackerEntity.class,
        NotificationEntity.class})
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
                    Builder<HabitsDatabase> habitsDatabaseBuilder =
                            Room.databaseBuilder(context, HabitsDatabase.class,
                                    "habits.db");
                    habitsDatabaseBuilder.addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            /*db.execSQL("CREATE TRIGGER if not exists habits_tracker_added after insert on
                             " +
                                    "habits_tracker\n" +
                                    "begin\n" +
                                    "UPDATE habits set lastCheckIn=strftime('%d/%m/%Y %H:%M:%f','now') " +
                                    "where habits.habitID = new.habitID;\n" +
                                    "end");*/
                        }
                    });
                    INSTANCE = habitsDatabaseBuilder.build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract HabitDAO habitDao();

    public abstract JournalEntryDAO journalDao();

    public abstract HabitTrackerDAO habitTrackerDAO();

   /*new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("Room Migration", "Migrating room database to newer version");
            database.execSQL("CREATE TABLE \"notifications\" (\"id\" INTEGER, " +
                    "\"from\" TEXT , \"to\" TEXT , \"data\" TEXT , " +
                    "\"received_time\" TEXT , \"title\" TEXT , \"description\" TEXT" +
                    " , \"is_read\" INTEGER, PRIMARY KEY(\"id\" AUTOINCREMENT))");
        }
    });*/

}
