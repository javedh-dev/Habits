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

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;

public class HabitDetails implements Serializable {

    @Ignore
    private boolean isSorted = false;

    @Embedded
    private HabitEntity habitEntity;

    @Relation(parentColumn = "habitID", entityColumn = "habitID", entity = HabitTrackerEntity.class)
    private List<HabitTrackerEntity> habitTrackerEntities;

    @Relation(parentColumn = "habitID", entityColumn = "habitID", entity = JournalEntryEntity.class)
    private List<JournalEntryEntity> journalEntryEntities;

    public HabitEntity getHabitEntity() {
        return habitEntity;
    }

    public void setHabitEntity(HabitEntity habitEntity) {
        this.habitEntity = habitEntity;
    }

    public List<HabitTrackerEntity> getHabitTrackerEntities() {
        if (isSorted) {
            Collections.sort(habitTrackerEntities, Collections.reverseOrder());
            isSorted = true;
        }
        return habitTrackerEntities;
    }

    public void setHabitTrackerEntities(List<HabitTrackerEntity> habitTrackerEntities) {
        this.habitTrackerEntities = habitTrackerEntities;
    }

    public List<JournalEntryEntity> getJournalEntryEntities() {
        return journalEntryEntities;
    }

    public void setJournalEntryEntities(List<JournalEntryEntity> journalEntryEntities) {
        this.journalEntryEntities = journalEntryEntities;
    }

    @NonNull
    @Override
    public String toString() {
        return "HabitDetails{" +
                "habitEntity=" + habitEntity +
                ", habitTrackerEntities=" + habitTrackerEntities +
                ", journalEntryEntities=" + journalEntryEntities +
                '}';
    }
}
