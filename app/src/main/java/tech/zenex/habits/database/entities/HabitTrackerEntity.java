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
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.joda.time.DateTimeComparator;
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
        return DateTimeComparator.getDateOnlyInstance().compare(this, o);
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
