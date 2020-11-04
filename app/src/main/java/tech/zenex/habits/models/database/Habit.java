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

package tech.zenex.habits.models.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "habits")
public class Habit {

    @PrimaryKey(autoGenerate = true)
    private int habitID;
    @NonNull
    private String name = "";
    private String description;
    private HabitType habitType;
    private Date creationDate;
    private boolean isOnceADay;
    private Date lastCheckIn;
    private int streakDays = 21;

    public Habit() {

    }

    @Ignore
    public Habit(@NonNull String name, String description, HabitType habitType, boolean isOnceADay) {
        this.name = name;
        this.description = description;
        this.habitType = habitType;
        this.isOnceADay = isOnceADay;
    }

    @Ignore
    public Habit(@NonNull String name, String description, HabitType habitType) {
        this.name = name;
        this.description = description;
        this.habitType = habitType;
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


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
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

    public Date getLastCheckIn() {
        return lastCheckIn;
    }

    public void setLastCheckIn(Date lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public enum HabitType {DEVELOP, BREAK}

    public enum Repetition {DAILY, WEEKLY, ALTERNATE_DAYS}
}
