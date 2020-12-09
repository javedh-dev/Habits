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

package tech.zenex.habits.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Collections;
import java.util.HashMap;

import tech.zenex.habits.database.HabitDetails;
import tech.zenex.habits.database.entities.HabitTrackerEntity;
import tech.zenex.habits.database.entities.JournalEntryEntity;

public class HabitsStats {

    private final HashMap<Long, Integer> journalCountByDate = new HashMap<>();
    private final HashMap<Long, Integer> checkInByDate = new HashMap<>();
    private int habitID;
    private int failureCounter;
    private LocalDateTime lastCheckIn;
    private LocalDateTime streakStart;
    private int totalJournalCount;
    private int streakDays;
    private int streakPercentage;
    private int totalFailures;
    private int totalCheckIns;

    private HabitsStats() {
    }

    @NonNull
    public static HabitsStats calculateStats(HabitDetails habitDetails) {
        HabitsStats stats = new HabitsStats();
        stats.calculate(habitDetails);
        return stats;
    }

    public int getHabitID() {
        return habitID;
    }

    public int getFailureCounter() {
        return failureCounter;
    }

    public LocalDateTime getLastCheckIn() {
        return lastCheckIn;
    }

    public LocalDateTime getStreakStart() {
        return streakStart;
    }

    public int getTotalJournalCount() {
        return totalJournalCount;
    }

    public HashMap<Long, Integer> getJournalCountByType() {
        return journalCountByDate;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public int getTotalFailures() {
        return totalFailures;
    }

    public int getStreakPercentage() {
        return Math.min(streakPercentage, 100);
    }

    public HashMap<Long, Integer> getCheckInByDate() {
        return checkInByDate;
    }

    public int getTotalCheckIns() {
        return totalCheckIns;
    }

    public void calculate(@NonNull HabitDetails habitDetails) {
        this.habitID = habitDetails.getHabitEntity().getHabitID();
        this.streakStart = habitDetails.getHabitEntity().getCreationDate();
        this.lastCheckIn = LocalDateTime.parse("1970-01-01T00:00:00.000");
        Collections.sort(habitDetails.getHabitTrackerEntities());
        Collections.sort(habitDetails.getJournalEntryEntities());
        for (HabitTrackerEntity hte : habitDetails.getHabitTrackerEntities()) {
            Long checkInDay = getLongDate(hte.getCheckInTime());
            Log.d("TrackerTime", hte.getCheckInTime().toString());
            if (habitDetails.getHabitEntity().getHabitType() != hte.getType()) {
                this.totalFailures += 1;
                this.failureCounter = this.totalFailures % 3;
                if (this.failureCounter == 0) {
                    streakStart = hte.getCheckInTime();
                }
            }
            incrementCheckIn(checkInDay);
            lastCheckIn = hte.getCheckInTime();
        }
        for (JournalEntryEntity jee : habitDetails.getJournalEntryEntities()) {
            Long checkInDay = jee.getTimestamp().toDateTime().getMillis();
            totalJournalCount += 1;
            incrementJournalCountByDate(checkInDay);
        }
        this.streakDays =
                Days.daysBetween(streakStart.toLocalDate(), LocalDateTime.now().toLocalDate()).getDays();
        this.streakPercentage =
                (int) (((float) this.streakDays / habitDetails.getHabitEntity().getStreakDays()) * 100);
    }

    private Long getLongDate(LocalDateTime checkInTime) {
        Log.d("Found date", DateTimeFormat.forPattern("dd/MM/yyyy").print(checkInTime));
        return DateTimeFormat.forPattern("dd/MM/yyyy").parseMillis(DateTimeFormat.forPattern("dd/MM/yyyy").print(checkInTime));
    }

    private void incrementJournalCountByDate(Long day) {
        Integer temp = journalCountByDate.get(day);
        if (temp != null) {
            journalCountByDate.put(day, temp + 1);
        } else {
            journalCountByDate.put(day, 1);
        }
    }

    private void incrementCheckIn(Long checkInDay) {
        Integer temp = this.checkInByDate.get(checkInDay);
        if (temp != null) {
            this.checkInByDate.put(checkInDay, temp + 1);
        } else {
            this.checkInByDate.put(checkInDay, 1);
        }
        this.totalCheckIns += 1;
    }

    @Override
    public String toString() {
        return "HabitsStats{" +
                "habitID=" + habitID +
                ", failureCounter=" + failureCounter +
                ", lastCheckIn=" + lastCheckIn +
                ", streakStart=" + streakStart +
                ", totalJournalCount=" + totalJournalCount +
                ", journalCountByDate=" + journalCountByDate +
                ", checkInByDate=" + checkInByDate +
                ", streakDays=" + streakDays +
                ", streakPercentage=" + streakPercentage +
                ", totalFailures=" + totalFailures +
                ", totalCheckIns=" + totalCheckIns +
                '}';
    }
}
