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

import org.joda.time.LocalDateTime;

import java.io.Serializable;

@Entity(tableName = "journal_entries", foreignKeys = @ForeignKey(entity = HabitEntity.class,
        parentColumns = "habitID", childColumns = "habitID", onDelete = ForeignKey.CASCADE),
        indices = {@Index("habitID")})
public class JournalEntryEntity implements Comparable<JournalEntryEntity>, Serializable {
    @PrimaryKey(autoGenerate = true)
    private int journalId;
    private LocalDateTime timestamp;
    private int habitID;
    private JournalType journalType;
    private String entry;

    public JournalEntryEntity() {
    }

    @Ignore
    public JournalEntryEntity(int habitID, JournalType journalType, String entry) {
        this.timestamp = LocalDateTime.now();
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

    public int getJournalId() {
        return journalId;
    }

    public void setJournalId(int journalId) {
        this.journalId = journalId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
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

    @Override
    public int compareTo(JournalEntryEntity o) {
        return this.timestamp.compareTo(o.timestamp);
    }

    @NonNull
    @Override
    public String toString() {
        return "JournalEntryEntity{" +
                "journalId=" + journalId +
                ", timestamp=" + timestamp +
                ", habitID=" + habitID +
                ", journalType=" + journalType +
                ", entry='" + entry + '\'' +
                '}';
    }

    public enum JournalType {
        GRATITUDE,
        GUILT,
        ENCOURAGE,
        GENERAL
    }
}
