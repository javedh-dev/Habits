/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.database;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

import tech.zenex.habits.database.entities.HabitEntity;
import tech.zenex.habits.database.entities.HabitTrackerEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;

public class HabitDetails implements Serializable {

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
