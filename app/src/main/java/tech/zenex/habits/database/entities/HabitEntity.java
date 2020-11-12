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

package tech.zenex.habits.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDateTime;

import java.util.List;

import tech.zenex.habits.database.HabitDetails;

@Entity(tableName = "habits")
public class HabitEntity implements Comparable<HabitEntity> {

    @PrimaryKey(autoGenerate = true)
    private int habitID;
    @NonNull
    private String name = "";
    private String description;
    private HabitType habitType;
    private LocalDateTime creationDate;
    private boolean isOnceADay;
    private LocalDateTime lastCheckIn = new LocalDateTime(0);
    private LocalDateTime lastFailed = LocalDateTime.now();
    private int streakDays = 21;
    private int color;

    @Ignore
    private List<HabitTrackerEntity> habitTrackerEntities;
    @Ignore
    private List<JournalEntryEntity> journalEntries;

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

    public HabitEntity(HabitDetails habitDetails) {
        this.name = habitDetails.getHabitEntity().getName();
        this.description = habitDetails.getHabitEntity().getDescription();
        this.creationDate = habitDetails.getHabitEntity().getCreationDate();
        this.habitID = habitDetails.getHabitEntity().getHabitID();
        this.habitType = habitDetails.getHabitEntity().getHabitType();
        this.isOnceADay = habitDetails.getHabitEntity().isOnceADay();
        this.lastCheckIn = habitDetails.getHabitEntity().getLastCheckIn();
        this.streakDays = habitDetails.getHabitEntity().getStreakDays();
        this.journalEntries = habitDetails.getJournalEntryEntities();
        this.habitTrackerEntities = habitDetails.getHabitTrackerEntities();

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
        return "Habit{" +
                "habitID=" + habitID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", habitType=" + habitType +
                ", creationDate=" + creationDate +
                ", isOnceADay=" + isOnceADay +
                '}';
    }

    public LocalDateTime getLastCheckIn() {
        return lastCheckIn;
    }

    public void setLastCheckIn(LocalDateTime lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public List<HabitTrackerEntity> getHabitTrackerEntities() {
        return habitTrackerEntities;
    }

    public void setHabitTrackerEntities(List<HabitTrackerEntity> habitTrackerEntities) {
        this.habitTrackerEntities = habitTrackerEntities;
    }

    public List<JournalEntryEntity> getJournalEntries() {
        return journalEntries;
    }

    public void setJournalEntries(List<JournalEntryEntity> journalEntries) {
        this.journalEntries = journalEntries;
    }

    @Override
    public int compareTo(HabitEntity o) {
        return DateTimeComparator.getDateOnlyInstance().compare(this, o);
    }

    public LocalDateTime getLastFailed() {
        return lastFailed;
    }

    public void setLastFailed(LocalDateTime lastFailed) {
        this.lastFailed = lastFailed;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public enum HabitType {DEVELOP, BREAK}

    public enum Repetition {DAILY, WEEKLY, ALTERNATE_DAYS}
}
