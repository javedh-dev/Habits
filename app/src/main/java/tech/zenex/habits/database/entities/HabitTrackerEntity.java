/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.joda.time.LocalDateTime;

import java.io.Serializable;

@Entity(tableName = "habits_tracker", foreignKeys = @ForeignKey(entity = HabitEntity.class,
        parentColumns = "habitID", childColumns = "habitID", onDelete = ForeignKey.CASCADE),
        indices = {@Index("habitID")})
public class HabitTrackerEntity implements Comparable<HabitTrackerEntity>, Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int habitID;
    private LocalDateTime checkInTime;
    private HabitEntity.HabitType type;

    public HabitTrackerEntity() {
    }

    @Ignore
    public HabitTrackerEntity(int habitID, HabitEntity.HabitType type) {
        this.habitID = habitID;
        this.type = type;
        this.checkInTime = LocalDateTime.now();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHabitID() {
        return habitID;
    }

    public void setHabitID(int habitID) {
        this.habitID = habitID;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    @Override
    public int compareTo(HabitTrackerEntity o) {
        return this.checkInTime.compareTo(o.checkInTime);
    }

    public HabitEntity.HabitType getType() {
        return type;
    }

    public void setType(HabitEntity.HabitType type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "HabitTrackerEntity{" +
                "id=" + id +
                ", habitID=" + habitID +
                ", checkInTime=" + checkInTime +
                ", type=" + type +
                '}';
    }
}
