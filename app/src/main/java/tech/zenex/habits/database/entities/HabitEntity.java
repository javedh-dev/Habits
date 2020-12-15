/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package tech.zenex.habits.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDateTime;

import java.io.Serializable;

import tech.zenex.habits.database.HabitDetails;

@Entity(tableName = "habits", indices = {@Index("habitID")})
public class HabitEntity implements Comparable<HabitEntity>, Serializable {

    @PrimaryKey(autoGenerate = true)
    private int habitID;
    @NonNull
    private String name = "";
    private String description;
    private HabitType habitType;
    private LocalDateTime creationDate;
    private boolean isOnceADay;
    private int streakDays = 21;
    private int color;

    public HabitEntity() {
    }

    @Ignore
    public HabitEntity(@NonNull String name, String description, HabitType habitType, boolean isOnceADay) {
        this.name = name;
        this.description = description;
        this.habitType = habitType;
        this.isOnceADay = isOnceADay;
    }

    @Ignore
    public HabitEntity(@NonNull String name, String description, HabitType habitType) {
        this.name = name;
        this.description = description;
        this.habitType = habitType;
    }

    public HabitEntity(@NonNull HabitDetails habitDetails) {
        this.name = habitDetails.getHabitEntity().getName();
        this.description = habitDetails.getHabitEntity().getDescription();
        this.creationDate = habitDetails.getHabitEntity().getCreationDate();
        this.habitID = habitDetails.getHabitEntity().getHabitID();
        this.habitType = habitDetails.getHabitEntity().getHabitType();
        this.isOnceADay = habitDetails.getHabitEntity().isOnceADay();
        this.streakDays = habitDetails.getHabitEntity().getStreakDays();
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HabitType getHabitType() {
        return habitType;
    }

    public void setHabitType(HabitType habitType) {
        this.habitType = habitType;
    }

    public int getHabitID() {
        return habitID;
    }

    public void setHabitID(int habitID) {
        this.habitID = habitID;
    }


    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isOnceADay() {
        return isOnceADay;
    }

    public void setOnceADay(boolean onceADay) {
        isOnceADay = onceADay;
    }

    @NonNull
    @Override
    public String toString() {
        return "HabitEntity{" +
                "habitID=" + habitID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", habitType=" + habitType +
                ", creationDate=" + creationDate +
                ", isOnceADay=" + isOnceADay +
                ", streakDays=" + streakDays +
                ", color=" + color +
                '}';
    }


    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    @Override
    public int compareTo(HabitEntity o) {
        return DateTimeComparator.getDateOnlyInstance().compare(this, o);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public enum HabitType {
        DEVELOP, BREAK;

        public HabitType not() {
            if (this == DEVELOP) return BREAK;
            else return DEVELOP;
        }
    }

}
