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

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import tech.zenex.habits.models.database.HabitTracker;

@Dao
public interface HabitTrackerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HabitTracker habitTracker);

    @Query(value = "DELETE FROM habits_tracker")
    void deleteAll();

    @Query(value = "Select * from habits_tracker")
    List<HabitTracker> getAllTrackerEntries();

    @Query(value = "Select * from habits_tracker where habitID = :habitID")
    List<HabitTracker> getAllTrackerEntriesForHabit(int habitID);

}
