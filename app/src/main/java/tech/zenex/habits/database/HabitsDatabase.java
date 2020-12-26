/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
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
import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;
import tech.zenex.habits.utils.HabitsConstants;

@Database(version = 1, entities = {HabitEntity.class, JournalEntryEntity.class, HabitTrackerEntity.class})
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
                                    HabitsConstants.DATABASE_FILENAME);
                    INSTANCE = habitsDatabaseBuilder.build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract HabitDAO habitDao();

    public abstract JournalEntryDAO journalDao();

    public abstract HabitTrackerDAO habitTrackerDAO();

}
