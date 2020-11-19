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

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDateTime;

import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.utils.HabitsPreferencesUtil;

@Entity(tableName = "habits", indices = {@Index("habitID")})
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
    private int failureCounter = 0;
    private LocalDateTime streakStart = LocalDateTime.now();
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

    public HabitEntity(HabitDetails habitDetails) {
        this.name = habitDetails.getHabitEntity().getName();
        this.description = habitDetails.getHabitEntity().getDescription();
        this.creationDate = habitDetails.getHabitEntity().getCreationDate();
        this.habitID = habitDetails.getHabitEntity().getHabitID();
        this.habitType = habitDetails.getHabitEntity().getHabitType();
        this.isOnceADay = habitDetails.getHabitEntity().isOnceADay();
        this.lastCheckIn = habitDetails.getHabitEntity().getLastCheckIn();
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
                ", lastCheckIn=" + lastCheckIn +
                ", failureCounter=" + failureCounter +
                ", streakStart=" + streakStart +
                ", streakDays=" + streakDays +
                ", color=" + color +
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

    public int getFailureCounter() {
        return failureCounter;
    }

    public void setFailureCounter(int failureCounter) {
        this.failureCounter = failureCounter;
    }

    public LocalDateTime getStreakStart() {
        return streakStart;
    }

    public void setStreakStart(LocalDateTime streakStart) {
        this.streakStart = streakStart;
    }

    @Ignore
    public void incrementFailedCounter(Context context) {
        failureCounter = failureCounter >= HabitsPreferencesUtil.getDefaultSharedPreference(context).getInt(
                "allowed_failures", 3) ? 0 : failureCounter + 1;
        Log.d("Failure Counter", String.format("%d", failureCounter));
        if (failureCounter == 0) {
            streakStart = LocalDateTime.now();
        }

    }

    public enum HabitType {DEVELOP, BREAK}

}
