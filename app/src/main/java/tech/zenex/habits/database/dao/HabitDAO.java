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

    @Query(value = "DELETE FROM habits")
    public abstract void deleteAllHabits();

    @Transaction
    @Query("Select * from habits")
    public abstract LiveData<List<HabitDetails>> getAllHabits();

    @Query(value = "DELETE FROM habits where habitID=:habitID")
    public abstract void deleteHabit(int habitID);
}
