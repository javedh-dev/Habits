/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import dev.javed.habits.database.entities.JournalEntryEntity;

@Dao
public abstract class JournalEntryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(JournalEntryEntity journalEntryEntity);

    @Query("SELECT journalId FROM journal_entries WHERE ROWID = :rowId")
    public abstract int getIdFromRowId(long rowId);
}
