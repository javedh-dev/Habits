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

import tech.zenex.habits.database.entities.JournalEntryEntity;

@Dao
public interface JournalEntryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JournalEntryEntity entry);

    @Query(value = "DELETE FROM journal_entries")
    void deleteAll();

    @Query(value = "Select * from journal_entries")
    List<JournalEntryEntity> getAllJournalEntries();

    @Query(value = "Select * from journal_entries where habitID = :habitID")
    List<JournalEntryEntity> getAllJournalEntriesForHabit(int habitID);
}
