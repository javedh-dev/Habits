/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.database.entities.HabitEntity;

@Dao
public abstract class HabitDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(HabitEntity habitEntity);

    @Transaction
    public long upsert(HabitEntity obj) {
        long id = insert(obj);
        if (id == -1) {
            update(obj);
        }
        return id;
    }

    @Query("SELECT habitID FROM habits WHERE ROWID = :rowId")
    public abstract int getIdFromRowId(long rowId);

    @Update
    public abstract void update(HabitEntity habitEntity);

    @Transaction
    @Query("Select * from habits")
    public abstract LiveData<List<HabitDetails>> getAllHabits();

    @Query(value = "DELETE FROM habits where habitID=:habitID")
    public abstract void deleteHabit(int habitID);
}
