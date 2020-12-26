/*
 * Copyright (C) 2020 - 2020 Javed Hussain <javedh.dev@gmail.com>
 * This file is part of Habits project.
 * This file and other under this project can not be copied and/or distributed
 * without the express permission of Javed Hussain
 */

package dev.javed.habits.database;

import androidx.room.TypeConverter;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import dev.javed.habits.database.entities.HabitEntity;
import dev.javed.habits.database.entities.JournalEntryEntity;

public class HabitsTypeConverters {

    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss.SSS");

    @TypeConverter
    public static LocalDateTime toDate(String dateTime) {
        return dateTime == null ? null : formatter.parseLocalDateTime(dateTime);
    }

    @TypeConverter
    public static String fromDate(LocalDateTime dateTime) {
        return dateTime == null ? null : formatter.print(dateTime);
    }

    @TypeConverter
    public String fromHabitTypeToString(HabitEntity.HabitType habitType) {
        return habitType.name();
    }

    @TypeConverter
    public HabitEntity.HabitType fromStringToHabitType(String habitType) {
        return habitType.equalsIgnoreCase("BREAK") ? HabitEntity.HabitType.BREAK :
                HabitEntity.HabitType.DEVELOP;
    }

    @TypeConverter
    public String fromJournalTypeToString(JournalEntryEntity.JournalType habitType) {
        return habitType.name();
    }

    @TypeConverter
    public JournalEntryEntity.JournalType fromStringToJournalType(String journalTYpe) {
        switch (journalTYpe) {
            case "GRATITUDE":
                return JournalEntryEntity.JournalType.GRATITUDE;
            case "GUILT":
                return JournalEntryEntity.JournalType.GUILT;
            case "ENCOURAGE":
                return JournalEntryEntity.JournalType.ENCOURAGE;
            default:
                return JournalEntryEntity.JournalType.GENERAL;
        }
    }
}
