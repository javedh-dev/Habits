/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.utils;

import androidx.annotation.NonNull;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Collections;
import java.util.HashMap;

import dev.javed.habits.database.HabitDetails;
import dev.javed.habits.database.entities.HabitTrackerEntity;
import dev.javed.habits.database.entities.JournalEntryEntity;

public class HabitsStats {

    public static final String DD_MM_YYYY = "dd/MM/yyyy";
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

    public int getStreakDays() {
        return streakDays;
    }

    public int getTotalFailures() {
        return totalFailures;
    }

    public int getStreakPercentage() {
        return Math.min(streakPercentage, 100);
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
        return DateTimeFormat.forPattern(DD_MM_YYYY).parseMillis(DateTimeFormat.forPattern(DD_MM_YYYY).print(checkInTime));
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

    @NonNull
    @Override
    public String toString() {
        return "HabitsStats{" +
                "journalCountByDate=" + journalCountByDate +
                ", checkInByDate=" + checkInByDate +
                ", habitID=" + habitID +
                ", failureCounter=" + failureCounter +
                ", lastCheckIn=" + lastCheckIn +
                ", streakStart=" + streakStart +
                ", totalJournalCount=" + totalJournalCount +
                ", streakDays=" + streakDays +
                ", streakPercentage=" + streakPercentage +
                ", totalFailures=" + totalFailures +
                ", totalCheckIns=" + totalCheckIns +
                '}';
    }
}
