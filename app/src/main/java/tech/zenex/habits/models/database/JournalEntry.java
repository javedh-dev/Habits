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

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "journal")
public class JournalEntry {
    @PrimaryKey(autoGenerate = true)
    private Long journalId;
    private Date timestamp;
    @ForeignKey(entity = Habit.class, parentColumns = "habitID", childColumns = "habitID", onDelete =
            ForeignKey.CASCADE)
    private int habitID;
    private JournalType journalType;
    private String entry;

    public JournalEntry() {
    }

    public JournalEntry(int habitID, JournalType journalType, String entry) {
        this.timestamp = new Date(System.currentTimeMillis());
        this.habitID = habitID;
        this.journalType = journalType;
        this.entry = entry;
    }

    public static JournalType getJournalType(String journalType) {
        switch (journalType.toUpperCase()) {
            case "GRATITUDE":
                return JournalType.GRATITUDE;
            case "GUILT":
                return JournalType.GUILT;
            case "ENCOURAGE":
                return JournalType.ENCOURAGE;
            default:
                return JournalType.GENERAL;
        }
    }

    public Long getJournalId() {
        return journalId;
    }

    public void setJournalId(Long journalId) {
        this.journalId = journalId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getHabitID() {
        return habitID;
    }

    public void setHabitID(int habitID) {
        this.habitID = habitID;
    }

    public JournalType getJournalType() {
        return journalType;
    }

    public void setJournalType(JournalType journalType) {
        this.journalType = journalType;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public enum JournalType {
        GRATITUDE,
        GUILT,
        ENCOURAGE,
        GENERAL
    }
}
